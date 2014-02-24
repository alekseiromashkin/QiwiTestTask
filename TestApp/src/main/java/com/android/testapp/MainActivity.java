package com.android.testapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class MainActivity extends Activity implements ItemsListFragment.TaskCallbacks,
        UsersListFragment.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale.setDefault(new Locale("ru", "RU"));

        if (savedInstanceState != null)
            return;

        if (this.isTablet())
            this.drawTablet();
        else
            this.drawPhone();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(User user) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BalancesListFragment fb = new BalancesListFragment(user);
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.content, fb);
        if (!this.isTablet())
            ft.addToBackStack(UsersListFragment.TAG);
        ft.commit();

    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate(int percent) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {

    }

    public boolean isTablet() {
        Context context = this.getApplicationContext();
        if (context == null)
            return false;
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void drawPhone() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        UsersListFragment fb = new UsersListFragment();
        ft.replace(R.id.content, fb);
        ft.commit();

    }

    private void drawTablet() {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        UsersListFragment users = new UsersListFragment();
        ft.replace(R.id.users, users);
        ft.commit();

        ft = fm.beginTransaction();
        BalancesListFragment balances = new BalancesListFragment();
        ft.replace(R.id.content, balances);
        ft.commit();

    }
}

