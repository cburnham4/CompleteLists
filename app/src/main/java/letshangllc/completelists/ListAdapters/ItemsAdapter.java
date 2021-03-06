package letshangllc.completelists.ListAdapters;

import android.content.Context;

import android.graphics.Paint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import letshangllc.completelists.Models.Item;
import letshangllc.completelists.Models.List;
import letshangllc.completelists.R;

/**
 * Created by cvburnha on 11/30/2015.
 */
 /* todo change to work with swipe to delete*/

    /*
    todo add checkbox listener
     */
public class ItemsAdapter extends ArrayAdapter<Item> {
    public ArrayList<Item> items;
    private Context context;
    private ArrayList<Item> completedItems;
    private int colorID;

    public static class ViewHolder {
        TextView tv_list;
        CheckBox bx_complete;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemsAdapter(Context context, ArrayList<Item> items, ArrayList<Item> completedItems) {
        super(context, R.layout.item_list, items);
        this.completedItems = completedItems;
        this.context = context;
        colorID = context.getResources().getColor(R.color.primaryAccent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_listitem, parent, false);
            viewHolder.tv_list = (TextView) convertView.findViewById(R.id.tv_list);
            viewHolder.bx_complete = (CheckBox) convertView.findViewById(R.id.bx_complete);

            viewHolder.bx_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*todo cross out text view */
                    Toast.makeText(context, item.getItemName(), Toast.LENGTH_LONG).show();
                    if(viewHolder.bx_complete.isChecked()){
                        completedItems.add(item);
                    }else{
                        if(completedItems.contains(items)){
                            completedItems.remove(items);
                        }
                    }

                }
            });
            convertView.setTag(viewHolder);
            viewHolder.bx_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;

                    Item item = (Item) cb.getTag(R.string.TAG_ITEM);
                    TextView tv = (TextView) cb.getTag(R.string.TAG_TEXTVIEW);
                    item.setIsSelected(cb.isChecked());
                    if(item.isSelected()){
                        completedItems.add(item);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }else{
                        completedItems.remove(item);
                        tv.setPaintFlags(0);
                    }
                }
            });


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tv_list.setText(item.getItemName());
        viewHolder.tv_list.setPaintFlags(0);
        viewHolder.bx_complete.setChecked(item.isSelected());
        viewHolder.bx_complete.setTag(R.string.TAG_ITEM, item);
        viewHolder.bx_complete.setTag(R.string.TAG_TEXTVIEW, viewHolder.tv_list);

        // Return the completed view to render on screen
        return convertView;
    }
}