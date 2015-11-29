package letshangllc.completelists;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import letshangllc.completelists.Database.ListTableContract;

public class Activity_ListItems extends AppCompatActivity {
    private Toolbar toolbar;
    private int lid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

        Intent recievedIntent = getIntent();
        lid = recievedIntent.getIntExtra(ListTableContract.COLUMN_NAME_LIST_ID, 0);
        this.setTitle("title " + lid);
    }
}
