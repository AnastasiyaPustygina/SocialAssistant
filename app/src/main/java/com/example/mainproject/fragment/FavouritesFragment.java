package com.example.mainproject.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainproject.OpenHelper;
import com.example.mainproject.adapter.OrgArrayAdapter;
import com.example.android.multidex.mainproject.R;
import com.example.mainproject.domain.Organization;
import java.util.ArrayList;

public class FavouritesFragment extends Fragment {
    private AppCompatButton bt_prof, bt_list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourites_fragment, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        bt_prof = getActivity().findViewById(R.id.bt_fav_prof);
        bt_list = getActivity().findViewById(R.id.bt_fav_list);
        ArrayList<Organization> arListOrg = new ArrayList<Organization>();
        OpenHelper openHelper = new OpenHelper(getContext(), "OpenHelder", null, OpenHelper.VERSION);
        String[] arr = openHelper.findFavOrgByLogin(getArguments().getString("LOG")).split(" ");
        for (int i = 1; i < arr.length; i++) {
            if (openHelper.findOrgByName(arr[i]).getName() != null)
                arListOrg.add(openHelper.findOrgByName(arr[i]));
        }
        RecyclerView recyclerView = getActivity().findViewById(R.id.rec_fav);
        OrgArrayAdapter orgArrayAdapter = new OrgArrayAdapter(
                getContext(), arListOrg, getArguments().getString("LOG"), this);
        recyclerView.setAdapter(orgArrayAdapter);
        Bundle bundleLog = new Bundle();
        bundleLog.putString("LOG", getArguments().getString("LOG"));
        bt_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_prof.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(FavouritesFragment.this).navigate(
                            R.id.action_favouritesFragment_to_mainFragment, bundleLog);
                });
                bt_prof.performClick();
            }
        });
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_list.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(FavouritesFragment.this).navigate(
                            R.id.action_favouritesFragment_to_listFragment, bundleLog);
                });
                bt_list.performClick();
            }
        });
        AppCompatButton btChat = getActivity().findViewById(R.id.bt_fav_chat);
        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btChat.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(FavouritesFragment.this).navigate(
                            R.id.action_favouritesFragment_to_listOfChatsFragment, bundleLog);
                });
                btChat.performClick();
            }
        });
        try {
            AppCompatButton btMap = getActivity().findViewById(R.id.bt_fav_map);
            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btMap.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(FavouritesFragment.this).navigate(
                                R.id.action_favouritesFragment_to_mapFragment, bundleLog);
                    });
                    btMap.performClick();
                }
            });
        }catch (Exception e){
            Log.d("FavFragment", "Получение разрешения на определение геолокации");
        }
    }
}