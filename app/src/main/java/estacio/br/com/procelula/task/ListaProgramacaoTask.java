package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import estacio.br.com.procelula.Activities.ProgramacaoActivity;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.ProgramacaoConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final ProgramacaoActivity activity;
    private ProgressDialog alert;
    private int celulaId;

    public ListaProgramacaoTask(ProgramacaoActivity activity, int celulaId) {
        this.celulaId = celulaId;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_PROGRAMACOES") <= 0) {
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
            String jsonResult = request.listByCelula("programacoes", celulaId);
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
            Toast.makeText(activity, "Houve um erro ao obter a lista de clientes", Toast.LENGTH_LONG).show();
        }
    }
}
