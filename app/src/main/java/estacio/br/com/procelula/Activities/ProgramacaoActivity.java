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
import android.widget.ListView;

import java.util.List;

import estacio.br.com.procelula.Dados.Aviso;
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
    private Toolbar mToolbar;
    private int celulaid = 0;
    final DbHelper db = new DbHelper(this);
    private Thread a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        listview_programacoes = (ListView) findViewById(R.id.listview_programacoes);

        insereListeners();

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

    private void insereListeners() {
        int permissaoUsuario = 0;
//        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));

        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            listview_programacoes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listview_programacoes.setSelected(true);
        }

        listview_programacoes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) listview_programacoes.getAdapter()).limpaItensSelecionados();
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
                    ((AdapterDelete) listview_programacoes.getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete) listview_programacoes.getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1) {
                    mode.setTitle(selectionCounter + " Selecionados");
                } else {
                    mode.setTitle(selectionCounter + " Selecionado");
                }

            }
        });
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