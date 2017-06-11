package estacio.br.com.procelula.Dados;

import java.io.Serializable;


public class GrupoEvangelistico implements Serializable {

    private int id;
    private int ges_celula_id;
    private String nome;
    private String data;
    private Boolean ativo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGes_celula_id() {
        return ges_celula_id;
    }

    public void setGes_celula_id(int ges_celula_id) {
        this.ges_celula_id = ges_celula_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getNome() + " (" + getData() + " dias)" ;
    }
}
