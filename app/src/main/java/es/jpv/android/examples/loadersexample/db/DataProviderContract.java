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

import android.provider.BaseColumns;

/**
 * Contract class for the SQLite database
 */
public final class DataProviderContract implements BaseColumns {

    /**
     * We are not supposed to create instances of this, so private constructor
     */
    private DataProviderContract() { }

    /**
     * Database file name
     */
    public static final String DB_NAME = "dummyitemsdata.db";

    /**
     * Database file version
     */
    public static final int DB_VERSION = 1;

    /**
     * Items table name
     */
    public static final String ITEMS_TABLE_NAME = "items";

    /**
     * Column with description of the item
     */
    public static final String ITEMS_COLUMN_ITEM = "item";

    /**
     * Index of ID column in Items table
     */
    public static final int ITEMS_ID = 0;

    /**
     * Index of item name column in Items table
     */
    public static final int ITEMS_ITEM = 1;

    /**
     * Amount of rows shown in the RecyclerView per loading cycle
     */
    public static final long ITEMS_RV_LIMIT = 15;

}
