package com.android.testapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ItemsCursorAdapter extends CursorAdapter {

    private static final int _ID = 0;
    private static final int NAME = 1;
    private static final int SECOND_NAME = 2;

    public ItemsCursorAdapter(Context context, Cursor c, int flags) {

        super(context, c, flags);

    }

    public long getItemId(int position) {

        Cursor cursor = this.getCursor();
        if (cursor == null)
            return 0;

        cursor.moveToPosition(position);
        return cursor.getInt(_ID);

    }

    public Object getItem(int position) {

        Cursor cursor = this.getCursor();
        if (cursor == null)
            return 0;

        cursor.moveToPosition(position);
        return new User(cursor.getInt(_ID), cursor.getString(NAME), cursor.getString(SECOND_NAME));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.users_list_item, viewGroup, false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText((cursor.getString(NAME) + " "
                + cursor.getString(SECOND_NAME)).trim());

    }
}
