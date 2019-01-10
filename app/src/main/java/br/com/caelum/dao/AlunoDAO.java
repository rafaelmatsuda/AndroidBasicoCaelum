package br.com.caelum.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.model.Aluno;

public class AlunoDAO extends SQLiteOpenHelper
{
    private static final int VERSAO = 2;
    private static final String TABELA = "Alunos";
    private static final String DATABASE = "CadastroCaelum";

    public AlunoDAO(Context context)
    {
        super(context, DATABASE, null, VERSAO);
    }

    public void onCreate(SQLiteDatabase database)
    {
        String ddl = "CREATE TABLE "+TABELA+" (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, telefone TEXT, endereco TEXT, site TEXT, nota REAL, caminhoFoto Text);";
        database.execSQL(ddl);
    }

    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova)
    {
        String sql = "ALTER TABLE "+TABELA+"  ADD COLUMN caminhoFoto Text";
        database.execSQL(sql);

    }

    public void insere(Aluno aluno)
    {
        ContentValues values = new ContentValues();

        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        getWritableDatabase().insert(TABELA, null, values);
    }

    public List<Aluno> getLista()
    {
        List<Aluno> alunos = new ArrayList<Aluno>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM "+TABELA, null);

        while(c.moveToNext())
        {
            Aluno aluno = new Aluno();

            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }

        c.close();

        return alunos;
    }

    public void deleta(Aluno aluno)
    {
        String[] args = {aluno.getId().toString()};

        getWritableDatabase().delete(TABELA, "id = ?", args);
    }

    public void altera (Aluno aluno){

        String[] args = {aluno.getId().toString()};

        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("telefone", aluno.getTelefone());
        values.put("endereco", aluno.getEndereco());
        values.put("site", aluno.getSite());
        values.put("nota", aluno.getNota());
        values.put("caminhoFoto", aluno.getCaminhoFoto());

        getWritableDatabase().update(TABELA, values ,"id = ?", args);
    }

    //region Verifica se existe aluno com o telefone passado
    public boolean existeAluno (String telefone)
    {
        String[] args = {telefone};

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" WHERE telefone = ?", args);

        if (c.getCount() > 0){
            return true;
        }

        return false;
    }
        //endregion
}

