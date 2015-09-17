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
/*
 * This file is a derivative work of android.widget.CursorAdapter
 *
 * Copyright (C) 2006 The Android Open Source Project
 */
package es.jpv.android.examples.loadersexample.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Abstract adapter class meant to use Cursor to populate a RecyclerView.
 *
 * The adapter class extending this one only needs to define the way views behave
 * (for example implementing click listeners) and how are populated. Cursor management is done here.
 *
 * @param <VH> The class extending this has to implement an static inner class extending
 *            RecyclerView.ViewHolder. This class manages the content and behaviour of the
 *            views contained in the RecyclerView
 */
public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected Cursor mCursor;
    protected DataSetObserver mDataSetObserver;
    protected int mRowIDColumn;
    protected boolean mDataValid;

    /**
     * Constructor
     *
     * @param context The context associated to the adapter
     * @param cursor The cursor which contains the data we want to deal with
     */
    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        swapCursor(cursor);
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
    public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

    /**
     * <p>Defines the content of a given ViewHolder instance.</p>
     * Allows population of data to be shown in the RecyclerView item or definition
     * of internal members, such as listeners
     *
     * @param viewHolder ViewHolder to which content will be binded
     * @param i          Position in relation to the adapter
     */
    @Override
    public abstract void onBindViewHolder(VH viewHolder, int i);

    /**
     * Returns the Cursor of the adapter
     *
     * @return the Cursor
     */
    public Cursor getCursor() {
        return this.mCursor;
    }

    /**
     * Sets a DataSetObserver to the Cursor
     *
     * @param dataSetObserver The DataSetObserver we want to set
     */
    public void setDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDataSetObserver = dataSetObserver;
    }

    /**
     * Returns an item stored into the Cursor, depending on position
     *
     * @param position position to retrieve
     * @return The item
     */
    public Object getItem(int position) {
        if(this.mDataValid && this.mCursor != null) {
            this.mCursor.moveToPosition(position);
            return this.mCursor;
        } else {
            return null;
        }
    }

    /**
     * Returns the ID (_id column) of the item stored into the Cursor, depending on position
     *
     * @param position position to retrieve
     * @return value of _id
     */
    @Override
    public long getItemId(int position) {
        return this.mDataValid && this.mCursor != null
                ? (this.mCursor.moveToPosition(position)?this.mCursor.getLong(this.mRowIDColumn):0L)
                :0L;
    }

    /**
     * Get the count of items stored in the Cursor
     *
     * @return number of entries in the Cursor
     */
    @Override
    public int getItemCount() {
        return this.mDataValid && this.mCursor != null?this.mCursor.getCount():0;
    }

    /**
     * Swap in a new Cursor. The returned old Cursor <em>is</em> closed.
     *
     * @param cursor The new cursor to be used.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = this.swapCursor(cursor);
        if(old != null) {
            old.close();
        }

    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  The returned old Cursor is <em>not</em>
     * closed.
     *
     * @param newCursor The new cursor to be used.
     * @return Returns the previously set Cursor, or null if there was not one.
     * If the given new Cursor is the same instance is the previously set
     * Cursor, null is also returned.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetChanged();
        }
        return oldCursor;
    }

}
