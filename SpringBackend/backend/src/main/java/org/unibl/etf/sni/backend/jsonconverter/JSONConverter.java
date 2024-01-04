package org.unibl.etf.sni.backend.jsonconverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONConverter {

    public static String convertObjectToString(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
