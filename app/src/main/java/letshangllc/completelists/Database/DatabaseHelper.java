package letshangllc.completelists.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by cvburnha on 11/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "CompleteListDB";


    private static final String TABLE_CREATE_LISTS =
            "CREATE TABLE " + ListTableContract.TABLE_NAME + " ("
                    + ListTableContract.COLUMN_LIST_ID + " integer primary key AUTOINCREMENT,"
                    + ListTableContract.COLUMN_LIST_NAME + " TEXT"
                    + ")";

    private static final String TABLE_CREATE_LIST_ITEMS =
            "Create TABLE " + ListItemsTableContract.TABLE_NAME +" ( "
                    + ListItemsTableContract.COLUMN_ITEMS_ID +" integer primary key AUTOINCREMENT, "
                    + ListItemsTableContract.COLUMN_LIST_ID + " integer, "
                    + ListItemsTableContract.COLUMN_ITEM_NAME + " TEXT, "
                    + ListItemsTableContract.COLUMN_ITEM_NOTE + " TEXT, "
                    + "FOREIGN KEY("+ListItemsTableContract.COLUMN_LIST_ID+") " +
                        "REFERENCES Lists("+ListTableContract.COLUMN_LIST_ID  +") "
                    + ")";;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_LISTS);
        db.execSQL(TABLE_CREATE_LIST_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
