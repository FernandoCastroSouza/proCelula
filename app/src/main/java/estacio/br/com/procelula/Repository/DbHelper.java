package estacio.br.com.procelula.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Dados.Usuario;

/**
 * Created by Fernando on 15/05/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "Banco Local", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TB_AVISOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AVISOS_CELULA_ID INTEGER," +
                "TITULO TEXT(255) NOT NULL," +
                "CONTEUDO TEXT NOT NULL" +
                "CREATED DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "MODIFIED DATETIME DEFAULT CURRENT_TIMESTAMP");

        db.execSQL("CREATE TABLE TB_CELULAS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ID_USUARIO INTEGER" +
                "NOME TEXT(255) NOT NULL," +
                "LIDER TEXT(255) NOT NULL," +
                "DIA TEXT(100) NOT NULL," +
                "HORARIO TEXT(255) NOT NULL," +
                "LOCAL TEXT NOT NULL," +
                "JEJUM TEXT(50) NOT NULL," +
                "PERIODO TEXT(50) NOT NULL," +
                "VERSICULO TEXT NOT NULL");

        db.execSQL("CREATE TABLE TB_GES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "GES_CELULA_ID INTEGER," +
                "NOME TEXT(255) NOT NULL," +
                "DIAS INT(3) NOT NULL");

        db.execSQL("CREATE TABLE TB_PROGRAMACOES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PROGRAMACOES_CELULA_ID INTEGER," +
                "NOME TEXT(50) NOT NULL," +
                "DATA DATE NOT NULL," +
                "HORARIO TEXT(50) NOT NULL," +
                "LOCAL TEXT NOT NULL," +
                "TELEFONE TEXT(20) NOT NULL," +
                "VALOR TEXT(20) NOT NULL");

        db.execSQL("CREATE TABLE TB_USUARIOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USUARIOS_CELULA_ID INTEGER," +
                "NOME TEXT(255) NOT NULL," +
                "SOBRENOME TEXT(255) NOT NULL," +
                "LOGIN TEXT(100) NOT NULL," +
                "SENHA TEXT(255) NOT NULL," +
                "EMAIL TEXT(100) NOT NULL," +
                "NASCIMENTO DATE NOT NULL," +
                "PERFIL INTEGER(1) NOT NULL," +
                "TOKEN TEXT(255) NOT NULL," +
                "CREATED DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "MODIFIED DATETIME DEFAULT CURRENT_TIMESTAMP");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String consulta(String SQL, String campo) throws android.database.CursorIndexOutOfBoundsException {
        String resultado = "";
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();
        resultado += cursor.getString(cursor.getColumnIndex(campo));
        return resultado;
    }

    public int contagem(String SQL) {
        int resultado;
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();
        resultado = cursor.getInt(0);
        return resultado;
    }

    public void alterar(String SQL) {
        SQLiteDatabase banco = this.getWritableDatabase();
        banco.execSQL(SQL);
    }

    public List<Aviso> listaAviso(String SQL) {
        List<Aviso> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Aviso aviso = new Aviso();

            aviso.setId_aviso(cursor.getInt(cursor.getColumnIndex("ID_AVISO")));
            aviso.setId_celula(cursor.getInt(cursor.getColumnIndex("ID_CELULA")));
            aviso.setTitulo(cursor.getString(cursor.getColumnIndex("TITULO")));
            aviso.setConteudo(cursor.getString(cursor.getColumnIndex("CONTEUDO")));

            lista.add(aviso);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarAviso(Aviso aviso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("AVISOS_CELULA_ID", aviso.getId_celula());
        content.put("TITULO", aviso.getTitulo());
        content.put("CONTEUDO", aviso.getConteudo());

        db.update("TB_AVISOS", content, "ID = " + aviso.getId_aviso(), null);
    }

    public List<Celula> listaCelula(String SQL) {
        List<Celula> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Celula celula = new Celula();

            celula.setId_celula(cursor.getInt(cursor.getColumnIndex("ID")));
            celula.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            celula.setLider(cursor.getString(cursor.getColumnIndex("LIDER")));
            celula.setUsuario(this.listaUsuario("SELECT * FROM TB_USUARIOS WHERE ID = " + cursor.getString(cursor.getColumnIndex("ID_USUARIO"))).get(0));
            celula.setDia(cursor.getString(cursor.getColumnIndex("DIA")));
            celula.setHorario(cursor.getString(cursor.getColumnIndex("HORARIO")));
            celula.setLocal_celula(cursor.getString(cursor.getColumnIndex("LOCAL")));
            celula.setDia_jejum(cursor.getString(cursor.getColumnIndex("JEJUM")));
            celula.setPeriodo(cursor.getString(cursor.getColumnIndex("PERIODO")));
            celula.setVersiculo(cursor.getString(cursor.getColumnIndex("VERSICULO")));

            lista.add(celula);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarCelula(Celula celula) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", celula.getId_celula());
        content.put("NOME", celula.getNome());
        content.put("LIDER", celula.getLider());
        content.put("DIA", celula.getDia());
        content.put("HORARIO", celula.getHorario());
        content.put("LOCAL", celula.getLocal_celula());
        content.put("JEJUM", celula.getDia_jejum());
        content.put("PERIODO", celula.getPeriodo());
        content.put("VERSICULO", celula.getVersiculo());

        db.update("TB_CELULAS", content, "ID = " + celula.getId_celula(), null);
    }

    public List<GrupoEvangelistico> listaGrupoEvangelistico(String SQL) {
        List<GrupoEvangelistico> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            GrupoEvangelistico grupoEvangelistico = new GrupoEvangelistico();

            grupoEvangelistico.setId_ge(cursor.getInt(cursor.getColumnIndex("ID")));
            grupoEvangelistico.setId_celula(cursor.getInt(cursor.getColumnIndex("GES_CELULA_ID")));
            grupoEvangelistico.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            grupoEvangelistico.setDias(cursor.getInt(cursor.getColumnIndex("DIAS")));

            lista.add(grupoEvangelistico);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarGrupoEvangelistico(GrupoEvangelistico grupoEvangelistico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", grupoEvangelistico.getId_ge());
        content.put("GES_CELULA_ID", grupoEvangelistico.getId_celula());
        content.put("NOME", grupoEvangelistico.getNome());
        content.put("DIAS", grupoEvangelistico.getDias());


        db.update("TB_GES", content, "ID = " + grupoEvangelistico.getId_ge(), null);
    }

    public List<Programacao> listaProgramacao(String SQL) {
        List<Programacao> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Programacao programacao = new Programacao();

            programacao.setId_programacao(cursor.getInt(cursor.getColumnIndex("ID")));
            programacao.setId_celula(cursor.getInt(cursor.getColumnIndex("PROGRAMACOES_CELULA_ID")));
            programacao.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            programacao.setData_prog(cursor.getString(cursor.getColumnIndex("DATA")));
            programacao.setHorario(cursor.getString(cursor.getColumnIndex("HORARIO")));
            programacao.setLocal_prog(cursor.getString(cursor.getColumnIndex("LOCAL")));
            programacao.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
            programacao.setValor(cursor.getString(cursor.getColumnIndex("VALOR")));

            lista.add(programacao);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarProgramacao(Programacao programacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", programacao.getId_programacao());
        content.put("PROGRAMACOES_CELULA_ID", programacao.getId_celula());
        content.put("NOME", programacao.getNome());
        content.put("DATA", programacao.getData_prog());
        content.put("HORARIO", programacao.getHorario());
        content.put("LOCAL", programacao.getLocal_prog());
        content.put("TELEFONE", programacao.getTelefone());
        content.put("VALOR", programacao.getValor());

        db.update("TB_PROGRAMACOES", content, "ID = " + programacao.getId_programacao(), null);
    }

    public List<Usuario> listaUsuario(String SQL) {
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Usuario usuario = new Usuario();

            usuario.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            usuario.setId_celula(cursor.getInt(cursor.getColumnIndex("USUARIOS_CELULA_ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            usuario.setSobrenome(cursor.getString(cursor.getColumnIndex("SOBRENOME")));
            usuario.setLogin(cursor.getString(cursor.getColumnIndex("LOGIN")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex("SENHA")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            usuario.setDataNascimento(cursor.getString(cursor.getColumnIndex("NASCIMENTO")));
            usuario.setPermissao(cursor.getInt(cursor.getColumnIndex("PERFIL")));

            lista.add(usuario);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", usuario.getId());
        content.put("USUARIOS_CELULA_ID", usuario.getNome());
        content.put("NOME", usuario.getEmail());
        content.put("SOBRENOME", usuario.getSenha());
        content.put("LOGIN", usuario.getSobrenome());
        content.put("SENHA", usuario.getDataNascimento());
        content.put("EMAIL", usuario.getId_celula());
        content.put("NASCIMENTO", usuario.getLogin());
        content.put("PERFIL", usuario.getPermissao());

        db.update("TB_USUARIOS", content, "ID = " + usuario.getId(), null);
    }
}
