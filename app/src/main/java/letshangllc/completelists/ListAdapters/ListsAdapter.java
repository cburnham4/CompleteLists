package letshangllc.completelists.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import letshangllc.completelists.Models.List;
import letshangllc.completelists.R;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class ListsAdapter extends ArrayAdapter<List> {
    public ArrayList<List> lists;
    private Context context;

    public static class ViewHolder{
        TextView tv_list;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListsAdapter(Context context, ArrayList<List> lists) {
        super(context, R.layout.item_list, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        List list  = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list, parent, false);
            viewHolder.tv_list = (TextView) convertView.findViewById(R.id.tv_list);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tv_list.setText(list.getName());
        // Return the completed view to render on screen
        return convertView;
    }


}
