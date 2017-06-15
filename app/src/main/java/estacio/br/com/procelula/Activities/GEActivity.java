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

import estacio.br.com.procelula.Dados.GrupoEvangelistico;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.AdapterDelete;
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

        insereListeners();
        mToolbar = (Toolbar) findViewById(R.id.th_ge);
        mToolbar.setTitle("Grupo Evangelístico");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_grupo_evangelistico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int permissaoUsuario = 0;
//        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            switch (item.getItemId()) {
                case R.id.action_adicionar:
                    android.content.Intent intent = new Intent(this, FormGEActivity.class);
                    startActivityForResult(intent, REQUEST_SALVAR);
                    getListViewGE().setChoiceMode(getListViewGE().getChoiceMode()); //Acerto para cancelar o modo de selecao da lista quando o usuario entra na insercao de ge
                case android.R.id.home:
                    System.gc();
                    finish();
            }
        } else {
            switch (item.getItemId()) {
                case android.R.id.home:
                    System.gc();
                    finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        int permissaoUsuario = 0;
//        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getListViewGE().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListViewGE().setSelected(true);
        }

        getListViewGE().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewGE().getAdapter()).limpaItensSelecionados();
                selectionCounter = 0;
                // TODO Auto-generated method stub

            }


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.action_deletar:
                        return true;
                    default:
                        return false;
                }

            }


            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                if (checked) {
                    selectionCounter++;
                    ((AdapterDelete) getListViewGE().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete) getListViewGE().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1) {
                    mode.setTitle(selectionCounter + " Selecionados");
                } else {
                    mode.setTitle(selectionCounter + " Selecionado");
                }
            }
        });
    }


    private ListView getListViewGE() {
        if (listview_ge == null) {
            listview_ge = (ListView) findViewById(R.id.listview_ge);
        }
        return listview_ge;
    }

    @Override
    protected void onResume() {
        try {
            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            List<GrupoEvangelistico> listaGrupoEvangelistico = db.listaGrupoEvangelistico("SELECT * FROM TB_GES");
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


