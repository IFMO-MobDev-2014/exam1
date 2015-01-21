package year2013.ifmo.exam1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

/**
 * Created by Юлия on 21.01.2015.
 */
public class AddDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String title, String desc);
    }

    NoticeDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.add_dialog, null))
                .setTitle(R.string.dialog_title2)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText titleEdit = (EditText) AddDialogFragment.this.getDialog().findViewById(R.id.add_title);
                        //EditText labEdit = (EditText) AddDialogFragment.this.getDialog().findViewById(R.id.add_labels);
                        EditText descEdit = (EditText) AddDialogFragment.this.getDialog().findViewById(R.id.add_desc);
                        String title = titleEdit.getText().toString();
                        String desc = descEdit.getText().toString();
                        mListener.onDialogPositiveClick(title, desc);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

