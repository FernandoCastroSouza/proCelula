package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class DeleteUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final Usuario usuario;
    private static final String QTDE = "qtde";
    private LoginActivity activity;

    public DeleteUsuarioTask(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(usuario.getId(), "usuarios");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(QTDE) > 0;
        }
        catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover o usuario", Toast.LENGTH_LONG).show();
        }
        else {
            DbHelper dao = new DbHelper(activity);
            dao.alterar("DELETE FROM TB_USUARIOS WHERE ID = " + usuario.getId());
            dao.close();
        }
    }
}
