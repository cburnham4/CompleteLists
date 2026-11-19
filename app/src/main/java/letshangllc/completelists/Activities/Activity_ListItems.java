package letshangllc.completelists.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

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
    private Timer adRefreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_list_items);
        applyEdgeToEdgeInsets();

        setUpToolbar();

        Intent receivedIntent = getIntent();
        lid = receivedIntent.getIntExtra(ListTableContract.COLUMN_LIST_ID, 0);
        String name = receivedIntent.getStringExtra(ListTableContract.COLUMN_LIST_NAME);
        setTitle(name);

        items = new ArrayList<>();
        completedItems = new ArrayList<>();
        itemsAdapter = new ItemsAdapter(this, items, completedItems);

        itemsCRUD = new ItemsCRUD(this, items, itemsAdapter, lid);

        ListView listView = findViewById(R.id.lv_items);
        listView.setAdapter(itemsAdapter);

        itemsCRUD.readLists();
        registerForContextMenu(listView);

        adsHelper = new AdsHelper(getWindow().getDecorView(),
                getResources().getString(R.string.admob_items_id), this);
        adsHelper.setUpAds();
        scheduleAdRefresh();
    }

    private void applyEdgeToEdgeInsets() {
        final View root = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void scheduleAdRefresh() {
        int delay = 1000;
        int period = getResources().getInteger(R.integer.ad_refresh_rate);
        adRefreshTimer = new Timer();
        adRefreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (adsHelper != null) {
                    adsHelper.refreshAd();
                }
            }
        }, delay, period);
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(Activity_ListItems.this, Activity_Lists.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_lv_lists, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null) {
            return super.onContextItemSelected(item);
        }
        Item listItem = itemsAdapter.getItem(info.position);
        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            displayDialog(listItem);
            Toast.makeText(this, "Edit : " + listItem.getItemName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.delete) {
            Toast.makeText(this, "Deleted: " + listItem.getItemName(), Toast.LENGTH_SHORT).show();
            itemsCRUD.deleteFromDatabase(listItem);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Dialog_AddItem dialog_addItem = new Dialog_AddItem();
            dialog_addItem.setHint("Ex. Bread");
            dialog_addItem.setCallback(newName -> {
                if (newName != null && !newName.trim().isEmpty()) {
                    itemsCRUD.insertItemIntoDB(newName.trim());
                }
            });
            dialog_addItem.show(getSupportFragmentManager(), "dialog");
            return true;
        } else if (id == R.id.action_complete) {
            for (Item listItem : new ArrayList<>(completedItems)) {
                items.remove(listItem);
                itemsAdapter.remove(listItem);
                itemsCRUD.deleteFromDatabase(listItem);
            }
            completedItems.clear();
            itemsAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDialog(Item item) {
        Dialog_EditItem dialog_editItem = new Dialog_EditItem();
        final Item item1 = item;
        dialog_editItem.setName(item1.getItemName());
        dialog_editItem.setCallback((DialogFragment dialog, String newName) ->
                itemsCRUD.updateName(item1, newName));
        dialog_editItem.show(getSupportFragmentManager(), "Edit_Item");
    }

    @Override
    protected void onPause() {
        if (adsHelper != null) {
            adsHelper.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adsHelper != null) {
            adsHelper.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adRefreshTimer != null) {
            adRefreshTimer.cancel();
            adRefreshTimer = null;
        }
        if (adsHelper != null) {
            adsHelper.onDestroy();
        }
        super.onDestroy();
    }
}
