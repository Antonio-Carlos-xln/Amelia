package com.antoniocarlos.amelia;

import java.util.Map;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Concrete implementation of Delegate (@see Delegate). After processing the input document, it produces an string containing a json document.
 * This class holds an instance of a baseDelegate, it mostly processies the events between the listener and this base delegate instance and at the end of the input converts the value to json.
 */
public class JSONDelegate implements Delegate<String>{
  /**
   * Holds the final json document.
   */
  String outputJsonData;
  /**
   * BaseDelegate instance meant to do the processing of the data.
   */
  Delegate<UnionListMap> handler;
  /**
   * Default Constructor.
   */
  public JSONDelegate(){
    this.handler = new BaseDelegate();
  }
 /**
  * onLet implementation. @see Delegate#onLet.  
  * This implementation only invokes the same method of the BaseDelegate.
  * 
  * @param ids a list of individual ids.
  * @param propsI the collection of all passed props.
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  @Override
  public void onLet(List<String> ids, UnionListMap props){
    this.handler.onLet(ids, props);
  }
  
 /**
  * onEntry implementation. @see Delegate#onEntry.
  * This implementation only invokes the same method of the BaseDelegate.
  * 
  * @param propsI the collection of all passed props.
  * @param ids a list of individual ids.
  * @param valI a value to be inserted at the currently being-build props.
  * @param refVal boolean that indicates whether or not the given value is a reference.
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  @Override
  public void onEntry(UnionListMap props, List<String> ids, Object val, boolean refVal){
    this.handler.onEntry(props, ids, val, refVal);
  }
 /**
  * onSet implementation. @see Delegate#onSet.
  * This implementation only invokes the same method of the BaseDelegate.
  * 
  * @param ids a list of individual ids.
  * @param valI the value to set the given property.
  * @param refVal whether or not the current val is a reference to other property of some object (including the global object).
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  @Override
  public void onSet(List<String> ids, Object val, boolean refVal){
    this.handler.onSet(ids, val, refVal);
  }
 /**
  * onDelete implementation. @see Delegate#onDelete.
  * This implementation only invokes the same method of the BaseDelegate.
  * 
  * @param ids a list of individual ids.
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  @Override
  public void onDelete(List<String> ids){
    this.handler.onDelete(ids);
  }
 /**
  * onEndOfInput implementation. @see Delegate#onEndOfInput.
  * This implementation only invokes the same method of the BaseDelegate.
  */
  @Override
  public void onEndOfInput(){
    this.handler.onEndOfInput();
  }
 /**
  * getData implementation. @see Delegate#getData.
  * Uses theGson library to build a json document out of the global object of the BaseDelegate.
  * @param configs a map with key value pairs to be used to postprocess the output data.
  * @return the json document string.
  */
  @Override
  public String getData(Map<String, Object> config){
    Gson gson = new GsonBuilder()
                      .registerTypeAdapter(UnionListMap.class, new UnionListMapJsonSerializer())
                      .create();
    this.outputJsonData = gson.toJson(this.handler.getData(config));
    return this.outputJsonData;
  }
}
