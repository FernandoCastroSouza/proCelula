package estacio.br.com.procelula.Activities;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Aviso;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;
import estacio.br.com.procelula.task.ListaAvisoTask;

public class AvisoActivity extends AppCompatActivity {

    private ListView lstAvisos;
    private Toolbar mToolbar;
    private FloatingActionButton addAviso;
    final DbHelper db = new DbHelper(this);
    private Thread a;
    private int celulaid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        mToolbar = (Toolbar) findViewById(R.id.th_aviso);
        mToolbar.setTitle("Avisos");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstAvisos = (ListView) findViewById(R.id.lstAvisos);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            List<Aviso> avisosLst = db.listaAviso("SELECT * FROM TB_AVISOS WHERE AVISOS_CELULA_ID = " + celulaid);
            ArrayAdapter<Aviso> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, avisosLst);

            lstAvisos.setAdapter(adapter);
            lstAvisos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Aviso avisoSelecionado = (Aviso) adapterView.getItemAtPosition(position);
                    switch (adapterView.getId()) {
                        case R.id.lstAvisos:
                            Utils.showMsgAlertOK(AvisoActivity.this, avisoSelecionado.getTitulo(), avisoSelecionado.getConteudo(), TipoMsg.INFO);
                            break;
                    }
                }
            });
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
        }
        a = new Thread(new Runnable() {
            @Override
            public void run() {
                new ListaAvisoTask(AvisoActivity.this, celulaid).execute();
            }
        });
        a.start();
        super.onResume();
    }
}


