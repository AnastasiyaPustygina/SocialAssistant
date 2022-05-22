package com.example.mainproject.rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mainproject.MainActivity;
import com.example.mainproject.OpenHelper;
import com.example.mainproject.R;
import com.example.mainproject.domain.Chat;
import com.example.mainproject.domain.Message;
import com.example.mainproject.domain.Organization;
import com.example.mainproject.domain.Person;
import com.example.mainproject.domain.mapper.ChatMapper;
import com.example.mainproject.domain.mapper.MessageMapper;
import com.example.mainproject.domain.mapper.OrganizationMapper;
import com.example.mainproject.fragment.ListFragment;
import com.example.mainproject.fragment.MainFragment;
import com.google.protobuf.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AppApiVolley implements  AppApi {



    private final Context context;
    public static final String BASE_URL = "http://192.168.1.33:8081";
    private com.android.volley.Response.ErrorListener errorListener;


    public AppApiVolley(Context context) {
        this.context = context;
        errorListener = new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API_TEST", error.toString());
                error.printStackTrace();
            }
        };
    }
    @Override
    public void fillOrganization() {
        String url = BASE_URL + "/organization";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(
                                context, "OpenHelder", null, OpenHelper.VERSION);
                        openHelper.deleteAllOrganization();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Organization organization = OrganizationMapper.
                                        organizationFromJson(jsonObject);

                                ArrayList<String> arrListName = new ArrayList<String>();
                                for (int j = 0; j < openHelper.findAllOrganizations().size(); j++) {
                                    arrListName.add(openHelper.findAllOrganizations().get(j).getName());
                                }

                                if(!arrListName.contains(organization.getName())) {
                                    openHelper.insertOrg(new Organization(organization.getName(),
                                            organization.getType(),
                                            organization.getDescription(), organization.getAddress(),
                                            organization.getNeeds(), organization.getLinkToWebsite()));
                                    Log.e("API_TEST_FILL_ORG", organization.getName());
                                }
                                else{
                                    openHelper.changeDescByLog(organization.getName(),
                                            organization.getDescription());
                                    openHelper.changeNeedsByLog(organization.getName(),
                                            organization.getNeeds());
                                }
                            }
                            Log.e("API_TEST_FILL_ORG_ALL", openHelper.findAllOrganizations().toString());
                        }catch (JSONException e) {
                            Log.e("API_TEST_FILL_ORG", e.getMessage());
                        }
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }

    @Override
    public void addPerson(Person person) {
        String url = BASE_URL + "/person";

        JSONObject params = new JSONObject();
        try {
            params.put("id", person.getId());
            params.put("name", person.getName());
            params.put("telephone", person.getTelephone());
            params.put("email", person.getEmail());
            params.put("city", person.getCity());

            SharedPreferences sharedPreferences = MainFragment.sharedPreferences;

            params.put("photo", sharedPreferences.getString("per_photo" + person.getName(),
                    "CANNOT_FIND_PERSON_PHOTO_PREF"));
            params.put("date_of_birth", person.getDateOfBirth());
            params.put("age", person.getAge());
        } catch (JSONException e) {
            Log.e("API_TASK_ADD_PER", e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("API_TEST_ADD_PERSON", response.toString());
            }
        }, errorListener
        );
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        referenceQueue.add(jsonObjectRequest);
    }

    @Override
    public void updatePerson(int id, String telephone, String email, String name, byte[] photoPer,
                                   int age, String dateOfBirth, String city) {
        String url = BASE_URL + "/person/" + id;
        Log.e("UPDATE_PER", id + " " + name + " " + telephone + " "
                + email + " " + age + " " + dateOfBirth + " " + city);
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("UPDATE_PER_PHOTO", Arrays.toString(photoPer));
                        SharedPreferences sharedPreferences = MainFragment.sharedPreferences;
                        String photo = sharedPreferences.getString("per_photo" + name,
                                "notPerPhotoInPref");


                        Log.e("AFTER_UPDATE_PER_PHOTO", photo);
                    }
                },
                errorListener){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                if(telephone != null) {
                    params.put("telephone", telephone);
                    params.put("email", "");
                }
                else{
                    params.put("telephone", "");
                    params.put("email", email);
                }
                params.put("city", city);
                SharedPreferences sharedPreferences = MainFragment.sharedPreferences;
                String photo = sharedPreferences.getString("per_photo" + name, "notPerPhotoInPref");
                params.put("photo", photo);
                params.put("date_of_birth", dateOfBirth);
                params.put("age", age + "");
                return params;
            }
        };
        referenceQueue.add(stringRequest);



    }



    @Override
    public void addChat(Chat chat) {
        String url = BASE_URL + "/chat";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fillChats();
                        Log.d("API_TEST_ADD_CHAT", response);
                    }
                },
                errorListener){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = MainFragment.sharedPreferences;
                String personStr = chat.getPerson().getId() + "!" + chat.getPerson().getName() +
                        "!" + chat.getPerson().getTelephone() + "!" + chat.getPerson().getEmail() +
                        "!" + chat.getPerson().getCity() + "!" +
                        sharedPreferences.getString("per_photo" + chat.getPerson().getName(),
                                "FILL_CHAT_CANNOT_PERSON_PHOTO")
                         + "!" + chat.getPerson().getDateOfBirth() + "!"+
                chat.getPerson().getAge();
                String orgStr = chat.getOrganization().getId() + "!" + chat.getOrganization().getName()
                        + "!" + chat.getOrganization().getType() + "!" +
                        sharedPreferences.getString("org_photo" + chat.getOrganization().getAddress(), "FILL_CHAT_CANNOT_ORG_PHOTO")
                        + "!" + chat.getOrganization().getDescription() + "!" +
                        chat.getOrganization().getAddress() + "!" + chat.getOrganization().getNeeds() +
                        "!" + chat.getOrganization().getLinkToWebsite();

                Log.e("ORGANIZATION_STR", orgStr);
                Log.e("PERSON_STR", personStr);
                Log.e("CHAT_ID", chat.getId() + "");

                params.put("id", chat.getId() + "");
                params.put("strPerson", personStr);
                params.put("strOrganization", orgStr);
                return params;
            }
        };
        Log.e("INSERT CHAT", chat.toString());
        referenceQueue.add(stringRequest);
    }

    @Override
    public void fillChats() {
        String url = BASE_URL + "/chat";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(context,
                                "OpenHelder", null, OpenHelper.VERSION);
                        openHelper.deleteAllChat();
                        Chat chat = null;
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                chat = ChatMapper.chatFromJson(jsonObject, context);
                                openHelper.insertChat(chat);
                            }
                        }catch (JSONException e) {
                            Log.e("API_TEST_FILL_CHAT", e.getMessage());
                        }
                        if(chat != null) Log.e("FILL CHAT", chat.toString() + "");
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }
    @Override
    public void fillMsg() {
        String url = BASE_URL + "/message";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        OpenHelper openHelper = new OpenHelper(context, "OpenHelder",
                                null, OpenHelper.VERSION);
                        openHelper.deleteAllMessage();
                        Log.e("API_TEST_FILL_MSG", response.length() + "");
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Message message = MessageMapper.messageFromJson(jsonObject, context);
                                openHelper.insertMsg(message);
                                Log.e("FILL MSG", message.toString());

                            }
                        }catch (JSONException e) {
                            Log.e("API_TEST", e.getMessage());
                        }
                        try {
                            Log.e("AFTER FILL MSG", openHelper.findAllMsg().toString());
                        }catch (Exception e){
                            Log.e("AFTER FILL MSG", e.getMessage());
                        }
                    }
                },
                errorListener);
        referenceQueue.add(jsonArrayRequest);
    }
//    @Override
//    public void fillMessageByChatID(int chat_id) {
//        String url = BASE_URL + "/chat/" + chat_id + "/message";
//        RequestQueue referenceQueue = Volley.newRequestQueue(context);
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try {
//                        OpenHelper openHelper = new OpenHelper(context,
//                                "OpenHelder", null, OpenHelper.VERSION);
//                            openHelper.deleteMsgByChatId(chat_id);
//
//                        Message message = null;
//                        Log.e("API_TEST_MSG", response.length() + "");
//                        try {
//                            for (int i = 0; i < response.length(); i++) {
//                                JSONObject jsonObject = response.getJSONObject(i);
//                                message = MessageMapper.messageFromJson(jsonObject, context);
//                                openHelper.insertMsg(message);
//                            }
//
//                        }catch (JSONException e) {
//                            Log.e("API_TEST_FILL_MSG", e.getMessage());
//                        }
//                        if(message != null) Log.e("FILL MSG", message + "");
//                        }catch (CursorIndexOutOfBoundsException e){
//                            Log.e("No message in chat", chat_id + "");
//                        }
//                    }
//                },
//                errorListener);
//        referenceQueue.add(jsonArrayRequest);
//    }

    @Override
    public void addMessage(Message message) {
        String url = BASE_URL + "/message";
        RequestQueue referenceQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API_TEST_ADD_MSG", response);
                    }
                },
                errorListener){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                OpenHelper openHelper = new OpenHelper(
                        context, "OpenHelder", null, OpenHelper.VERSION);
                Chat chat = openHelper.findChatById(message.getChat_id());

                SharedPreferences sharedPreferences = MainFragment.sharedPreferences;
                String personStr = chat.getPerson().getId() + "!" + chat.getPerson().getName() +
                        "!" + chat.getPerson().getTelephone() + "!" + chat.getPerson().getEmail() +
                        "!" + chat.getPerson().getCity() + "!" +
                        sharedPreferences.getString("per_photo" + chat.getPerson().getName()
                                , "FILL_CHAT_CANNOT_PERSON_PHOTO")
                        + "!" + chat.getPerson().getDateOfBirth() + "!"+
                        chat.getPerson().getAge();
                String orgStr = chat.getOrganization().getId() + "!" + chat.getOrganization().getName()
                        + "!" + chat.getOrganization().getType() + "!" +
                        sharedPreferences.getString("org_photo" + chat.getOrganization().getAddress(), "FILL_CHAT_CANNOT_ORG_PHOTO")
                        + "!" + chat.getOrganization().getDescription() + "!" +
                        chat.getOrganization().getAddress() + "!" + chat.getOrganization().getNeeds() +
                        "!" + chat.getOrganization().getLinkToWebsite();

                params.put("id", message.getId() + "");
                params.put("whose", message.getWhose());
                params.put("value", message.getValues());
                params.put("time", message.getTime());
                params.put("chat_id", message.getChat_id() + "");
                params.put("strPerson", personStr);
                params.put("strOrganization", orgStr);

                return params;
            }
        };
        Log.e("INSERT MESSAGE", message.toString());
        referenceQueue.add(stringRequest);
    }
}
