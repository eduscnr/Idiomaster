package com.example.idiomaster.repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.idiomaster.database.DatabaseSqlite;
import com.example.idiomaster.modelo.Usuario;

public class DaoImplement implements IDao{
    private DatabaseSqlite dbHelper;
    private SQLiteDatabase db;
    public DaoImplement(Context context) {
        dbHelper = new DatabaseSqlite(context);
    }
    @Override
    public boolean registrarUsuario(String emailUsuario) {
        db = dbHelper.getReadableDatabase();
        ContentValues  valoresNuevoUsuario = new ContentValues();
        valoresNuevoUsuario.put("email", emailUsuario);
        long idUsuario = db.insert("Usuario", null, valoresNuevoUsuario);
        String idiomas[] = {"es", "it", "en"};
        for (String idioma : idiomas){
            ContentValues voloresProgresionUsuario = new ContentValues();
            voloresProgresionUsuario.put("usuario_id", idUsuario);
            voloresProgresionUsuario.put("progreso_mundo", 1);
            voloresProgresionUsuario.put("progreso_nivel", 1);
            voloresProgresionUsuario.put("idioma", idioma);
            db.insert("ProgresoUsuario", null, voloresProgresionUsuario);
        }
        return true;
    }

    @Override
    public Usuario iniciarSesionUsuario(String emailUsuario) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ProgresoUsuario WHERE usuario_id = ? AND idioma = ?",
                new String[]{String.valueOf(obtenerIdUsuario(db, emailUsuario)), "es"});
        if (cursor.moveToFirst()) {
            System.out.println("paso por aquí");
            return new Usuario(
                    emailUsuario,
                    cursor.getInt(cursor.getColumnIndex("progreso_mundo")),
                    cursor.getInt(cursor.getColumnIndex("progreso_nivel")),
                    cursor.getString(cursor.getColumnIndex("idioma"))
            );
        }
        return null;
    }
    private long obtenerIdUsuario(SQLiteDatabase db, String emailUsuario) {
        Cursor cursor = db.rawQuery("SELECT id FROM Usuario WHERE email = ?", new String[]{emailUsuario});
        long idUsuario = -1;

        if (cursor.moveToFirst()) {
            idUsuario = cursor.getLong(cursor.getColumnIndex("id"));
        }

        cursor.close();
        return idUsuario;
    }


    @Override
    public Usuario cambiarIdioma(String emailUsuario, String idiomaCambiar) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ProgresoUsuario WHERE usuario_id = ? AND idioma = ?",
                new String[]{String.valueOf(obtenerIdUsuario(db, emailUsuario)), idiomaCambiar});
        if (cursor.moveToFirst()) {
            System.out.println("paso por aquí");
            return new Usuario(
                    emailUsuario,
                    cursor.getInt(cursor.getColumnIndex("progreso_mundo")),
                    cursor.getInt(cursor.getColumnIndex("progreso_nivel")),
                    cursor.getString(cursor.getColumnIndex("idioma"))
            );
        }
        return null;
    }
}
