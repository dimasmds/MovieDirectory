package com.riotfallen.moviedirectory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.riotfallen.moviedirectory.R;
import com.riotfallen.moviedirectory.adapter.pager.MoviePagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.mainActivityToolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        drawerLayout = findViewById(R.id.mainActivityDrawerLayout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        TabLayout tabLayout = findViewById(R.id.mainActivityTabLayout);
        viewPager = findViewById(R.id.mainActivityViewPager);
        FloatingActionButton mainFab = findViewById(R.id.mainActivityFAB);
        final NavigationView navigationView = findViewById(R.id.mainActivityNavigationView);
        TextView changeLanguage = findViewById(R.id.menuMainChangeLanguage);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menuNowPlaying);

        mainFab.bringToFront();
        mainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText(R.string.now_playing));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.upcoming));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.favorite));

        MoviePagerAdapter pagerAdapter = new MoviePagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (viewPager.getCurrentItem()){
                    case 0:
                        navigationView.setCheckedItem(R.id.menuNowPlaying);
                        break;
                    case 1:
                        navigationView.setCheckedItem(R.id.menuUpcoming);
                        break;
                    case 2:
                        navigationView.setCheckedItem(R.id.menuFavorite);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (menuItem.getItemId()) {
            case R.id.menuNowPlaying:
                viewPager.setCurrentItem(0);
                break;
            case R.id.menuUpcoming:
                viewPager.setCurrentItem(1);
                break;
            case R.id.menuFavorite:
                viewPager.setCurrentItem(2);
                break;
            case R.id.menuSearch:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
        }

        return true;
    }
}