package estacio.br.com.procelula.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Usuario;

public class UsuarioConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Usuario fromJson(JSONObject jsonObject) {
        Usuario obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Usuario.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Usuario> fromJson(JSONArray jsonArray) {
        List<Usuario> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Usuario obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
