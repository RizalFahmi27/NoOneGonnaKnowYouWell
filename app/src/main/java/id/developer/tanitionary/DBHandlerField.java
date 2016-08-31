package id.developer.tanitionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rizal Fahmi on 31-Aug-16.
 */
public class DBHandlerField extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "field_data_database";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_TITLE = "title";
    private static final String TABLE_TITLE_iD = "title_id";
    private static final String TABLE_TITLE_NAME = "title_name";



    private static final String TABLE_CHILD = "child";
    private static final String TABLE_CHILD_ID = "child_id";
    private static final String TABLE_CHILD_DESCRIPTION = "child_desc";
    private static final String TABLE_CHILD_PARENT = "child_parent";
    private static final String

    public DBHandlerField(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, version);

        SQLiteDatabase db = this.getReadableDatabase();
        if(checkIfDBExist(db)==0){
            insertTitle(context);
            insertChild(context);
        }
    }

    private void insertChild(Context context) {
    }

    public DBHandlerField(Context context){
        super(context,DATABASE_NAME,null,);

    }

    private void insertTitle(Context context) {
    }

    private int checkIfDBExist(SQLiteDatabase db) {
        Cursor c =  db.rawQuery("SELECT * FROM "+TABLE_TITLE,null);
        int count = c.getCount();
        c.close();
        return count;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
