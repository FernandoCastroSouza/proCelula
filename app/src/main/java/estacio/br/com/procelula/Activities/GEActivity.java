package estacio.br.com.procelula.Activities;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;
import estacio.br.com.procelula.task.ListaGrupoEvangelisticoTask;

public class GEActivity extends AppCompatActivity {
    public static final int REQUEST_SALVAR = 1;
    private ListView listview_ge;
    private ImageView imageview_lista_vazia;
    private Toolbar mToolbar;
    private Thread a;
    private int celulaid;
    final DbHelper db = new DbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge);

        listview_ge = (ListView) findViewById(R.id.listview_ge);
        mToolbar = (Toolbar) findViewById(R.id.th_ge);
        mToolbar.setTitle("Grupo Evangelístico");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            List<GrupoEvangelistico> listaGrupoEvangelistico = db.listaGrupoEvangelistico("SELECT * FROM TB_GES WHERE GES_CELULA_ID = " + celulaid + ";");
            ArrayAdapter<GrupoEvangelistico> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listaGrupoEvangelistico);

            listview_ge.setAdapter(adapter);
            listview_ge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    GrupoEvangelistico grupoEvangelistico = (GrupoEvangelistico) adapterView.getItemAtPosition(position);
                    switch (adapterView.getId()) {
                        case R.id.lstAvisos:
                            Utils.showMsgAlertOK(GEActivity.this, grupoEvangelistico.getNome(), "PROGRAMAÇÃO", TipoMsg.INFO);
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
                new ListaGrupoEvangelisticoTask(GEActivity.this).execute();
            }
        });
        a.start();
        super.onResume();
    }
}


