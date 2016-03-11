package com.example.maurafitzgerald.prog02;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by maurafitzgerald on 2/29/16.
 */
public class Graph extends Activity {

    TextView county_state;
    TextView romney;
    TextView obama;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);

        Intent intent = getIntent();

        Typeface fauna = Typeface.createFromAsset(getResources().getAssets(), "FaunaOne-Regular.ttf");
        Typeface playfair = Typeface.createFromAsset(getResources().getAssets(), "PlayfairDisplay-Regular.ttf");

        TextView yearText = (TextView) findViewById(R.id.yearText);
        yearText.setTypeface(fauna);
        TextView presText = (TextView) findViewById(R.id.presText);
        presText.setTypeface(playfair);
        TextView romText = (TextView) findViewById(R.id.romText);
        romText.setTypeface(fauna);
        TextView obamaText = (TextView) findViewById(R.id.obamaText);
        obamaText.setTypeface(fauna);

        county_state = (TextView)findViewById(R.id.county_state);
        county_state.setText((String)intent.getExtras().get("countyState"));
        county_state.setTypeface(fauna);
        county_state.setTextSize(11);

        romney = (TextView) findViewById(R.id.romney_percent);
        romney.setText((String)intent.getExtras().get("romney"));
        romney.setTypeface(playfair);
        romney.setTextSize(25);

        obama = (TextView)findViewById(R.id.obama_percent);
        obama.setText((String)intent.getExtras().get("obama"));
        obama.setTypeface(playfair);
        obama.setTextSize(25);
    }

}
