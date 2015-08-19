package es.jpv.android.examples.loadersexample;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DataProviderContract implements BaseColumns {

    private DataProviderContract() { }

    /**
     * The URI scheme used for content URIs
     */
    public static final String SCHEME = "content";

    /**
     * The provider's authority
     */
    public static final String AUTHORITY = "es.jpv.android.examples.loadersexample";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     * Database file name
     */
    public static final String DB_NAME = "DummyItemsData";

    /**
     * Database file version
     */
    public static final int VERSION = 1;

    /**
     * Items table name
     */
    public static final String ITEMS_TABLE_NAME = "items";

    /**
     * Items table content URI
     */
    public static final Uri ITEMS_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, ITEMS_TABLE_NAME);

    /**
     * Column with description of the item
     */
    public static final String ITEMS_COLUMN_ITEM = "item";
}
