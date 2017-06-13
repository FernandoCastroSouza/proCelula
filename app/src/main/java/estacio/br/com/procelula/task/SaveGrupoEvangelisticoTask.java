package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.GEActivity;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class SaveGrupoEvangelisticoTask extends AsyncTask<String, Object, Long> {
    private final GEActivity activity;
    private final GrupoEvangelistico grupoevangelistico;

    private static final String ID = "id";

    public SaveGrupoEvangelisticoTask(GEActivity activity, GrupoEvangelistico grupoevangelistico) {
        this.activity = activity;
        this.grupoevangelistico = grupoevangelistico;
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(grupoevangelistico, "grupoevangelisticos");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o grupoevangelistico", Toast.LENGTH_LONG).show();
        } else {
            DbHelper dao = new DbHelper(activity);
            dao.atualizarGrupoEvangelistico(grupoevangelistico);
            dao.close();
        }
        activity.finish();
    }
}
