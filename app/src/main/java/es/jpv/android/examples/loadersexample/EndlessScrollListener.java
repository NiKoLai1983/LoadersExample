package es.jpv.android.examples.loadersexample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private EndlessScrollLoader mLoader;
    private LinearLayoutManager llm;
    private int lastItemCount;

    public EndlessScrollListener(EndlessScrollLoader callback, LinearLayoutManager llm) {
        mLoader = callback;
        this.llm = llm;
    }

    /**
     * Endless RecyclerView page loader interface
     */
    public interface EndlessScrollLoader {
        /**
         * Endless RecyclerView page loader
         *
         * When a RecyclerView is scrolled until its end this method is invoked to load more rows
         */
        public void loadMore();
    }

    /**
     * Callback method to be invoked when RecyclerView's scroll state changes.
     *
     * @param recyclerView The RecyclerView whose scroll state has changed.
     * @param newState     The updated scroll state.
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (lastItemCount == llm.getItemCount()) return;
            int lastVisibleItem = llm.findLastCompletelyVisibleItemPosition() + 1;
            if (lastVisibleItem == llm.getItemCount()) {
                lastItemCount = llm.getItemCount();
                mLoader.loadMore();
            }
        }
    }
}
