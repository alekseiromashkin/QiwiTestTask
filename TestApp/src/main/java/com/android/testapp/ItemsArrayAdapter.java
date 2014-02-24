package com.android.testapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemsArrayAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final List<Item> items;
    private int resourceId;

    private static class ViewHolder {

        public TextView text;

    }

    public ItemsArrayAdapter(Context context, int textViewResourceId, List<Item> items) {

        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
        this.resourceId = textViewResourceId;

    }

    @Override
    public long getItemId(int position) {

        return this.items.get(position).getId();

    }

    @Override
    public Item getItem(int position) {

        return this.items.get(position);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId, null, false);
            holder = new ViewHolder();
            if (convertView != null) {
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            }
        } else {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.text.setText(items.get(position).toString());

        return convertView;

    }

    public void clear() {

        this.items.clear();
        this.notifyDataSetChanged();

    }
}
