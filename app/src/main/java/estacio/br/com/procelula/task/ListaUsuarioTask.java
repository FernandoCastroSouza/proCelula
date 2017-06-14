package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.UsuarioConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final LoginActivity activity;
    private ProgressDialog alert;

    public ListaUsuarioTask(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_USUARIOS") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando suas informações");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listAll("usuarios");
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Usuario> usuarios = new UsuarioConverter().fromJson(jsonArray);
            if (usuarios != null && !usuarios.isEmpty()) {
                DbHelper dao = new DbHelper(activity);
                for (int i = 0; i < usuarios.size(); i++) {
                    dao.atualizarUsuario(usuarios.get(i));
                }
                dao.close();
            } else {
                System.out.println("O objeto acabou ficando vazio!");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    alert.dismiss();
                } catch (NullPointerException e) {
                    System.out.println("Alert esta nulo");
                }
            }
        });
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro de conexão", Toast.LENGTH_LONG).show();
        }
    }
}
