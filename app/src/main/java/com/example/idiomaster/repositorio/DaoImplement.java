package com.example.idiomaster.repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.idiomaster.database.DatabaseSqlite;
import com.example.idiomaster.iniciar.IniciarSesion;
import com.example.idiomaster.modelo.ProgresoFireBase;
import com.example.idiomaster.modelo.Usuario;
import com.example.idiomaster.modelo.UsuarioFireBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoImplement implements IDao{
    private DatabaseSqlite dbHelper;
    private SQLiteDatabase db;
    public DaoImplement(Context context) {
        dbHelper = new DatabaseSqlite(context);
    }
/*    @Override
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
    }*/

/*    @Override
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
    }*/
    /*
    private long obtenerIdUsuario(SQLiteDatabase db, String emailUsuario) {
        Cursor cursor = db.rawQuery("SELECT id FROM Usuario WHERE email = ?", new String[]{emailUsuario});
        long idUsuario = -1;

        if (cursor.moveToFirst()) {
            idUsuario = cursor.getLong(cursor.getColumnIndex("id"));
        }

        cursor.close();
        return idUsuario;
    }
*/

/*    @Override
    public Usuario cambiarIdioma(String emailUsuario, String idiomaCambiar) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ProgresoUsuario WHERE usuario_id = ? AND idioma = ?",
                new String[]{String.valueOf(obtenerIdUsuario(db, emailUsuario)), idiomaCambiar});
        if (cursor.moveToFirst()) {
            return new Usuario(
                    emailUsuario,
                    cursor.getInt(cursor.getColumnIndex("progreso_mundo")),
                    cursor.getInt(cursor.getColumnIndex("progreso_nivel")),
                    cursor.getString(cursor.getColumnIndex("idioma"))
            );
        }
        return null;
    }*/

/*    @Override
    public boolean actualizarProgreso(String emailUsuario, String idioma, int progresoMundo, int progresoNivel) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long idUsuario = obtenerIdUsuario(db, emailUsuario);
        if(idUsuario == -1){
            return false;
        }
        ContentValues valores = new ContentValues();
        valores.put("progreso_mundo", progresoMundo);
        valores.put("progreso_nivel", progresoNivel);
        int filasActualizadas = db.update("ProgresoUsuario", valores, "usuario_id = ? and idioma = ?",
                new String[]{String.valueOf(idUsuario), idioma});
        return filasActualizadas>0;
    }*/

/*
    @Override
    public boolean buscarUsuario(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String select = "SELECT COUNT(*) FROM Usuario WHERE email = ?";
        String []paramentros = {email};
        Cursor cursor = db.rawQuery(select, paramentros);
        int usuarioExistente = 0;
        if(cursor != null){
            cursor.moveToFirst();
            usuarioExistente = cursor.getInt(0);
            cursor.close();
        }
        return usuarioExistente>0;
    }
*/

    //USO DE FIREBASES
    public void registrarNuevoUsuario(String email) throws JSONException {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        List<ProgresoFireBase> progreso = new ArrayList<>(Arrays.asList(
                new ProgresoFireBase("es", 1, 1),
                new ProgresoFireBase("en", 1, 1),
                new ProgresoFireBase("it", 1, 1)

        ));
        UsuarioFireBase nuevoObjeto = new UsuarioFireBase(email, progreso);
        mDatabase.child("usuarios").push().setValue(nuevoObjeto, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("RegistroUsuario", "Error al registrar el nuevo usuario: " + databaseError.getMessage());
                } else {
                    Log.d("RegistroUsuario", "Nuevo usuario registrado en Firebase");
                }
            }
        });
    }
    public void recuperarUsuario(String email) {
        // Crear una consulta para buscar el usuario por su correo electrónico
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("usuarios").orderByChild("email").equalTo(email);
        // Ejecutar la consulta y escuchar los resultados
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    // Obtener el email del usuario
                    String emailUsuario = usuarioSnapshot.child("email").getValue(String.class);
                    // Obtener el progreso en español del usuario
                    for (DataSnapshot progresoSnapshot : usuarioSnapshot.child("progreso").getChildren()) {
                        String idioma = progresoSnapshot.child("idioma").getValue(String.class);
                        if ("es".equals(idioma)) {
                            int progresoMundo = progresoSnapshot.child("progresoMundo").getValue(Integer.class);
                            int progresoNivel = progresoSnapshot.child("progresoNivel").getValue(Integer.class);
                            // Aquí puedes hacer lo que necesites con el progreso en español
                            Usuario usuario = new Usuario(email, progresoMundo, progresoNivel, idioma);
                            IniciarSesion.setInicioSesionUsuario(usuario);
                            break; // Salir del bucle una vez que se encuentre el progreso en español
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores si ocurrieron al recuperar los datos del usuario
                System.out.println("Error al recuperar el usuario: " + databaseError.getMessage());
            }
        });
    }
    public void actualizarProgresoFirebase(String email, String nuevoIdioma, int nuevoProgresoMundo, int nuevoProgresoNivel) {
        // Crear una consulta para buscar el usuario por su correo electrónico
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("usuarios").orderByChild("email").equalTo(email);

        // Ejecutar la consulta y escuchar los resultados
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    // Actualizar el progreso del usuario
                    DatabaseReference usuarioRef = usuarioSnapshot.getRef();
                    for (DataSnapshot progresoSnapshot : usuarioSnapshot.child("progreso").getChildren()) {
                        String idioma = progresoSnapshot.child("idioma").getValue(String.class);
                        if (idioma.equals(nuevoIdioma)) {
                            // Actualizar el progreso del mundo y nivel
                            progresoSnapshot.getRef().child("progresoMundo").setValue(nuevoProgresoMundo);
                            progresoSnapshot.getRef().child("progresoNivel").setValue(nuevoProgresoNivel);
                            // Actualizar el idioma
                            progresoSnapshot.getRef().child("idioma").setValue(nuevoIdioma);
                            // Puedes realizar otras actualizaciones si es necesario
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores si ocurrieron al recuperar los datos del usuario
                System.out.println("Error al actualizar el progreso: " + databaseError.getMessage());
            }
        });
    }
    public void cambiarIdiomaFirebase(String email, String nuevoIdioma) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("usuarios").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot usuarioSnapshot : dataSnapshot.getChildren()) {
                    // Iterar sobre los progresos del usuario
                    for (DataSnapshot progresoSnapshot : usuarioSnapshot.child("progreso").getChildren()) {
                        String idioma = progresoSnapshot.child("idioma").getValue(String.class);
                        if (idioma.equals(nuevoIdioma)) {
                            // Obtener el progreso asociado al nuevo idioma
                            int progresoMundo = progresoSnapshot.child("progresoMundo").getValue(Integer.class);
                            int progresoNivel = progresoSnapshot.child("progresoNivel").getValue(Integer.class);
                            // Crear un objeto Usuario con el nuevo progreso
                            Usuario usuario = new Usuario(email, progresoMundo, progresoNivel, nuevoIdioma);
                            IniciarSesion.setInicioSesionUsuario(usuario);
                            // Puedes utilizar el objeto Usuario como desees
                            break; // Salir del método después de encontrar el progreso
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar errores si ocurrieron al recuperar los datos del usuario
                System.out.println("Error al cambiar el idioma del usuario: " + databaseError.getMessage());
            }
        });
    }

}
