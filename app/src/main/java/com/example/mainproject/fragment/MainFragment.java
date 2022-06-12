package com.example.mainproject.fragment;


import android.content.Context;
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
import java.io.ByteArrayOutputStream;
import java.util.Arrays;


public class MainFragment extends Fragment {

    private TextView tv_age, tv_city, tv_dateOfBirth, tv_data, tv_name, tv_forData;
    private AppCompatButton bt_fav, bt_list, bt_chat, bt_map;
    private ImageView iv_ava;
    private ActivityResultLauncher<String> myActivityResultLauncher;



    public static SharedPreferences sharedPreferences = SignInFragment.sharedPreferences;


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
                        byte[] photoPer = null;
                        Bitmap bitmap = null;
                        try {
                            iv_ava.buildDrawingCache();
                            bitmap = iv_ava.getDrawingCache().copy(Bitmap.Config.RGB_565, false);
                            iv_ava.setDrawingCacheEnabled(false);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            Bitmap.CompressFormat imFor = Bitmap.CompressFormat.JPEG;
                            bitmap.compress(imFor, 100, stream);
                            photoPer = stream.toByteArray();

                            bitmap.recycle();
                        }catch (Exception e){
                            Log.e("DOWNLOAD IMAGES","Cannot to use a recycled bitmap");
                        }



                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < photoPer.length - 1; i++) {
                            stringBuilder.append(String.valueOf(photoPer[i])).append(" ");
                        }
                        stringBuilder.append(String.valueOf(
                                photoPer[photoPer.length - 1]));

                        editor.putString("per_photo" + person.getName(), stringBuilder.toString());
                        editor.commit();

                        new AppApiVolley(getContext()).updatePerson(
                                person.getId(), person.getTelephone(), person.getEmail(),
                        person.getName(), photoPer,
                                person.getAge(), person.getDateOfBirth() ,person.getCity());
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
        try {
            iv_ava.setImageBitmap(BitmapFactory.decodeByteArray(
                    client.getPhotoPer(), 0, client.getPhotoPer().length));
        }catch (Exception e){
            e.printStackTrace();
        }
        String dataValue;
        if(client.getTelephone() == null || client.getTelephone().isEmpty()){
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
        Log.e("PER_BYTE_ARRAY", Arrays.toString(client.getPhotoPer()));
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


