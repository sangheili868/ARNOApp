package com.cs40333.cmaheu.arnoapp;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class NeedAdapter extends ArrayAdapter<Need> {
    private final Context mContext;
    public NeedAdapter(Context context, ArrayList<Need> needs) {
        super(context, 0, needs);
        mContext=context;
    }

    public int getBGColor( int numVols) {

        if(numVols< 4)
            return ResourcesCompat.getColor(mContext.getResources(),R.color.bad,null);
        else if (numVols <6)
            return ResourcesCompat.getColor(mContext.getResources(),R.color.okay,null);
        else
            return ResourcesCompat.getColor(mContext.getResources(),R.color.good,null);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Need thisneed = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_need, parent, false);

        TextView needDate = (TextView) convertView.findViewById(R.id.shiftText);
        TextView needCount = (TextView) convertView.findViewById(R.id.volunteerText);
        LinearLayout mornLay = (LinearLayout) convertView.findViewById(R.id.needLayout);

        String dayofweek=(new SimpleDateFormat("EEE", Locale.US)).format(thisneed.getDay());
        String mydate=(new SimpleDateFormat("MMM d")).format(thisneed.getDay()).toString();

        needDate.setText(dayofweek+" "+thisneed.getTime()+", "+mydate);
        needCount.setText(String.valueOf("Volunteers: "+thisneed.getVolunteers()));
        mornLay.setBackgroundColor(getBGColor(thisneed.getVolunteers()));

        return convertView;
    }
}
