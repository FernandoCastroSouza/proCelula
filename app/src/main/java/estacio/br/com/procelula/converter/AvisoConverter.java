package estacio.br.com.procelula.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import estacio.br.com.procelula.Dados.Aviso;

public class AvisoConverter {

    private ObjectMapper mapper = new ObjectMapper();

    public Aviso fromJson(JSONObject jsonObject) {
        Aviso obj;
        try {
            obj = mapper.readValue(jsonObject.toString(), Aviso.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    public List<Aviso> fromJson(JSONArray jsonArray) {
        List<Aviso> list = new ArrayList<>();
        JSONObject objectJson;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                objectJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Aviso obj = fromJson(objectJson);
            if (obj != null) {
                list.add(obj);
            }
        }
        return list;
    }
}
