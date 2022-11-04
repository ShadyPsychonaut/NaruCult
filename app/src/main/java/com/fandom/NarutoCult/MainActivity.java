package com.fandom.NarutoCult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fandom.NarutoCult.main_tabs.BrowseFragment;
import com.fandom.NarutoCult.main_tabs.region_tab.RegionsFragment;
import com.fandom.NarutoCult.main_tabs.people_tab.PeopleFragment;
import com.fandom.NarutoCult.theme_activity.ThemeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private PeopleFragment peopleFragment;
    private RegionsFragment regionsFragment;
    private BrowseFragment browseFragment;

    private BroadcastReceiver broadcastReceiver;

    private DatabaseReference mDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDataRef = FirebaseDatabase.getInstance().getReference();

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_Layout);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            checkUsernamePresent();
            pagerAdapter();
        }
        creatingBroadcastReceiver();

    }

    public void checkUsernamePresent() {
        mDataRef.child("user_data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("username")) {
                    Intent intent = new Intent(MainActivity.this, SetUpProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void creatingBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkWifiMobileNetwork()) {
            Snackbar.make(tabLayout, "Mobile data is turned off", Snackbar.LENGTH_LONG)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS), 0);
                        }
                    }).show();
        }

    }

    public boolean checkWifiMobileNetwork() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            startLaunchActivity();
            return;
        }


        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d("TAG", "onSuccess: " + getTokenResult.getToken());
                    }
                });
    }

    public void startLaunchActivity() {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
        finish();
    }

    public void pagerAdapter() {
        peopleFragment = new PeopleFragment();
        regionsFragment = new RegionsFragment();
        browseFragment = new BrowseFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(peopleFragment, "People");
        viewPagerAdapter.addFragment(regionsFragment, "Regions");
        viewPagerAdapter.addFragment(browseFragment, "Browse");  //for some rare techniques
        viewPager.setAdapter(viewPagerAdapter);

//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_remove_red_eye_black_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_landscape_black_24dp);
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_outline_black_24dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.theme) {
            Intent intent = new Intent(this, ThemeActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}

