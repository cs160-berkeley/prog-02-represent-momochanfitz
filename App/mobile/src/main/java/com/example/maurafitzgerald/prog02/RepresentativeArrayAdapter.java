package com.example.maurafitzgerald.prog02;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class RepresentativeArrayAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Representative> reps;

    public RepresentativeArrayAdapter(Context context, ArrayList<Representative> representatives) {
        this.context = context;
        this.reps = representatives;
    }

    @Override
    public int getCount(){
        return reps.size();
    }

    @Override
    public Representative getItem(int position) {
        return reps.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View repRow = inflater.inflate(R.layout.rep_row_layout, parent, false);

        TextView nameView = (TextView) repRow.findViewById(R.id.rep_name);
        ImageView photoView = (ImageView) repRow.findViewById(R.id.profile_image);
        TextView tweetView = (TextView) repRow.findViewById(R.id.tweet);
        ImageView partyIcon = (ImageView) repRow.findViewById(R.id.partyIcon);
        TextView emailView = (TextView) repRow.findViewById(R.id.email);
        TextView websiteView = (TextView) repRow.findViewById(R.id.website);

        Representative current = getItem(position);

        nameView.setText(current.name);
        photoView.setImageResource(current.imageID);
        tweetView.setText(current.lastTweet);
        partyIcon.setImageResource(current.politicalIcon);
        emailView.setText(current.email);
        websiteView.setText(current.website);

        repRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListRepresentatives) context).showRepresentative(pos);
            }
        });

        return repRow;
    }
}
