package com.antoniocarlos.amelia;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * A simple Gson custom serializer for the UnionListmap class.
 */
public class UnionListMapJsonSerializer implements JsonSerializer<UnionListMap> {
    /**
     * Required method for tel how Gson should serialize an object of this class.
     *
     * @param union the union element currently being visited by the Gson serializer.
     * @param typeOfSrc represents the type of the Source element.
     * @param context the context of serialization that control the serialization of objects and properties and
     * has the defined configurations for the whole serialization process.
     * @return a json element representing the data contained at the UnionListMap instance. 
     */
    @Override
    public JsonElement serialize(UnionListMap union, Type typeOfSrc, JsonSerializationContext context) {
      if(union.isMap()){
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<Object, Object> entry : ((Map<Object, Object>)union.getMap()).entrySet()){
          String key = (String)entry.getKey();
          Object value = entry.getValue();
          if(value instanceof UnionListMap){
            jsonObject.add(key, context.serialize(value));
            continue;
          }
          if(value instanceof Boolean){
            jsonObject.addProperty(key,(Boolean) value);
            continue;
          }
          if(value instanceof Integer){
            jsonObject.addProperty(key,(Integer) value);
            continue;
          }
          if(value instanceof Double){
            jsonObject.addProperty(key,(Double) value);
            continue;
          }
          if(value instanceof String){
            jsonObject.addProperty(key,(String) value);
            continue;
          }
        }
        return jsonObject;
      }else{
        JsonArray jarray = new JsonArray();
        for(Object element : union.getList()){
          if(element instanceof UnionListMap){
            jarray.add(context.serialize(element));
            continue;
          }
          jarray.add(context.serialize(element));
        }
        return jarray;
      }
  }
}
