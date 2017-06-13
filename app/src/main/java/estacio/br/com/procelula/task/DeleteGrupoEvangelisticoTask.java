package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class DeleteGrupoEvangelisticoTask extends AsyncTask<String, Object, Boolean> {
    private final GrupoEvangelistico grupoevangelistico;
    private static final String QTDE = "qtde";
    private LoginActivity activity;

    public DeleteGrupoEvangelisticoTask(GrupoEvangelistico grupoevangelistico) {
        this.grupoevangelistico = grupoevangelistico;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(grupoevangelistico.getId(), "grupoevangelisticos");
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
            Toast.makeText(activity, "Houve um erro ao remover o grupoevangelistico", Toast.LENGTH_LONG).show();
        }
        else {
            DbHelper dao = new DbHelper(activity);
            dao.alterar("DELETE FROM TB_GES WHERE ID = " + grupoevangelistico.getId());
            dao.close();
        }
    }
}
