package estacio.br.com.procelula.Activities;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;


public class CelulaActivity extends AppCompatActivity{

    private TextView nome;
    private TextView lider;
    private ImageView foto;
    private TextView dia;
    private TextView horario;
    private TextView local;
    private TextView semana;
    private TextView periodo;
    private TextView versiculo;

    private Celula celula;
    private Toolbar mToolbar;
    private Thread a;
    private int celulaid = 0;
    final DbHelper db = new DbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celula);

        mToolbar = (Toolbar) findViewById(R.id.th_celula);
        mToolbar.setTitle("Célula");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome  = (TextView) findViewById(R.id.nome);
        lider = (TextView) findViewById(R.id.lider);
        dia  = (TextView) findViewById(R.id.dia);
        horario = (TextView) findViewById(R.id.horario);
        local  = (TextView) findViewById(R.id.local);
        semana = (TextView) findViewById(R.id.semana);
//        periodo  = (TextView) findViewById(R.id.periodo);
        versiculo = (TextView) findViewById(R.id.versiculo);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editar:
                Intent intent = new Intent(this, CelulaEditarActivity.class);
                startActivity(intent);
            case android.R.id.home:
                System.gc();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {

        try {
            celulaid = Integer.parseInt(db.consulta("SELECT FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            Celula celula = db.listaCelula("SELECT * FROM TB_CELULAS WHERE ID = " + celulaid).get(0);

//            getNome().setText(celula.getNome());
//            getLider().setText(celula.getLider());
//            getDia().setText(celula.converteDiaCelula());
//            getHorario().setText(celula.getHorario());
//            getLocal().setText(celula.getLocal_celula());
//            getSemana().setText(celula.converteDiaJejum() + " - " + celula.getPeriodo()); //TODO relacionar numeros com dias da semana
//            getVersiculo().setText("\"" + celula.getVersiculo() + "\"");

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
        }
        a = new Thread(new Runnable() {
            @Override
            public void run() {
//                new ListaAvisoTask(CelulaActivity.this, celulaid).execute();
            }
        });
        a.start();
        super.onResume();
    }

}