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
package es.jpv.android.examples.loadersexample.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

/**
 * Allows to execute SQL stataments on the UI thread asynchronously
 */
public class DBAsyncTask extends AsyncTask<Object, Void, Exception> {

    SQLiteOpenHelper mDB = null;
    Loader mLoader = null;

    /**
     * Constructor
     * </p>
     * @param mDB A class extending SQLiteOpenHelper
     * @param mLoader A Loader instance. This loader is notified after task completes
     */
    public DBAsyncTask(@NonNull SQLiteOpenHelper mDB, @NonNull Loader mLoader) {
        this.mDB = mDB;
        this.mLoader = mLoader;
    }

    /**
     * <p>Performs an asynchronous SQL query on the database</p>
     * </p>
     * @param params The parameters of the query.
     *               <p>params[0] contains an SQL statement with arguments as ?</p>
     *               <p>params[1] contains the arguments of the statament as an array</p>
     *               <p>params[2] contains true if we want the database to be writable</p>
     * @return If an Exception is generated, we return it.
     */
    @Override
    protected Exception doInBackground(Object... params) {

        String sqlStatement = (String) params[0];
        Object[] sqlParams = (Object[]) params[1];
        boolean writeMode = params[2] != null && (boolean) params[2];

        try {
            SQLiteDatabase database =
                    writeMode ? mDB.getWritableDatabase() : mDB.getReadableDatabase();
            if (sqlParams != null && sqlParams.length != 0) {
                database.execSQL(sqlStatement, sqlParams);
            } else {
                database.execSQL(sqlStatement);
            }

        } catch (Exception e) {
            return e;
        }

        return null;
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p>The Loader associated with the task will be notified that changes have been made</p>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param e The result of the operation computed by {@link #doInBackground}.
     */
    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        mLoader.onContentChanged();
    }

}
