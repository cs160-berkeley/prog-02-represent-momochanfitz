package com.example.maurafitzgerald.prog02;

import com.google.android.gms.wearable.DataMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by maurafitzgerald on 2/29/16.
 */
public class RepData implements Serializable{
    //TODO: implement API to grab real data

    private ArrayList<Representative> representatives = new ArrayList<>();
    private int numReps;
    public String zipCode;
    public String county;
    public String state;
    public String percent_romney;
    public String percent_obama;

    public RepData(String zipCode) {
        this.zipCode = ""+zipCode;
        this.numReps = 3;

        //Replace with API calls
        this.county = "Los Angeles";
        this.state = "CA";
        this.percent_obama = "95%";
        this.percent_romney = "5%";

        // FAKE DATA FOR NOW
        ArrayList<String> dianneCommittees = new ArrayList<>(Arrays.asList(
                "Commerce, Science, and Transportation", "Environment and Public Works",
                "Foreign Relations", "Select Committee on Ethics"));
        ArrayList<String> dianneBills = new ArrayList<>(Arrays.asList(
                "Bill1", "Bill2", "Bill3"));
        Representative dianne = new Representative("Senator Dianne Feinstein", R.drawable.dianne,
                "Whether it's the wildflowers or the tortoises, the desert has so much unique" +
                        " wildlife and vegetation. #ProtectCADesert", R.drawable.democrat,
                "senator@feinstein.senate.gov", "http://feinstein.senate.gov/", "March 1st, 2015",
                dianneCommittees, dianneBills);

        ArrayList<String> barbaraBills = new ArrayList<>(Arrays.asList(
                "Bill1", "Bill2", "Bill3"));
        ArrayList<String> barbaraCommittees = new ArrayList<>(Arrays.asList(
                "Commerce, Science, and Transportation", "Environment and Public Works",
                "Foreign Relations", "Select Committee on Ethics"));
        Representative barbara = new Representative("Senator Barbara Boxer", R.drawable.barbara,
                "Great news from @POTUS! New national monuments will permanently protect 1.8" +
                        " million acres of CA desert. http://lat.ms/1QaP6hq", R.drawable.republican,
                "senator@boxer.senate.gov", "http://www.boxer.senate.gov/", "April 4th, 2015",
                barbaraCommittees, barbaraBills);

        ArrayList<String> mauraBills = new ArrayList<>(Arrays.asList(
                "Bill1", "Bill2", "Bill3"));
        ArrayList<String> mauraCommittees = new ArrayList<>(Arrays.asList(
                "Commerce, Science, and Transportation", "Environment and Public Works",
                "Foreign Relations", "Select Committee on Ethics"));
        Representative maura = new Representative("Senator Maura Fitzgerald", R.drawable.ted,
                "I am not actually a senator, but I also care about the desert.",
                R.drawable.independent, "momochanfitz@gmail.com", "http://github.com/momochanfitz",
                "January 10th, never", mauraCommittees, mauraBills);
        Representative[] reps = {dianne, barbara, maura};

        for (int i = 0; i < numReps; i++) {
            representatives.add(reps[i]);
        }
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
    public String[] getAllParties() {
        String[] parties = new String[numReps];
        for (int i = 0; i < numReps; i++) {
            parties[i] = getRepresentative(i).getPartyName();
        }
        return parties;
    }

    public void shuffleRepresentatives() {
        //TODO: remove this, only to show shaking watch causes change
        Collections.shuffle(this.representatives);
    }


}
