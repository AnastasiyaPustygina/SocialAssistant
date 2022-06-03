package com.example.mainproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mainproject.OpenHelper;
import com.example.android.multidex.mainproject.R;

public class SignInFragment extends Fragment {
    private EditText ed_data;
    private EditText ed_pass;
    private AppCompatButton btSignIn, btReg;
    public static final String APP_PREFERENCES = "my_pref";
    public static SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }


   @Override
    public void onStart() {
        super.onStart();
       sharedPreferences = getContext().getSharedPreferences
               (APP_PREFERENCES, Context.MODE_PRIVATE);
       TextView checking = getActivity().findViewById(R.id.tv_check);
        ed_data = getActivity().findViewById(R.id.ed_signIn_data);
        ed_pass = getActivity().findViewById(R.id.ed_signIn_pass);
        btSignIn = getActivity().findViewById(R.id.bt_signIn_fr_signIn);
        btReg = getActivity().findViewById(R.id.bt_reg_fr_signIn);
       btReg.setOnClickListener((view1) -> {
           NavHostFragment.findNavController(SignInFragment.this).navigate(
                   R.id.action_signInFragment_to_regFragment);
       });
       btSignIn.performClick();
        OpenHelper oh = new OpenHelper(getContext(), "OpenHelder", null, OpenHelper.VERSION);
        try {
            Log.e("sif", oh.findAllChats().toString());
        } catch (Exception e) {e.printStackTrace();}

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_pass.getText().toString().isEmpty() || ed_data.getText().toString().isEmpty())
                    checking.setText("Не все поля заполнены");
                else if ((ed_pass.getText().toString().hashCode() + "").equals(oh.findPassByLogin(ed_data.getText().toString())))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("LOG", ed_data.getText().toString());
                    btSignIn.setOnClickListener((view1) -> {
                        NavHostFragment.findNavController(SignInFragment.this).navigate(
                                R.id.action_signInFragment_to_mainFragment, bundle);
                    });
                    btSignIn.performClick();
                } else {
                    checking.setText("Логин или пароль не верны ");
                }
            }
        });

    }

}
