package es.jpv.android.examples.loadersexample;

import android.provider.BaseColumns;

public final class DataProviderContract implements BaseColumns {

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
}
