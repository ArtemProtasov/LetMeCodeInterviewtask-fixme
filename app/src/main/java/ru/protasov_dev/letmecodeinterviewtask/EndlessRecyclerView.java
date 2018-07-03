package ru.protasov_dev.letmecodeinterviewtask;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class EndlessRecyclerView extends RecyclerView {
    private int visibleItemCount, totalItemCount, pastVisiblesItems;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private OnLoadMoreListener onLoadMoreListener;

    public EndlessRecyclerView(Context context) {
        super(context);
        init();
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EndlessRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recycler_reviews);

        //Использовать линейный менеджер компановки
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (dy > 0) {
            visibleItemCount = layoutManager.getChildCount();
            totalItemCount = layoutManager.getItemCount();
            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                Log.d("EndlessRecyclerView", "Call load more");
                if (onLoadMoreListener != null) {
                    //Если нужны новые данные, то делаем колбек onLoadMore
                    onLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
