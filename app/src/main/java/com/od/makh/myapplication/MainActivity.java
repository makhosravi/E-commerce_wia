package com.od.makh.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.od.makh.myapplication.Activities.CreatePostActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Fragments.HomeFragment;
import com.od.makh.myapplication.Fragments.ListFragmentRoot;
import com.od.makh.myapplication.Fragments.ProfileFragmentRoot;
import com.od.makh.myapplication.Fragments.SearchFragment;

import wiadevelopers.com.library.BottomNav.BottomNavigationView;
import wiadevelopers.com.library.DivarUtils;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;

    ProfileFragmentRoot profileFragmentRoot = new ProfileFragmentRoot();
    SearchFragment searchFragment = new SearchFragment();
    ListFragmentRoot listFragmentRoot = new ListFragmentRoot();
    HomeFragment homeFragment = new HomeFragment();

    TextView txtToolbarTitle, txtAddAdvertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DivarUtils.statusBarColor(getWindow(), R.color.mainColor);
        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypefaces();
        setupToolbar();
        setListeners();
        setupViewPager();
    }

    private void findViews()
    {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        txtToolbarTitle = (TextView) findViewById(R.id.txtToolbarTitle);
        txtAddAdvertisement = (TextView) findViewById(R.id.txtAddAdvertisement);
    }

    private void setupToolbar()
    {
        String city = DivarUtils.readDataFromStorage(Constant.USER_CITY,null);
        txtToolbarTitle.setText("همه‌ی آگهی‌های" + " " +city);
        txtToolbarTitle.setSelected(true);

        Drawable img = DivarUtils.createDrawable(R.drawable.ic_add,Constant.TEXTVIEW_DRAWABLE_SIZE);
        txtAddAdvertisement.setCompoundDrawables(null,null,img,null);
        txtAddAdvertisement.setText("    آگهی جدید");
    }

    private void setTypefaces()
    {
        txtAddAdvertisement.setTypeface(DivarUtils.faceLight);
        txtToolbarTitle.setTypeface(DivarUtils.face);
    }

    private void setListeners()
    {
        txtAddAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
            }
        });
    }

    private void setupViewPager()
    {
        DivarUtils.ViewPagerAdapter adapter = new DivarUtils.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(profileFragmentRoot);
        adapter.addFragment(searchFragment);
        adapter.addFragment(listFragmentRoot);
        adapter.addFragment(homeFragment);

        DivarUtils.setUpViewPager(MainActivity.this,viewPager,adapter,bottomNavigationView,
                R.menu.bottom_navigastion_items,3,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_FILTER_HOME_FRAGMENT)
            homeFragment.onActivityResult(requestCode,resultCode,data);

        else if (requestCode == Constant.REQUEST_FILTER_LIST_FRAGMENT)
            listFragmentRoot.onActivityResult(requestCode,resultCode,data);

        else if (requestCode == DivarUtils.SPEECH_INPUT_REQUEST_CODE)
            searchFragment.onActivityResult(requestCode,resultCode,data);

    }
}
