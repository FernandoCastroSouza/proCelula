package estacio.br.com.procelula.Activities;


import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;
import estacio.br.com.procelula.task.ListaUsuarioTask;


public class AniversariantesActivity extends AppCompatActivity {


    private ListView listview_aniversariantes;
    private Toolbar mToolbar;
    private ImageView imageViewListaVazia;
    private int celulaid;
    final DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);

        imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        listview_aniversariantes = (ListView) findViewById(R.id.listview_aniversariantes);

        mToolbar = (Toolbar) findViewById(R.id.th_aniversariante);
        mToolbar.setTitle("Aniversariantes");
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
            List<Usuario> listaUsuario = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + celulaid + ";");
            for (int i = 0; i < listaUsuario.size(); i++) {
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date data = formatoEntrada.parse(listaUsuario.get(i).getNascimento());
                    Date dataMes = new Date();
                    if (data.getMonth() != dataMes.getMonth()) {
                        listaUsuario.remove(i);
                    }
                } catch (ParseException | NullPointerException e) {
                    System.out.println(e.getMessage());
                    listaUsuario.remove(i);
                }
            }
            if (listaUsuario.size() > 0) {
                ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, listaUsuario);

                listview_aniversariantes.setAdapter(adapter);
                listview_aniversariantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        Usuario usuario = (Usuario) adapterView.getItemAtPosition(position);
                        switch (adapterView.getId()) {
                            case R.id.lstAvisos:
                                Utils.showMsgAlertOK(AniversariantesActivity.this, usuario.getNome(), usuario.getNascimento(), TipoMsg.INFO);
                                break;
                        }
                    }
                });
            } else {
                imageViewListaVazia.setVisibility(View.VISIBLE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela aniversariantes vazia!");
            imageViewListaVazia.setVisibility(View.VISIBLE);
        }
        new ListaUsuarioTask(AniversariantesActivity.this).execute();
        super.onResume();
    }
}
