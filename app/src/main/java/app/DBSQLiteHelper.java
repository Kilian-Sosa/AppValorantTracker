package app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    String sqlCreateUse = "CREATE TABLE Users(email Varchar(60), password Varchar(30), nick varchar(60), friends varchar(500))";
    String sqlCreateRec = "CREATE TABLE Records(ID INTEGER primary key AUTOINCREMENT, user Varchar(60), idM int, map Varchar(30), idC int, character Varchar(30), " +
            "win int, score Varchar(30), performance Varchar(30), date Varchar(30), notes Varchar(400), shared int default 0)";

    public DBSQLiteHelper(Context contexto, String nombre,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateUse);
        db.execSQL(sqlCreateRec);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Records");
        db.execSQL(sqlCreateUse);
        db.execSQL(sqlCreateRec);
    }
}