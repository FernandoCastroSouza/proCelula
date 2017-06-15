package estacio.br.com.procelula.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Dados.Usuario;


public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "Banco Local", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF EXISTS  TB_AVISOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AVISOS_CELULA_ID INTEGER," +
                "TITULO TEXT(255) ," +
                "CONTEUDO TEXT , " +
                "CREATED DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "MODIFIED DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE IF EXISTS  TB_CELULAS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ID_USUARIO INTEGER," +
                "NOME TEXT(255) ," +
                "LIDER TEXT(255) ," +
                "DIA TEXT(100) ," +
                "HORARIO TEXT(255) ," +
                "LOCAL TEXT ," +
                "JEJUM TEXT(50) ," +
                "PERIODO TEXT(50) ," +
                "VERSICULO TEXT )");

        db.execSQL("CREATE TABLE IF EXISTS  TB_GES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "GES_CELULA_ID INTEGER," +
                "NOME TEXT(255) ," +
                "DIAS INT(3) )");

        db.execSQL("CREATE TABLE IF EXISTS  TB_PROGRAMACOES (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PROGRAMACOES_CELULA_ID INTEGER," +
                "NOME TEXT(50) ," +
                "DATA DATE ," +
                "HORARIO TEXT(50) ," +
                "LOCAL TEXT ," +
                "TELEFONE TEXT(20) ," +
                "VALOR TEXT(20) )");

        db.execSQL("CREATE TABLE IF EXISTS  TB_USUARIOS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USUARIOS_CELULA_ID INTEGER," +
                "NOME TEXT(255) ," +
                "SOBRENOME TEXT(255) ," +
                "LOGIN TEXT(100) ," +
                "SENHA TEXT(255) ," +
                "EMAIL TEXT(100) ," +
                "NASCIMENTO DATE ," +
                "PERFIL INTEGER(1) ," +
                "TOKEN TEXT(255) ," +
                "CREATED DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "MODIFIED DATETIME DEFAULT CURRENT_TIMESTAMP)");

        db.execSQL("CREATE TABLE IF EXISTS TB_LOGIN (" +
                "ID INTEGER PRIMARY KEY, " +
                "LOGIN VARCHAR(100), " +
                "SENHA VARCHAR(255)," +
                "USUARIOS_CELULA_ID INTEGER)");
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
            aviso.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            aviso.setAvisos_celula_id(cursor.getInt(cursor.getColumnIndex("AVISOS_CELULA_ID")));
            aviso.setTitulo(cursor.getString(cursor.getColumnIndex("TITULO")));
            aviso.setConteudo(cursor.getString(cursor.getColumnIndex("CONTEUDO")));

            lista.add(aviso);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public List<Celula> listaCelula(String SQL) {
        List<Celula> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Celula celula = new Celula();

            celula.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            celula.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            celula.setLider(cursor.getString(cursor.getColumnIndex("LIDER")));
            celula.setUsuario(this.listaUsuario("SELECT * FROM TB_USUARIOS WHERE ID = " + cursor.getString(cursor.getColumnIndex("ID_USUARIO"))).get(0));
            celula.setDia(cursor.getString(cursor.getColumnIndex("DIA")));
            celula.setHorario(cursor.getString(cursor.getColumnIndex("HORARIO")));
            celula.setLocal(cursor.getString(cursor.getColumnIndex("LOCAL")));
            celula.setJejum(cursor.getString(cursor.getColumnIndex("JEJUM")));
            celula.setPeriodo(cursor.getString(cursor.getColumnIndex("PERIODO")));
            celula.setVersiculo(cursor.getString(cursor.getColumnIndex("VERSICULO")));

            lista.add(celula);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public List<GrupoEvangelistico> listaGrupoEvangelistico(String SQL) {
        List<GrupoEvangelistico> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            GrupoEvangelistico grupoEvangelistico = new GrupoEvangelistico();

            grupoEvangelistico.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            grupoEvangelistico.setGes_celula_id(cursor.getInt(cursor.getColumnIndex("GES_CELULA_ID")));
            grupoEvangelistico.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
//            grupoEvangelistico.setDias(cursor.getInt(cursor.getColumnIndex("DIAS")));

            lista.add(grupoEvangelistico);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public List<Programacao> listaProgramacao(String SQL) {
        List<Programacao> lista = new ArrayList<>();
        SQLiteDatabase banco = this.getReadableDatabase();
        Cursor cursor;

        cursor = banco.rawQuery(SQL, null);
        cursor.moveToFirst();

        do {
            Programacao programacao = new Programacao();

            programacao.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            programacao.setProgramacoes_celula_id(cursor.getInt(cursor.getColumnIndex("PROGRAMACOES_CELULA_ID")));
            programacao.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            programacao.setData(cursor.getString(cursor.getColumnIndex("DATA")));
            programacao.setHorario(cursor.getString(cursor.getColumnIndex("HORARIO")));
            programacao.setLocal(cursor.getString(cursor.getColumnIndex("LOCAL")));
            programacao.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
            programacao.setValor(cursor.getString(cursor.getColumnIndex("VALOR")));

            lista.add(programacao);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
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
//            usuario.setCelula(this.listaCelula("SELECT * FROM TB_CELULAS WHERE ID = " + cursor.getString(cursor.getColumnIndex("USUARIOS_CELULA_ID"))).get(0));
            usuario.setUsuarios_celula_id(cursor.getInt(cursor.getColumnIndex("USUARIOS_CELULA_ID")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            usuario.setSobrenome(cursor.getString(cursor.getColumnIndex("SOBRENOME")));
            usuario.setLogin(cursor.getString(cursor.getColumnIndex("LOGIN")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex("SENHA")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
            usuario.setNascimento(cursor.getString(cursor.getColumnIndex("NASCIMENTO")));
            usuario.setPermissao(cursor.getInt(cursor.getColumnIndex("PERFIL")));

            lista.add(usuario);
            System.gc();
        } while (cursor.moveToNext());
        cursor.close();
        System.gc();

        return lista;
    }

    public void atualizarAviso(Aviso aviso) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("AVISOS_CELULA_ID", aviso.getAvisos_celula_id());
        content.put("TITULO", aviso.getTitulo());
        content.put("CONTEUDO", aviso.getConteudo());

        if (contagem("SELECT COUNT(*) FROM TB_AVISOS") > 0) {
            db.update("TB_AVISOS", content, "ID = " + aviso.getId(), null);
        } else {
            db.insert("TB_AVISOS", null, content);
        }
    }

    public void atualizarCelula(Celula celula) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", celula.getId());
        content.put("NOME", celula.getNome());
        content.put("LIDER", celula.getLider());
        content.put("DIA", celula.getDia());
        content.put("HORARIO", celula.getHorario());
        content.put("LOCAL", celula.getLocal());
        content.put("JEJUM", celula.getDia());
        content.put("PERIODO", celula.getPeriodo());
        content.put("VERSICULO", celula.getVersiculo());

        db.update("TB_CELULAS", content, "ID = " + celula.getId(), null);
    }

    public void atualizarGrupoEvangelistico(GrupoEvangelistico grupoEvangelistico) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", grupoEvangelistico.getId());
        content.put("GES_CELULA_ID", grupoEvangelistico.getGes_celula_id());
        content.put("NOME", grupoEvangelistico.getNome());
//        content.put("DIAS", grupoEvangelistico.getDias());
        db.update("TB_GES", content, "ID = " + grupoEvangelistico.getId(), null);
    }



    public void atualizarProgramacao(Programacao programacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", programacao.getId());
        content.put("PROGRAMACOES_CELULA_ID", programacao.getProgramacoes_celula_id());
        content.put("NOME", programacao.getNome());
        content.put("DATA", programacao.getData());
        content.put("HORARIO", programacao.getHorario());
        content.put("LOCAL", programacao.getLocal());
        content.put("TELEFONE", programacao.getTelefone());
        content.put("VALOR", programacao.getValor());

        db.update("TB_PROGRAMACOES", content, "ID = " + programacao.getId(), null);
    }

    public void atualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", usuario.getId());
        content.put("USUARIOS_CELULA_ID", usuario.getUsuarios_celula_id());
        content.put("NOME", usuario.getNome());
        content.put("SOBRENOME", usuario.getSobrenome());
        content.put("LOGIN", usuario.getLogin());
        content.put("SENHA", usuario.getSenha());
        content.put("EMAIL", usuario.getEmail());
        content.put("NASCIMENTO", usuario.getNascimento());
        content.put("PERFIL", usuario.getPermissao());
        if (usuario.getId() < 0) {
            db.insert("TB_USUARIOS", null, content);
        } else {
            if (contagem("SELECT COUNT(*) FROM TB_USUARIOS WHERE ID = " + String.valueOf(usuario.getId())) > 0) {
                db.update("TB_USUARIOS", content, "ID = " + usuario.getId(), null);
            } else {
                db.insert("TB_USUARIOS", null, content);
            }
        }
    }

    public void atualizarLogin(int id, String login, String senha, int idCelula) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues content = new ContentValues();

        content.put("ID", id);
        content.put("LOGIN", login);
        content.put("SENHA", senha);
        content.put("USUARIOS_CELULA_ID", idCelula);

        if (contagem("SELECT COUNT(*) FROM TB_LOGIN") > 0) {
            db.update("TB_LOGIN", content, "ID = " + id, null);
        } else {
            db.insert("TB_LOGIN", null, content);
        }
    }

}
