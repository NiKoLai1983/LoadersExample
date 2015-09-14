package es.jpv.android.examples.loadersexample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;

    //Strings to construct SQL statements
    private static String EQUAL = "=";
    private static String ARG = "?";

    //Private SQL
    private static final String ITEMS_TABLE_DEFINITION =
            "CREATE TABLE IF NOT EXISTS " + DataProviderContract.ITEMS_TABLE_NAME +
                " ( " + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DataProviderContract.ITEMS_COLUMN_ITEM + " TEXT);";
    private static final String ITEMS_INSERT_ROW =
            "INSERT INTO " + DataProviderContract.ITEMS_TABLE_NAME + " (" +
                    DataProviderContract.ITEMS_COLUMN_ITEM + ") VALUES (?);";
    private static final String ITEMS_TABLE_DROP =
            "DROP TABLE IF EXISTS " + DataProviderContract.ITEMS_TABLE_NAME;

    //Public SQL
    public static final String ITEMS_ADD_ITEM = "INSERT INTO "
            + DataProviderContract.ITEMS_TABLE_NAME + "(" + DataProviderContract.ITEMS_COLUMN_ITEM
            + ") VALUES (" + ARG + ")";
    public static final String ITEMS_DELETE_ITEM_BY_ID = "DELETE FROM " +
            DataProviderContract.ITEMS_TABLE_NAME + " WHERE " + BaseColumns._ID + EQUAL + ARG;

    public static synchronized DBHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(
                    context.getApplicationContext(),
                    DataProviderContract.DB_NAME,
                    null,
                    DataProviderContract.DB_VERSION
            );
        }
        return sInstance;
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DataProviderContract.DB_NAME, factory, DataProviderContract.DB_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ITEMS_TABLE_DEFINITION);
        //Create root items
        for (int i=0 ; i<101 ; i++) {
            db.execSQL(ITEMS_INSERT_ROW, new Object[] { "Item " + i });
        }
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ITEMS_TABLE_DROP);
        onCreate(db);
    }
}
