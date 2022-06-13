package com.example.mainproject.fragment;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.mainproject.OpenHelper;
import com.example.android.multidex.mainproject.R;
import com.example.mainproject.domain.Person;
import com.example.mainproject.rest.AppApiVolley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;


public class MainFragment extends Fragment {

    private TextView tv_age, tv_city, tv_dateOfBirth, tv_data, tv_name, tv_forData;
    private AppCompatButton bt_fav, bt_list, bt_chat, bt_map;
    private ImageView iv_ava;
    private ActivityResultLauncher<String> myActivityResultLauncher;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        myActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

                    @Override
                    public void onActivityResult(Uri result) {
                        OpenHelper openHelper = new OpenHelper(getContext(), "OpenHelder",
                                null, OpenHelper.VERSION);
                        Person person = openHelper.findPersonByLogin(getArguments().getString("LOG"));
                        iv_ava.setImageURI(result);
                        FirebaseApp.initializeApp(getContext());
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                        StorageReference uploadImageRef = storageReference.child(
                                "images/" + result.getLastPathSegment());
                        UploadTask uploadTask = uploadImageRef.putFile(result);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                        new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                String uploadedImageUrl = task.getResult().toString();
                                                openHelper.changePhotoByPersonLogin(person.getName(),
                                                        uploadedImageUrl);
                                                new AppApiVolley(getContext()).updatePerson(
                                                        person.getId(), person.getTelephone(), person.getEmail(), person.getName(),
                                                        openHelper.findPersonByLogin(person.getName()).getPhotoPer(),
                                                        person.getAge(), person.getDateOfBirth() ,person.getCity(), person.getPassword());
                                            }
                                        }
                                );
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Не удалось загрузить изображение",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
        );

    }

    @Override
    public void onStart() {
        super.onStart();
        iv_ava = getActivity().findViewById(R.id.iv_avaForFirstTime);
        bt_map = getActivity().findViewById(R.id.bt_main_map);
        bt_list = getActivity().findViewById(R.id.bt_main_list);
        bt_fav = getActivity().findViewById(R.id.bt_main_fav);
        tv_name = getActivity().findViewById(R.id.tv_main_name);
        tv_data = getActivity().findViewById(R.id.tv_main_data);
        tv_dateOfBirth = getActivity().findViewById(R.id.tv_main_dateOfBirth);
        tv_age = getActivity().findViewById(R.id.tv_main_age);
        tv_city = getActivity().findViewById(R.id.tv_main_city);
        tv_forData = getActivity().findViewById(R.id.tv_main_forData);
        String nameVal = getArguments().getString("LOG");
        tv_name.setText(nameVal);
        bt_chat = getActivity().findViewById(R.id.bt_main_chat);


        OpenHelper openHelper = new OpenHelper(getContext(), "OpenHelder",
                null, OpenHelper.VERSION);
        Person client = openHelper.findPersonByLogin(nameVal);
        try{

            if(client.getPhotoPer() != null)
            Picasso.get().load(client.getPhotoPer()).into(iv_ava);
            else iv_ava.setImageDrawable(getResources().getDrawable(R.drawable.ava_for_project));
        }catch (Exception e){
            iv_ava.setImageDrawable(getResources().getDrawable(R.drawable.ava_for_project));
        }

        String dataValue;
        if(client.getTelephone() == null || client.getTelephone().isEmpty() || client.getTelephone().equals("null")){
            tv_forData.setText("Адрес электронной почты: ");
            dataValue = client.getEmail();
        } else{
            tv_forData.setText("Номер телефона : ");
            dataValue = client.getTelephone();
        }
        tv_data.setText(dataValue);
        tv_dateOfBirth.setText(client.getDateOfBirth());
        tv_age.setText(String.valueOf(client.getAge()));
        tv_city.setText(client.getCity());
        Bundle bundleLog = new Bundle();
        bundleLog.putString("LOG", getArguments().getString("LOG"));
        iv_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myActivityResultLauncher.launch("image/*");
            }
        });


        bt_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_fav.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(MainFragment.this).navigate(
                            R.id.action_mainFragment_to_favouritesFragment, bundleLog);
                });
                bt_fav.performClick();
            }
        });
        bt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_list.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(MainFragment.this).navigate(
                            R.id.action_mainFragment_to_listFragment, bundleLog);
                });
                bt_list.performClick();
            }
        });
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_chat.setOnClickListener((view1) -> {
                    NavHostFragment.
                            findNavController(MainFragment.this).navigate(
                            R.id.action_mainFragment_to_listOfChatsFragment, bundleLog);
                });
                bt_chat.performClick();
            }
        });
        try{
            bt_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bt_map.setOnClickListener((view1) -> {
                        NavHostFragment.
                                findNavController(MainFragment.this).navigate(
                                R.id.action_mainFragment_to_mapFragment, bundleLog);
                    });
                    bt_map.performClick();
                }
            });
        }catch (Exception e){
            Log.d("FavFragment", "Получение разрешения на определение геолокации");
        }
    }

}