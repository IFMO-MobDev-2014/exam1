package ru.ifmo.md.exam1;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.Calendar;


public class MyDialog extends DialogFragment {
    public static final String ARG_EDIT = "edit";
    public static final String ARG_ID = "id";
    public static final String ARG_NAME = "name";
    public static final String ARG_DATE = "date";
    public static final String ARG_DESC = "desc";
    public static final String ARG_CATEGORY = "category";

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final boolean edit = getArguments().getBoolean(ARG_EDIT, false);
        if (!edit)
            builder.setTitle("Add new item");
        else
            builder.setTitle("Edit item");
        final View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.my_dialog, null);
        builder.setView(view);
        final CheckBox checkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
        final CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
        if (edit) {
            ((EditText) view.findViewById(R.id.editTextName)).setText(getArguments().getString(ARG_NAME));
            ((EditText) view.findViewById(R.id.editTextDesc)).setText(getArguments().getString(ARG_DESC));
            String cat = getArguments().getString(ARG_CATEGORY);
            if (cat != null) {
                if (cat.contains("Other")) {
                    ((CheckBox) view.findViewById(R.id.checkBox1)).setChecked(true);
                }
                if (cat.contains("Study")) {
                    ((CheckBox) view.findViewById(R.id.checkBox2)).setChecked(true);
                }
                if (cat.contains("Work")) {
                    ((CheckBox) view.findViewById(R.id.checkBox3)).setChecked(true);
                }
            }

        }
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pos) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", ((EditText) view.findViewById(R.id.editTextName)).getText().toString());
                contentValues.put("date", java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
                contentValues.put("description", ((EditText) view.findViewById(R.id.editTextDesc)).getText().toString());
                String cat = "";
                if (checkBox1.isChecked()) {
                    cat += checkBox1.getText();
                }
                if (checkBox2.isChecked()) {
                    if (!cat.isEmpty())
                        cat += ", ";
                    cat += checkBox2.getText();
                }
                if (checkBox3.isChecked()) {
                    if (!cat.isEmpty())
                        cat += ", ";
                    cat += checkBox3.getText();
                }
                contentValues.put("category", cat);
                if (edit) {
                    contentValues.put("_id", getArguments().getLong(ARG_ID));
                    getActivity().getContentResolver().update(MyContentProvider.URI, contentValues, null, null);
                } else

                    getActivity().getContentResolver().insert(MyContentProvider.URI, contentValues);

                // getActivity().getLoaderManager().restartLoader(0, null, MainActivity.this);
                dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        return builder.create();
    }
}
