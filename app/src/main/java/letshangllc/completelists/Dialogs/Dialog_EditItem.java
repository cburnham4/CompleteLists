package letshangllc.completelists.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import letshangllc.completelists.R;

/**
 * Created by cvburnha on 11/29/2015.
 */
public class Dialog_EditItem extends DialogFragment {

    String name;

    public interface EditItemListener {
        public void onDialogPositiveClick(DialogFragment dialog, String newName);
    }


    public void setCallback(EditItemListener mListener) {
        this.mListener = mListener;
    }

    EditItemListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_edititem, null);

        final EditText et_item_name = (EditText) view.findViewById(R.id.et_item_name);
        et_item_name.setText(this.name);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton("FINISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newName = et_item_name.getText().toString();
                        mListener.onDialogPositiveClick(Dialog_EditItem.this, newName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog_EditItem.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setName(String name) {
        this.name = name;
    }
}
