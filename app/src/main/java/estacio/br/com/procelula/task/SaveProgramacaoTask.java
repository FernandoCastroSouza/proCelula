package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import estacio.br.com.procelula.Activities.ProgramacaoActivity;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.ws.WebService;

public class SaveProgramacaoTask extends AsyncTask<String, Object, Long> {
    private final ProgramacaoActivity activity;
    private final Programacao programacao;

    private static final String ID = "id";

    public SaveProgramacaoTask(ProgramacaoActivity activity, Programacao programacao) {
        this.activity = activity;
        this.programacao = programacao;
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.save(programacao, "programacoes");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return jsonObject.getLong(ID);
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    protected void onPostExecute(Long id) {
        if (id == 0) {
            Toast.makeText(activity, "Houve um erro ao salvar o programacao", Toast.LENGTH_LONG).show();
        } else {
            DbHelper dao = new DbHelper(activity);
            dao.atualizarProgramacao(programacao);
            dao.close();
        }
        activity.finish();
    }
}
