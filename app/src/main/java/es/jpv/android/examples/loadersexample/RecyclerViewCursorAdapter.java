package es.jpv.android.examples.loadersexample;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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
     * @param context
     * @param cursor
     */
    public RecyclerViewCursorAdapter(Context context, Cursor cursor) {
        mContext = context;
        swapCursor(cursor);
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
    public abstract VH onCreateViewHolder(ViewGroup viewGroup, int i);

    /**
     * Defines the content of a given ViewHolder instance.
     * Allows population of data to be shown in the RecyclerView item or definition
     * of internal members, such as listeners
     *
     * @param viewHolder
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
     * @param dataSetObserver
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
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
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
