package letshangllc.completelists.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;


/**
 * Created by cvburnha on 11/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CompleteListDB";


    private static final String TABLE_CREATE_LISTS =
            "CREATE TABLE Lists("
                    + "lid integer primary key AUTOINCREMENT,"
                    + "list TEXT"
                    + ")";

    private static final String TABLE_CREATE_LIST_ITEMS =
            "Create table Items("
                    + "iid integer primary key AUTOINCREMENT,"
                    + "lid integer,"
                    + "item TEXT,"
                    + "FOREIGN KEY(lid) REFERENCES Lists(lid)"
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
