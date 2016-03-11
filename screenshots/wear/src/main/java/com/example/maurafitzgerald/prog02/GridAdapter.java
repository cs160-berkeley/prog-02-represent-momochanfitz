package com.example.maurafitzgerald.prog02;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurafitzgerald on 2/29/16.
 */
public class GridAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private ArrayList<SimpleRow> mPages;
    private String[] names;
    private String[] parties;
    private String[] roles;

    private String romney;
    private String obama;
    private String county;
    private String state;


    public GridAdapter(Context ctx, FragmentManager fm, String[] names, String[] parties, String[] roles,
                       String romney, String obama, String county, String state) {
        super(fm);
        mContext = ctx;
        this.names = names;
        this.roles = roles;
        this.parties = parties;
        this.romney = romney;
        this.obama = obama;
        this.county = county;
        this.state = state;
        initPages();
    }

    public int getIcon(String party) {
        if (party.equals("Democrat")) {
            return R.drawable.democrat;
        } else if (party.equals("Republican")){
            return R.drawable.republican;
        } else {
            return R.drawable.independent;
        }
    }

    private void initPages() {
        // Create a set of pages in a 2D array
        mPages = new ArrayList<>();
        Log.d("NAMES", names[0]);
        SimpleRow row1 = new SimpleRow();
        for (int i = 0; i < names.length; i++) {
            int myIcon = getIcon(parties[i]);
            Log.d("Icon", ""+ myIcon);
            GridPage currGrid = new GridPage(names[i], parties[i], getIcon(parties[i]), roles[i]);
            row1.addPages(currGrid);
        }
        mPages.add(row1);
    }



    @Override
    public Fragment getFragment(int row, int col) {
        GridPage page = (mPages.get(row)).getPages(col);
        String title = page.name;
        String party = page.party;
        int image = page.icon;
        String role = page.role;


        //String text =
        //      page.textRes != 0 ? mContext.getString(page.textRes) : null;
        //CardFragment fragment = CardFragment.create(title,party,image);
        CustomFragment fragment = new CustomFragment();
        Bundle args = new Bundle();
        args.putString("Name", title);
        args.putInt("Party", image);
        args.putString("PartyName", party);
        args.putInt("RepID", col);
        args.putString("Obama", obama + "%");
        args.putString("Romney", romney + "%");
        args.putString("County", county);
        args.putString("State", state);
        args.putString("Role", role);
        fragment.setArguments(args);
        return fragment;
    }


    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return mPages.size();
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return mPages.get(rowNum).size();
    }

    public class SimpleRow {

        ArrayList<GridPage> mPagesRow = new ArrayList<GridPage>();

        public void addPages(GridPage page) {
            mPagesRow.add(page);
        }

        public GridPage getPages(int index) {
            return mPagesRow.get(index);
        }

        public int size(){
            return mPagesRow.size();
        }
    }

}
