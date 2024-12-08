package com.example.projetodecadastro2pt.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cadastro_notas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "notas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RA = "ra";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_DISCIPLINA = "disciplina";
    public static final String COLUMN_NOTA = "nota";
    public static final String COLUMN_BIMESTRE = "bimestre";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RA + " TEXT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_DISCIPLINA + " TEXT, " +
                COLUMN_NOTA + " REAL, " +
                COLUMN_BIMESTRE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addNota(String ra, String nome, String disciplina, double nota, String bimestre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RA, ra);
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_DISCIPLINA, disciplina);
        values.put(COLUMN_NOTA, nota);
        values.put(COLUMN_BIMESTRE, bimestre);
        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }

    public List<String> getNotas() {
        List<String> notas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NOTA + " FROM " + TABLE_NAME, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int columnIndex = cursor.getColumnIndex(COLUMN_NOTA);
                    if (columnIndex != -1) {
                        double nota = cursor.getDouble(columnIndex);
                        notas.add(String.valueOf(nota));  // Adiciona a nota à lista
                    } else {
                        notas.add("Nota não disponível");
                    }
                } while (cursor.moveToNext());
            }
        }

        cursor.close();
        db.close();
        return notas;
    }


    public List<String> getDisciplinas() {
        List<String> disciplinas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT DISTINCT " + COLUMN_DISCIPLINA + " FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(COLUMN_DISCIPLINA);
                if (columnIndex != -1) {
                    disciplinas.add(cursor.getString(columnIndex));
                } else {
                    disciplinas.add("Disciplina não disponível");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return disciplinas;
    }


    public List<String> getMediasByDisciplina(String disciplina) {
        List<String> medias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_RA + ", " + COLUMN_NOME + ", AVG(" + COLUMN_NOTA + ") AS media " +
                        "FROM " + TABLE_NAME + " WHERE " + COLUMN_DISCIPLINA + " = ? GROUP BY " + COLUMN_RA + ", " + COLUMN_NOME,
                new String[]{disciplina});

        // Verifica se a consulta retornou resultados
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    // Verifica se as colunas existem
                    int raColumnIndex = cursor.getColumnIndex(COLUMN_RA);
                    int nomeColumnIndex = cursor.getColumnIndex(COLUMN_NOME);
                    int mediaColumnIndex = cursor.getColumnIndex("media");

                    if (raColumnIndex != -1 && nomeColumnIndex != -1 && mediaColumnIndex != -1) {
                        String ra = cursor.getString(raColumnIndex);
                        String nome = cursor.getString(nomeColumnIndex);
                        double media = cursor.getDouble(mediaColumnIndex);
                        medias.add(ra + " - " + nome + ": " + media);
                    } else {
                        Log.e("DatabaseError", "Uma ou mais colunas não foram encontradas.");
                    }
                } while (cursor.moveToNext());
            }
        } else {
            Log.e("DatabaseError", "Nenhum resultado encontrado para a disciplina: " + disciplina);
        }

        cursor.close();
        return medias;
    }
}
