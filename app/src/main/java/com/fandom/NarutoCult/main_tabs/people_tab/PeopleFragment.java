package com.fandom.NarutoCult.main_tabs.people_tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.fandom.NarutoCult.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {

    private String[] s1, s2, you_links, quote1, quote2, quote3, quote4;
    private String[] images_link;
    private MyRecyclerAdapter myRecyclerAdapter;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        setHasOptionsMenu(true);

        getData();

        ArrayList<PeopleItem> browseList = new ArrayList<>();
        for (int i = 0; i < s1.length; i++)
            browseList.add(new PeopleItem(images_link[i], s1[i], s2[i], you_links[i], quote1[i], quote2[i], quote3[i], quote4[i]));

        myRecyclerAdapter = new MyRecyclerAdapter(getActivity(), browseList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void getData() {
        images_link = getResources().getStringArray(R.array.character_images_links);
        s1 = getResources().getStringArray(R.array.character_clan_names);
        s2 = getResources().getStringArray(R.array.character_names);
        you_links = getResources().getStringArray(R.array.youtube_links);
        quote1 = getResources().getStringArray(R.array.quote1);
        quote2 = getResources().getStringArray(R.array.quote2);
        quote3 = getResources().getStringArray(R.array.quote3);
        quote4 = getResources().getStringArray(R.array.quote4);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.people_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
