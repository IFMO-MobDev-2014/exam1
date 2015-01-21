package year2013.ifmo.exam1;

import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends ActionBarActivity implements AddDialogFragment.NoticeDialogListener,
        DeleteDialogFragment.NoticeDialogListener, LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;
    private ListView listView;
    private Cursor item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String from[] = {Item.Expression.TITLE_NAME, Item.Expression.DATE_NAME};
        int to[] = {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null, from, to);

        listView = (ListView) findViewById(R.id.expr_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(listener);
        listView.setOnItemLongClickListener(longListener);
        getLoaderManager().initLoader(0, null, this);
    }

    public void onAddButtonClick(View view) {
        DialogFragment dialogFragment = new AddDialogFragment();
        dialogFragment.show(getFragmentManager(), "task");
    }

    public void onMenuButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, LabelsActivity.class);
        startActivity(intent);
    }

    private AdapterView.OnItemLongClickListener longListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            item = ((Cursor) adapter.getItem(position));

            DialogFragment dialogFragment = new DeleteDialogFragment();
            dialogFragment.show(getFragmentManager(), "task");

            return true;
        }
    };

    @Override
    public void onDialogPositiveClick() {
        Uri uri = ContentUris.withAppendedId(Item.Expression.CONTENT_URI,
                item.getLong(Item.Expression.ID_COLUMN));
        getContentResolver().delete(uri, null, null);
    }

    @Override
    public void onDialogPositiveClick(String title, String desc) {
        ContentValues cv = new ContentValues();
        cv.put(Item.Expression.TITLE_NAME, title);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        cv.put(Item.Expression.DATE_NAME, df.format(new Date().getTime()));
        cv.put(Item.Expression.LABEL_NAME, "");
        cv.put(Item.Expression.DESCRIPTION_NAME, desc);
        getContentResolver().insert(Item.Expression.CONTENT_URI, cv);
    }

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor o = ((Cursor) adapter.getItem(position));
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra(ViewActivity.EXTRA_TITLE_NAME,
                    o.getString(Item.Expression.TITLE_COLUMN));
            intent.putExtra(ViewActivity.EXTRA_LABELS_NAME,
                    o.getString(Item.Expression.LABEL_COLUMN));
            intent.putExtra(ViewActivity.EXTRA_DESCRIPTION,
                    o.getString(Item.Expression.DESCRIPTION_COLUMN));
            startActivity(intent);
        }
    };

    static final String[] SUMMARY_PROJECTION = new String[]{
            Item.Expression._ID,
            Item.Expression.TITLE_NAME,
            Item.Expression.DATE_NAME,
            Item.Expression.LABEL_NAME,
            Item.Expression.DESCRIPTION_NAME
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Item.Expression.CONTENT_URI;

        return new CursorLoader(getBaseContext(), baseUri,
                SUMMARY_PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
