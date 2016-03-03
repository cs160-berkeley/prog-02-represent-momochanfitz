package com.example.maurafitzgerald.prog02;

import android.app.Activity;
import android.content.Intent;
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

        county_state = (TextView)findViewById(R.id.county_state);
        county_state.setText((String)intent.getExtras().get("countyState"));

        romney = (TextView) findViewById(R.id.romney_percent);
        romney.setText((String)intent.getExtras().get("romney"));

        obama = (TextView)findViewById(R.id.obama_percent);
        obama.setText((String)intent.getExtras().get("obama"));
    }

}
