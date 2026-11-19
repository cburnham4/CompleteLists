package letshangllc.completelists.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import letshangllc.completelists.R;

public class Dialog_EditItem extends DialogFragment {

    private String name;

    public interface EditItemListener {
        void onDialogPositiveClick(DialogFragment dialog, String newName);
    }

    public void setCallback(EditItemListener mListener) {
        this.mListener = mListener;
    }

    private EditItemListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edititem, null);

        final EditText et_item_name = view.findViewById(R.id.et_item_name);
        et_item_name.setText(this.name);

        builder.setView(view)
                .setPositiveButton("FINISH", (dialog, id) -> {
                    String newName = et_item_name.getText().toString();
                    if (mListener != null) {
                        mListener.onDialogPositiveClick(Dialog_EditItem.this, newName);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    if (getDialog() != null) {
                        getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void setName(String name) {
        this.name = name;
    }
}
