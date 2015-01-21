package year2013.ifmo.exam1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class LabelsActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labels);

        String from[] = {Item.Label.TITLE_NAME, Item.Label.NUMBER_NAME};
        int to[] = {android.R.id.text1, android.R.id.text2};
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null, from, to);

        listView = (ListView) findViewById(R.id.label_list);
        listView.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

    }

    static final String[] SUMMARY_PROJECTION = new String[]{
            Item.Label._ID,
            Item.Label.TITLE_NAME,
            Item.Label.NUMBER_NAME
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Item.Label.CONTENT_URI;

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
