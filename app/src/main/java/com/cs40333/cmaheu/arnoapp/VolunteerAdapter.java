package com.cs40333.cmaheu.arnoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chris_000 on 5/9/2017.
 */

public class VolunteerAdapter extends ArrayAdapter<String> {
    public VolunteerAdapter(Context context, ArrayList<String> volunteers) {
        super(context, 0, volunteers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String volunteer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_volunteer, parent, false);
        // Lookup view for data population
        //TextView volunteerName = (TextView) convertView.findViewById(R.id.txt_volunteer);
        // Populate the data into the template view using the data object
        //volunteerName.setText(volunteer);
        // Return the completed view to render on screen
        return convertView;
    }
}
