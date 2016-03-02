package com.example.maurafitzgerald.prog02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class ShowRepresentative extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_representative);

        Intent intent = getIntent();
        int pos = intent.getIntExtra(ListRepresentatives.EXTRA_MESSAGE, 0);
        RepData repData = (RepData)intent.getSerializableExtra("DATA");

        Representative current = repData.getRepresentative(pos);
        String name = current.name;
        getSupportActionBar().setTitle(name);

        ImageView partyIcon = (ImageView) findViewById(R.id.political_icon);
        partyIcon.setImageResource(current.politicalIcon);

        ImageView profileImage = (ImageView) findViewById(R.id.profileLarge);
        profileImage.setImageResource(current.imageID);

        TextView nameText = (TextView) findViewById(R.id.nameLarge);
        nameText.setText(current.name);

        TextView partyName = (TextView) findViewById(R.id.partyName);
        partyName.setText(current.getPartyName());

        TextView endOfTerm = (TextView) findViewById(R.id.end_of_term);
        endOfTerm.setText(current.endOfTerm);

        // Set up committee adapter
        ListView committeesList = (ListView) findViewById(R.id.committeeList);
        committeesList.setEnabled(false);
        ArrayList<String> committees = current.committees;

        final ArrayAdapter<String> committeeAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, committees);

        committeesList.setAdapter(committeeAdapter);

        // Set up bills adapter
        ListView billsList = (ListView) findViewById(R.id.billsList);
        billsList.setEnabled(false);
        ArrayList<String> bills = current.bills;

        final ArrayAdapter<String> billsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bills);

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
