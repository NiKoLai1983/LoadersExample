package es.jpv.android.examples.loadersexample;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerViewCursorAdapter
        extends RecyclerView.Adapter<RecyclerViewCursorAdapter.ViewHolder> {

    Context mContext;
    Cursor mCursor;
    int mViewLayout = R.layout.rv_item;

    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    @Override
    public RecyclerViewCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Inflate the view from layout resource
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(mViewLayout, viewGroup, false);
        // Set the view's size, margins, paddings and layout parameters if required
        // TODO Set additional parameters on the view if required
        // Finally return a ViewHolder instance based on the view we inflated
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewCursorAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
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
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
