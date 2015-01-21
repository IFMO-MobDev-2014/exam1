package ru.ifmo.mobdev.exam1;

/**
 * @author sugakandrey
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.ifmo.mobdev.exam1.db.TasksDatabase;

public class AddTaskDialog extends DialogFragment {
    private ArrayList<String> labels;
    private boolean flag = false;

    private OnCompleteListener onCompleteListener;
    private boolean flag2 = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onCompleteListener = (OnCompleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        boolean edit = getArguments().getBoolean(MainActivity.EDIT);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View modifiedView = inflater.inflate(R.layout.add_task_dialog, null);
        String name = getArguments().getString(TasksDatabase.NAME);
        String description = getArguments().getString(TasksDatabase.DESCRIPTION);
        final String[] labels2 = getArguments().getStringArray(MainActivity.LABLES);
        final EditText taskName = (EditText) modifiedView.findViewById(R.id.add_name);
        final EditText taskDescription = (EditText) modifiedView.findViewById(R.id.add_description);
        if (labels2 != null) {
            TextView currentLabels = (TextView) modifiedView.findViewById(R.id.current_labels);
            currentLabels.setText("Categories: " + MainActivity.serialize(labels2));
        }
        if (name != null) {
            taskName.setText(name);
        }
        if (description != null) {
            taskDescription.setText(description);
        }
        final Spinner labler = (Spinner) modifiedView.findViewById(R.id.labler);

        labels = getArguments().getStringArrayList(MainActivity.LABLES_LIST);
        if (labels != null) {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, labels);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            labler.setAdapter(dataAdapter);
        }
        final Spinner remover = (Spinner) modifiedView.findViewById(R.id.remover);
        if (labels2 != null) {
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, labels2);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            remover.setAdapter(dataAdapter2);
        }

        labler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag) {
                    onCompleteListener.onLabelSelected(labels.get(position));
                }
                flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        remover.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (flag2) {
                    String lab = MainActivity.serialize(labels2);
                    String s = labels.get(position);
                    lab = lab.replace(" , " + s, " ");
                    onCompleteListener.onLabelsRemoved(lab);
                }
                flag2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onCompleteListener.onLabelsRemoved(MainActivity.serialize(labels2));
            }
        });
        if (!edit)
            builder.setView(modifiedView).setTitle("Add new task");

        builder.setView(modifiedView)
                .setTitle("Change task")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = taskName.getText().toString();
                        String desc = taskDescription.getText().toString();
                        if (name.trim().equals("")) {
                            Toast.makeText(getActivity().getBaseContext(), "", Toast.LENGTH_SHORT).show();
                        } else if (desc.trim().equals("")) {
                            Toast.makeText(getActivity().getBaseContext(), "Please enter feed URL", Toast.LENGTH_SHORT).show();
                        } else {
                            onCompleteListener.onComplete(name, desc, null);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }

                );
        return builder.create();
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(String name, String description, String[] labels);

        public abstract void onLabelSelected(String s);

        public abstract void onLabelsRemoved(String s);
    }
}