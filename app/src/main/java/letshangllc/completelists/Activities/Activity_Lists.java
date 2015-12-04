package letshangllc.completelists.Activities;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import letshangllc.completelists.AdsHelper;
import letshangllc.completelists.Database.ListTableContract;
import letshangllc.completelists.Database.ListsCRUD;
import letshangllc.completelists.Dialogs.Dialog_AddItem;
import letshangllc.completelists.Dialogs.Dialog_EditItem;
import letshangllc.completelists.ListAdapters.ListsAdapter;
import letshangllc.completelists.Models.List;
import letshangllc.completelists.R;

public class Activity_Lists extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<List> lists;
    private ListsAdapter listsAdapter;
    private ListsCRUD listsCRUD;
    private AdsHelper adsHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.lv_lists);

        lists = new ArrayList<>();

        listsAdapter = new ListsAdapter(this, lists);

        listsCRUD = new ListsCRUD(this, lists,listsAdapter);
        listsCRUD.readLists();
        listView.setAdapter(listsAdapter);

        listView.setOnItemClickListener(new ListViewOnClick());

        registerForContextMenu(listView);

        this.setTitle("All Lists");
        adsHelper = new AdsHelper(getWindow().getDecorView(), getResources().getString(R.string.admob_lists_id), this);

        adsHelper.setUpAds();
        int delay = 1000; // delay for 1 sec.
        int period = getResources().getInteger(R.integer.ad_refresh_rate);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                adsHelper.refreshAd();  // display the data
            }
        }, delay, period);
    }

    /**
     * Set up the context menu
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_lv_lists, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        List list = listsAdapter.getItem(info.position);
        switch(item.getItemId()){
            case R.id.edit:
                displayDialog(list);
                Toast.makeText(this, "Edit : " + list.getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "Deleted: " + list.getName(), Toast.LENGTH_SHORT).show();
                listsCRUD.deleteFromDatabase(list);
                break;
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_lists, menu);
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
                dialog_addItem.setHint("Ex. Groceries");
                dialog_addItem.setCallback(new Dialog_AddItem.AddItemListener() {
                    @Override
                    public void onDialogPositiveClick(String newName) {
                        if(!newName.isEmpty() && !newName.equals("")){
                            listsCRUD.insertIntoDB(newName);
                        }
                    }
                });
                dialog_addItem.show(getSupportFragmentManager(), "dialog");
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ListViewOnClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(Activity_Lists.this, Activity_ListItems.class);
            List list = listsAdapter.getItem(position);
            int lid = list.getLid();
            String name = list.getName();
            intent.putExtra(ListTableContract.COLUMN_LIST_ID, lid);
            intent.putExtra(ListTableContract.COLUMN_LIST_NAME, name);
            startActivity(intent);
        }
    }

    private void displayDialog(List list){
        Dialog_EditItem dialog_editItem = new Dialog_EditItem();
        final List list1 = list;
        /* Set the name for the dialog to use */
        dialog_editItem.setName(list1.getName());
        /* Set the callback for when the user presses Finish */
        dialog_editItem.setCallback(new Dialog_EditItem.EditItemListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, String newName) {
                listsCRUD.updateDB(list1, newName);
            }
        });
        dialog_editItem.show(this.getSupportFragmentManager(), "Edit_Day");
    }



}
