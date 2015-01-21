package year2013.ifmo.exam1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ViewActivity extends ActionBarActivity {

    public static final String EXTRA_TITLE_NAME = "title";
    public static final String EXTRA_LABELS_NAME = "labels";
    public static final String EXTRA_DESCRIPTION = "description";

    public static String TITLE;
    public static String LABELS;
    public static String DESCRIPTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        TITLE = getIntent().getStringExtra(EXTRA_TITLE_NAME);
        LABELS = getIntent().getStringExtra(EXTRA_LABELS_NAME);
        DESCRIPTION = getIntent().getStringExtra(EXTRA_DESCRIPTION);

        TextView title = (TextView) findViewById(R.id.title_text);
        title.setText(TITLE);
        /*TextView labels = (TextView) findViewById(R.id.labels_text);
        labels.setText(LABELS);*/
        TextView desc = (TextView) findViewById(R.id.desc_text);
        desc.setText(DESCRIPTION);

    }



}
