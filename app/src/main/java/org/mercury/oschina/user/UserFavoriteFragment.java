package org.mercury.oschina.user;

import android.os.Bundle;
import android.view.View;

import org.mercury.oschina.base.BaseRecyclerAdapter;
import org.mercury.oschina.base.BaseRecyclerViewFragment;
import org.mercury.oschina.http.HttpApi;
import org.mercury.oschina.user.adapter.FavoriteListAdapter;
import org.mercury.oschina.user.bean.FavoriteResponse;
import org.mercury.oschina.widget.EmptyLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Mercury on 2016-08-14 19:33:46.
 * 收藏列表
 */
public class UserFavoriteFragment extends BaseRecyclerViewFragment<FavoriteResponse> {

    public FavoriteListAdapter mAdapter;

    public boolean isLoadMore;

    public static final String REQUEST_CATALOG = "REQUEST_CATALOG";
    // 0-全部|1-软件|2-话题|3-博客|4-新闻|5代码|7-翻译
    public static final int TYPE_DEFAULT = 0;
    public static final int    CATALOG_HOT     = 2;
    public int requestType;

    @Override
    protected void response(Call<FavoriteResponse> call, Response<FavoriteResponse> response) {
        FavoriteResponse bean = response.body();
        if (bean == null) {
            return;
        }

        if (isLoadMore) {
            loadMore(bean.getFavoriteList());
        } else {
            if (bean.getFavoriteList() == null) {
                mEmptyLayout.setVisibility(View.VISIBLE);
                mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                return;
            }
            refresh(bean.getFavoriteList());
        }
    }

    /**
     * 重新这个方法,将参数置为false,就是不为recyclrview添加默认的分割线
     * @param add
     */
    @Override
    protected void addItemDivider(boolean add) {
        super.addItemDivider(false);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (bundle != null) {
            requestType = bundle.getInt(REQUEST_CATALOG);
        }
    }

    @Override
    protected Call<FavoriteResponse> getCall(HttpApi retrofitCall) {
        Call<FavoriteResponse> favoriteList = retrofitCall.getFavoriteList(TYPE_DEFAULT, pageIndex);
        return favoriteList;
    }

    @Override
    protected BaseRecyclerAdapter getRecyclerAdapter() {
        FavoriteListAdapter adapter = new FavoriteListAdapter(getActivity());
        mAdapter = adapter;
        return adapter;
    }

    public void loadMore(List list) {
        if (list == null || list.size() == 0) {
            mRecyclerView.loadMoreEnd();
            return;
        }
        mAdapter.addAll(list);
        mRecyclerView.loadMoreComplete();
    }

    public void refresh(List list) {
        if (mAdapter == null) {
            mAdapter.addAll(list);
        } else {
            mAdapter.setData(list);
        }
        mRefreshLayout.setRefreshing(false);
        mRecyclerView.refreshComplete();
    }

    @Override
    public void onRefresh() {
        isLoadMore = false;
        mRecyclerView.setCanloadMore(false);
        pageIndex = 1;
        requestData();

    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        pageIndex++;
        requestData();

    }

    @Override
    public void onItemClick(int position, long itemId) {

    }
}
