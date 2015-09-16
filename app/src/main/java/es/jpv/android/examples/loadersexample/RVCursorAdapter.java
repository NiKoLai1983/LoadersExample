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

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RVCursorAdapter extends RecyclerViewCursorAdapter<RVCursorAdapter.ViewHolder> {

    /**
     * The XML layout ID to be inflated to represent each item shown in the RecyclerView
     */
    int mViewLayout = R.layout.rv_item;

    OnItemClickListener onItemClickListener;
    OnItemLongClickListener onItemLongClickListener;

    public RVCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    /**
     * New ViewHolder objects are created here.
     * Those objects represent an single item shown inside the RecyclerView
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * Defines the content of a given ViewHolder instance.
     * Allows population of data to be shown in the RecyclerView item or definition
     * of internal members, such as listeners
     *
     * @param viewHolder
     * @param i          Position in relation to the adapter
     */
    @Override
    public void onBindViewHolder(RVCursorAdapter.ViewHolder viewHolder, int i) {
        getCursor().moveToPosition(i);
        viewHolder.itemID.setText(mCursor.getString(DataProviderContract.ITEMS_ID));
        viewHolder.itemName.setText(mCursor.getString(DataProviderContract.ITEMS_ITEM));
        viewHolder.position = i;
    }

    public static interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public static interface OnItemLongClickListener {
        public void onItemLongClick(View v, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        LinearLayout linearLayout;
        TextView itemName;
        TextView itemID;
        int position;
        OnItemClickListener mClickListener;
        OnItemLongClickListener mLongClickListener;

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

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClick(v, position);
            }
        }

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
