package com.lsjr.zizisteward.activity.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lsjr.zizisteward.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopBarBaseActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_top_bar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        //设置不显示自带的 Title
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}