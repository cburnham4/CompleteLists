package letshangllc.completelists;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import letshangllc.completelists.Database.DatabaseHelper;
import letshangllc.completelists.Database.ListTableContract;
import letshangllc.completelists.Dialogs.Dialog_AddItem;
import letshangllc.completelists.ListAdapters.ListsAdapter;
import letshangllc.completelists.Models.List;

public class Activity_Lists extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<List> lists;
    private ListsAdapter listsAdapter;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        databaseHelper = new DatabaseHelper(this);
        this.readLists();
        listsAdapter = new ListsAdapter(lists, this);
        recyclerView.setAdapter(listsAdapter);

    }

    private void readLists(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        lists= new ArrayList<>();
        String[] projetion = {ListTableContract.COLUMN_NAME_LIST_ID, ListTableContract.COLUMN_NAME_LIST};
        Cursor c = db.query(ListTableContract.TABLE_NAME, projetion, null,null,null,null,null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            lists.add(new List(c.getInt(0),c.getString(1)));
            c.moveToNext();
        }
        c.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                Dialog_AddItem dialog_addItem = new Dialog_AddItem();
                dialog_addItem.setCallback(new Dialog_AddItem.AddItemListener() {
                    @Override
                    public void onDialogPositiveClick(String newName) {
                        if(!newName.isEmpty() && !newName.equals("")){
                            insertIntoDB(newName);
                        }
                    }
                });
                dialog_addItem.show(getSupportFragmentManager(), "dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void insertIntoDB(String newList){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ListTableContract.COLUMN_NAME_LIST, newList);
        db.insert(ListTableContract.TABLE_NAME, null, values);

        /* Add the new day to the listview */
        lists.add(new List(getLastLid(), newList));
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    private void updateDB(List list, String newName){
        /*Update the day in the Database */
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(ListTableContract.COLUMN_NAME_LIST, newName);
        db.update(ListTableContract.TABLE_NAME, newValues, "" + ListTableContract.COLUMN_NAME_LIST_ID + " = " + list.getLid(), null);
        /*Update the day on the listview */
        list.setName(newName);
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    /* Todo delete the items in the list from other table */
    private void deleteFromDatabase(List list){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        int lid = list.getLid();
        /* Delete from db where did */
        db.delete(ListTableContract.TABLE_NAME, "" + ListTableContract.COLUMN_NAME_LIST_ID + " = " + list.getLid(), null);
        //db.delete("Lifts", "did = " + did, null);

        lists.remove(list);
        listsAdapter.notifyDataSetChanged();
        db.close();
    }

    private int getLastLid(){
        return 0;
    }
}
