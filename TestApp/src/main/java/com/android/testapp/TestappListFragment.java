package com.android.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface TestappListFragment {

    void onCreate(Bundle savedInstanceState);
    View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState);
    void update();

}
