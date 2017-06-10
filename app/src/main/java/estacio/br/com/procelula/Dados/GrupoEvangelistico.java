package estacio.br.com.procelula.Dados;

import java.io.Serializable;


public class GrupoEvangelistico implements Serializable {

    private int id;
    private int ges_celula_id;
    private String nome;
    private int dias;

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

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return getNome() + " (" + Integer.toString(getDias()) + " dias)" ;
    }
}
