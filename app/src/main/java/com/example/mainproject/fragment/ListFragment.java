package com.example.mainproject.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

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
import com.example.mainproject.domain.Person;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private AppCompatButton bt_prof, bt_fav;
    private OrgArrayAdapter orgArrayAdapter;
    private RecyclerView recyclerView;
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, container, false);
    }

    @Override
    public void onStart() {

        super.onStart();
        bt_prof = getActivity().findViewById(R.id.bt_list_prof);
        bt_fav = getActivity().findViewById(R.id.bt_list_fav);
        spinner = getActivity().findViewById(R.id.sp_list);
        OpenHelper openHelper = new OpenHelper(getContext(), "OpenHelder", null, OpenHelper.VERSION);
        Person person = openHelper.findPersonByLogin(getArguments().getString("LOG"));

        recyclerView = getActivity().findViewById(R.id.rec_list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Organization> arListOrg = new ArrayList<Organization>();
                if(i == 1){
                    arListOrg =
                            (ArrayList<Organization>) openHelper.findOrgByCity(person.getCity());

                }
                if(i == 2){
                    arListOrg =
                            (ArrayList<Organization>) openHelper.findOrgByType("Детский дом");
                }
                if(i == 3){
                    arListOrg =
                            (ArrayList<Organization>) openHelper.findOrgByType("Дом престарелых");
                }
                if(i == 4){
                    arListOrg =
                            (ArrayList<Organization>) openHelper.findOrgByType("Хоспис");
                }
                if(i == 0){
                    arListOrg = openHelper.findAllOrganizations();
                }

                orgArrayAdapter =
                        new OrgArrayAdapter(getContext(), arListOrg, getArguments().getString("LOG"),
                                ListFragment.this);
                recyclerView.setAdapter(orgArrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                orgArrayAdapter =
                        new OrgArrayAdapter(getContext(), openHelper.findAllOrganizations(), getArguments().getString("LOG"),
                                ListFragment.this);
                recyclerView.setAdapter(orgArrayAdapter);
            }
        });

        Bundle bundleLog = new Bundle();
        bundleLog.putString("LOG", getArguments().getString("LOG"));
        bt_prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_prof.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListFragment.this).navigate(
                            R.id.action_listFragment_to_mainFragment, bundleLog);
                });
                bt_prof.performClick();
            }
        });
        bt_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_fav.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListFragment.this).navigate(
                            R.id.action_listFragment_to_favouritesFragment, bundleLog);
                });
                bt_fav.performClick();
            }
        });

        AppCompatButton btChat = getActivity().findViewById(R.id.bt_list_chat);
        btChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btChat.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(ListFragment.this).navigate(
                            R.id.action_listFragment_to_listOfChatsFragment, bundleLog);
                });
                btChat.performClick();
            }
        });
        AppCompatButton btMap = getActivity().findViewById(R.id.bt_list_map);
        try {
            btMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btMap.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(ListFragment.this).navigate(
                                R.id.action_listFragment_to_mapFragment, bundleLog);
                    });
                    btMap.performClick();
                }
            });
        }catch (Exception e){
            Log.d("FavFragment", "Получение разрешения на определение геолокации");
        }
    }
}