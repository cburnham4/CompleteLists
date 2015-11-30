package letshangllc.completelists.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import letshangllc.completelists.Database.ListTableContract;
import letshangllc.completelists.R;

public class Activity_ListItems extends AppCompatActivity {
    private Toolbar toolbar;
    private int lid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent recievedIntent = getIntent();
        lid = recievedIntent.getIntExtra(ListTableContract.COLUMN_LIST_ID, 0);
        String name = recievedIntent.getStringExtra(ListTableContract.COLUMN_LIST_NAME);
        this.setTitle(name);
    }
}
