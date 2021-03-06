package nguyen.huy.moneylover.Transaction.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import nguyen.huy.moneylover.MainEvent.View.ListEventActivity;
import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.Transaction.Controller.ReportDatabaseManager;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.R;

public class TransactionActivity extends AppCompatActivity{

    EditText edtNhapSoTien,edtThemGhiChu,edtThemBan,edtDatNhacNho;
    public static EditText edtChonNhom;
    public static ImageView imgchonNhom;
    public static EditText edtChonNgay;
    public static EditText edtChonPhuongThuc;
    public static ImageView imgChonPhuongThuc;
    public static EditText edtChonSuKien;
    public static ImageView imgChonSuKien;
    public static int IDSuKien=0;

    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minh_activity_thuchi);

        addControls();

        //Khởi tạo ngày mặc định cho mục chọn ngày
        edtChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void addControls() {
        edtNhapSoTien=findViewById(R.id.edtNhapSoTien);
        edtChonNhom=findViewById(R.id.edtChonNhom);
        edtThemGhiChu=findViewById(R.id.edtThemGhiChu);
        edtChonNgay=findViewById(R.id.edtChonNgay);
        edtChonPhuongThuc =findViewById(R.id.edtPhuongThucTT);
        edtThemBan=findViewById(R.id.edtThemBan);
        edtDatNhacNho=findViewById(R.id.edtDatNhacNho);
        edtChonSuKien=findViewById(R.id.edtChonSuKien);
        imgchonNhom=findViewById(R.id.imgChonNhom);
        imgChonPhuongThuc=findViewById(R.id.imgPhuongThucTT);
        imgChonSuKien=findViewById(R.id.imageChonSuKien);
    }

    public void xuLyLuu(View view) {
        if(TextUtils.isEmpty(edtNhapSoTien.getText().toString())){
            edtNhapSoTien.setError("Bạn chưa nhập số tiền");
            return;
        }
        if(TextUtils.isEmpty(edtChonNhom.getText().toString())){
            Toast.makeText(getApplicationContext(),"Bạn chưa chọn nhóm",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(edtChonPhuongThuc.getText().toString())){
            edtChonPhuongThuc.setError("Bạn chưa chọn phương thức thanh toán");
            return;
        }

        String result[]= DayTimeManager.ConvertFormatDay(edtChonNgay.getText().toString());
        TransactionManager.SaveTransactionToDatabase(TaoGiaoDich(),result);
        ReportDatabaseManager.SaveToDatabase(TaoGiaoDich());
        //Chuyển hình chọn nhóm về ban đầu
        /*Resources res=getResources();
        Drawable drawable=res.getDrawable(R.drawable.question2);
        imgchonNhom.setImageDrawable(drawable);*/

        finish();
    }

    private Transaction TaoGiaoDich(){
        String SoTien=edtNhapSoTien.getText().toString();
        String Nhom=edtChonNhom.getText().toString();
        String GhiChu=edtThemGhiChu.getText().toString();
        String Ngay=edtChonNgay.getText().toString();
        String PhuongThuc= edtChonPhuongThuc.getText().toString();
        String Banbe=edtThemBan.getText().toString();
        String NhacNho=edtDatNhacNho.getText().toString();
        String SuKien=edtChonSuKien.getText().toString();
        //Khởi tạo giao dịch mới
        Transaction giaodich=new Transaction(SoTien,Nhom,GhiChu,Ngay,PhuongThuc,Banbe,NhacNho,SuKien);
        giaodich.setIDSuKien(IDSuKien);
        return giaodich;
    }


    public void xuLyChonNhom(View view) {
        Intent intent=new Intent(TransactionActivity.this, SelectGroupActivity.class);
        startActivity(intent);
    }

    public void xuLyThoat(View view) {
        finish();
    }

    public void xuLyHienThiNgay(View view) {
        TransactionManager.displayDayEditText(view,edtChonNgay, TransactionActivity.this);
    }

    public void xuLyChonPhuongThuc(View view) {
        Intent intent=new Intent(TransactionActivity.this, PaymentMethodActivity.class);
        startActivity(intent);
    }

    public void xuLyChonSuKien(View view) {
        Intent intent=new Intent(TransactionActivity.this, ListEventActivity.class);
        startActivity(intent);
    }
}
