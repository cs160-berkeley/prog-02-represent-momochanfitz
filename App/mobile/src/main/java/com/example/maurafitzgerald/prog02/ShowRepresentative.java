package com.example.maurafitzgerald.prog02;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class ShowRepresentative extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_representative);

        ScrollView rl = (ScrollView) findViewById(R.id.lin_layout);
        rl.setBackgroundColor(ContextCompat.getColor(this, R.color.background));

        Intent intent = getIntent();
        int pos = intent.getIntExtra(ListRepresentatives.EXTRA_MESSAGE, 0);
        RepData repData = (RepData)intent.getSerializableExtra("DATA");

        Typeface fauna = Typeface.createFromAsset(getResources().getAssets(), "FaunaOne-Regular.ttf");
        Typeface playfair = Typeface.createFromAsset(getResources().getAssets(), "PlayfairDisplay-Regular.ttf");

        Representative current = repData.getRepresentative(pos);
        String name = current.name;
        getSupportActionBar().setTitle(name);

        TextView endHeader = (TextView) findViewById(R.id.end_header);
        endHeader.setTextColor(ContextCompat.getColor(this, R.color.text));
        endHeader.setTypeface(playfair);

        TextView comText = (TextView) findViewById(R.id.comText);
        comText.setTextColor(ContextCompat.getColor(this, R.color.text));
        comText.setTypeface(playfair);

        TextView billText = (TextView) findViewById(R.id.billText);
        billText.setTextColor(ContextCompat.getColor(this, R.color.text));
        billText.setTypeface(playfair);

        TextView endOfTerm = (TextView) findViewById(R.id.end_of_term);
        endOfTerm.setTypeface(fauna);
        endOfTerm.setTextColor(ContextCompat.getColor(this, R.color.text));

        ImageView partyIcon = (ImageView) findViewById(R.id.political_icon);
        partyIcon.setImageResource(current.politicalIcon);

        de.hdodenhof.circleimageview.CircleImageView profileImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profileLarge);
        Log.d("d", "CURRENT IMAGE ID: " + current.imageID);
        Picasso.with(getApplicationContext()).load(current.imageID).resize(500, 500).centerCrop().into(profileImage);

        TextView nameText = (TextView) findViewById(R.id.nameLarge);
        nameText.setText(current.name);
        nameText.setTextColor(ContextCompat.getColor(this, R.color.text));
        nameText.setTypeface(playfair);
        nameText.setTextSize(25);

        TextView roleText = (TextView) findViewById(R.id.roleLarge);
        roleText.setText(current.role);
        roleText.setTextColor(ContextCompat.getColor(this, R.color.text));
        roleText.setTypeface(playfair);
        roleText.setTextSize(25);

        TextView partyName = (TextView) findViewById(R.id.partyName);
        partyName.setTextColor(ContextCompat.getColor(this, R.color.text));
        partyName.setTypeface(fauna);
        partyName.setText(current.party);


        endOfTerm.setText(current.endOfTerm);

        // Set up committee adapter
        ListView committeesList = (ListView) findViewById(R.id.committeeList);
        committeesList.setEnabled(false);
        ArrayList<String> committees = current.committees;

        CustomListAdapter committeeAdapter = new CustomListAdapter(this, committees);
        committeesList.setAdapter(committeeAdapter);

        // Set up bills adapter
        ListView billsList = (ListView) findViewById(R.id.billsList);
        billsList.setEnabled(false);
        ArrayList<String> bills = current.bills;

        CustomListAdapter billsAdapter = new CustomListAdapter(this, bills);
        billsList.setAdapter(billsAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent h = new Intent(this, ListRepresentatives.class);
                h.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(h);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
