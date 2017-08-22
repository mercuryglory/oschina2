package org.mercury.oschina.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import org.mercury.oschina.user.FragmentInfo;
import org.mercury.oschina.R;
import org.mercury.oschina.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by wang.zhonghao on 2017/8/18.
 * 只选择加载哪个fragment,不负责具体的fragment
 */

public class UserSingleInfoActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar     toolbar;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;

    public static final String FRAGMENT_KEY = "FRAGMENT_KEY";

    private FragmentInfo mInfo;

    @Override
    protected int getContentView() {
        return R.layout.base_activity_fragment;
    }

    @Override
    protected void initWidget() {
        toolbar.setTitle(mInfo.getTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
    }

    @Override
    protected void initData() {
        try {
            addFragment(R.id.fl_container, (Fragment) mInfo.getClazz().newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initBundle(Bundle bundle) {
        if (bundle != null) {
            mInfo = (FragmentInfo) bundle.getSerializable(FRAGMENT_KEY);
        }
    }


    public static void show(Context context, FragmentInfo info) {
        Intent intent = new Intent(context, UserSingleInfoActivity.class);
        intent.putExtra(FRAGMENT_KEY, info);
        context.startActivity(intent);
    }

}