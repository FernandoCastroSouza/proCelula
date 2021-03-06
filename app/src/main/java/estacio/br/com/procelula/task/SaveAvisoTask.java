package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.FormAvisoActivity;
import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.ws.WebService;

public class SaveAvisoTask extends AsyncTask<String, Object, Boolean> {
    private final FormAvisoActivity activity;
    private final Aviso aviso;
    private ProgressDialog alert;


    public SaveAvisoTask(FormAvisoActivity activity, Aviso aviso) {
        this.activity = activity;
        this.aviso = aviso;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Salvando aviso");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(aviso, "avisos");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao salvar o aviso", Toast.LENGTH_LONG).show();
        }
        alert.dismiss();
        activity.finish();
    }
}
