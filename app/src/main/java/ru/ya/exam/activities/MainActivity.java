package ru.ya.exam.activities;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

import ru.ya.exam.R;
import ru.ya.exam.database.MContentProvider;
import ru.ya.exam.database.MSQLiteHelper;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks < Cursor > {
    public static final String ITEM_ID = "ITEM_ID";
    public static final String [] LABELS = {"Matan", "Android", "Diff equations"};

    ListView listView;
    SimpleCursorAdapter adapter;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("----------------------------------------------", "--");
        setContentView(R.layout.activity_main);
        contentResolver = getContentResolver();

        listView = (ListView)findViewById(R.id.listView);
        String [] from = {MSQLiteHelper.COLUMN_TITLE, MSQLiteHelper.COLUMN_DATE, MSQLiteHelper.COLUMN_ID};
        int [] to = {R.id.item_title, R.id.item_date, R.id.item_id};
        adapter = new SimpleCursorAdapter(this, R.layout.item, null, from, to, 0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                String itemId = ((TextView)view.findViewById(R.id.item_id)).getText().toString();
                intent.putExtra(ITEM_ID, Integer.valueOf(itemId));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String itemId = ((TextView)findViewById(R.id.item_id)).getText().toString();
                contentResolver.delete(MContentProvider.ITEM_URI, MSQLiteHelper.COLUMN_ID + "=?", new String[]{itemId});
                return false;
            }
        });


        getLoaderManager().initLoader(0, null, this);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_item) {
            ContentValues values = new ContentValues();
            values.put(MSQLiteHelper.COLUMN_TITLE, "empty");
            Uri uri = contentResolver.insert(MContentProvider.ITEM_URI, values);
            List< String > list = uri.getPathSegments();
            Integer idNumber = Integer.valueOf(list.get(list.size() - 1));
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            intent.putExtra(ITEM_ID, idNumber);
            startActivity(intent);
        }
        if (id == R.id.edit_label_activity) {
            Intent intent = new Intent(MainActivity.this, LabelActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MContentProvider.ITEM_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


//
//new AsyncTask<String, Long, Integer>() {
//
//           @Override
//           protected Integer doInBackground(String[] params) {
//               return null;
//           }
//
//           @Override
//           protected void onProgressUpdate(Void... values) {
//               super.onProgressUpdate(values);
//           }
//
//           @Override
//           protected void onPostExecute(Integer aVoid) {
//               super.onPostExecute(aVoid);
//           }
//       }.execute("asdfasdf");

