package letshangllc.completelists.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import letshangllc.completelists.ListAdapters.ListsAdapter;
import letshangllc.completelists.Models.List;

/**
 * Created by cvburnha on 11/29/2015.
 */
public class ListsCRUD {
    DatabaseHelper databaseHelper;
    private ArrayList<List> lists;
    private ListsAdapter listsAdapter;

    /* todo reorganize this */

    public ListsCRUD(Context context, ArrayList<List> lists, ListsAdapter listsAdapter){
        this.databaseHelper = new DatabaseHelper(context);
        this.lists = lists;
        this.listsAdapter = listsAdapter;
    }

    public void readLists(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        //lists= new ArrayList<>();
        String[] projetion = {ListTableContract.COLUMN_LIST_ID, ListTableContract.COLUMN_LIST_NAME};
        Cursor c = db.query(ListTableContract.TABLE_NAME, projetion, null,null,null,null,null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            lists.add(new List(c.getInt(0),c.getString(1)));
            c.moveToNext();
        }
        c.close();
        listsAdapter.notifyDataSetChanged();
    }

    public void insertIntoDB(String newList){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListTableContract.COLUMN_LIST_NAME, newList);
        db.insert(ListTableContract.TABLE_NAME, null, values);

        /* Add the new day to the listview */
        lists.add(new List(getLastLid(), newList));
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    public void updateDB(List list, String newName){
        /*Update the day in the Database */
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(ListTableContract.COLUMN_LIST_NAME, newName);
        db.update(ListTableContract.TABLE_NAME, newValues, "" + ListTableContract.COLUMN_LIST_ID + " = " + list.getLid(), null);
        /*Update the day on the listview */
        list.setName(newName);
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    /* Todo delete the items in the list from other table */
    public void deleteFromDatabase(List list){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        /* Delete from db where lid */
        db.delete(ListTableContract.TABLE_NAME, "" + ListTableContract.COLUMN_LIST_ID + " = " + list.getLid(), null);

        lists.remove(list);
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    public int getLastLid(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT Max(lid) FROM Lists";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(0);
    }
}
