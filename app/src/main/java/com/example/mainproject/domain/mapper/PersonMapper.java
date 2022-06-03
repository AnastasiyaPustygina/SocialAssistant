package com.example.mainproject.domain.mapper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mainproject.OpenHelper;
import com.example.mainproject.domain.Person;
import com.example.mainproject.fragment.SignInFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonMapper {
    public static Person personFromJson(JSONObject jsonObject, Context context) {
        Person person = null;
        try {
            OpenHelper openHelper = new OpenHelper(context, "OpenHelder", null, OpenHelper.VERSION);
            String data = jsonObject.getString("telephone").isEmpty() ?
                    jsonObject.getString("email") : jsonObject.getString("telephone");
            SharedPreferences sharedPreferences = SignInFragment.sharedPreferences;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("per_photo" + jsonObject.getString("name"),
                    jsonObject.getString("photo"));
            editor.commit();
            person = new Person(jsonObject.getInt("id"), data,
                    jsonObject.getString("name"), jsonObject.getInt("age"),
                    jsonObject.getString("date_of_birth"),
                    jsonObject.getString("city"),
                    jsonObject.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public static Person personFromChatJson(JSONObject jsonObject, Context context) {
        Person person = null;
        try {
            person = personFromJson(jsonObject.getJSONObject("personDto"), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }
}