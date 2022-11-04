package com.fandom.NarutoCult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity implements ProfileFrag.OnProfileFragmentInteractionListener,
        SettingsFrag.OnSettingsFragInteractionListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        openSettingsFragment(SettingsFrag.newInstance(" ", " "));

    }

    @Override
    public void onFragmentInteraction(String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().popBackStack();
//                getSupportFragmentManager().beginTransaction()
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//                        .commit();
            }
        });
    }

    @Override
    public void onSettingFragInteraction(String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void openSettingsFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_container, fragment, "SettingsFrag");
        // transaction.addToBackStack("true");
        transaction.commit();
    }

}
