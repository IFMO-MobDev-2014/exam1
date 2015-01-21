package ru.ifmo.md.exam1;


import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class DetailsScreen extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        final CheckBox checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        final CheckBox checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
