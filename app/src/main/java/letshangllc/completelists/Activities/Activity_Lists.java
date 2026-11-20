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
    private Timer adRefreshTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_lists);
        applyEdgeToEdgeInsets();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = findViewById(R.id.lv_lists);

        lists = new ArrayList<>();
        listsAdapter = new ListsAdapter(this, lists);

        listsCRUD = new ListsCRUD(this, lists, listsAdapter);
        listsCRUD.readLists();
        listView.setAdapter(listsAdapter);

        listView.setOnItemClickListener(new ListViewOnClick());
        registerForContextMenu(listView);

        setTitle(R.string.all_lists_title);
        adsHelper = new AdsHelper(getWindow().getDecorView(),
                getResources().getString(R.string.admob_lists_id), this);
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
        List list = listsAdapter.getItem(info.position);
        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            displayDialog(list);
            Toast.makeText(this, "Edit : " + list.getName(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.delete) {
            Toast.makeText(this, "Deleted: " + list.getName(), Toast.LENGTH_SHORT).show();
            listsCRUD.deleteFromDatabase(list);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Dialog_AddItem dialog_addItem = new Dialog_AddItem();
            dialog_addItem.setHint("Ex. Groceries");
            dialog_addItem.setCallback(newName -> {
                if (newName != null && !newName.trim().isEmpty()) {
                    listsCRUD.insertIntoDB(newName.trim());
                }
            });
            dialog_addItem.show(getSupportFragmentManager(), "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ListViewOnClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(Activity_Lists.this, Activity_ListItems.class);
            List list = listsAdapter.getItem(position);
            intent.putExtra(ListTableContract.COLUMN_LIST_ID, list.getLid());
            intent.putExtra(ListTableContract.COLUMN_LIST_NAME, list.getName());
            startActivity(intent);
        }
    }

    private void displayDialog(List list) {
        Dialog_EditItem dialog_editItem = new Dialog_EditItem();
        final List list1 = list;
        dialog_editItem.setName(list1.getName());
        dialog_editItem.setCallback((DialogFragment dialog, String newName) ->
                listsCRUD.updateDB(list1, newName));
        dialog_editItem.show(getSupportFragmentManager(), "Edit_Day");
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
