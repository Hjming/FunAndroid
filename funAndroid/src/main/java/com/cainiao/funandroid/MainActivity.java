package com.cainiao.funandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cainiao.funandroid.activity.OKHttpActivity;
import com.cainiao.funandroid.activity.RxJavaActivity;
import com.cainiao.funcommonlibrary.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_okhttp)
    TextView tvOkhttp;
    @BindView(R.id.tv_RxJava)
    TextView tvRxJava;
    private Context mContext;

    private long mFirstTime;

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

    /**
     * 退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {
                // 如果两次按键时间间隔大于800毫秒，则不退出
                ToastUtil.showToast( "再按一次可退出程序");
                // 更新firstTime
                mFirstTime = secondTime;
                return true;
            } else {
                // FunApplication.instance.AppExit(this);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
