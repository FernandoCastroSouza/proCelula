package estacio.br.com.procelula.Dados;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Usuario {
    public static final int PERMISSAO_BASICA = 0;
    public static final int PERMISSAO_LIDER  = 1;
    public static final int PERMISSAO_PASTOR = 2;

    public static final String ID_USUARIO_SP       = "ID_USUARIO";
    public static final String NOME_SP             = "NOME";
    public static final String SOBRENOME_SP        = "SOBRENOME";
    public static final String DATA_NASCIMENTO_SP  = "DATA_NASCIMENTO";
    public static final String LOGIN_SP            = "LOGIN";
    public static final String PERMISSAO_SP        = "PERMISSAO;";

    private int id;
    private int usuarios_celula_id;
    private String nome;
    private String sobrenome;
    private String login;
    private String senha;
    private String email;
    private String nascimento;
    private int perfil;
    private String token;
    private Boolean ativo;
    private String created;
    private String modified;

    private Celula celula;

    private int permissao;

    public Usuario() {
        permissao = PERMISSAO_BASICA;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public Celula getCelula() {
        return celula;
    }

    public void setCelula(Celula celula) {
        this.celula = celula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getPermissao() {
        return permissao;
    }

    public void setPermissao(int permissao) {
        this.permissao = permissao;
    }

    public int getUsuarios_celula_id() {
        return usuarios_celula_id;
    }

    public void setUsuarios_celula_id(int usuarios_celula_id) {
        this.usuarios_celula_id = usuarios_celula_id;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return getNome() +  " " + getSobrenome() + " - Dia " + getNascimento();
    }

    public List<Usuario> setFields(JSONArray jsonArray) {
        List<Usuario> usuarios = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++) {
            try {
                Usuario usuario = new Usuario();
                JSONObject object = (JSONObject) jsonArray.get(i);
                usuario.setId(object.getInt("id"));
                usuario.setUsuarios_celula_id(object.getInt("usuarios_celula_id"));
                usuario.setNome(object.getString("nome"));
                usuario.setSobrenome(object.getString("sobrenome"));
                usuario.setLogin(object.getString("login"));
                usuario.setSenha(object.getString("senha"));
                usuario.setEmail(object.getString("email"));
                usuario.setNascimento(object.getString("nascimento"));
                usuario.setPerfil(object.getInt("perfil"));
                usuarios.add(usuario);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    //    public Collection<Usuario> retornaUsuarios(){
//        List<Usuario> usuarios = null;
//        Type listType = new TypeToken<List<Usuario>>(){}.getType();
//        usuarios = new Gson().fromJson(setFields(WebService.listarUsuarios()), listType);
//        return usuarios;
//    }
}