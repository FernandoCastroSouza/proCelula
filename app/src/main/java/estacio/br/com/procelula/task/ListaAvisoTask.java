package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import estacio.br.com.procelula.Activities.AvisoActivity;
import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.AvisoConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaAvisoTask extends AsyncTask<String, Object, Boolean> {
    private final AvisoActivity activity;
    private int celulaId;
    private ProgressDialog alert;

    public ListaAvisoTask(AvisoActivity activity, int celulaId) {
        this.activity = activity;
        this.celulaId = celulaId;
    }

    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_AVISOS") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando os avisos");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listByCelula("avisos", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Aviso> avisos = new AvisoConverter().fromJson(jsonArray);
            if (avisos != null && !avisos.isEmpty()) {
                DbHelper dao = new DbHelper(activity);
                for (int i = 0; i < avisos.size(); i++) {
                    dao.atualizarAviso(avisos.get(i));
                }
                dao.close();
            } else {
                System.out.println("O objeto acabou ficando vazio!");
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
