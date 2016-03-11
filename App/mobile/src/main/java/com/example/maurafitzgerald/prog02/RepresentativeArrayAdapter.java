package com.example.maurafitzgerald.prog02;

import android.app.Activity;
import android.content.Context;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.twitter.sdk.android.tweetui.CompactTweetView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by maurafitzgerald on 2/28/16.
 */
public class RepresentativeArrayAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<Representative> reps;
    private ArrayList<ListRepresentatives.TweetInfo> tweets;

    public RepresentativeArrayAdapter(Context context, ArrayList<Representative> representatives,
                                      ArrayList<ListRepresentatives.TweetInfo> tweets) {
        this.context = context;
        this.reps = representatives;
        this.tweets = tweets;
    }

    @Override
    public int getCount() {
        return reps.size();
    }


    @Override
    public Representative getItem(int position) {
        return reps.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View repRow = inflater.inflate(R.layout.rep_row_layout, parent, false);

        repRow.setBackgroundColor(ContextCompat.getColor(context, R.color.background));


        TextView nameView = (TextView) repRow.findViewById(R.id.rep_name);
        ImageView photoView = (ImageView) repRow.findViewById(R.id.profile_image);
        ImageView partyIcon = (ImageView) repRow.findViewById(R.id.partyIcon);
        TextView emailView = (TextView) repRow.findViewById(R.id.email);
        TextView websiteView = (TextView) repRow.findViewById(R.id.website);
        LinearLayout tweetView = (LinearLayout) repRow.findViewById(R.id.lin);


        Typeface fauna = Typeface.createFromAsset(context.getResources().getAssets(),  "FaunaOne-Regular.ttf");
        Typeface playfair = Typeface.createFromAsset(context.getResources().getAssets(),  "PlayfairDisplay-Regular.ttf");


        TextView title = (TextView) repRow.findViewById(R.id.title);
        title.setTypeface(playfair);
        title.setTextColor(ContextCompat.getColor(context, R.color.text));
        title.setTextSize(25);

        nameView.setTypeface(playfair);
        nameView.setTextColor(ContextCompat.getColor(context, R.color.text));
        nameView.setTextSize(25);

        ImageView email = (ImageView) repRow.findViewById(R.id.email_icon);
        email.setImageResource(R.drawable.email);
        emailView.setTypeface(fauna);
        emailView.setTextSize(14);

        ImageView website = (ImageView) repRow.findViewById(R.id.web_icon);
        website.setImageResource(R.drawable.web);
        websiteView.setTypeface(fauna);
        websiteView.setTextSize(14);


        Representative current = getItem(position);


        nameView.setText(current.name);

        partyIcon.setImageResource(current.politicalIcon);
        emailView.setText(current.email);

        websiteView.setText(current.website);
        title.setText(current.role);

        for (int j = 0; j < tweets.size(); j++) {
            if (current.twitterID == "null"){
                Log.d("d", "ID IS NULL");
            }
            if (tweets.get(j).displayTweet.user.screenName.equals(current.twitterID)) {
                Log.d("d", "Name: " + current.name + " TweetURL: " + tweets.get(j).imageURL);
                current.imageID = tweets.get(j).imageURL;
                tweetView.addView(new CompactTweetView(context, tweets.get(j).displayTweet));
                Picasso.with(context).load(current.imageID).placeholder(R.drawable.empty_profile).into(photoView);
            }
        }

        repRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListRepresentatives) context).showRepresentative(pos);
            }
        });
        return repRow;
    }

}
