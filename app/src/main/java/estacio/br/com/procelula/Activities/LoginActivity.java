package estacio.br.com.procelula.Activities;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.Utils.TipoMsg;
import estacio.br.com.procelula.Utils.Utils;
import estacio.br.com.procelula.task.ListaUsuarioTask;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLogin;
    private EditText edtSenha;
    private Button button_entrar;
    private Thread a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final DbHelper db = new DbHelper(this);
        try {
            if (db.contagem("SELECT COUNT(*) TB_LOGIN") > 0) {
                if (db.consulta("SELECT SENHA FROM TB_USUARIOS WHERE LOGIN = '" + db.consulta("SELECT LOGIN FROM TB_LOGIN;", "LOGIN") + "'", "SENHA")
                        .equals(db.consulta("SELECT SENHA FROM TB_LOGIN;", "SENHA"))) {
                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    System.gc();
                    finish();
                } else {
                    Utils.showMessageToast(this, "Deu ruim");
                }
            }
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Nenhum usuario logado!");
        }

        edtLogin = (EditText) findViewById(R.id.edittext_login);
        edtSenha = (EditText) findViewById(R.id.edittext_senha);
        button_entrar = (Button) findViewById(R.id.button_entrar);

        button_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (edtLogin.getText().toString().trim().isEmpty()) {
                        edtLogin.setError("Por favor, informe o seu login");
                    } else {
                        if (db.consulta("SELECT SENHA FROM TB_USUARIOS WHERE LOGIN = '" + edtLogin.getText().toString().trim() + "'", "SENHA").equals(edtSenha.getText().toString().trim())) {
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            Usuario usuario = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE LOGIN = '" + edtLogin.getText().toString().trim() + "'").get(0);
                            db.atualizarLogin(usuario.getId(), usuario.getLogin(), usuario.getSenha(), usuario.getUsuarios_celula_id());
                            db.close();
                            startActivity(intent);
                            finish();
                        } else {
                            if (!edtSenha.getText().toString().trim().isEmpty()) {
                                edtSenha.setError("Senha incorreta");
                                edtSenha.setText("");
                                edtSenha.requestFocus();
                            } else {
                                edtSenha.setError("Por favor, informe sua senha");
                                edtSenha.setText("");
                                edtSenha.requestFocus();
                            }
                        }
                    }
                } catch (CursorIndexOutOfBoundsException e) {
                    Utils.showMsgAlertOK(LoginActivity.this, "ATENÇÃO", "Login não encontrado", TipoMsg.ERRO);
                    edtLogin.setText("");
                    edtLogin.requestFocus();
                }
            }
        });

        a = new Thread(new Runnable() {
            @Override
            public void run() {
                new ListaUsuarioTask(LoginActivity.this).execute();
            }
        });
        a.start();
    }
}