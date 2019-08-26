package com.qymage.sys.common.base.baseView;

import java.util.List;

public interface MainView<T> extends BaseView{

    void showToastError();
    void showFinishDates(List<T> dates);

    void showEmptyView(String msg);

    void hasNoMoreDate();

    void loadMoreFinish(List dates);

    void showRefreshFinish(List score);
}
