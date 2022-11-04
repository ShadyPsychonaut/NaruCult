package com.fandom.NarutoCult.theme_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fandom.NarutoCult.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThemeActivity extends AppCompatActivity implements ThemeAdapter.OnPlayPressListener {

    private static final String TAG = "ThemeActivity";

    private ThemeAdapter themeAdapter;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private String[] title;
    private int[] icons = {R.drawable.icon4, R.drawable.icon10, R.drawable.icon2, R.drawable.icon9, R.drawable.icon7
            , R.drawable.icon5, R.drawable.icon1, R.drawable.icon3, R.drawable.icon8, R.drawable.icon6};
    private String[] songs;
    private List<ThemeList> tracks;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;

    private volatile boolean stopThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        recyclerView = findViewById(R.id.ThemeRecyclerView);

        toolbar = findViewById(R.id.Theme_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());

        tracks = new ArrayList<>();

        getData();

        for (int i = 0; i < title.length; i++)
            tracks.add(new ThemeList(title[i], songs[i], icons[i]));

        setThemeAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkWifiMobileNetwork()) {
            Snackbar.make(recyclerView, "Mobile data is turned off", Snackbar.LENGTH_LONG).show();
        }
    }

    public boolean checkWifiMobileNetwork() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();

    }

    public void getData() {
        title = getResources().getStringArray(R.array.theme_titles);
        songs = getResources().getStringArray(R.array.themes);
    }

    public void setThemeAdapter() {
        themeAdapter = new ThemeAdapter(this, tracks);
        recyclerView.setAdapter(themeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        themeAdapter.setOnPlayPressListener(this);

    }

    @Override
    public void onPressingPlay(Button btn, MediaPlayer mp, SeekBar positionBar, TextView elapsedTime) {
        mediaPlayer = mp;

        Log.d(TAG, "onPressingPlay: The thread is " + Thread.currentThread());

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mp.seekTo(progress);
                    positionBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        }));

        stopThread = false;
        runnable = () -> {
            if (stopThread)
                return;
            positionBar.setProgress(mp.getCurrentPosition());
            Handler handler1 = new Handler();
            handler1.postDelayed(runnable, 100);

            int milliSeconds = mp.getCurrentPosition();
            if (milliSeconds != 0) {
                //if audio is playing, showing current time;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds);
                if (minutes == 0) {
                    elapsedTime.setText("0:" + seconds + "/"
                            + calculateDuration(mp.getDuration()));
                } else {
                    if (seconds >= 60) {
                        long sec = seconds - (minutes * 60);
                        elapsedTime.setText(minutes + ":" + sec + "/" +
                                calculateDuration(mp.getDuration()));
                    }
                }
            } else {
                //Displaying total time if audio not playing
                int totalTime = mp.getDuration();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTime);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTime);
                if (minutes == 0) {
                    elapsedTime.setText("0:" + seconds);
                } else {
                    if (seconds >= 60) {
                        long sec = seconds - (minutes * 60);
                        elapsedTime.setText(minutes + ":" + sec);
                    }
                }
            }
        };

        handler.post(runnable);

        handler.post(() -> btn.setOnClickListener(v -> {
            if (mp.isPlaying()) {
                mp.pause();
                btn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            } else {
                mp.start();
                btn.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                runnable.run();
            }
        }));
    }

    private String calculateDuration(int duration) {
        String finalDuration = "";
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        if (minutes == 0) {
            finalDuration = "0:" + seconds;
        } else {
            if (seconds >= 60) {
                long sec = seconds - (minutes * 60);
                finalDuration = minutes + ":" + sec;
            }
        }
        return finalDuration;
    }

    public void stopThread() {
        stopThread = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
