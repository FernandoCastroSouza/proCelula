package estacio.br.com.procelula.task;


import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.ProgramacaoConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final LoginActivity activity;

    public ListaProgramacaoTask(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listAll("programacoes");
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Programacao> programacoes = new ProgramacaoConverter().fromJson(jsonArray);
            if (programacoes != null && !programacoes.isEmpty()) {
                DbHelper dao = new DbHelper(activity);
                for (int i = 0; i < programacoes.size(); i++) {
                    dao.atualizarProgramacao(programacoes.get(i));
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
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao obter a lista de clientes", Toast.LENGTH_LONG).show();
        }
    }
}
