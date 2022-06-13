package com.example.mainproject.adapter;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainproject.OpenHelper;
import com.example.android.multidex.mainproject.R;
import com.example.mainproject.fragment.FavouritesFragment;
import com.example.mainproject.fragment.ListFragment;
import com.example.mainproject.fragment.MapFragment;
import com.example.mainproject.domain.Chat;
import com.example.mainproject.domain.Organization;
import com.example.mainproject.domain.Person;
import com.example.mainproject.rest.AppApiVolley;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrgArrayAdapter extends RecyclerView.Adapter<OrgArrayAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Organization> arrayOrg;

    private String nameOfPerson;
    private Fragment fragment;

    public OrgArrayAdapter(Context context, List<Organization> arrayOrg, String nameOfPerson,
                           Fragment fragment) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.arrayOrg = arrayOrg;
        this.nameOfPerson = nameOfPerson;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.short_description, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Organization organization = arrayOrg.get(position);
        OpenHelper openHelper1 = new OpenHelper(context, "OpenHelder", null, OpenHelper.VERSION);
        Person person = openHelper1.findPersonByLogin(nameOfPerson);

        try {
            if (openHelper1.findFavOrgByLogin(nameOfPerson).contains(organization.getName()))
                holder.btIdenFav.setBackgroundResource(R.drawable.bt_fav_true);
            else holder.btIdenFav.setBackgroundResource(R.drawable.bt_fav_false);
        }catch (Exception e){
            Log.e("findFavOrgByLogin", e.getMessage());

        }


        holder.nameOrg.setText(organization.getName());
        holder.typeOrg.setText("Тип: " + organization.getType());
        holder.needsOrg.setText("Потребности: " + organization.getNeeds());
        holder.ph.setImageDrawable(context.getResources().getDrawable(R.drawable.ava_for_project));
        try{
            if(organization.getPhotoOrg() != null && !organization.getPhotoOrg().equals("null")) {
                Log.e("notNullPhoto", organization.getPhotoOrg());
                Picasso.get().load(organization.getPhotoOrg()).into(holder.ph);
            }
        }catch (Exception e){
            holder.ph.setImageDrawable(context.getResources().getDrawable(R.drawable.ava_for_project));
        }
        holder.btIdenFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                person.changeFavOrg(organization.getName());
                if(!openHelper1.findFavOrgByLogin(nameOfPerson).contains(organization.getName()))
                    holder.btIdenFav.setBackgroundResource(R.drawable.bt_fav_true);
                else holder.btIdenFav.setBackgroundResource(R.drawable.bt_fav_false);
                openHelper1.changeFavOrg(nameOfPerson, organization.getName());
            }
        });
        holder.fullInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("NameOrg", holder.nameOrg.getText().toString());
                bundle.putString("LOG", nameOfPerson);
                if(fragment.getClass().equals(ListFragment.class)) {
                    bundle.putString("PrevFragment", "list");
                    holder.fullInfo.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_listFragment_to_fullInfoFragment, bundle);
                    });
                }
                else if(fragment.getClass().equals(FavouritesFragment.class)) {
                    bundle.putString("PrevFragment", "fav");
                    holder.fullInfo.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_favouritesFragment_to_fullInfoFragment, bundle);
                    });
                }
                else if(fragment.getClass().equals(MapFragment.class)) {
                    bundle.putString("PrevFragment", "map");
                    holder.fullInfo.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_mapFragment_to_fullInfoFragment, bundle);
                    });
                }
                holder.fullInfo.performClick();
            }
        });
        holder.btHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat chat = new Chat(
                        person, organization);
                try{
                    if (openHelper1.findChatIdByOrgIdAndPerId(
                            organization.getId(), person.getId()) == -100) {
                        Log.e("ooabh", openHelper1.findAllChats().toString());

                        chat = new Chat(
                                openHelper1.findPersonByLogin(nameOfPerson), organization);
                        openHelper1.insertChat(chat);
                        new AppApiVolley(context).addChat(openHelper1.findChatByPersonIdAndOrgId(
                                openHelper1.findPersonByLogin(nameOfPerson).getId(), organization.getId()));
                    }
                }catch (CursorIndexOutOfBoundsException e){
                    new AppApiVolley(context).addChat(chat);
                }
                Bundle bundleNameOfOrg = new Bundle();
                bundleNameOfOrg.putString("NameOrg", holder.nameOrg.getText().toString());
                bundleNameOfOrg.putString("LOG", nameOfPerson);
                if(fragment.getClass().equals(ListFragment.class)) {
                    holder.btHelp.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_listFragment_to_chatFragment, bundleNameOfOrg);
                    });
                }
                else {
                    holder.btHelp.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(fragment).navigate(
                                R.id.action_favouritesFragment_to_chatFragment, bundleNameOfOrg);
                    });
                }
                holder.btHelp.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayOrg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOrg, typeOrg, needsOrg;
        ImageView ph;
        AppCompatButton btIdenFav;
        AppCompatButton btHelp, fullInfo;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            btIdenFav = itemView.findViewById(R.id.bt_fav_identifier);
            nameOrg = itemView.findViewById(R.id.shDes_name);
            typeOrg = itemView.findViewById(R.id.shDes_type);
            needsOrg = itemView.findViewById(R.id.shDes_needs);
            ph = itemView.findViewById(R.id.shDes_photoOrg);
            btHelp = itemView.findViewById(R.id.bt_shDes_help);
            fullInfo = itemView.findViewById(R.id.bt_shDes_fullInfo);

        }
    }

}