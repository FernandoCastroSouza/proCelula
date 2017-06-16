package estacio.br.com.procelula.Activities;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import estacio.br.com.procelula.Dados.Programacao;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.AdapterDelete;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;
import estacio.br.com.procelula.task.ListaProgramacaoTask;


public class ProgramacaoActivity extends AppCompatActivity {
    public static final int REQUEST_SALVAR = 1;
    public static final String PROGRAMACAO_SELECIONADA = "programacao_selecionada";
    private ListView listview_programacoes;
    private ImageView imageview_lista_vazia;
    private Toolbar mToolbar;
    private int celulaid = 0;
    final DbHelper db = new DbHelper(this);
    private Thread a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        listview_programacoes = (ListView) findViewById(R.id.listview_programacoes);

        mToolbar = (Toolbar) findViewById(R.id.th_programacao);
        mToolbar.setTitle("Programações");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_programacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adicionar:
                Intent intent = new Intent(this, FormProgramacaoActivity.class);
                startActivityForResult(intent, REQUEST_SALVAR);
            case android.R.id.home:
                System.gc();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        try {
            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            List<Programacao> listaProgramacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE PROGRAMACOES_CELULA_ID = " + celulaid);
            ArrayAdapter<Programacao> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listaProgramacao);

            listview_programacoes.setAdapter(adapter);
            listview_programacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Programacao programacao = (Programacao) adapterView.getItemAtPosition(position);
                    switch (adapterView.getId()) {
                        case R.id.lstAvisos:
                            Utils.showMsgAlertOK(ProgramacaoActivity.this, programacao.getNome(), "PROGRAMAÇÃO", TipoMsg.INFO);
                            break;
                    }
                }
            });
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
            imageview_lista_vazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                new ListaProgramacaoTask(ProgramacaoActivity.this, celulaid).execute();
            }
        });
        a.start();
        super.onResume();
    }
}