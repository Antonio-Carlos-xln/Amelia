package com.antoniocarlos.amelia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;
/**
 * Concrete delegate implementation (@see Delegate). Process the input document and returns a nesd data structure (@see UnionListMap).
 */
public class BaseDelegate implements Delegate<UnionListMap>{
 /**
  * Top level collection, always a map instance.
  */
  protected UnionListMap global;
  /**
   * key resolver that allows names to be resolved following the amelia language conventions for maps and lists.
   */
  protected KeyResolver kr;
  /**
   * Defaut Constructor. 
   */
  public BaseDelegate(){
    kr = new DollarSignKeyResolver();
    global = new UnionListMap(UnionListMap.Type.MAP, kr);
  }
  /**
   * onLet implementation. @see Delegate#onLet.
   * 
   * @param ids a list of individual ids.
   * @param propsI the collection of all passed props.
   * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
   */
  @Override
  public void onLet(List<String> ids, UnionListMap propsI){
    UnionListMap props = UnionListMap.fromMap(DeepCopy.ofMap(propsI.getMap()),kr);
    acceptValue(ids, global, props);
  }
 /**
  * onEntry implementation. @see Delegate#onEntry.
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
  public void onEntry(UnionListMap props, List<String> ids, Object valI,boolean refVal){
    Object val  = valI;
    if(refVal){
      val = processValueID((String)val);
    }
    acceptValue(ids,props,val);
  }
 
  /**
   * Helper method that sets or creaets a property at any element (including the global one).
   * 
   * @param ids a list of individual ids.
   * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
   */  
  private void acceptValue(List<String> idsI, UnionListMap local,Object val){
    if(idsI.size() > 1){
      List<String> ids = new ArrayList<String>(idsI);
      Collections.reverse(ids);
      UnionListMap t = local;
      boolean isIndex = false;
      boolean contains = false;
      boolean last = false;
      boolean isMap = false;
      String key = null;
      for(int c = 0;c<ids.size();c++){
       key = ids.get(c);
       last = c == (ids.size() - 1);
       if(last){
         t.add(key,val);         
         continue;
       }
       if(t.contains(key)){
         t = (UnionListMap)t.get(key);
       }else{
        if(kr.canResolve(key)){
          if(kr.isContainer(key)){
           UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,kr);
           key = kr.cleanKey(key);
           t.add(key,u);
           t = u;
          }else{
           UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,kr);
           t.add(key,u);
           t = u;
          }
        }else{
          UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,kr);
          t.add(key,u);
          t = u;
        }
       }
      }
    }else{
      local.add(idsI.get(0),val);
    }
  }
 
 /**
  * helper method that removes a property from any element (including the global one).
  * 
  * @param ids a list of individual ids.
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  private void removeAcceptedValue(List<String> ids,UnionListMap local){
    if(ids.size() > 1){
      Collections.reverse(ids);
      UnionListMap t = local;
        for(int c = 0;c<ids.size() - 1;c++){
          t = (UnionListMap) t.get(ids.get(c));
        }
      t.remove(ids.get(ids.size() - 1));
    }else{
      String key = ids.get(0);
      local.remove(key);
    }
  }

 /**
  * Helper method for processing reference values, ones that hold IDs that point to the actual data.
  * 
  * @param key a single (possibly composed) ID string.
  * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
  * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
  * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
  */
  private Object processValueID(String key){
    List<String> ids = Arrays.asList(key.split("of"))
      .stream()
      .map(s -> s.trim())
      .collect(Collectors.toList());
    if(ids.size() > 1){
      Collections.reverse(ids);
      UnionListMap t = global;
      for(int c =0 ; c < ids.size() - 1; c++){
        t = (UnionListMap) t.get(ids.get(c));
      }
      return t.get(ids.get(ids.size() -1));
    }else{
      return global.get(ids.get(0));
    }
  }

 /**
   * onSet implementation. @see Delegate#onSet.
   * 
   * @param ids a list of individual ids.
   * @param valI the value to set the given property.
   * @param refVal whether or not the current val is a reference to other property of some object (including the global object).
   * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
   */
  @Override
  public void onSet(List<String> ids, Object valI, boolean refVal){
    Object val = valI;
    if(refVal){
      val = processValueID((String)val);
    }
    acceptValue(ids,global,val);
  }
  
  /**
   * onDelete implementation. @see Delegate#onDelete.
   * 
   * @param ids a list of individual ids.
   * @throws IllegalKeyException if at any moment a statement misuses the naming conventions for lists and objects.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any statement attempts to retrieve a value from outside the bounds of any list.
   */
  @Override
  public void onDelete(List<String> ids){  
    removeAcceptedValue(ids,global);
  }

 /**
   * onEndOfInput implementation. @see Delegate#onEndOfInput.
   */
  @Override
  public void onEndOfInput(){}

 /**
   * getData implementation. @see Delegate#getData.
   * 
   * @param configs a map with key value pairs to be used to postprocess the output data.
   * @return the unionlistmap instance that holds all of the data after all statements.
   * 
   */
  @Override
  public UnionListMap getData(Map<String, Object> config){
    return this.global;
  }
 
 /**
   * Utility class for deep copying data structures.
   */
  class DeepCopy {
   
    /**
     * Static method for deep copying a map.
     * @param map the map to be deep copyied.
     * @return the deep copy.
     *
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> ofMap(Map<String, Object> original) {
        Map<String, Object> copy = new HashMap<>();

        for (Map.Entry<String, Object> entry : original.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof Map) {
                copy.put(key, ofMap((Map<String, Object>) value));
            } else if (value instanceof ArrayList) {
                copy.put(key, ofArrayList((ArrayList<Object>) value));
            } else if (value instanceof String || value instanceof Number || value instanceof Boolean || value instanceof Character) {
                copy.put(key, value);
            } else {
                throw new UnsupportedOperationException("Unsupported type: " + value.getClass());
            }
        }
        return copy;
        }
   
     /**
      * Static method for deep copying a list.
      * @param list the list to be deep copyied.
      * @return the deep copy.
      *
      */
      private static ArrayList<Object> ofArrayList(ArrayList<Object> original) {
        ArrayList<Object> copy = new ArrayList<>();
        
        for (Object item : original) {
            if (item instanceof Map) {
                copy.add(ofMap((Map<String, Object>) item));
            } else if (item instanceof ArrayList) {
                copy.add(ofArrayList((ArrayList<Object>) item));
            } else if (item instanceof String || item instanceof Number || item instanceof Boolean || item instanceof Character) {
                copy.add(item);
            } else {
                throw new UnsupportedOperationException("Unsupported type in ArrayList: " + item.getClass());
            }
        }
        return copy;
      }
  }
}