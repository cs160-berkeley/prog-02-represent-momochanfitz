package com.example.maurafitzgerald.prog02;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


import javax.net.ssl.HttpsURLConnection;


/**
 * Created by maurafitzgerald on 2/29/16.
 */
public class RepData implements Serializable {
    //TODO: implement API to grab real data

    private ArrayList<Representative> representatives = new ArrayList<>();
    private int numReps = 0;
    public String county;
    public String countyDataName;
    public String state;
    public String percent_romney;
    public String percent_obama;
    public MyLocation myLoc;
    public String dataReturned;
    public boolean apiComplete = false;
    public boolean emptyAPI = false;

    public RepData(final MyLocation myLoc) {
        this.myLoc = myLoc;
        this.county = myLoc.county;
        this.countyDataName = this.county.replace(" County", "");
        Log.d("d", "COUNTY DATA NAME: " + this.countyDataName);
        this.state = myLoc.state;
        this.percent_obama = "95%";
        this.percent_romney = "5%";
        new connectToSunlight().execute();
    }



    public ArrayList<String> getCommittees(String id) {
        try {
            String apikey = "3d0bdbc85b1b46649e7ceb0ebb3fe95e";
            String baseURL = "https://congress.api.sunlightfoundation.com";
            String idAddition = "/committees?apikey=" + apikey + "&member_ids=" + id;
            String url = baseURL + idAddition;
            URL apiurl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) apiurl.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String committeeData = stringBuilder.toString();
                JSONObject jsonRootObject = new JSONObject(committeeData);
                JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                ArrayList<String> committeeArray = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    // Representative Attributes
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String committee = jsonObject.optString("name");
                    if (!committee.equals("null")) {
                        committeeArray.add(committee);
                    }
                }
                return committeeArray;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<String> getBills(String id) {
        try {
            String apikey = "3d0bdbc85b1b46649e7ceb0ebb3fe95e";
            String baseURL = "https://congress.api.sunlightfoundation.com";
            String idAddition = "/bills?apikey=" + apikey + "&sponsor_id=" + id;
            String url = baseURL + idAddition;
            URL apiurl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) apiurl.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                String committeeData = stringBuilder.toString();
                JSONObject jsonRootObject = new JSONObject(committeeData);
                JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                ArrayList<String> billArray = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String bill = jsonObject.optString("short_title");
                    if (!bill.equals("null")) {
                        billArray.add(bill);
                    }
                }
                return billArray;
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private class connectToSunlight extends AsyncTask<URL, Integer, String> {
        // Do the long-running work in here
        protected String doInBackground(URL... urls) {
            try {
                String apikey = "3d0bdbc85b1b46649e7ceb0ebb3fe95e";
                String baseURL = "https://congress.api.sunlightfoundation.com";
                String zipCodeAddition;
                if (myLoc.zip != "") {
                    Log.d("d", "MYZIP: " + myLoc.zip);
                    zipCodeAddition = "/legislators/locate?apikey=" + apikey + "&zip="
                            + myLoc.zip;
                } else {
                    zipCodeAddition = "/legislators/locate?apikey=" + apikey + "&latitude="
                            + myLoc.lat + "&longitude=" + myLoc.lng;
                }
                String url = baseURL + zipCodeAddition;
                URL apiurl = new URL(url);
                HttpsURLConnection urlConnection = (HttpsURLConnection) apiurl.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    dataReturned = stringBuilder.toString();
                    JSONObject jsonRootObject = new JSONObject(dataReturned);
                    JSONArray jsonArray = jsonRootObject.optJSONArray("results");
                    if (jsonArray.length() == 0) {
                        emptyAPI = true;
                        apiComplete = true;
                        return "finished";
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        // Representative Attributes
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.optString("title");
                        String role = getRole(title);
                        String f_name = jsonObject.optString("first_name");
                        String l_name = jsonObject.optString("last_name");
                        String name = f_name + " " + l_name;
                        String p_party = jsonObject.optString("party");
                        String party = getParty(p_party);
                        String email = jsonObject.optString("oc_email");
                        String website = jsonObject.optString("website");
                        String termEnd = jsonObject.optString("term_end");
                        String twitterID = jsonObject.optString("twitter_id");
                        String bioguideID = jsonObject.optString("bioguide_id");
                        ArrayList<String> committees = getCommittees(bioguideID);

                        ArrayList<String> bills = getBills(bioguideID);

                        // FAKE BILLS/COMMITTEES
                        // TODO: get real bills and committees
                        ArrayList<String> fakeBills = new ArrayList<>(Arrays.asList(
                                "Bill1", "Bill2", "Bill3"));

                        Representative currentRep = new Representative(name, role, party, twitterID,
                                bioguideID, email, website, termEnd, committees, bills);

                        Log.d("d", name);
                        Log.d("d", twitterID);
                        representatives.add(currentRep);
                        numReps++;
                    }
                    apiComplete = true;
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "finished";
        }
    }


    public String getRole(String title) {
        if (title.equals("Sen")) {
            return "Senator ";
        } else if (title.equals("Rep")) {
            return "Representative ";
        }
        return "";
    }

    public String getParty(String party) {
        if (party.equals("D")) {
            return "Democrat";
        } else if (party.equals("R")) {
            return "Republican";
        } else if (party.equals("I")) {
            return "Independent";
        }
        return "";
    }


    public Representative getRepresentative(int position) {
        return representatives.get(position);
    }

    public ArrayList<Representative> getAllRepresentatives() {
        return representatives;
    }

    public String[] getAllNames() {
        String[] names = new String[numReps];
        for (int i = 0; i < numReps; i++) {
            names[i] = getRepresentative(i).name;
        }
        return names;
    }

    public String[] getAllRoles() {
        String[] roles = new String[numReps];
        for (int i = 0; i < numReps; i++) {
            roles[i] = getRepresentative(i).role;
        }
        return roles;
    }

    public String[] getAllParties() {
        String[] parties = new String[numReps];
        for (int i = 0; i < numReps; i++) {
            parties[i] = getRepresentative(i).party;
        }
        return parties;
    }
}
