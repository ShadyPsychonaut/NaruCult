package com.fandom.NarutoCult.theme_activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fandom.NarutoCult.R;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {

    private Context context;
    private List<ThemeList> tracks;
    private OnPlayPressListener listener;

    private static final String TAG = "ThemeAdapter";


    public ThemeAdapter(Context ctx, List<ThemeList> tracks) {
        context = ctx;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public ThemeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.theme_player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemeAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(tracks.get(position).getTitle());
        holder.imageView.setImageResource(tracks.get(position).getIcon());

        holder.progressBar.setVisibility(View.INVISIBLE);

        holder.playBtn.setOnClickListener(v -> {
            holder.progressBar.setVisibility(View.VISIBLE);
            if (listener != null) {
                if (position != RecyclerView.NO_POSITION) {
                    new Thread(() -> {
                        Log.i(TAG, "onClick: The thread is" + Thread.currentThread());
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(() -> holder.playBtn.setBackgroundResource(R.drawable.ic_pause_black_24dp));

                        try {
                            holder.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            holder.mediaPlayer.setDataSource(context, Uri.parse(tracks.get(position).getTrackUrl()));
                            holder.mediaPlayer.prepare();
                            holder.mediaPlayer.start();
                            handler.postDelayed(() -> holder.progressBar.setVisibility(View.INVISIBLE), 1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                            holder.mediaPlayer = MediaPlayer.create(context, R.raw.drums);
                            holder.mediaPlayer.start();
                            handler.post(() -> holder.progressBar.setVisibility(View.INVISIBLE));
                            handler.postDelayed(() -> Toast.makeText(context, "Turn on mobile data", Toast.LENGTH_LONG).show(), 2000);
                        }

                        final int totalTime = holder.mediaPlayer.getDuration();

                        //positionBar
                        holder.positionBar.setMax(totalTime);
                        holder.positionBar.setTag(position);

                        listener.onPressingPlay(holder.playBtn, holder.mediaPlayer, holder.positionBar, holder.elapsedTimeLabel);
                    }).start();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView, elapsedTimeLabel;
        private Button playBtn;
        private SeekBar positionBar;
        private MediaPlayer mediaPlayer;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.theme_icon);
            textView = itemView.findViewById(R.id.song_title);

            elapsedTimeLabel = itemView.findViewById(R.id.elapsedTimeLabel);

            positionBar = itemView.findViewById(R.id.positionBar);
            playBtn = itemView.findViewById(R.id.playBtn);
            progressBar = itemView.findViewById(R.id.progressBarTheme);

            mediaPlayer = new MediaPlayer();
        }
    }

    public interface OnPlayPressListener {

        void onPressingPlay(Button btn, MediaPlayer mp, SeekBar positionBar, TextView elapsedTime);

    }

    public void setOnPlayPressListener(OnPlayPressListener listener) {
        this.listener = listener;
    }

}

