package com.android.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class UsersListFragment extends ItemsListFragment {

    OnItemClickListener mCallback;
    public static final String TAG = "users";

    public interface OnItemClickListener {
        public void onItemClicked(User user);
    }

    public UsersListFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnItemClickListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.restoreCache();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        this.itemsListView.setOnItemClickListener(itemsListViewOnItemClickListener);
        return v;

    }

    @Override
    public void update() {

        new UpdateTask().execute(HttpController.GET_USERS, UpdateTask.CACHE);

    }

    AdapterView.OnItemClickListener itemsListViewOnItemClickListener
            = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Item item = (Item) adapterView.getAdapter().getItem(i);
            mCallback.onItemClicked((User) item);

        }

    };

}
