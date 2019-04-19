package nguyen.huy.moneylover;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import nguyen.huy.moneylover.MainLayout.TabAdapter;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private FloatingActionButton fabAdd;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNavigationDrawer();
        createTabLayout();
        addControls();
        addEvent();
    }


    private void createNavigationDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void createTabLayout() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tablayout_header);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void addControls() {
        fabAdd = findViewById(R.id.fabAdd);

    }

    private void addEvent() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        handleItemClicked(menuItem.getItemId());
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQuanLyThuChi();
            }
        });
    }


    private void handleItemClicked(int itemId) {
        switch (itemId){
            case R.id.nav_TietKiem : doTietKiem() ; break;
            case R.id.nav_SuKien : doSuKien() ; break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doQuanLyThuChi() {
        //them thu chi (+)
        Toast.makeText(this,"Thu chi",Toast.LENGTH_SHORT).show();
    }

    private void doTietKiem() {
        Toast.makeText(this,"Tiet kiem",Toast.LENGTH_SHORT).show();
        //quan ly tiet kiem
    }

    private void doSuKien() {
        Toast.makeText(this,"SuKien",Toast.LENGTH_SHORT).show();
        //quan ly su kien
    }
}
