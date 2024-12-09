package br.unipar.alunoonline.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.unipar.alunoonline.model.Aluno;
import br.unipar.alunoonline.model.Disciplina;
import br.unipar.alunoonline.model.Nota;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cadastro_nota.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTA = "tb_nota";
    public static final String TABLE_DISCIPLINA = "tb_disciplina";
    public static final String TABLE_ALUNO = "tb_aluno";
    public static final String[] TABLE_ALUNO_COLUMN = {"id", "nome", "ra"};
    public static final String[] TABLE_DISCIPLINA_COLUMN = {"id", "nome"};
    public static final String[] TABLE_NOTA_COLUMN = {"id", "nota", "bimestre", "disciplina_id", "aluno_id"};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createDisciplinasTable = "CREATE TABLE " + TABLE_DISCIPLINA + " (" +
                TABLE_DISCIPLINA_COLUMN[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_DISCIPLINA_COLUMN[1] + " TEXT UNIQUE)";

        String createNotasTable = "CREATE TABLE " + TABLE_NOTA + " (" +
                TABLE_NOTA_COLUMN[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_NOTA_COLUMN[1] + " REAL, " +
                TABLE_NOTA_COLUMN[2] + " TEXT, " +
                TABLE_NOTA_COLUMN[3] + " INTEGER, " +
                TABLE_NOTA_COLUMN[4] + " INTEGER, " +
                "FOREIGN KEY(" + TABLE_NOTA_COLUMN[3] + ") REFERENCES " + TABLE_DISCIPLINA + "(" + TABLE_DISCIPLINA_COLUMN[0] + "), " +
                "FOREIGN KEY(" + TABLE_NOTA_COLUMN[4] + ") REFERENCES " + TABLE_ALUNO + "(" + TABLE_ALUNO_COLUMN[0] + "))";

        String createAlunosTable = "CREATE TABLE " + TABLE_ALUNO + " (" +
                TABLE_ALUNO_COLUMN[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_ALUNO_COLUMN[1] + " TEXT, " +
                TABLE_ALUNO_COLUMN[2] + " TEXT UNIQUE)";

        db.execSQL(createDisciplinasTable);
        db.execSQL(createAlunosTable);
        db.execSQL(createNotasTable);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_DISCIPLINA + " (" + TABLE_DISCIPLINA_COLUMN[1] + ") VALUES ('Matemática')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_DISCIPLINA + " (" + TABLE_DISCIPLINA_COLUMN[1] + ") VALUES ('Português')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_DISCIPLINA + " (" + TABLE_DISCIPLINA_COLUMN[1] + ") VALUES ('História')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_DISCIPLINA + " (" + TABLE_DISCIPLINA_COLUMN[1] + ") VALUES ('Geografia')");

        Log.d("DatabaseHelper", "Disciplinas inseridas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCIPLINA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALUNO);
        onCreate(db);
    }

    public boolean addNota(double nota, String bimestre, String disciplinaNome, String alunoNome, String ra) {
        SQLiteDatabase db = null;
        Disciplina disciplina = getDisciplinaByNome(disciplinaNome);
        Aluno aluno = getAlunoByNome(alunoNome);
        if (aluno == null) {
            aluno = inserirAluno(alunoNome, ra);
        }

        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TABLE_NOTA_COLUMN[1], nota);
            values.put(TABLE_NOTA_COLUMN[2], bimestre);
            values.put(TABLE_NOTA_COLUMN[3], disciplina.getId());
            values.put(TABLE_NOTA_COLUMN[4], aluno.getId());

            long result = db.insert(TABLE_NOTA, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao adicionar nota: " + e.getMessage());
            return false;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
    }

    public List<Nota> getNotasPorAlunoEDisciplina(int alunoId, int disciplinaId) {
        List<Nota> notas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT " +
                    TABLE_NOTA_COLUMN[0] + ", " +
                    TABLE_NOTA_COLUMN[1] + ", " +
                    TABLE_NOTA_COLUMN[2] + ", " +
                    TABLE_NOTA_COLUMN[3] + ", " +
                    TABLE_NOTA_COLUMN[4] +
                    " FROM " + TABLE_NOTA +
                    " WHERE " + TABLE_NOTA_COLUMN[4] + " = ?" + " AND " + TABLE_NOTA_COLUMN[3] + " = ?",
                    new String[]{String.valueOf(alunoId), String.valueOf(disciplinaId)});

            if (cursor.moveToFirst()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(cursor.getInt(0));
                    nota.setNota(cursor.getDouble(1));
                    nota.setBimestre(cursor.getString(2));
                    nota.setDisciplina(getDisciplinaById(cursor.getInt(3)));
                    nota.setAluno(getAlunoById(cursor.getInt(4)));
                    notas.add(nota);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar notas: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return notas;
    }

    public List<Nota> getNotasPorDisciplinaEAluno(int disciplinaId, int alunoId) {
        List<Nota> notas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT " +
                    TABLE_NOTA_COLUMN[0] + ", " +
                    TABLE_NOTA_COLUMN[1] + ", " +
                    TABLE_NOTA_COLUMN[2] + ", " +
                    TABLE_NOTA_COLUMN[3] + ", " +
                    TABLE_NOTA_COLUMN[4] +
                    " FROM " + TABLE_NOTA +
                    " WHERE " + TABLE_NOTA_COLUMN[3] + " = ?" + " AND " + TABLE_NOTA_COLUMN[4] + " = ?",
                    new String[]{String.valueOf(disciplinaId), String.valueOf(alunoId)});

            if (cursor.moveToFirst()) {
                do {
                    Nota nota = new Nota();
                    nota.setId(cursor.getInt(0));
                    nota.setNota(cursor.getDouble(1));
                    nota.setBimestre(cursor.getString(2));
                    nota.setDisciplina(getDisciplinaById(cursor.getInt(3)));
                    nota.setAluno(getAlunoById(cursor.getInt(4)));
                    notas.add(nota);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar notas: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return notas;
    }

    public Aluno getAlunoById(Integer alunoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ALUNO + " WHERE id = ?", new String[]{String.valueOf(alunoId)});

            if (cursor.moveToFirst()) {
                Aluno aluno = new Aluno();
                aluno.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                aluno.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                aluno.setRa(cursor.getString(cursor.getColumnIndexOrThrow("RA")));
                return aluno;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar aluno: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null;
    }

    public Disciplina getDisciplinaById(int disciplinaId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_DISCIPLINA + " WHERE id = ?", new String[]{String.valueOf(disciplinaId)});

            if (cursor.moveToFirst()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                disciplina.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                return disciplina;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar disciplina: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null;
    }

    public Aluno getAlunoByNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_ALUNO + " WHERE nome LIKE ?", new String[]{"%" + nome + "%"});

            if (cursor.moveToFirst()) {
                Aluno aluno = new Aluno();
                aluno.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                aluno.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                aluno.setRa(cursor.getString(cursor.getColumnIndexOrThrow("RA")));
                return aluno;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar aluno por nome: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null;
    }

    public Disciplina getDisciplinaByNome(String nome) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_DISCIPLINA + " WHERE nome LIKE ?", new String[]{"%" + nome + "%"});

            if (cursor.moveToFirst()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                disciplina.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                return disciplina;
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar disciplina por nome: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return null;
    }

    public List<Disciplina> getAllDisciplinas() {
        List<Disciplina> disciplinas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_DISCIPLINA, null);

            if (cursor.moveToFirst()) {
                do {
                    Disciplina disciplina = new Disciplina();
                    disciplina.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    disciplina.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                    disciplinas.add(disciplina);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar todas as disciplinas: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return disciplinas;
    }

    public Aluno inserirAluno(String nome, String ra) {
        SQLiteDatabase db = null;
        Aluno aluno = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TABLE_ALUNO_COLUMN[1], nome);
            values.put(TABLE_ALUNO_COLUMN[2], ra);

            long id = db.insert(TABLE_ALUNO, null, values);

            if (id != -1) {
                aluno = getAlunoById((int) id);
            }

            return aluno;
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao inserir aluno: " + e.getMessage());
            return null;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    @SuppressLint("Range")
    public List<Aluno> getAlunosPorDisciplina(Integer disciplinaId) {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            String query = "SELECT " + TABLE_NOTA_COLUMN[4] + " FROM " + TABLE_NOTA + " WHERE " + TABLE_NOTA_COLUMN[3] + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(disciplinaId)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Aluno aluno = getAlunoById(cursor.getInt(cursor.getColumnIndex(TABLE_NOTA_COLUMN[4])));
                    aluno.getNotas().addAll(getNotasPorAlunoEDisciplina(aluno.getId(), disciplinaId));
                    alunos.add(aluno);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar notas por disciplina: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return alunos;
    }

    @SuppressLint("Range")
    public List<Disciplina> getDisciplinasPorAluno(Integer alunoId) {
        List<Disciplina> disciplinas = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            String query = "SELECT " + TABLE_NOTA_COLUMN[3] + ", " + TABLE_NOTA_COLUMN[4] + " FROM " + TABLE_NOTA + " WHERE " + TABLE_NOTA_COLUMN[4] + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(alunoId)});

            if (cursor != null && cursor.moveToFirst()) {
                Aluno aluno = getAlunoById(cursor.getInt(cursor.getColumnIndex(TABLE_NOTA_COLUMN[4])));
                Disciplina disciplina = getDisciplinaById(cursor.getInt(cursor.getColumnIndex(TABLE_NOTA_COLUMN[3])));
                do {
                    aluno.getNotas().addAll(getNotasPorDisciplinaEAluno(cursor.getColumnIndex(TABLE_NOTA_COLUMN[3]), alunoId));
                    disciplina.getAlunos().add(aluno);
                    disciplinas.add(disciplina);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar notas por disciplina: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return disciplinas;
    }

    @SuppressLint("Range")
    public List<Aluno> getAllAlunos() {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();

            String query = "SELECT * FROM " + TABLE_ALUNO;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Aluno aluno = new Aluno();
                    aluno.setId(cursor.getInt(cursor.getColumnIndex(TABLE_ALUNO_COLUMN[0])));
                    aluno.setNome(cursor.getString(cursor.getColumnIndex(TABLE_ALUNO_COLUMN[1])));
                    aluno.setRa(cursor.getString(cursor.getColumnIndex(TABLE_ALUNO_COLUMN[2])));

                    alunos.add(aluno);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Erro ao buscar todos os alunos: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return alunos;
    }
}
