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

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;

public class DBAsyncTask extends AsyncTask<Object, Void, Exception> {

    SQLiteOpenHelper mDB = null;
    Loader mLoader = null;

    public DBAsyncTask(@NonNull SQLiteOpenHelper mDB, @NonNull Loader mLoader) {
        this.mDB = mDB;
        this.mLoader = mLoader;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
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
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param e The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        mLoader.onContentChanged();
    }
}
