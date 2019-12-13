package com.cainiao.funandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cainiao.funandroid.activity.OKHttpActivity;
import com.cainiao.funandroid.activity.RxJavaActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_okhttp)
    TextView tvOkhttp;
    @BindView(R.id.tv_RxJava)
    TextView tvRxJava;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_okhttp, R.id.tv_RxJava})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_okhttp:
                intent = new Intent(this, OKHttpActivity.class);
                startActivity(intent);

                break;
            case R.id.tv_RxJava:
                intent = new Intent(this, RxJavaActivity.class);
                startActivity(intent);
                break;
        }
    }
}
