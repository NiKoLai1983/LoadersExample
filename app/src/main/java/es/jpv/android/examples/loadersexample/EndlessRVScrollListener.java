/*
 * Copyright (C) 2015 Jesús Platas Varet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.jpv.android.examples.loadersexample;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * <p>Detects when a RecyclerView has been scrolled to the bottom.</p>
 * <p>This is useful not to load a big dataset into a RecyclerView all at once.</p>
 * <p>The class responsible of loading more rows has to implement
 * the EndlessRVScrollListener.EndlessScrollLoader interface</p>
 */
public class EndlessRVScrollListener extends RecyclerView.OnScrollListener {

    private EndlessScrollLoader mLoader;
    private LinearLayoutManager llm;
    private int lastItemCount;

    public EndlessRVScrollListener(EndlessScrollLoader callback, LinearLayoutManager llm) {
        mLoader = callback;
        this.llm = llm;
    }

    /**
     * Endless RecyclerView page loader interface
     */
    public interface EndlessScrollLoader {
        /**
         * <p>Endless RecyclerView page loader</p>
         *
         * When a RecyclerView is scrolled until its end this method is invoked to load more rows
         */
        public void loadMore();
    }

    /**
     * Callback method to be invoked when RecyclerView's scroll state changes.
     *
     * When the RecyclerView stops scrolling we check the last item that is fully visible.
     * Should the last visible list item match with the number of items in the adapter,
     * we have reached the end of the list so we do another query to get more rows
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
