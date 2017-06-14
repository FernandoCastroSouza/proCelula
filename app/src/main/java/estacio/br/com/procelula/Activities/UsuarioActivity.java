package estacio.br.com.procelula.Activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import estacio.br.com.procelula.Dados.Celula;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.Dao.UsuarioDAO;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Utils.AdapterDelete;
import estacio.br.com.procelula.Utils.Utils;

public class UsuarioActivity extends AppCompatActivity {
    public static final int REQUEST_SALVAR = 1;
    private static final String STATE_LISTA_USUARIOS = "STATE_LISTA_USUARIOS";

    private ImageView imageViewListaVazia;
    private ListView listview_usuarios;
    private Toolbar mToolbar;
    private FloatingActionButton addUsuario;
    private ArrayList<Usuario> mListaUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        if (savedInstanceState == null) {
            new PopulaUsuariosTask().execute(getSPCelula());
        } else {
            if (savedInstanceState.get(STATE_LISTA_USUARIOS) != null) {
                mListaUsuarios = (ArrayList<Usuario>) savedInstanceState.get(STATE_LISTA_USUARIOS);
                getListViewUsuario().setAdapter(new AdapterDelete<Usuario>(this, mListaUsuarios));
            }
        }
        insereListeners();
        mToolbar = (Toolbar) findViewById(R.id.th_usuario);
        mToolbar.setTitle("Usuarios");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int permissaoUsuario = 0;
        try {
            permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (permissaoUsuario == 0) {
            getAddUsuario().setVisibility(View.GONE);
        } else {
            getAddUsuario().setVisibility(View.VISIBLE);

        }

        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_usuario);
            assert fab != null;
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(UsuarioActivity.this, FormUsuarioActivity.class);
                    startActivityForResult(i, REQUEST_SALVAR);
                    getListViewUsuario().setChoiceMode(getListViewUsuario().getChoiceMode()); //Acerto para cancelar o modo de selecao da lista quando o usuario entra na insercao de usuarios
                }
            });
        }


    }

    private Celula getSPCelula() {
        return Utils.retornaCelulaSharedPreferences(this);
    }

    @Override
    public void onSaveInstanceState(Bundle estadoDeSaida) { //TODO fazer tratamento de giro de tela nas outras telas
        super.onSaveInstanceState(estadoDeSaida);
        if (getListViewUsuario().getAdapter() != null) {
            estadoDeSaida.putSerializable(STATE_LISTA_USUARIOS, mListaUsuarios);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SALVAR && resultCode == RESULT_OK) {
            new PopulaUsuariosTask().execute(getSPCelula());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adicionar:
                Intent intent = new Intent(this, FormUsuarioActivity.class);
                startActivityForResult(intent, REQUEST_SALVAR);
                getListViewUsuario().setChoiceMode(getListViewUsuario().getChoiceMode()); //Acerto para cancelar o modo de selecao da lista quando o usuario entra na insercao de usuarios

            case android.R.id.home:
                System.gc();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void insereListeners() {
        int permissaoUsuario = 0;
        permissaoUsuario = Integer.parseInt(Utils.retornaSharedPreference(this, LoginActivity.PERMISSAO_SP, "0"));

//        getListViewUsuario().setOnItemClickListener(this);
        if (permissaoUsuario == Usuario.PERMISSAO_LIDER || permissaoUsuario == Usuario.PERMISSAO_PASTOR) {
            getListViewUsuario().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            getListViewUsuario().setSelected(true);
        }

        getListViewUsuario().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            int selectionCounter;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((AdapterDelete) getListViewUsuario().getAdapter()).limpaItensSelecionados();
                selectionCounter = 0;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_deletar:
//                        Utils.showMessageConfirm(UsuarioActivity.this, "Remover usuario", "Deseja realmente remover esse usuario?", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new RemoveUsuarioTask(
//                                        ((AdapterDelete<Usuario>) getListViewUsuario().getAdapter()).getItensSelecionados(),
//                                        new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                mode.finish();
//                                            }
//                                        }
//                                ).execute();
//                            }
//                        });

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
                    ((AdapterDelete) getListViewUsuario().getAdapter()).selectedItem(position, position);

                } else {
                    selectionCounter--;
                    ((AdapterDelete) getListViewUsuario().getAdapter()).removeSelection(position);
                }
                if (selectionCounter > 1) {
                    mode.setTitle(selectionCounter + " Selecionados");
                } else {
                    mode.setTitle(selectionCounter + " Selecionado");
                }
            }
        });
    }

    private ListView getListViewUsuario() {
        if (listview_usuarios == null) {
            listview_usuarios = (ListView) findViewById(R.id.usuarioslist);
        }
        return listview_usuarios;
    }

    private FloatingActionButton getAddUsuario() {
        if (addUsuario == null) {
            addUsuario = (FloatingActionButton) findViewById(R.id.add_usuario);
        }
        return addUsuario;
    }

    private ImageView getImageViewListaVazia() {
        if (imageViewListaVazia == null) {
            imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        }
        return imageViewListaVazia;
    }


    private class PopulaUsuariosTask extends AsyncTask<Celula, Void, Integer> {//desisto kkkk ja fiz bastante arruma ai pra nois //kkkkk blz
        private final int RETORNO_SUCESSO = 0; //
        private final int FALHA_SQLEXCEPTION = 1; // provavel falha de conexao
        private ProgressDialog progressDialog;

        public PopulaUsuariosTask() {
            mListaUsuarios = new ArrayList<Usuario>();
        }

        @Override
        protected Integer doInBackground(Celula... celulas) {
            try {
                if (getSPCelula() != null) {
                    mListaUsuarios = new UsuarioDAO().retornaUsuarios(celulas[0]);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return FALHA_SQLEXCEPTION;
            }
            return RETORNO_SUCESSO;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(UsuarioActivity.this, "Carregando usuarios", "Aguarde por favor...", true);
        }

        @Override
        protected void onPostExecute(Integer resultadoUsuario) {
            progressDialog.dismiss();
            switch (resultadoUsuario) {
                case RETORNO_SUCESSO:
                    if (mListaUsuarios.size() > 0) {
                        getImageViewListaVazia().setVisibility(View.GONE);
                        getListViewUsuario().setVisibility(View.VISIBLE);
                    } else {
                        getImageViewListaVazia().setVisibility(View.VISIBLE);
                        getListViewUsuario().setVisibility(View.GONE);
                    }
                    getListViewUsuario().setAdapter(new AdapterDelete<Usuario>(getApplicationContext(), mListaUsuarios));
                    break;
                case FALHA_SQLEXCEPTION:
                    Utils.mostraMensagemDialog(UsuarioActivity.this, "Não foi possível carregar os usuarios. Verifique sua conexão e tente novamente.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    break;
            }
            super.onPostExecute(resultadoUsuario);
        }
    }


}


