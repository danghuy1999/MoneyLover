package nguyen.huy.moneylover.MainLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import nguyen.huy.moneylover.Transaction.Model.Transaction;
import nguyen.huy.moneylover.Transaction.Adapter.AdapterParentListView;
import nguyen.huy.moneylover.Transaction.Controller.TransactionManager;
import nguyen.huy.moneylover.Transaction.Controller.DayTimeManager;
import nguyen.huy.moneylover.R;
import nguyen.huy.moneylover.Report.View.ReportActivity;
import nguyen.huy.moneylover.Tool.Convert;

public class FragmentThisMonth extends Fragment implements FirebaseAuth.AuthStateListener {
    public FragmentThisMonth() {
    }
//    AdapterTransaction adapterThuChi;
    AdapterParentListView adapterParentListView;
    //TODO : this
    ArrayList<ArrayList<Transaction>> arrayObject = new ArrayList<>();
    ListView listView;
    DatabaseReference databaseReference;
    TextView txtSoTienVao,txtSoTienRa,txtSoDu;
    String[] result;
    LinearLayout lyReport;
    FirebaseAuth auth;
    FirebaseUser user;
    Calendar calendar=Calendar.getInstance();
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        auth.removeAuthStateListener(this);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_this_month, container,false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        listView=view.findViewById(R.id.listGiaoDichThuChi);
        adapterParentListView = new AdapterParentListView(getActivity(),R.layout.minh_custom_listview_parent, arrayObject);
        listView.setAdapter(adapterParentListView);
        String ngaythangnam= simpleDateFormat.format(calendar.getTime());
        lyReport = view.findViewById(R.id.lyReport);
        result= DayTimeManager.ConvertFormatDay(ngaythangnam);

        readAllDayinThisMonth(result[0]);

        txtSoTienVao=view.findViewById(R.id.txtSoTienVaoListView);
        txtSoTienRa=view.findViewById(R.id.txtSoTienRaListView);
        txtSoDu=view.findViewById(R.id.txtSoTienDuListView);

        readTienVaoTienRa(result);

        addEvents();

        TransactionManager.setBalance();

        return view;

    }

    private void addEvents() {
        lyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("ValueThisMonth", arrayObject);
                startActivity(intent);
            }
        });

    }

    private void readAllDayinThisMonth(final String thang){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(thang).child("Ngày");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long tienvaothang=0;
                long tienrathang=0;
                if (!arrayObject.isEmpty()) arrayObject.clear();
                for(DataSnapshot snapshot :dataSnapshot.getChildren())
                {
                    long tienvaongay=0;
                    long tienrangay=0;
                    String ngay = snapshot.getKey();
                    ArrayList<Transaction> arrTransaction = new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.child("Giao dịch").getChildren())
                    {
                        Transaction transaction = childSnapshot.getValue(Transaction.class);
                        if(TransactionManager.checkMoneyIO(transaction)){
                            tienvaongay=tienvaongay+Long.parseLong(transaction.getSotien());
                        }
                        else{
                            tienrangay=tienrangay+Long.parseLong(transaction.getSotien());
                        }
                        arrTransaction.add(transaction);
                    }

                    tienvaothang=tienvaothang+tienvaongay;
                    tienrathang=tienrathang+tienrangay;
                    if(tienvaongay==0 && tienrangay==0) {
                        DatabaseReference dt = FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(thang).child("Ngày").child(ngay);
                        dt.child("Tiền vào").setValue(null);
                        dt.child("Tiền ra").setValue(null);
                        break;
                    }

                    arrayObject.add(arrTransaction);

                    TransactionManager.UpdateMoneyOutcomeInDay(DayTimeManager.ConvertFormatDayGetMonth(ngay),tienvaongay);
                    TransactionManager.UpdateMoneyIncomeDay(DayTimeManager.ConvertFormatDayGetMonth(ngay),tienrangay);


                }
                TransactionManager.UpdateMoneyIncomeMonth(thang,tienvaothang);
                TransactionManager.UpdateMoneyOutcomeMonth(thang,tienrathang);
                Collections.reverse(arrayObject);
                adapterParentListView.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Đọc lây tiền vào tiền ra
    private void readTienVaoTienRa(String[] result){
        databaseReference=FirebaseDatabase.getInstance().getReference().child(TransactionManager.user).child("Thu chi").child(result[0]);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Tiền vào").getValue()!=null && dataSnapshot.child("Tiền ra").getValue()!=null) {
                    long tienvao=Long.parseLong(dataSnapshot.child("Tiền vào").getValue().toString());
                    long tienra=Long.parseLong(dataSnapshot.child("Tiền ra").getValue().toString());
                    txtSoTienVao.setText(Convert.Money(tienvao));
                    txtSoTienRa.setText(Convert.Money(tienra));
                    long sodu = tienvao-tienra;
                    txtSoDu.setText(Convert.Money(sodu));
                    txtSoTienVao.setTextColor(Color.BLUE);
                    txtSoTienRa.setTextColor(Color.RED);
                    txtSoDu.setTextColor(Color.BLACK);
                }
                else{
                    txtSoTienRa.setText("0 đ");
                    txtSoTienVao.setText("0 đ");
                    txtSoDu.setText("0 đ");
                    txtSoTienVao.setTextColor(Color.BLUE);
                    txtSoTienRa.setTextColor(Color.RED);
                    txtSoDu.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            readAllDayinThisMonth(result[0]);
            readTienVaoTienRa(result);
            TransactionManager.setBalance();
        }
    }
}
