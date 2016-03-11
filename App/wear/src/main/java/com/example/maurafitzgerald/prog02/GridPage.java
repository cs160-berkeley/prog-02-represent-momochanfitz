package com.example.maurafitzgerald.prog02;

import android.util.Log;

/**
 * Created by maurafitzgerald on 2/29/16.
 */
public class GridPage {
    String name;
    String party;
    int icon;
    String role;

    public GridPage(String name, String party, int icon, String role) {
        this.name = name;
        this.party = party;
        this.icon = icon;
        this.role = role;
        Log.d("in grid page", "ROLE: " + role);
    }
}
