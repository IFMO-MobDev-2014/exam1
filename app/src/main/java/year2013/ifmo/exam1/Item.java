package year2013.ifmo.exam1;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Юлия on 21.01.2015.
 */
public class Item {

    public static final String AUTHORITY =
            "year2013.ifmo.exam1.provider.item";

    public static class Expression implements BaseColumns{

        public static final int ID_COLUMN = 0;
        public static final int TITLE_COLUMN = 1;
        public static final int DATE_COLUMN = 2;
        public static final int LABEL_COLUMN = 3;
        public static final int DESCRIPTION_COLUMN = 4;

        private Expression() {}

        public static final String EXPRESSION_NAME = "expression";

        public static final Uri EXPRESSION_URI = Uri.parse("content://" +
                AUTHORITY + "/" + Expression.EXPRESSION_NAME);

        public static final Uri CONTENT_URI = EXPRESSION_URI;

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.expression.data";

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.expression.data";

        public static final String TITLE_NAME = "title";

        public static final String LABEL_NAME = "label";

        public static final String DESCRIPTION_NAME = "description";

        public static final String DATE_NAME = "date";

    }

    public  static class Label implements BaseColumns{

        public static final int ID_COLUMN = 0;
        public static final int TITLE_COLUMN = 1;
        public static final int NUMBER_COLUMN = 2;

        private Label() {}

        public static final String LABEL_NAME = "label";

        public static final Uri LABEL_URI = Uri.parse("content://" +
                AUTHORITY + "/" + Label.LABEL_NAME);

        public static final Uri CONTENT_URI = LABEL_URI;

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/vnd.label.data";

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/vnd.label.data";

        public static final String TITLE_NAME = "title";

        public static final String NUMBER_NAME = "number";

    }

}
