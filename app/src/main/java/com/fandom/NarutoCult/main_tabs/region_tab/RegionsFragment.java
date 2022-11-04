package com.fandom.NarutoCult.main_tabs.region_tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fandom.NarutoCult.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegionsFragment extends Fragment {

    private String[] regionNames, countryNames, imgLinks, symLinks;

    private RecyclerView recyclerView;
    private RegionRecyclerAdapter adapter;

    private List<RegionList> regionList;

    public RegionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_images, container, false);

        getData();

        recyclerView = view.findViewById(R.id.regions_recycler);

        regionList = new ArrayList<>();

        for (int i = 0; i < regionNames.length; i++) {
            regionList.add(new RegionList(regionNames[i],countryNames[i], imgLinks[i], symLinks[i]));
        }

        setAdapter();

        return view;
    }

    public void getData() {
        regionNames = getResources().getStringArray(R.array.region_names);
        countryNames = getResources().getStringArray(R.array.country_name);
        imgLinks = getResources().getStringArray(R.array.region_img_links);
        symLinks = getResources().getStringArray(R.array.region_symbols_links);
    }

    public void setAdapter() {
        adapter = new RegionRecyclerAdapter(getActivity(), regionList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

}
