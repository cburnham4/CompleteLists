package letshangllc.completelists.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import letshangllc.completelists.Activity_ListItems;
import letshangllc.completelists.Models.List;
import letshangllc.completelists.R;

/**
 * Created by cvburnha on 11/26/2015.
 */
public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {
    public ArrayList<List> lists;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ListsAdapter(ArrayList<List> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ListsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, new ViewHolder.OnClickCallback() {
            @Override
            public void callback(View v, int position) {
                Intent intent = new Intent(context, Activity_ListItems.class);
                intent.putExtra("lid", lists.get(position).getLid());
                context.startActivity(intent);
                Log.i("CLICKed ", "Clicked    ");
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        final List list = lists.get(position);


        viewHolder.tv_list.setText(list.getName());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return  lists.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_list;
        public OnClickCallback onClickCallback;

        public ViewHolder(View view, OnClickCallback onClickCallback) {
            super(view);
            tv_list = (TextView) view.findViewById(R.id.tv_list);
            this.onClickCallback = onClickCallback;
            view.setOnClickListener(this);
            //bx_complete_task = (CheckBox) view.findViewById(R.id.bx_complete_task);


        }

        @Override
        public void onClick(View v) {
            onClickCallback.callback(v, this.getLayoutPosition());
        }

        public static interface OnClickCallback{
            public void callback(View v, int position);
        }
    }

}
