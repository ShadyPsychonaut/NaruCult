package com.fandom.NarutoCult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFrag extends Fragment implements View.OnClickListener, FirebaseAuth.AuthStateListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "SettingsFrag";

    private ImageView dpImage;
    private TextView usernameText, emailText;

    private MaterialButton btnSignOut;

    private MaterialCardView profileCard;

    private DatabaseReference mDataRef;
    private FirebaseAuth mAuth;

    private SharedPreferences preferences;

    private String username;
    private String imgUrl;
    private String favChar;
    private String favElem;

    private HashMap<String, String> profileMap;

    private OnSettingsFragInteractionListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFrag newInstance(String param1, String param2) {
        SettingsFrag fragment = new SettingsFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        dpImage = view.findViewById(R.id.imageView_dp);
        usernameText = view.findViewById(R.id.textView_userName);
        emailText = view.findViewById(R.id.textView_email);

        profileCard = view.findViewById(R.id.profile_card);

        btnSignOut = view.findViewById(R.id.btn_sign_out);
        btnSignOut.setOnClickListener(this);

        usernameText.setVisibility(View.INVISIBLE);
        emailText.setVisibility(View.INVISIBLE);

        if (mListener != null) {
            mListener.onSettingFragInteraction("Settings");
        }

        setProfile();

        profileMap = new HashMap<>();

        if (preferences.contains("favElem")) {
            profileMap.put("username", preferences.getString("username", "LoadingFailed"));
            profileMap.put("imageUrl", preferences.getString("imageUrl", "no"));
            profileMap.put("favChar", preferences.getString("favChar", "Itachi"));
            profileMap.put("favElem", preferences.getString("favElem", "Fire"));
        } else {
            SystemClock.sleep(1300);
            profileMap.put("username", username);
            profileMap.put("imageUrl", imgUrl);
            profileMap.put("favChar", favChar);
            profileMap.put("favElem", favElem);
        }

        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ProfileFrag.newInstance("", "", profileMap);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(getActivity().findViewById(R.id.settings_container).getId(), fragment, "ProfileFrag");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack("Profile");
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        AuthUI.getInstance().signOut(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent("finish_activity"); // to finish main activity
            getActivity().sendBroadcast(intent);
            getActivity().finish();
            startLaunchActivity();
            return;
        }

        firebaseAuth.getCurrentUser().getIdToken(true)
                .addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                    @Override
                    public void onSuccess(GetTokenResult getTokenResult) {
                        Log.d(TAG, "onSuccess: " + getTokenResult.getToken());
                    }
                });

    }

    public void startLaunchActivity() {
        Intent intent = new Intent(getActivity(), LaunchActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void setProfile() {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        assert currentUser != null;
        assert getActivity() != null;
        preferences = getActivity().getSharedPreferences("user_info" + "." + currentUser.getUid(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit(); // to save user info in local storage

        if (preferences.contains("favElem")) {
            Glide.with(getActivity())
                    .load(preferences.getString("imageUrl", "anyRandomString"))
                    .centerCrop()
                    .into(dpImage);
//            dpImage.setImageURI(currentUser.getPhotoUrl());
            usernameText.setText(preferences.getString("username", currentUser.getDisplayName()));
            usernameText.setVisibility(View.VISIBLE);
            emailText.setText(currentUser.getEmail());
            emailText.setVisibility(View.VISIBLE);
        } else {
            mDataRef = FirebaseDatabase.getInstance().getReference("user_data");
            mDataRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UploadProfile profile = dataSnapshot.getValue(UploadProfile.class);

                    assert profile != null;
                    username = profile.getUsername();
                    imgUrl = profile.getImageUrl();
                    favChar = profile.getFavChar();
                    favElem = profile.getFavElem();
                    Log.d(TAG, "onDataChange: username: " + username);

//                    if (!preferences.contains("username")) {
                    Glide.with(getActivity())
                            .load(imgUrl)
                            .centerCrop()
                            .into(dpImage);
//            dpImage.setImageURI(currentUser.getPhotoUrl());
                    usernameText.setText(username);
                    usernameText.setVisibility(View.VISIBLE);
                    emailText.setText(currentUser.getEmail());
                    emailText.setVisibility(View.VISIBLE);
//                    }

                    editor.putString("username", username);
                    editor.putString("imageUrl", imgUrl);
                    editor.putString("favElem", favElem);
                    editor.putString("favChar", favChar);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
//            Log.d(TAG, "setProfile: Image Uri: " + currentUser.getPhotoUrl());
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSettingsFragInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnSettingsFragInteractionListener {
        void onSettingFragInteraction(String title);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
