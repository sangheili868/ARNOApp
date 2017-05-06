package com.cs40333.cmaheu.arnoapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by JoeS on 5/6/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(500, 500));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(64, 64, 64, 64);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = { R.drawable.aiden, R.drawable.amy,
            R.drawable.andy, R.drawable.babar, R.drawable.chester, R.drawable.eli,
            R.drawable.fred, R.drawable.jumpingjack, R.drawable.meatloaf,
            R.drawable.samuel, R.drawable.champ, R.drawable.charcoal,
            R.drawable.clark, R.drawable.fawn, R.drawable.fiona,
            R.drawable.gertie, R.drawable.hannah
    };
}

