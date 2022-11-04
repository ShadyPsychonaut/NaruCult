package com.fandom.NarutoCult.main_tabs.people_tab.people_activity.your_views_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fandom.NarutoCult.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class YourViewsFragment extends Fragment implements View.OnClickListener, ValueEventListener {

    private EditText editMessage;
    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    private MyViewRecyclerAdapter recyclerAdapter;

    private DatabaseReference mDataRef;
    private ValueEventListener mDBListener;

    private List<UploadMyView> uploads;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String n1, n2;

    public YourViewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SocialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourViewsFragment newInstance(String param1, String param2, String t1, String t2) {
        YourViewsFragment fragment = new YourViewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("Name1", t1);
        args.putString("Name2", t2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            n1 = getArguments().getString("Name1");
            n2 = getArguments().getString("Name2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brief, container, false);

        editMessage = view.findViewById(R.id.viewEdit);
        fab = view.findViewById(R.id.fab_brief);

        recyclerView = view.findViewById(R.id.viewRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        uploads = new ArrayList<>();

        recyclerAdapter = new MyViewRecyclerAdapter(getActivity(), uploads);
        recyclerView.setAdapter(recyclerAdapter);

        mDataRef = FirebaseDatabase.getInstance().getReference("ViewsNav"+n1+n2);

        fab.setOnClickListener(this);

        mDBListener = mDataRef.addValueEventListener(this);

        return view;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        uploads.clear();

        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
            UploadMyView upload = postSnapshot.getValue(UploadMyView.class);
            assert upload != null;
            upload.setKey(postSnapshot.getKey());
            uploads.add(upload);
        }

        recyclerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String message = editMessage.getText().toString();
        if(!message.trim().isEmpty()){
            UploadMyView upload = new UploadMyView(message);
            String uploadId = mDataRef.push().getKey();
            mDataRef.child(uploadId).setValue(upload);
        } else
            Toast.makeText(getActivity(), "No text found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataRef.removeEventListener(mDBListener);
    }
}
