package com.android.testapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class ItemsListFragment extends Fragment implements TestappListFragment {

    Object data;
    Context context;
    ListView itemsListView;
    LinearLayout errorLinearLayout;
    TextView messageTextView;
    ProgressBar loadingProgressBar;
    TaskCallbacks mCallbacks;

    public interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int percent);
        void onCancelled();
        void onPostExecute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.setRetainInstance(true);
        this.context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        if (view == null || this.context == null)
            return null;

        this.loadingProgressBar = (ProgressBar) view.findViewById(R.id.loadingProgressBar);
        this.itemsListView = (ListView) view.findViewById(R.id.itemListView);
        this.itemsListView.setHeaderDividersEnabled(false);
        this.errorLinearLayout = (LinearLayout) view.findViewById(R.id.errorLinearLayout);
        this.messageTextView = (TextView) view.findViewById(R.id.messageTextView);
        view.findViewById(R.id.repeatButton).setOnClickListener(updateButtonOnClickListener);

        Button updateButton = new Button(this.context);
        updateButton.setText(this.context.getString(R.string.update_button__text));
        updateButton.setOnClickListener(updateButtonOnClickListener);
        this.itemsListView.addHeaderView(updateButton);

        if (this.data == null)
            this.update();
        else
            this.draw(null);

        return view;

    }

    View.OnClickListener updateButtonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            update();

        }

    };

    public void showLoadingProgressBar() {

        this.hideErrorLinearLayout();
        this.hideItemsListView();
        this.loadingProgressBar.setVisibility(View.VISIBLE);

    }

    public void showItemsListView(BaseAdapter adapter) {

        this.hideErrorLinearLayout();
        this.hideLoadingProgressBar();
        this.itemsListView.setVisibility(View.VISIBLE);
        this.itemsListView.setAdapter(adapter);

    }

    public void showErrorLinearLayout(String message) {

        this.hideLoadingProgressBar();
        this.hideItemsListView();
        this.messageTextView.setText(message);
        this.errorLinearLayout.setVisibility(View.VISIBLE);

    }

    public void hideLoadingProgressBar() {

        this.loadingProgressBar.setVisibility(View.GONE);

    }

    public void hideItemsListView() {

        this.itemsListView.setVisibility(View.INVISIBLE);

    }

    public void hideErrorLinearLayout() {

        this.errorLinearLayout.setVisibility(View.GONE);
        this.messageTextView.setText("");

    }

    public void setItemsArrayAdapter(List<Item> list) {

        this.showItemsListView(new ItemsArrayAdapter(context, R.layout.users_list_item,
                list));

    }

    public void setItemsCursorAdapter(Cursor cursor) {

        this.showItemsListView(new ItemsCursorAdapter(context, cursor, 0));

    }

    private void setCache(final List<User> data) {

        new Thread()
        {
            @Override
            public void run() {
                ContentValues[] values = new ContentValues[data.size()];

                int i = 0;
                for (User item : data) {
                    ContentValues value = new ContentValues();
                    value.put(DatabaseContentProvider._ID, item.getId());
                    value.put(DatabaseContentProvider.NAME, item.getName());
                    value.put(DatabaseContentProvider.SECOND_NAME, item.getSecondName());
                    values[i] = value;
                    i++;
                }

                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(DatabaseContentProvider.USERS_CONTENT_URI, null, null);
                contentResolver.bulkInsert(DatabaseContentProvider.USERS_CONTENT_URI, values);
            }
        }.start();

    }

    public void restoreCache() {

        if (this.context != null) {
            Cursor cursor = this.context.getContentResolver()
                    .query(DatabaseContentProvider.USERS_CONTENT_URI, null, null, null, null);
            if (cursor != null && (cursor.getCount() > 0))
                data = cursor;
        }

    }

    @SuppressWarnings("unchecked")
    public void draw(Exception e) {

        if (e != null)
            this.data = new Error(e.hashCode(), e.toString());

        if (this.data instanceof Error)
            this.showErrorLinearLayout(this.data.toString());
        if (this.data instanceof List)
            this.setItemsArrayAdapter((List<Item>) data);
        if (this.data instanceof Cursor)
            this.setItemsCursorAdapter((Cursor) this.data);

    }

    public class UpdateTask extends AsyncTask<String, Integer, Void> {

        private InputStream is;
        private Exception e = null;
        public static final String CACHE = "cache";
        public static final String NOCACHE = "nocache";

        @Override
        protected void onPreExecute() {

            if (mCallbacks != null) {
                mCallbacks.onPreExecute();
            }
            data = null;
            showLoadingProgressBar();

        }

        @Override
        @SuppressWarnings("unchecked")
        protected Void doInBackground(String... params) {

            XmlParser parser = new XmlParser();
            try {
                is = HttpController.get(params[0]);
                data = parser.parse(is);
                if (data instanceof List && params[1].equals(CACHE))
                    setCache((List<User>) data);
            } catch (XmlPullParserException e) {
                this.e = e;
                e.printStackTrace();
            } catch (IOException e) {
                this.e = e;
                e.printStackTrace();
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        this.e = e;
                        e.printStackTrace();
                    }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... i) {

            if (mCallbacks != null) {
                mCallbacks.onProgressUpdate(i[0]);
            }

        }

        @Override
        protected void onCancelled() {

            if (mCallbacks != null) {
                mCallbacks.onCancelled();
            }

        }

        @Override
        protected void onPostExecute(Void res) {

            if (mCallbacks != null) {
                mCallbacks.onPostExecute();
            }

            draw(this.e);

        }
    }

}
