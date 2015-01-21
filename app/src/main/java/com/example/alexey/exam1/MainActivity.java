package com.example.alexey.exam1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TextView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        if (!provider.isTableExists(provider.db,provider.TABLE_NAME)) {
            provider.add_table(provider.TABLE_NAME);
        }
        TabHost tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tasks");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Cat");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Lab");
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        showData();

    }

    public void showData()
    {
        ListView lv=(ListView)findViewById(R.id.listView3);
        Cursor cursor=getContentResolver().query(provider.CONTENT_URI,null,provider.NCAT + " = 1 ",null,"main");
        cursor.moveToFirst();
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor,
                new String[]{provider.NAME},
                new int[]{android.R.id.text1});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //  TextView textView = (TextView) view.findViewById(android.R.id.text1);
                //  String str=textView.getText().toString();
                //  go_2();
                //work with click
            }
        });
        lv=(ListView)findViewById(R.id.listView2);
        Cursor cursor1=getContentResolver().query(provider.CONTENT_URI,null,provider.NCAT + " = 0 ",null,"main");
        cursor1.moveToFirst();
        SimpleCursorAdapter adapter1=new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor1,
                new String[]{provider.NAME},
                new int[]{android.R.id.text1});
        lv.setAdapter(adapter1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        lv=(ListView)findViewById(R.id.listView);
        Cursor cursor3=getContentResolver().query(provider.CONTENT_URI,null,provider.NCAT + " = 2 ",null,"main");
        cursor.moveToFirst();
        SimpleCursorAdapter adapter3=new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor3,
                new String[]{provider.NAME},
                new int[]{android.R.id.text1});
        lv.setAdapter(adapter3);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String str=textView.getText().toString();
                Intent intent = new Intent(MainActivity.this,data.class);
                intent.putExtra("1",str);
                startActivity(intent);
            }
        });

    }
    public void add_cat(View view) {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Enter name of category!");
        final EditText input = new EditText(MainActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(value.equals("")) return;

                ContentValues cv = new ContentValues();
                cv.put(provider.NCAT, 0);
                cv.put(provider.NAME, value);
                getContentResolver().insert(provider.CONTENT_URI, cv);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }


    public void add_lab(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Enter name of label!");
        final EditText input = new EditText(MainActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(value.equals("")) return;

                ContentValues cv = new ContentValues();
                cv.put(provider.NCAT, 1);
                cv.put(provider.NAME, value);
                getContentResolver().insert(provider.CONTENT_URI, cv);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }



    public void go(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Enter task name!");
        final EditText input = new EditText(MainActivity.this);
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                value=value.replaceAll("-", "_").replaceAll(" ","_");
                if(value.equals("")) return;
                go_1(value);
                //dd db
                //reload data
                //0 cat
                //1 lab
                //2 task
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }
    AlertDialog.Builder alert_1;
    public void go_1(final String name){
        alert_1 = new AlertDialog.Builder(MainActivity.this);
        alert_1.setTitle("Choose category!");
        final ListView input = new ListView(MainActivity.this);
        Cursor cursor1=getContentResolver().query(provider.CONTENT_URI,null,provider.NCAT + " = 0 ",null,"main");
        cursor1.moveToFirst();
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor1,
                new String[]{provider.NAME},
                new int[]{android.R.id.text1});
        input.setAdapter(adapter);
        input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String str=textView.getText().toString();
                go_2(name,str);
            }
        });
        alert_1.setView(input);

        alert_1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert_1.show();
    }
    String labels;
    AlertDialog.Builder alert_2;
    public void go_2(final String name, final String name1)
    {



        alert_2 = new AlertDialog.Builder(MainActivity.this);
        alert_2.setTitle("Choose labels!");
        labels="";
        final ListView input = new ListView(MainActivity.this);
        Cursor cursor1=getContentResolver().query(provider.CONTENT_URI,null,provider.NCAT + " = 1 ",null,"main");
        cursor1.moveToFirst();
        SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cursor1,
                new String[]{provider.NAME},
                new int[]{android.R.id.text1});
        input.setAdapter(adapter);
        input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String str=textView.getText().toString();
               if (!labels.contains(str)){
                   labels+=" ";
                   labels+=str;
               }
            }
        });
        alert_2.setView(input);


        alert_2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                ContentValues cv = new ContentValues();
                cv.put(provider.NCAT, 2);
                cv.put(provider.NAME, name);
                cv.put(provider.LABEL,labels);
                cv.put(provider.NUMCAT,name1);
                getContentResolver().insert(provider.CONTENT_URI, cv);


            }
        });

        alert_2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert_2.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
