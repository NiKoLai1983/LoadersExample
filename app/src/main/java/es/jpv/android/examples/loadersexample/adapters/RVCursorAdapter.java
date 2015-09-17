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
package es.jpv.android.examples.loadersexample.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.jpv.android.examples.loadersexample.R;
import es.jpv.android.examples.loadersexample.db.DataProviderContract;

/**
 * Adapter class for the RecyclerView.
 *
 * Cursor management is done by the superclass. We only need to define RecyclerView.ViewHolder
 * structure and how those objects are created and binded.
 */
public class RVCursorAdapter extends RecyclerViewCursorAdapter<RVCursorAdapter.ViewHolder> {

    /**
     * The XML layout ID to be inflated to represent each item shown in the RecyclerView
     */
    int mViewLayout = R.layout.rv_item;

    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    /**
     * Constructor
     *
     * @param context The context associated to the adapter
     * @param cursor The cursor which contains the data we want to deal with
     */
    public RVCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    /**
     * <p>New ViewHolder objects are created here.</p>
     * <p>Those objects represent an single item shown inside the RecyclerView.</p>
     * LayoutInflater can be used to inflate a new RecyclerView.ViewHolder instance using XML
     * layout resources.
     *
     * @param viewGroup Parent view
     * @param i         Position in relation to the adapter
     * @return RecyclerView.ViewHolder object
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Inflate the view from layout resource
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mViewLayout, viewGroup, false);
        // Set the view's size, margins, paddings and layout parameters if required
        // TODO Set additional parameters on the view if required
        // Finally return a ViewHolder instance based on the view we inflated
        return new ViewHolder(v, onItemClickListener, onItemLongClickListener);
    }

    /**
     * <p>Sets an OnItemClickListener to this adapter</p>
     * <p>The listener will be called when an item of the RecyclerView is clicked</p>
     * <p>The listener can be any class implementing RVCursorAdapter.OnItemClickListener</p>
     *
     * @param onItemClickListener The listener to be called when an item is clicked
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * <p>Sets an OnItemLongClickListener to this adapter</p>
     * <p>The listener will be called when an item of the RecyclerView is long-clicked</p>
     * <p>The listener can be any class implementing RVCursorAdapter.OnItemLongClickListener</p>
     *
     * @param onItemLongClickListener The listener to be called when an item is long-clicked
     */
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * <p>Defines the content of a given ViewHolder instance.</p>
     * Allows population of data to be shown in the RecyclerView item or definition
     * of internal members, such as listeners
     *
     * @param viewHolder ViewHolder to which content will be binded
     * @param i          Position in relation to the adapter
     */
    @Override
    public void onBindViewHolder(RVCursorAdapter.ViewHolder viewHolder, int i) {
        getCursor().moveToPosition(i);
        viewHolder.itemID.setText(mCursor.getString(DataProviderContract.ITEMS_ID));
        viewHolder.itemName.setText(mCursor.getString(DataProviderContract.ITEMS_ITEM));
        viewHolder.position = i;
    }

    /**
     * <p>Listener interface to handle clicks on items</p>
     */
    public static interface OnItemClickListener {
        /**
         * Called when a View contained into a RVCursorAdapter is clicked
         *
         * @param v The View that was clicked
         * @param position Position with respect to the adapter that was clicked
         */
        public void onItemClick(View v, int position);
    }

    /**
     * <p>Listener interface to handle long-clicks on items</p>
     */
    public static interface OnItemLongClickListener {
        /**
         * Called when a View contained into a RVCursorAdapter is long-clicked
         *
         * @param v The View that was long-clicked
         * @param position Position with respect to the adapter that was long-clicked
         */
        public void onItemLongClick(View v, int position);
    }

    /**
     * <p>Defines the content of each of the items contained in the RecyclerView</p>
     * <p>This includes data and click listeners</p>
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout linearLayout;
        TextView itemName;
        TextView itemID;
        int position;
        OnItemClickListener mClickListener;
        OnItemLongClickListener mLongClickListener;

        /**
         * Constructor
         *
         * @param itemView The View to be shown in the RecyclerView as an item
         * @param mClickListener This listener will handle clicks on the View
         * @param mLongClickListener This listener will handle long-clicks on the View
         */
        ViewHolder(
                View itemView,
                OnItemClickListener mClickListener,
                OnItemLongClickListener mLongClickListener) {
            super(itemView);
            this.mClickListener = mClickListener;
            this.mLongClickListener = mLongClickListener;
            itemID = (TextView) itemView.findViewById(R.id.row_id);
            itemName = (TextView) itemView.findViewById(R.id.textView);
            itemName.setOnClickListener(this);
            itemName.setOnLongClickListener(this);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);
        }

        /**
         * Handles click events on ViewHolder instances
         *
         * @param v View that has been clicked
         */
        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, position);
            }
        }

        /**
         * Handles long-click events on ViewHolder instances
         *
         * @param v View that has been long-clicked
         * @return Returns true if there was a listener set on the View and the event was sent
         */
        @Override
        public boolean onLongClick(View v) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(v, position);
                return true;
            }
            return false;
        }
    }
}
