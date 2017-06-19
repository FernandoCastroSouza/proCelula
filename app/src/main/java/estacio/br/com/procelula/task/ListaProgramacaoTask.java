package estacio.br.com.procelula.task;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import estacio.br.com.procelula.Activities.ProgramacaoActivity;
import estacio.br.com.procelula.Activities.ProgramacaoSelecionadaActivity;
import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.ProgramacaoConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaProgramacaoTask extends AsyncTask<String, Object, Boolean> {
    private final ProgramacaoActivity activity;
    private ProgressDialog alert;
    private int celulaId;
    private DbHelper db;
    private ListView listview_programacoes;
    private ImageView imageview_lista_vazia;

    public ListaProgramacaoTask(ProgramacaoActivity activity, int celulaId) {
        this.celulaId = celulaId;
        this.activity = activity;
        db = new DbHelper(activity);
        listview_programacoes = (ListView) activity.findViewById(R.id.listview_programacoes);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
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
                db.alterar("DELETE FROM TB_PROGRAMACOES;");
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
        try {
            int celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            final List<Programacao> listaProgramacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE PROGRAMACOES_CELULA_ID = " + celulaid);
            ArrayAdapter<Programacao> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaProgramacao);
            listview_programacoes.setAdapter(adapter);
            listview_programacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(activity, ProgramacaoSelecionadaActivity.class);
                    intent.putExtra("id_prog", listaProgramacao.get(position).getId());
                    activity.startActivity(intent);
                }
            });
            if (listaProgramacao.size() > 0) {
                imageview_lista_vazia.setVisibility(View.GONE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao obter a lista de clientes", Toast.LENGTH_LONG).show();
        }
    }
}
