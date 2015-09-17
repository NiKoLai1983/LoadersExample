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

import es.jpv.android.examples.loadersexample.adapters.RVCursorAdapter;
import es.jpv.android.examples.loadersexample.db.DBHelper;
import es.jpv.android.examples.loadersexample.db.DBSelectCursorLoader;
import es.jpv.android.examples.loadersexample.db.DataProviderContract;


/**
 * This fragment contains a RecyclerView with items which we can interact with
 */
public class MainActivityFragment extends Fragment
        implements RVCursorAdapter.OnItemClickListener, RVCursorAdapter.OnItemLongClickListener,
                    EndlessRVScrollListener.EndlessScrollLoader {

    final int LOADER_ID = 1;
    final String[] PROJECTION = new String[] {
            BaseColumns._ID,
            DataProviderContract.ITEMS_COLUMN_ITEM
    };
    RVCursorAdapter adapter;
    long lastLimitLoaded = DataProviderContract.ITEMS_RV_LIMIT;

    public MainActivityFragment() { }

    /**
     * <p>Creates the RecyclerView shown in the Fragment with its adapter.</p>
     * <p>The adapter is created with no data at this point.</p>
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new EndlessRVScrollListener(this, llm));
        adapter = new RVCursorAdapter(getActivity(), null);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    /**
     * Initiates the Loader to perform the first load of data onto the RecyclerView
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString("Table", DataProviderContract.ITEMS_TABLE_NAME);
        loaderArgs.putStringArray("Projection", PROJECTION);
        loaderArgs.putString("Order", BaseColumns._ID);
        loaderArgs.putLong("Limit", lastLimitLoaded);
        getLoaderManager().initLoader(LOADER_ID, loaderArgs, new DBSelectLoader());
    }

    /**
     * Called when a View contained into a RVCursorAdapter is clicked
     *
     * @param v The View that was clicked
     * @param position Position with respect to the adapter that was clicked
     */
    @Override
    public void onItemClick(View v, int position) {
        Integer rowID = retrieveItemID(v);
        if (rowID == null) {
            throw new IllegalStateException("Unexpected View type has been clicked");
        }
        Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        //Be careful with getItemCount().
        ((DBSelectCursorLoader) loader).execSQL(
                DBHelper.ITEMS_ADD_ITEM,
                true,
                "Item " + adapter.getItemCount()
        );
        Toast.makeText(getActivity(),
                "Clicked " + ((TextView) v.findViewById(R.id.textView)).getText() +
                        " on position " + position + " with DB ID " + rowID.toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a View contained into a RVCursorAdapter is long-clicked
     *
     * @param v The View that was long-clicked
     * @param position Position with respect to the adapter that was long-clicked
     */
    @Override
    public void onItemLongClick(View v, int position) {
        Integer rowID = retrieveItemID(v);
        if (rowID == null) {
            throw new IllegalStateException("Unexpected View type has been clicked");
        }
        Loader<?> loader = getLoaderManager().getLoader(LOADER_ID);
        ((DBSelectCursorLoader) loader).execSQL(
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
     * <p>Endless RecyclerView page loader</p>
     *
     * When a RecyclerView is scrolled until its end this method is invoked to load more rows
     */
    @Override
    public void loadMore() {
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString("Table", DataProviderContract.ITEMS_TABLE_NAME);
        loaderArgs.putStringArray("Projection", PROJECTION);
        loaderArgs.putString("Order", BaseColumns._ID);
        loaderArgs.putLong("Limit", lastLimitLoaded);
        getLoaderManager().restartLoader(LOADER_ID, loaderArgs, new DBSelectLoader());
    }

    private class DBSelectLoader implements LoaderManager.LoaderCallbacks<Cursor> {

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new DBSelectCursorLoader(
                    getActivity(),
                    DBHelper.getInstance(getActivity()),
                    args.getString("Table"),
                    args.getStringArray("Projection"),
                    args.getString("Selection"),
                    args.getStringArray("SelectionArgs"),
                    args.getString("Order"),
                    args.getLong("Limit")
            );
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
            lastLimitLoaded = lastLimitLoaded + DataProviderContract.ITEMS_RV_LIMIT;
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
            lastLimitLoaded = DataProviderContract.ITEMS_RV_LIMIT;
        }

    }

}
