package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.FormAvisoActivity;
import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class SaveAvisoTask extends AsyncTask<String, Object, Long> {
    private static final String ID = "id";
    private final FormAvisoActivity activity;
    private final Aviso aviso;


    public SaveAvisoTask(FormAvisoActivity activity, Aviso aviso) {
        this.activity = activity;
        this.aviso = aviso;
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(aviso, "avisos");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o aviso", Toast.LENGTH_LONG).show();
        } else {
            DbHelper dao = new DbHelper(activity);
            dao.atualizarAviso(aviso);
            dao.close();
        }
        activity.finish();
    }
}
