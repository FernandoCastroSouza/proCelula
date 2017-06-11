package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class SaveUsuarioTask extends AsyncTask<String, Object, Long> {
    private final LoginActivity activity;
    private final Usuario usuario;

    private static final String ID = "id";

    public SaveUsuarioTask(LoginActivity activity, Usuario usuario) {
        this.activity = activity;
        this.usuario = usuario;
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(usuario, "usuarios");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o usuario", Toast.LENGTH_LONG).show();
        } else {
            DbHelper dao = new DbHelper(activity);
            dao.atualizarUsuario(usuario);
            dao.close();
        }
        activity.finish();
    }
}
