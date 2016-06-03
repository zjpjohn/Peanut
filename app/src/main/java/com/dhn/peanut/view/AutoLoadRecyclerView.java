package com.dhn.peanut.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by DHN on 2016/5/31.
 */
public class AutoLoadRecyclerView extends RecyclerView {

    public interface LoadMoreListener {
        void loadMore();
    }

    private boolean mIsLoading = false;
    private LoadMoreListener loadMoreListener;
    private boolean isDragLoad = true;
    private boolean isSettlingLoad = false;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    /**
     * 拖动和滑动时是否加载图片
     * @param isDragLoad
     * @param isSettlingLoad
     */
    public void setLoadingConfig(boolean isDragLoad, boolean isSettlingLoad) {
        this.isDragLoad = isDragLoad;
        this.isSettlingLoad = isSettlingLoad;
    }

    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        if (!canScrollVertically(1)) {
            loadMoreListener.loadMore();
        }
    }
}
