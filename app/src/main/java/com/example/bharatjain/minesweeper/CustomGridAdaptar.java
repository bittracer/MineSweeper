package com.example.bharatjain.minesweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bharatjain on 09/10/2017.
 */

public class CustomGridAdaptar extends BaseAdapter {

    private Context context;
    private Integer[] items;
    LayoutInflater layoutInflater;

    public CustomGridAdaptar(Context context,Integer[] items){
        this.context = context;
        this.items = items;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = layoutInflater.inflate(R.layout.cell,null);
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView_2);
        imageView.setImageResource(items[i]);
        return view;
    }
}
