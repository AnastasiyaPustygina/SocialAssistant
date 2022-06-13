package com.example.mainproject.domain.mapper;

import android.content.Context;


import com.example.mainproject.domain.Message;

import org.json.JSONException;
import org.json.JSONObject;

public class MessageMapper {
    public static Message messageFromJson(JSONObject jsonObject, Context context) {
        Message message = null;
        try {

            message = new Message(Integer.parseInt(jsonObject.getString("id")),
                    jsonObject.getString("whose"),
                    ChatMapper.chatFromMsgJson(jsonObject, context).getId(),
                    jsonObject.getString("value"),
                    jsonObject.getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }
}