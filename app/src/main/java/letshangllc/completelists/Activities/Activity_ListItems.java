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
import letshangllc.completelists.Database.ItemsCRUD;
import letshangllc.completelists.Database.ListTableContract;
import letshangllc.completelists.Dialogs.Dialog_AddItem;
import letshangllc.completelists.Dialogs.Dialog_EditItem;
import letshangllc.completelists.ListAdapters.ItemsAdapter;
import letshangllc.completelists.Models.Item;
import letshangllc.completelists.R;

public class Activity_ListItems extends AppCompatActivity {
    private Toolbar toolbar;
    private int lid;
    private ItemsCRUD itemsCRUD;
    private ArrayList<Item> items;
    private ArrayList<Item> completedItems;
    private ItemsAdapter itemsAdapter;

    private AdsHelper adsHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        this.setUpToolbar();

        Intent recievedIntent = getIntent();
        lid = recievedIntent.getIntExtra(ListTableContract.COLUMN_LIST_ID, 0);
        String name = recievedIntent.getStringExtra(ListTableContract.COLUMN_LIST_NAME);
        this.setTitle(name);

        items = new ArrayList<>();
        completedItems = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, items, completedItems);

        itemsCRUD = new ItemsCRUD(this, items, itemsAdapter, lid);

        ListView listView = (ListView) findViewById(R.id.lv_items);
        listView.setAdapter(itemsAdapter);

        itemsCRUD.readLists();

        registerForContextMenu(listView);

        adsHelper = new AdsHelper(getWindow().getDecorView(), getResources().getString(R.string.admob_items_id), this);

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

    /* todo swipe to delete */
    private void setUpToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_ListItems.this, Activity_Lists.class);
                startActivity(intent);
                finish();
            }
        });
        //toolbar.setLogo(R.drawable.icon114);
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
        Item listItem = itemsAdapter.getItem(info.position);
        switch(item.getItemId()){
            case R.id.edit:
                displayDialog(listItem);
                Toast.makeText(this, "Edit : " + listItem.getItemName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this, "Deleted: " + listItem.getItemName(), Toast.LENGTH_SHORT).show();
                itemsCRUD.deleteFromDatabase(listItem);
                break;
        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_items, menu);
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
                dialog_addItem.setHint("Ex. Bread");
                dialog_addItem.setCallback(new Dialog_AddItem.AddItemListener() {
                    @Override
                    public void onDialogPositiveClick(String newName) {
                        if(!newName.isEmpty() && !newName.equals("")){
                            itemsCRUD.insertItemIntoDB(newName);
                        }
                    }
                });
                dialog_addItem.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.action_complete:
                for(Item listItem : completedItems){
                    items.remove(listItem);
                    itemsAdapter.remove(listItem);
                    itemsCRUD.deleteFromDatabase(listItem);
                }
                completedItems.clear();
                itemsAdapter.notifyDataSetChanged();


                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void displayDialog(Item item){
        Dialog_EditItem dialog_editItem = new Dialog_EditItem();
        final Item item1 = item;
        /* Set the name for the dialog to use */
        dialog_editItem.setName(item1.getItemName());
        /* Set the callback for when the user presses Finish */
        dialog_editItem.setCallback(new Dialog_EditItem.EditItemListener() {
            @Override
            public void onDialogPositiveClick(DialogFragment dialog, String newName) {
                itemsCRUD.updateName(item1, newName);
            }
        });
        dialog_editItem.show(this.getSupportFragmentManager(), "Edit_Item");
    }
}
