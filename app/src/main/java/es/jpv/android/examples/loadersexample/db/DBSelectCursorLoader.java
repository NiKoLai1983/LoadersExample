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
 * This file is a derivative work of android.support.v4.content.CursorLoader
 *
 * Copyright (C) 2011 The Android Open Source Project
 */
package es.jpv.android.examples.loadersexample.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Allows to execute SQL SELECT statements on the UI thread asynchronously
 */
public class DBSelectCursorLoader extends AsyncTaskLoader<Cursor> {
    final ForceLoadContentObserver mObserver;

    SQLiteOpenHelper mDB;
    String mTable;
    String[] mProjection;
    String mSelection;
    String[] mSelectionArgs;
    String mSortOrder;
    Long mLimit;

    Cursor mCursor;

    /**
     * Runs a query asynchronously
     *
     * @return A Cursor object with the data
     */
    @Override
    public Cursor loadInBackground() {
        Cursor cursor = mDB.getReadableDatabase().query(
                mTable,
                mProjection,
                mSelection,
                mSelectionArgs,
                null,
                null,
                mSortOrder,
                String.valueOf(mLimit)
        );
        if (cursor != null) {
            // Ensure the cursor window is filled
            cursor.getCount();
            cursor.registerContentObserver(mObserver);
        }
        return cursor;
    }

    /* Runs on the UI thread */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    /**
     * Creates an empty unspecified DBSelectCursorLoader.  You must follow this with
     * calls to {@link #setDB(SQLiteOpenHelper)}, {@link #setSelection(String)}, etc
     * to specify the query to perform.
     */
    public DBSelectCursorLoader(Context context) {
        super(context);
        mObserver = new ForceLoadContentObserver();
    }

    /**
     * Creates a fully-specified DBSelectCursorLoader.  See
     * {@link android.content.ContentResolver#query(Uri, String[], String, String[], String)
     * ContentResolver.query()} for documentation on the meaning of the
     * parameters. db refers to the instance of the SQLiteOpenHelper subclass used in the app
     */
    public DBSelectCursorLoader(Context context, SQLiteOpenHelper db, String tableName,
                                String[] projection, String selection, String[] selectionArgs,
                                String sortOrder, Long limit) {
        super(context);
        mObserver = new ForceLoadContentObserver();
        mDB = db;
        mTable = tableName;
        mProjection = projection;
        mSelection = selection;
        mSelectionArgs = selectionArgs;
        mSortOrder = sortOrder;
        mLimit = limit;
    }

    /**
     * Starts an asynchronous load of the data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     *
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

    public SQLiteOpenHelper getDB() {
        return mDB;
    }

    public void setDB(SQLiteOpenHelper db) {
        mDB = db;
    }

    public String[] getProjection() {
        return mProjection;
    }

    public void setProjection(String[] projection) {
        mProjection = projection;
    }

    public String getSelection() {
        return mSelection;
    }

    public void setSelection(String selection) {
        mSelection = selection;
    }

    public String[] getSelectionArgs() {
        return mSelectionArgs;
    }

    public void setSelectionArgs(String[] selectionArgs) {
        mSelectionArgs = selectionArgs;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(String sortOrder) {
        mSortOrder = sortOrder;
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
        writer.print(prefix); writer.print("mUri="); writer.println(mDB);
        writer.print(prefix); writer.print("mProjection=");
        writer.println(Arrays.toString(mProjection));
        writer.print(prefix); writer.print("mSelection="); writer.println(mSelection);
        writer.print(prefix); writer.print("mSelectionArgs=");
        writer.println(Arrays.toString(mSelectionArgs));
        writer.print(prefix); writer.print("mSortOrder="); writer.println(mSortOrder);
        writer.print(prefix); writer.print("mCursor="); writer.println(mCursor);
    }

    /**
     * Executes a raw SQL statement asynchronously
     *
     * @param sql The SQL statement to execute. Arguments have to be written as ?
     * @param writeMode false if we want to access the database on read-only mode
     * @param bindArgs Arguments to be binded to the SQL statement
     */
    public void execSQL(String sql, boolean writeMode, Object... bindArgs) {
        new DBAsyncTask(mDB, this).execute(sql, bindArgs, writeMode);
    }
}

