package com.fandom.NarutoCult.main_tabs.people_tab.people_activity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fandom.NarutoCult.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;


/**
 * A simple {@link Fragment} subclass.
 */
public class GlimpseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView card1, card2, card3, card4;

    private YouTubePlayerSupportFragmentX youTubePlayerFragment;
    private String link, quo1, quo2, quo3, quo4;

    public GlimpseFragment() {
        // Required empty public constructor
    }

    static GlimpseFragment newInstance(String param1, String param2, String you_link, String q1,
                                       String q2, String q3, String q4) {
        GlimpseFragment fragment = new GlimpseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("you_link", you_link);
        args.putString("quo1", q1);
        args.putString("quo2", q2);
        args.putString("quo3", q3);
        args.putString("quo4", q4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            link = getArguments().getString("you_link");
            quo1 = getArguments().getString("quo1");
            quo2 = getArguments().getString("quo2");
            quo3 = getArguments().getString("quo3");
            quo4 = getArguments().getString("quo4");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_glimpse, container, false);

        TextView text1, text2, text3, text4;
        text1 = v.findViewById(R.id.quo1);
        text2 = v.findViewById(R.id.quo2);
        text3 = v.findViewById(R.id.quo3);
        text4 = v.findViewById(R.id.quo4);

        text1.setText(quo1);
        text2.setText(quo2);
        text3.setText(quo3);
        text4.setText(quo4);

        youTubePlayerFragment = YouTubePlayerSupportFragmentX.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(YoutubeConfig.getAPIKey(), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!b) {
                    youTubePlayer.loadVideo(link);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

        card1 = v.findViewById(R.id.card1);
        card2 = v.findViewById(R.id.card2);
        card3 = v.findViewById(R.id.card3);
        card4 = v.findViewById(R.id.card4);

        card1.setMinimumWidth(getWidth()/2);
        card2.setMinimumWidth(getWidth()/2);
        card3.setMinimumWidth(getWidth()/2);
        card4.setMinimumWidth(getWidth()/2);

        return v;
    }

    public int getWidth(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        return width;
    }

}
