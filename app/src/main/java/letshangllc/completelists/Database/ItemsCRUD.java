package letshangllc.completelists.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import letshangllc.completelists.ListAdapters.ItemsAdapter;
import letshangllc.completelists.Models.Item;

/**
 * Created by cvburnha on 11/30/2015.
 */
public class ItemsCRUD {
    DatabaseHelper databaseHelper;
    private ArrayList<Item> items;
    private ItemsAdapter itemsAdapter;
    private int lid;

    public ItemsCRUD(Context context, ArrayList<Item> items, ItemsAdapter itemsAdapter, int lid){
        this.databaseHelper = new DatabaseHelper(context);
        this.items = items;
        this.itemsAdapter = itemsAdapter;
        this.lid = lid;
    }

    public void readLists(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] projetion = {ListItemsTableContract.COLUMN_ITEMS_ID,
                ListItemsTableContract.COLUMN_LIST_ID,
                ListItemsTableContract.COLUMN_ITEM_NAME,
                ListItemsTableContract.COLUMN_ITEM_NOTE};
        String selection = ListItemsTableContract.COLUMN_LIST_ID + " = " + lid;
        Cursor c = db.query(ListItemsTableContract.TABLE_NAME, projetion, selection,null,null,null,null);
        c.moveToFirst();

        while (c.isAfterLast() == false) {
            items.add(new Item(c.getInt(0), c.getInt(1),c.getString(2), c.getString(3)));
            c.moveToNext();
        }
        c.close();
        itemsAdapter.notifyDataSetChanged();
        db.close();
    }

    public void insertItemIntoDB(String name){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ListItemsTableContract.COLUMN_ITEM_NAME, name);
        values.put(ListItemsTableContract.COLUMN_LIST_ID, lid);
        db.insert(ListItemsTableContract.TABLE_NAME, null, values);

        /* Add the new day to the listview */
        items.add(new Item(getLastIid(), lid, name, ""));
        itemsAdapter.notifyDataSetChanged();
        db.close();
    }

    public void updateName(Item item, String newName){
        /*Update the day in the Database */
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(ListItemsTableContract.COLUMN_ITEM_NAME, newName);
        db.update(ListItemsTableContract.TABLE_NAME, newValues, "" + ListItemsTableContract.COLUMN_ITEMS_ID + " = " + item.getId(), null);
        /*Update the day on the listview */
        item.setItemName(newName);
        itemsAdapter.notifyDataSetChanged();
        db.close();
    }


    public void deleteFromDatabase(Item item){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        /* Delete from db where lid */
        db.delete(ListItemsTableContract.TABLE_NAME, "" + ListItemsTableContract.COLUMN_ITEMS_ID + " = " + item.getId(), null);

        items.remove(item);
        itemsAdapter.notifyDataSetChanged();
        db.close();
    }

    private int getLastIid(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String sql = "SELECT Max(iid) FROM " + ListItemsTableContract.TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(0);
    }
}
