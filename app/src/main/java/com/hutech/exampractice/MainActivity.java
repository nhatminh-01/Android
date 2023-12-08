package com.hutech.exampractice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.hutech.exampractice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    public ActivityMainBinding binding;
    public TextView drawerProfileName, drawerProfileText;

    public FrameLayout main_frame; // là một Frament
    public  DrawerLayout mDrawer;

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId())
                    {
                        case R.id.navigation_home:
                            setFragement(new CategoryFragment());
                            return true;

                        case R.id.navigation_leaderboard:
                            setFragement(new LeaderBoardFragment());
                            return true;

                        case R.id.navigation_account:
                            setFragement(new AccountFragment());
                            return true;
                    }
                    return false;
                }
            };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //int cat_index = DbQuery.g_catList.get(DbQuery.g_selected_cat_index);
        getSupportActionBar().setTitle("Categories");
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        main_frame = findViewById(R.id.nav_host_fragment_content_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        //DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mDrawer = findViewById(R.id.drawer_layout);

        // Code sẵn
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                // R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                R.id.nav_home, R.id.nav_leaderboard, R.id.nav_account)
                .setOpenableLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupDrawerContent(navigationView);

        // Sét thông tin người dùng
        drawerProfileName = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_name);
        drawerProfileText = navigationView.getHeaderView(0).findViewById(R.id.nav_drawer_text_img);

        String name = DbQuery.myProfile.getName();
        drawerProfileName.setText(name);
        drawerProfileText.setText(name.toUpperCase().substring(0,1));



        setFragement(new CategoryFragment());


    }

    // Tạo Fragment cho menu
    public void setFragement(Fragment fragement){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(main_frame.getId(), fragement);
        transaction.commit();
    }

    // Hàm lăng nghe ---> điều hướng
    public void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Lắng nghe drawer để load fragment tương ứng
    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment;
        switch (menuItem.getItemId()){
            case R.id.nav_leaderboard:
                setFragement(new LeaderBoardFragment() );
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
                break;
            case R.id.nav_account:
                setFragement(new AccountFragment());
                bottomNavigationView.setSelectedItemId(R.id.nav_account);
                break;
            case R.id.nav_bookmark:
                Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
            default:
                fragment = new CategoryFragment();
                bottomNavigationView.setSelectedItemId(R.id.nav_home);
                setFragement(fragment);
        }
        mDrawer.closeDrawers();

    }

    // Bấm vào menu
   @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.nav_home){
            setFragement(new CategoryFragment());
        }else if(id == R.id.nav_account){
            setFragement(new AccountFragment());
        }else if( id == R.id.nav_leaderboard){
            setFragement(new LeaderBoardFragment());
        }else if(id == R.id.nav_bookmark)
        {
            Intent intent = new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}