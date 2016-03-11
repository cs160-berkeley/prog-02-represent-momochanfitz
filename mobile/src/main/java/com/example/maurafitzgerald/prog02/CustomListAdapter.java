package com.example.maurafitzgerald.prog02;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maurafitzgerald on 3/10/16.
 */


public class CustomListAdapter extends ArrayAdapter<String> {
    Context context;
    public CustomListAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        Typeface fauna = Typeface.createFromAsset(context.getResources().getAssets(),  "FaunaOne-Regular.ttf");

        TextView listItem = (TextView) convertView.findViewById(R.id.text_view);
        listItem.setTypeface(fauna);
        listItem.setTextSize(14);
        listItem.setTextColor(ContextCompat.getColor(context, R.color.text));
        listItem.setBackgroundColor(ContextCompat.getColor(context, R.color.background));
        // Populate the data into the template view using the data object
        listItem.setText(item);
        // Return the completed view to render on screen
        return convertView;
    }
}
