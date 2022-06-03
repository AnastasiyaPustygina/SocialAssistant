package com.example.mainproject.rest;

import android.graphics.Bitmap;
import android.icu.text.StringPrepParseException;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mainproject.domain.Chat;
import com.example.mainproject.domain.Message;
import com.example.mainproject.domain.Person;

public interface AppApi {

    void fillPerson();

    void fillOrganization();

    void addPerson(Person person);

    void addChat(Chat chat);

    void fillChats();

    void checkNewMsg();

    void fillMsg();
    void addMessage(Message message);
    void updatePerson(int id, String telephone, String email, String name, byte[] photoPer,
                      int age, String dateOfBirth, String city, String password);

}




