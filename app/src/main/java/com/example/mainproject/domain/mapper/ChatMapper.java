package com.example.mainproject.domain.mapper;

import android.content.Context;

import com.example.mainproject.domain.Chat;
import com.example.mainproject.domain.Person;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatMapper {
    public static Chat chatFromJson(JSONObject jsonObject, Context context) {
        Chat chat = null;
        try {

            chat = new Chat(Integer.parseInt(jsonObject.getString("id")),
                    PersonMapper.personFromChatJson(jsonObject, context),
                    OrganizationMapper.organizationFromChatJson(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chat;
    }
    public static Chat chatFromMsgJson(JSONObject jsonObject, Context context) {
        Chat chat = null;
        try {
            chat = chatFromJson(jsonObject.getJSONObject("chatDto"), context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return chat;
    }
}