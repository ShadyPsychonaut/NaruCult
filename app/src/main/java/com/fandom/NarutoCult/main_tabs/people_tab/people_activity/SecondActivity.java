package com.fandom.NarutoCult.main_tabs.people_tab.people_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fandom.NarutoCult.R;
import com.fandom.NarutoCult.main_tabs.people_tab.people_activity.your_views_fragment.YourViewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SecondActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private String link, quo1, quo2, quo3, quo4;
    private String t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        toolbar = findViewById(R.id.toolbarSecond);
        setSupportActionBar(toolbar);

        getData();

        //shows back icon
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottomBar);
        openFragment(GlimpseFragment.newInstance("", "", link, quo1, quo2, quo3, quo4));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.glimpse:
                        openFragment(GlimpseFragment.newInstance("", "", link, quo1, quo2, quo3, quo4));
                        return true;
                    case R.id.brief:
                        openFragment(YourViewsFragment.newInstance("", "", t1, t2));
                        return true;
                    case R.id.social:
                        openFragment(SocialFragment.newInstance("", ""));
                        return true;
                }
                return false;
            }
        });

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void getData() {
        if (getIntent().hasExtra("data1") && getIntent().hasExtra("data2")) {
            t1 = getIntent().getStringExtra("data1");
            t2 = getIntent().getStringExtra("data2");
            link = getIntent().getStringExtra("you_key");
            quo1 = getIntent().getStringExtra("quo1");
            quo2 = getIntent().getStringExtra("quo2");
            quo3 = getIntent().getStringExtra("quo3");
            quo4 = getIntent().getStringExtra("quo4");

            setTitle(t1 + " " + t2);
        } else
            Toast.makeText(this, "Fookin' Error", Toast.LENGTH_SHORT).show();
    }
}