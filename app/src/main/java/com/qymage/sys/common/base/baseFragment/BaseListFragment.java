package com.qymage.sys.common.base.baseFragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.lzy.okgo.callback.StringCallback;
import com.qymage.sys.R;
import com.qymage.sys.common.config.Constants;
import com.qymage.sys.databinding.FragmentBaselistBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by HK on 2018/5/29.
 */

public abstract class BaseListFragment<T> extends FragmentLazy<FragmentBaselistBinding>{


    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    protected  DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    protected CommonAdapter mAdapter;

    protected List<T> mList = new ArrayList<>();
    protected int page = 1;
    protected int pageSize = 10;

    private Type type;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_baselist;
    }

    public abstract int getItemLayout();

    public abstract void fillValue(RecyclerView.ViewHolder holder, T t, int position);

    protected abstract String getUrl();

    protected abstract Map<String,String> Querys();

    protected abstract void setTitle();

    public BaseListFragment() {
        type = getSuperclassTypeParameter(getClass());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    @Override
    protected void baseInit() {
        super.baseInit();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.twrefreslayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(Constants.RequestMode.FRIST);
            }
        });
        mBinding.twrefreslayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData(Constants.RequestMode.LOAD_MORE);
            }
        });

        mAdapter = new CommonAdapter(getItemLayout(), mList);
        mBinding.recyclerview.setAdapter(mAdapter);
        setTitle();

    }

    @Override
    protected void initData() {
        showLoading();
        getData(Constants.RequestMode.FRIST);
    }

    public void getData(Constants.RequestMode mode) {
        postData(Querys()).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                closeLoading();
                mBinding.emptylayout.showContent();
                mBinding.twrefreslayout.finishRefresh();
                mBinding.twrefreslayout.finishLoadMore();
                if (!TextUtils.isEmpty(s)) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getInt("code") == 200) {
                            String listre = jsonObject.getString("data");
                            Object o = new Gson().fromJson(listre, $Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, type));
                            ArrayList<T> result = (ArrayList<T>) o;
                            if (mode == Constants.RequestMode.FRIST) {
                                if (result != null && result.size() > 0) {
                                    mList.clear();
                                    mList.addAll(result);
                                } else {
                                    mBinding.emptylayout.showEmpty();
                                }

                            } else if (mode == Constants.RequestMode.LOAD_MORE) {
                                if (result != null && result.size() > 0) {
                                    mList.addAll(result);
                                } else {
                                    page--;
                                    mBinding.twrefreslayout.finishLoadMoreWithNoMoreData();
                                    showToast("数据加载完毕");
                                }
                            }
                            mAdapter.notifyDataSetChanged();

                        } else {
                            showToast(jsonObject.getString("message"));
                            mBinding.emptylayout.showError();
                            mBinding.emptylayout.setRetryListener(() -> {
                                mBinding.twrefreslayout.autoRefresh();
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                mBinding.twrefreslayout.finishRefresh();
                mBinding.twrefreslayout.finishLoadMore();
                showToast(e.getMessage());
            }
        });
    }


    private class CommonAdapter extends BaseQuickAdapter<T, BaseViewHolder> {

        public CommonAdapter(int layoutResId, @Nullable List<T> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {
            fillValue(helper, item, helper.getAdapterPosition());
        }
    }




}
