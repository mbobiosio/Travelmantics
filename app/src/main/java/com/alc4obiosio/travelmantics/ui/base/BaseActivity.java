package com.alc4obiosio.travelmantics.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alc4obiosio.travelmantics.util.NetworkUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        setUnBinder(ButterKnife.bind(this));
    }

    public boolean isNetworkConnected() {
        return NetworkUtil.isDeviceConnectedToInternet(this);
    }

    public void setUnBinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected abstract int getLayoutResID();
}
