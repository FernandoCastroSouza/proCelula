package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class DeleteProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final Programacao programacao;
    private static final String QTDE = "qtde";
    private LoginActivity activity;

    public DeleteProgramacaoTask(Programacao programacao) {
        this.programacao = programacao;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(programacao.getId(), "programacoes");
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
            Toast.makeText(activity, "Houve um erro ao remover o programacao", Toast.LENGTH_LONG).show();
        }
        else {
            DbHelper dao = new DbHelper(activity);
            dao.alterar("DELETE FROM TB_PROGRAMACOES WHERE ID = " + programacao.getId());
            dao.close();
        }
    }
}
