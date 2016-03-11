package com.example.maurafitzgerald.prog02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class ListRepresentatives extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.maurafitzgerald.prog02.MESSAGE";
    private static final String ACTION_STRING_ACTIVITY = "ToActivity";


    //TODO: Remove fake zip code, implement location
    private Toolbar actionBar;
    private ArrayList<Representative> allReps;
    private RepData repData;
    private Integer repID = null;
    private BroadcastReceiver activityReceiver;
    private ArrayList<TweetInfo> repTweets = new ArrayList<>();
    private Tweet tweetResult;
    private Integer tweetCount = 0;
    private RepresentativeArrayAdapter adapter;
    private boolean showing = false;
    private boolean registered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_representatives);
        CoordinatorLayout rl = (CoordinatorLayout)findViewById(R.id.activity_layout);
        rl.setBackgroundColor(ContextCompat.getColor(this, R.color.background));


        activityReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                repID = (Integer) intent.getExtras().get("RepID");
                showing = true;
                showRepresentative(repID);
            }
        };

        actionBar = (Toolbar) findViewById(R.id.list_reps_toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (activityReceiver != null && registered == false) {
            IntentFilter intentFilter = new IntentFilter(ACTION_STRING_ACTIVITY);
            registerReceiver(activityReceiver, intentFilter);
            registered = true;
        }

        Intent intent = getIntent();

        this.repData = (RepData) intent.getSerializableExtra("DATA");
        String countyState = repData.myLoc.county + ", " + repData.myLoc.state;





        getSupportActionBar().setTitle(countyState);

        this.repID = (Integer) intent.getExtras().get("CurrentRep");


        ArrayList<Representative> representatives = repData.getAllRepresentatives();
        this.allReps = representatives;


        GetTwitterData task = new GetTwitterData();
        task.execute();
    }

    public void continueSetUp() {

        new GetTweets().execute();
    }

    public void setUpPartTwo() {
        //Log.d("d", "After get tweets: " + repTweets.get(2).displayTweet.text);
        RepresentativeArrayAdapter adapter =
                new RepresentativeArrayAdapter(this, allReps, repTweets);

        ListView repList = (ListView) findViewById(R.id.listReps);
        repList.setAdapter(adapter);

    }

    public void showRepresentative(int position) {
        Intent intent = new Intent(this, ShowRepresentative.class);
        intent.putExtra(EXTRA_MESSAGE, position);
        intent.putExtra("DATA", repData);
        Log.d("d", "STARTIN SHOW");
        showing = true;
        startActivityForResult(intent, 1111);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (activityReceiver != null) {
                    try {
                        unregisterReceiver(activityReceiver);
                        registered = false;
                    } catch (IllegalArgumentException e) {

                    }

                }
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStop() {
        super.onStop();
        try {
            if (showing != true) {
                unregisterReceiver(activityReceiver);
                registered = false;
            }
        } catch (IllegalArgumentException e) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(activityReceiver);
            finishActivity(1111);
            registered = false;
        } catch (IllegalArgumentException e) {

        }
    }


    public void addTweetInfo(TweetInfo tweet) {
        this.repTweets.add(tweet);
    }

    public void setDisplayTweet(Tweet tweet) {
        for (int i = 0; i < allReps.size(); i++) {
            if (tweet.id == repTweets.get(i).tweetId) {
                repTweets.get(i).setTweet(tweet);
            }
        }
        tweetCount += 1;
    }


    private class GetTwitterData extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig("qBftKBQCoKHGtmECAgIJphxTd",
                    "of5YLaDjE6KGrtjtmkoY68sY8nUHTpjU6WGVt4p6LKMZ2v2QrL");
            Fabric.with(getApplicationContext(), new Twitter(authConfig));

            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
            StatusesService statusesService = twitterApiClient.getStatusesService();


            for (int i = 0; i < allReps.size(); i++) {
                String repTweet = allReps.get(i).twitterID;
                if (repTweet.equals("null")) {
                    addTweetInfo(null);
                }
                statusesService.userTimeline(null, repTweet, null, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> listResult) {
                        if (listResult.data.size() > 0) {
                            Long tweetId = listResult.data.get(0).id;
                            String smallImage = listResult.data.get(0).user.profileImageUrl;
                            String profileImage = smallImage.replaceAll("_normal", "");

                            TweetInfo currentRep = new TweetInfo(tweetId, profileImage);
                            addTweetInfo(currentRep);
                        }
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                    }
                });
            }
            while (repTweets.size() != allReps.size()) {
            }
            return "done";
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(String result) {
            continueSetUp();
        }
    }

    private class GetTweets extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
            final Integer[] index = new Integer[1];
            for (int i = 0; i < repTweets.size(); i++) {
                index[0] = i;
                TweetInfo currentRep = repTweets.get(i);
                Long tweetId = currentRep.tweetId;


                TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        tweetResult = result.data;
                        setDisplayTweet(tweetResult);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                    }
                });
            }
            while (tweetCount != repTweets.size()) {
            }
            return "done";
        }

        // This is called each time you call publishProgress()
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        // This is called when doInBackground() is finished
        @Override
        protected void onPostExecute(String result) {
            setUpPartTwo();
        }
    }


    public class TweetInfo {
        public Long tweetId;
        public String imageURL;
        public Tweet displayTweet = null;

        public TweetInfo(Long tweetId, String profileURL) {
            this.tweetId = tweetId;
            this.imageURL = profileURL;
        }

        public void setTweet(Tweet tweet) {
            this.displayTweet = tweet;
        }
    }


}
