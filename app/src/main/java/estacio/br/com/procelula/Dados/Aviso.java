package estacio.br.com.procelula.Dados;

import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import estacio.br.com.procelula.ws.WebService;

public class Aviso implements Serializable {
    private int id;
    private int avisos_celula_id;
    private String titulo;
    private String conteudo;
    private Boolean ativo;
    private String created;
    private String modified;
    private ListView avisoslist;

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvisos_celula_id() {
        return avisos_celula_id;
    }

    public void setAvisos_celula_id(int avisos_celula_id) {
        this.avisos_celula_id = avisos_celula_id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public ListView getAvisoslist() {
        return avisoslist;
    }

    @Override
    public String toString() {
        return getTitulo();
    }

    public void setAvisoslist(ListView avisoslist) {
        this.avisoslist = avisoslist;
    }

    public static Collection<Aviso> retornaAvisosConvertidos(){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<Aviso>>(){}.getType();
        Collection<Aviso> avisos = gson.fromJson(WebService.listarAvisos(), collectionType);
        return avisos;
    }
}
