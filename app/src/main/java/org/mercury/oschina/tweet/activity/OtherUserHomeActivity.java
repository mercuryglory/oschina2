package org.mercury.oschina.tweet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.mercury.oschina.R;
import org.mercury.oschina.base.BaseActivity;
import org.mercury.oschina.tweet.adapter.UserHomeAdapter;
import org.mercury.oschina.tweet.bean.User;
import org.mercury.oschina.tweet.TweetListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wang.zhonghao on 2017/8/15
 * description:  其它用户的个人中心界面
 */
public class OtherUserHomeActivity extends BaseActivity {

    @Bind(R.id.iv_user_portrait)
    CircleImageView ivUserPortrait;
    @Bind(R.id.tv_username)
    TextView        tvUsername;
    @Bind(R.id.tv_user_jointime)
    TextView        tvUserJointime;
    @Bind(R.id.tv_user_lastlogin)
    TextView        tvUserLastlogin;
    @Bind(R.id.iv_portrait_logo)
    CircleImageView ivPortraitLogo;
    @Bind(R.id.tv_name_logo)
    TextView        tvNameLogo;
    @Bind(R.id.toolbar)
    Toolbar         toolbar;
    @Bind(R.id.layout_appbar)
    AppBarLayout    layoutAppbar;
    @Bind(R.id.tablayout)
    TabLayout       tablayout;
    @Bind(R.id.view_divider)
    View      viewDivider;
    @Bind(R.id.viewPager)
    ViewPager viewPager;

    private static final String KEY_AUTHORID = "KEY_AUTHORID";


    private UserHomeAdapter mAdapter;
    private int pageIndex = 0;
    private List<Pair<String,Fragment>> mFragments;
    private User       mUser;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.menu_follow:
                showToast("follow");
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);

        layoutAppbar.addOnOffsetChangedListener(new AppbarListener());

        tvUsername.setText(mUser.getName());
        tvNameLogo.setText(mUser.getName());
        getImageLoader().load(mUser.getPortrait()).error(R.mipmap.widget_dface).into
                (ivPortraitLogo);
        getImageLoader().load(mUser.getPortrait()).error(R.mipmap.widget_dface).into
                (ivUserPortrait);



        if (mFragments == null) {
            mFragments = new ArrayList<>();
            mFragments.add(new Pair<String, Fragment>("动弹", TweetListFragment.instantiate(mUser.getUid())));
            mFragments.add(new Pair<String, Fragment>("博客", new Fragment()));
            mFragments.add(new Pair<String, Fragment>("讨论", new Fragment()));

        }
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position).second;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).first;
        }
    }


    @Override
    protected void initBundle(Bundle bundle) {
        mUser = bundle.getParcelable(KEY_AUTHORID);
        if (mUser == null) {
            return;
        }
    }


    private class AppbarListener implements AppBarLayout.OnOffsetChangedListener {
        int     maxOffset = -1;
        boolean isShow    = false;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (maxOffset == -1) {
                maxOffset = appBarLayout.getTotalScrollRange();
            }
            if (maxOffset + verticalOffset == 0) {
                ivPortraitLogo.setVisibility(View.VISIBLE);
                tvNameLogo.setVisibility(View.VISIBLE);
                viewDivider.setVisibility(View.GONE);
                isShow = true;
            } else if (isShow) {
                ivPortraitLogo.setVisibility(View.GONE);
                tvNameLogo.setVisibility(View.GONE);
                viewDivider.setVisibility(View.VISIBLE);
                isShow = false;
            }
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_other_user;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_other_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static void show(Context context, User user) {
        Intent intent = new Intent(context, OtherUserHomeActivity.class);
        intent.putExtra(KEY_AUTHORID, user);
        context.startActivity(intent);

    }


}
