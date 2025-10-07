package com.dev.util;

import com.dev.dto.SelectVideo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 29/09/2018
 * @version 1
 */
public class JsonToListGson {
    
    public static ArrayList<SelectVideo> convert(String json) {
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<SelectVideo>>() {}.getType();
        ArrayList<SelectVideo> lista = gson.fromJson(json, listType);

        return lista;
    }
    
}
