package com.android.testapp;

import java.util.List;

@SuppressWarnings("unused")
public class BalancesListFragment extends ItemsListFragment {

    private User user;
    public static final String TAG = "balances";

    public BalancesListFragment() {}

    public BalancesListFragment(User user) {

        this.user = user;

    }

    @Override
    public void update() {

        if (this.user != null)
            new UpdateTask().execute(HttpController.balanceLink(this.user.getId()),
                    UpdateTask.NOCACHE);

    }

    @Override
    public void setItemsArrayAdapter(List<Item> list) {

        this.showItemsListView(new ItemsArrayAdapter(context, R.layout.balances_list_item,
                list));

    }

}
