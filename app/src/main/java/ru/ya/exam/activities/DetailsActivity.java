package ru.ya.exam.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import ru.ya.exam.R;
import ru.ya.exam.database.MContentProvider;
import ru.ya.exam.database.MSQLiteHelper;

public class DetailsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks <Cursor> {
    Integer itemId;
    ListView listView;
    TextView titleView;
    TextView descriptionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        itemId = intent.getIntExtra(MainActivity.ITEM_ID, -1);
        if (itemId == -1) throw new Error();

        titleView = (TextView)findViewById(R.id.title);
        descriptionView = (TextView)findViewById(R.id.description);
        listView = (ListView)findViewById(R.id.detail_list);

        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(DetailsActivity.this, EditActivity.class);
            intent.putExtra(MainActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MContentProvider.ITEM_URI, null, MSQLiteHelper.COLUMN_ID + "=?",
                new String[]{itemId.toString()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 1)  throw new Error();
        titleView.setText(cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_TITLE)));
        descriptionView.setText(cursor.getString(cursor.getColumnIndex(MSQLiteHelper.COLUMN_DESCRIPTION)));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
