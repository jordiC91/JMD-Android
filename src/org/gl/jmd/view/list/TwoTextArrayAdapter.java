package org.gl.jmd.view.list;

import java.util.List;

import org.gl.jmd.view.list.item.Item;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TwoTextArrayAdapter extends ArrayAdapter<Item> {
	
    private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, 
        HEADER_ITEM,
        FOOTER
    }

    public TwoTextArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}