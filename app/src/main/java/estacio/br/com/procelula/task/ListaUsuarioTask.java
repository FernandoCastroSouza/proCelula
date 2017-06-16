package estacio.br.com.procelula.task;


import android.app.Activity;
import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import estacio.br.com.procelula.Activities.AniversariantesActivity;
import estacio.br.com.procelula.Activities.LoginActivity;
import estacio.br.com.procelula.Dados.Usuario;
import estacio.br.com.procelula.R;
import estacio.br.com.procelula.Repository.DbHelper;
import estacio.br.com.procelula.converter.UsuarioConverter;
import estacio.br.com.procelula.ws.WebService;

public class ListaUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final Activity activity;
    private ProgressDialog alert;
    private ListView listview_aniversariantes;
    private ImageView imageViewListaVazia;
    private DbHelper db;

    public ListaUsuarioTask(Activity activity) {
        this.activity = activity;
        try {
            db = new DbHelper(activity);
            listview_aniversariantes = (ListView) activity.findViewById(R.id.listview_aniversariantes);
            imageViewListaVazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    protected void onPreExecute() {
        DbHelper dao = new DbHelper(activity);
        if (dao.contagem("SELECT COUNT(*) FROM TB_USUARIOS") <= 0) {
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
            String jsonResult = request.listAll("usuarios");
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Usuario> usuarios = new UsuarioConverter().fromJson(jsonArray);
            if (usuarios != null && !usuarios.isEmpty()) {
                DbHelper dao = new DbHelper(activity);
                for (int i = 0; i < usuarios.size(); i++) {
                    dao.atualizarUsuario(usuarios.get(i));
                }
                dao.close();
            } else {
                System.out.println("O objeto acabou ficando vazio!");
                return false;
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
                ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaUsuario);
                listview_aniversariantes.setAdapter(adapter);
                imageViewListaVazia.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            try {
                imageViewListaVazia.setVisibility(View.VISIBLE);
            } catch (Exception e1) {
                System.out.println("imageViewListaVazia vazia");
            }
        }
        if (!statusOK) {
            Toast.makeText(activity, "Você não esta conectado a internet", Toast.LENGTH_LONG).show();
        }
    }
}
