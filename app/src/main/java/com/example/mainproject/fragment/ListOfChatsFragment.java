package com.example.mainproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainproject.adapter.ChatListArrayAdapter;
import com.example.android.multidex.mainproject.R;
import com.example.mainproject.rest.AppApiVolley;

public class ListOfChatsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_of_chats_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = getActivity().findViewById(R.id.rec_listOfChats);
        ChatListArrayAdapter chatListArrayAdaptor = new ChatListArrayAdapter(getContext(),
                ListOfChatsFragment.this, getArguments().getString("LOG"));
        recyclerView.setAdapter(chatListArrayAdaptor);
        AppCompatButton btFav, btProfile, btList, btMap;
        btMap = getActivity().findViewById(R.id.bt_listOfChats_maps);
        btFav = getActivity().findViewById(R.id.bt_listOfChats_fav);
        btProfile = getActivity().findViewById(R.id.bt_listOfChats_profile);
        btList = getActivity().findViewById(R.id.bt_listOfChats_list);
        Bundle bundleLog = new Bundle();
        bundleLog.putString("LOG", getArguments().getString("LOG"));
        btFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btFav.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListOfChatsFragment.this).navigate(
                            R.id.action_listOfChatsFragment_to_favouritesFragment, bundleLog);
                });
                btFav.performClick();
            }
        });
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btProfile.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListOfChatsFragment.this).navigate(
                            R.id.action_listOfChatsFragment_to_mainFragment, bundleLog);
                });
                btProfile.performClick();
            }
        });

        btList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btList.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListOfChatsFragment.this).navigate(
                            R.id.action_listOfChatsFragment_to_listFragment, bundleLog);
                });
                btList.performClick();
            }
        });
        try {
            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btMap.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(ListOfChatsFragment.this).navigate(
                                R.id.action_listOfChatsFragment_to_mapFragment, bundleLog);
                    });
                    btMap.performClick();
                }
            });
        }catch (Exception e){
            Log.d("FavFragment", "Получение разрешения на определение геолокации");
        }
    }
}