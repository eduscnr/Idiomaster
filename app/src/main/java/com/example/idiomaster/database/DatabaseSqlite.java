package com.example.idiomaster.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSqlite  extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DE_DATOS = "DatosUsuarios.db";
    private static final int VERSION_BASE_DE_DATOS = 1;
    public DatabaseSqlite(Context context) {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Usuario "+ "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT UNIQUE"+ ")");
        sqLiteDatabase.execSQL("CREATE TABLE ProgresoUsuario(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario_id INTEGER, " +
                "progreso_mundo INTEGER," +
                "progreso_nivel INTEGER, " +
                "idioma TEXT, " +
                " FOREIGN KEY (usuario_id) REFERENCES Usuarios(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Usuario");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ProgresoUsuario");
        onCreate(sqLiteDatabase);
    }
}
