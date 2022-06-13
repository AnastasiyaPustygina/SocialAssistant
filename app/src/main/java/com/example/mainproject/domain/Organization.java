package com.example.mainproject.domain;

import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.mainproject.fragment.SignInFragment;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class Organization {
    private int id;
    private String name;
    private String type;
    private String photoOrg;
    private String description;
    private String address;
    private String needs;
    private String linkToWebsite;

    public Organization(String name, String type, String photoOrg,
                        String description, String address,
                        String needs, String linkToWebsite) {
        this.name = name;
        this.type = type;
        this.photoOrg = photoOrg;
        this.description = description;
        this.address = address;
        this.needs = needs;
        this.linkToWebsite = linkToWebsite;
    }


    public Organization(String name, String type, String photoOrg,
                        String description, String address, String needs) {
        this.name = name;
        this.type = type;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap.CompressFormat imFor = Bitmap.CompressFormat.PNG;
        this.photoOrg = photoOrg;
        this.description = description;
        this.address = address;
        this.needs = needs;
    }

    public Organization(int id, String name, String type, String photoOrg, String description,
                        String address, String needs, String linkToWebsite) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.photoOrg = photoOrg;
        this.description = description;
        this.address = address;
        this.needs = needs;
        this.linkToWebsite = linkToWebsite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPhotoOrg() {
        return photoOrg;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getNeeds() {
        return needs;
    }

    public String getLinkToWebsite() {
        return linkToWebsite;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", needs='" + needs + '\'' +
                ", linkToWebsite='" + linkToWebsite + '\'' +
                '}';
    }
}