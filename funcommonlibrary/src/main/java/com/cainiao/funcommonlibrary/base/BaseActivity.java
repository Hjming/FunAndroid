package com.cainiao.funcommonlibrary.base;


import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
    private static final String TAG = "BaseActivity";
    protected Context mContext;


    public BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);

        mContext = this;

        getWindow().setSoftInputMode(3);


    }
}
