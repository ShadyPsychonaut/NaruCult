package com.fandom.NarutoCult;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String[] profileValues;

    private ImageView profile_image;
    private TextView usernameText, favCharText, favElemText;

    private OnProfileFragmentInteractionListener mListener;

    public ProfileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFrag newInstance(String param1, String param2, HashMap<String, String> profileMap) {
        ProfileFrag fragment = new ProfileFrag();
        String[] values = {profileMap.get("username"), profileMap.get("imageUrl"), profileMap.get("favElem"), profileMap.get("favChar")};
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putStringArray("profileMap", values);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            profileValues = getArguments().getStringArray("profileMap");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Profile");
        }

        profile_image = view.findViewById(R.id.profile_imageView);
        usernameText = view.findViewById(R.id.textView_sub_username);
        favCharText = view.findViewById(R.id.textView_sub_hero);
        favElemText = view.findViewById(R.id.textView_sub_element);

        setFields();

        return view;
    }

    public void setFields() {
        Glide.with(getActivity())
                .load(profileValues[1])
                .centerCrop()
                .into(profile_image);

        usernameText.setText(profileValues[0]);
        favCharText.setText(profileValues[3]);
        favElemText.setText(profileValues[2]);

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnProfileFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnProfileFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }

}
