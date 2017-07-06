package com.che.materialdesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dove on 2017/7/3.
 */

public class EnterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_btn_flow)
    TextView mTvBtnFlow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        ButterKnife.bind(this);

        mTvBtnFlow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_btn_flow:
                startActivity(new Intent(this, com.che.materialdesign.demo_wechat.MainActivity.class));
                break;
            default:
                break;
        }
    }
}
