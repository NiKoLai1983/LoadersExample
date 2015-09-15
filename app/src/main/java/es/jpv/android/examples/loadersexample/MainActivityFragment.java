package es.jpv.android.examples.loadersexample;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements RVCursorAdapter.OnItemClickListener, RVCursorAdapter.OnItemLongClickListener,
                    EndlessScrollListener.EndlessScrollLoader {

    final int LOADER_ID = 1;
    final String[] PROJECTION = new String[] {
            BaseColumns._ID,
            DataProviderContract.ITEMS_COLUMN_ITEM
    };
    RVCursorAdapter adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new EndlessScrollListener(this, llm));
        adapter = new RVCursorAdapter(getActivity(), null);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, new DBLoader());
    }

    @Override
    public void onItemClick(View v, int position) {
        Integer rowID = retrieveItemID(v);
        if (rowID == null) {
            throw new IllegalStateException("Unexpected View type has been clicked");
        }
        Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        //Be careful with getItemCount().
        ((DBCursorLoader) loader).execSQL(
                DBHelper.ITEMS_ADD_ITEM,
                true,
                "Item " + adapter.getItemCount()
        );
        Toast.makeText(getActivity(),
                "Clicked " + ((TextView) v.findViewById(R.id.textView)).getText() +
                        " on position " + position + " with DB ID " + rowID.toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View v, int position) {
        Integer rowID = retrieveItemID(v);
        if (rowID == null) {
            throw new IllegalStateException("Unexpected View type has been clicked");
        }
        Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        ((DBCursorLoader) loader).execSQL(
                DBHelper.ITEMS_DELETE_ITEM_BY_ID,
                true,
                rowID
        );
        Toast.makeText(getActivity(),
                "Deleted " + ((TextView) v.findViewById(R.id.textView)).getText() +
                        " on position " + position + " with DB ID " + rowID.toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns the item BaseColumn ID (_id)
     *
     * @param v
     * @return _id value of the item
     */
    public Integer retrieveItemID(View v) {
        //If TextView is clicked we need to get the Parent to find the hidden TextView with the _id
        if (v instanceof TextView) {
            v = (LinearLayout) v.getParent();
        }
        return new Integer(((TextView) v.findViewById(R.id.row_id)).getText().toString());
    }

    /**
     * Endless RecyclerView page loader
     * <p/>
     * When a RecyclerView is scrolled until its end this method is invoked to load more rows
     */
    @Override
    public void loadMore() {
        Toast.makeText(getActivity(), "End of the list!!", Toast.LENGTH_SHORT).show();
    }

    private class DBLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new DBCursorLoader(
                    getActivity(),
                    DBHelper.getInstance(getActivity()),
                    PROJECTION,
                    null,
                    null,
                    null);
        }

        /**
         * Called when a previously created loader has finished its load.  Note
         * that normally an application is <em>not</em> allowed to commit fragment
         * transactions while in this call, since it can happen after an
         * activity's state is saved.  See {@link FragmentManager#beginTransaction()
         * FragmentManager.openTransaction()} for further discussion on this.
         * <p/>
         * <p>This function is guaranteed to be called prior to the release of
         * the last data that was supplied for this Loader.  At this point
         * you should remove all use of the old data (since it will be released
         * soon), but should not do your own release of the data since its Loader
         * owns it and will take care of that.  The Loader will take care of
         * management of its data so you don't have to.  In particular:
         * <p/>
         * <ul>
         * <li> <p>The Loader will monitor for changes to the data, and report
         * them to you through new calls here.  You should not monitor the
         * data yourself.  For example, if the data is a {@link Cursor}
         * and you place it in a {@link CursorAdapter}, use
         * the {@link CursorAdapter#CursorAdapter(Context,
         * Cursor, int)} constructor <em>without</em> passing
         * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
         * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
         * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
         * from doing its own observing of the Cursor, which is not needed since
         * when a change happens you will get a new Cursor throw another call
         * here.
         * <li> The Loader will release the data once it knows the application
         * is no longer using it.  For example, if the data is
         * a {@link Cursor} from a {@link CursorLoader},
         * you should not call close() on it yourself.  If the Cursor is being placed in a
         * {@link CursorAdapter}, you should use the
         * {@link CursorAdapter#swapCursor(Cursor)}
         * method so that the old Cursor is not closed.
         * </ul>
         *
         * @param loader The Loader that has finished.
         * @param data   The data generated by the Loader.
         */
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            adapter.swapCursor(data);
        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            adapter.swapCursor(null);
        }

    }

}
