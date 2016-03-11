package com.example.maurafitzgerald.prog02;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class Representative implements Serializable{
    public String name;
    public String imageID;
    public String role;
    public String party;
    public String twitterID;
    public String bioguideID;
    public String lastTweet;
    public String email;
    public String website;
    public int politicalIcon;
    public String endOfTerm;
    public ArrayList<String> committees;
    public ArrayList<String> bills;
    //TODO: change bills class to have ArrayList details to set subItems - based on API data

    public Representative(String name, String role, String party, String twitterID,
                          String bioguideID, String email, String website, String endOfTerm,
                          ArrayList<String> committees, ArrayList<String> bills) {
        //TODO: get last tweet
        //TODO: get image from twitter
        this.name = name;
        this.role = role;
        this.party = party;
        this.twitterID = twitterID;
        this.bioguideID = bioguideID;
        this.email = email;
        this.website = website;
        this.endOfTerm = endOfTerm;

        this.committees = committees;
        this.bills = bills;
        //TODO: change this
        this.politicalIcon = getPoliticalIcon(party);
        this.lastTweet = "Whassssup mofo";
    }

    public Integer getPoliticalIcon(String party) {
        if (party.equals("Democrat")) {
            return R.drawable.democrat;
        } else if (party.equals("Republican")) {
            return R.drawable.republican;
        } else {
            return R.drawable.independent;
        }
    }
}
