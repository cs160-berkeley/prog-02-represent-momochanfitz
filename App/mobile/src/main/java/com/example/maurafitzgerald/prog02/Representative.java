package com.example.maurafitzgerald.prog02;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class Representative implements Serializable{
    public String name;
    public int imageID;
    public String lastTweet;
    public String email;
    public String website;
    public int politicalIcon;
    public String endOfTerm;
    public ArrayList<String> committees;
    public ArrayList<String> bills;
    //TODO: change bills class to have ArrayList details to set subItems - based on API data

    public Representative(String name, int image, String lastTweet, int politicalParty,
                          String email, String website, String endOfTerm,
                          ArrayList<String> committees, ArrayList<String> bills) {
        this.name = name;
        this.imageID = image;
        this.lastTweet = lastTweet;
        this.politicalIcon = politicalParty;
        this.email = email;
        this.website = website;
        this.endOfTerm = endOfTerm;
        this.committees = committees;
        this.bills = bills;
    }

    public String getPartyName() {
        if (this.politicalIcon == R.drawable.democrat) {
            return "Democrat";
        } else if (this.politicalIcon == R.drawable.republican) {
            return "Republican";
        } else if (this.politicalIcon == R.drawable.independent) {
            return "Independent";
        }
        return "";
    }

}
