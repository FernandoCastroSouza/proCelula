package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.FormGEActivity;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.ws.WebService;

public class SaveGrupoEvangelisticoTask extends AsyncTask<String, Object, Boolean> {
    private final FormGEActivity activity;
    private final GrupoEvangelistico grupoevangelistico;
    private ProgressDialog alert;

    public SaveGrupoEvangelisticoTask(FormGEActivity activity, GrupoEvangelistico grupoevangelistico) {
        this.activity = activity;
        this.grupoevangelistico = grupoevangelistico;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Salvando GE");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(grupoevangelistico, "ges");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao salvar o grupoevangelistico", Toast.LENGTH_LONG).show();
        }
        alert.dismiss();
        activity.finish();
    }
}
