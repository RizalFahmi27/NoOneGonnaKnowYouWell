//package id.developer.tanitionary;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.DatabaseErrorHandler;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by Rizal Fahmi on 31-Aug-16.
// */
//public class DBHandlerKamus extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "databasekamus";
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String TABLE_RELATION = "relasi";
//    private static final String TABLE_RELATION_ID = "id_relasi";
//
//
//
//
//    private static final String TABLE_SYMTOMPS = "gejala";
//    private static final String TABLE_DISEASE = "penyakit";
//
//
//
//
//
//
//    public DBHandlerKamus(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        if(checkIfDBIsExist(db)==0){
//            insertFilm(context);
//            insertJadwal(context);
//        }
//        db.close();
//    }
//
//    int checkIfDBIsExist(SQLiteDatabase db){
//        Cursor c =  db.rawQuery("SELECT * FROM "+TABLE_FILM,null);
//        int count = c.getCount();
//        c.close();
//        return count;
//    }
//
//    public DBHandlerKamus(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//}
