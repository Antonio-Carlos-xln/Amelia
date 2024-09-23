package com.antoniocarlos.amelia;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for encapsulating the nesting of lists and objects and the naming resolving procedures.
 */
public class UnionListMap{
  /**
   *  Whether the current instance is a map o a list. 
   */
  protected boolean isMap;
  /**
   * Internal map if this instance is a map, null otherwise.
   */
  protected Map<String,Object> map;
  /**
   * Internal list if this instance is a list, null otherwise.
   */
  protected List<Object> list;
  /**
   * Internal keyResolver instance for resolve namings according to the required naming convention.
   */
  protected KeyResolver kr;
  /**
   * Enum for telling the constructor what kind of collection should be chosen internally.
   */
  enum Type{
    LIST,
    MAP
  }
  /**
   * Default constructor
   *
   * @param type an enum elemen representing the desired type of the internal collection.
   * @param kr a keyResolver instance that will be used to resolve all naming.
   */
  UnionListMap(Type type,KeyResolver kr){
    this.kr = kr;
    this.isMap = type == Type.MAP;
    if(isMap){
      this.map = new HashMap<String,Object>();
      this.list = null;
    }else{
      this.list = new ArrayList<Object>();
      this.map = null;
    }
  }
  
  /**
   * Helper static method for constructing this collection from an external map.
   *
   * @param map the given map to be used.
   * @param kr the KeyResolver that will be used.
   * @return the new instance.
   */
  public static UnionListMap fromMap(Map<String,Object> map, KeyResolver kr){
    UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,kr);
    map
      .entrySet()
      .stream()
      .forEach(e -> u.add(e.getKey(),e.getValue()));
    return u;
  }
  /**
   * Helper static method for constructing this collection from an external list.
   *
   * @param list the given list to be used.
   * @param kr the KeyResolver that will be used.
   * @return the new instance.
   */
  public static UnionListMap fromList(List<Object> l,  KeyResolver kr){
    UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,kr);
    l
      .stream()
      .forEach(e -> u.add("$_a",e));  
    return u;
  }
  
  /**
   * The toString implementation that describes how to convert this class to string.
   * The right way of doing so is by delegating the method invokation to the underlying collection.
   *
   * @return the String representation of this instance.
   */  
  @Override
  public String toString(){
    if(isMap){
      return this.map.toString();
    }else{
      return this.list.toString();
    }
  }
  
  /**
   * The "equals" implementation that describes how to check equality with this class.
   * The right way of doing so is by delegating the method invokation to the underlying collection.
   *
   * @return whether the objects are equal or not.
   */  
  @Override
  public boolean equals(Object o){
    if(isMap){
      return this.map.equals(o);
    }else{
      return this.list.equals(o);
    }
  }

  /**
   * This method tells whether the current instance is a map or not.
   *
   * @return whether the object is a map or not.
   */  
  public boolean isMap(){
    return this.isMap;
  }

  /**
   * Returns the current underlying map or null, if this instance is not a map.
   *
   * @return The underlying map or null.
   */  
  public  Map getMap(){
    if(isMap){
      return this.map;
    }
    return null;
  }
  
  /**
   * Returns the current underlying list or null, if this instance is not a list.
   *
   * @return The underlying map or null.
   */  
  public List getList(){
    if(!isMap){
      return this.list;
    }
    return null;
  }
  /**
   * Adds an element to the underlying collection.
   *
   * @param key where to put the element.
   * @param val element that should be added.
   * @throws IllegalKeyException if at any moment a misuse of the naming conventions for lists and objects happens.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any attempt to retrieve a value from outside the bounds of the list occurs.
   *
   */
  public void add(String key,Object val){
    boolean isElement = !kr.isContainer(key);
    boolean canResolve = kr.canResolve(key);
    boolean canResolveIndex = kr.canResolveIndex(key);
    if(isMap){
      if(canResolve){
        if(isElement){
          throw new IllegalKeyTypeException("Key: "+key+" passed attempts to numerically place element. Illegal Operation");
        }
        String cleanKey = kr.cleanKey(key);
        UnionListMap u = new UnionListMap(Type.LIST,kr);
        u.add("$_a",val);
        this.map.put(cleanKey,u);
        return;
      }
      this.map.put(key,val);
    }else{
      if(!canResolve){
        throw new IllegalKeyTypeException("Key: "+key+" attemps to namely place a value in a list. Illegal Operation"); 
      }
      if(!canResolveIndex){
        this.list.add(val);
        return;
      }
      try{
        int index = kr.resolveIndex(key);
        this.list.add(index,val);
      }catch(Exception e){}       
      
    }
  }
  /**
   * Checks if a certain key is present in the underlying collection.
   *
   * @param key what to check for.
   *
   */
  public boolean contains(String key){
    boolean isElement = !kr.isContainer(key);
    boolean canResolve = kr.canResolve(key);
    boolean canResolveIndex = kr.canResolveIndex(key);
    if(isMap){
      if(canResolveIndex){
        return false;
      }
      String keyClean = key;
      if(canResolve){
        keyClean = kr.cleanKey(key);
      }
      return this.map.containsKey(keyClean);
    }else{
      if(!canResolve){
        return false;     
      }
      if(!isElement || !canResolveIndex){
        return false;
      }
      int index = kr.resolveIndex(key);
      if(index < 0 || index >= list.size()){
        return false;
      }
      return list.get(index) != null;
    }
  }
  /**
   * Retrives a certain element of the underlying collection, if this is a list-based instance, null otherwise.
   *
   * @param index what to retrieve.
   * @throws IndexOutOfBoundsException if it attempts to retrieve a value from outside the bounds of the list.
   *
   */
  public Object get(int index){
    if(!isMap){
      return this.list.get(index);
    }
    throw new IllegalKeyTypeException("Key: "+index+" Attempts to numerically acess map. Illegal Operation");
  }
  
  /**
   * Retrieves an element from the underlying collection.
   *
   * @param key what to look for.
   * @throws IllegalKeyException if at any moment a misuse of the naming conventions for lists and objects happens.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any attempt to retrieve a value from outside the bounds of the list occurs.
   *
   */
 public Object get(String key){
   boolean isElement = !kr.isContainer(key);
   boolean canResolve = kr.canResolve(key);
   boolean canResolveIndex = kr.canResolveIndex(key);
   if(isMap){
     if(canResolve && isElement){
       throw new IllegalKeyTypeException("Key: "+key+" Attempts to numerically acess map. Illegal Operation");
     }
     String cleanKey = key;
     if(canResolve){
       cleanKey = kr.cleanKey(key);
     }
     return this.map.get(cleanKey);
     
   }else{
     if(!canResolve){
       throw new IllegalKeyTypeException("Key: "+key+" attempts to namelly acess element of a list")    ;
     }
     int index = kr.resolveIndex(key);
     return this.list.get(index);
     }
 }
   /**
   * Removes an element from the underlying collection.
   *
   * @param key what to look for.
   * @throws IllegalKeyException if at any moment a misuse of the naming conventions for lists and objects happens.
   * @throws NullPointerException if any of the ids attempt to retrieve abscent key from an object.
   * @throws IndexOutOfBoundsException if any attempt to retrieve a value from outside the bounds of the list occurs.
   *
   */ 
  public void remove(String key){
    boolean isElement = !kr.isContainer(key);
    boolean canResolve = kr.canResolve(key);
    boolean canResolveIndex = kr.canResolveIndex(key);
    if(isMap){
      if(canResolve && isElement){
        throw new IllegalKeyTypeException("Key: "+key+" Attempts to numerically acess map. Illegal Operation");
      }
      String cleanKey = key;
      if(canResolve){
        cleanKey = kr.cleanKey(key);
      }
      this.map.remove(cleanKey);      
    }else{
      if(!canResolve || !canResolveIndex){
        throw new IllegalKeyTypeException("Key: "+key+" does not provide index to retrieve from list");
      }
      int index = kr.resolveIndex(key);
      this.list.remove(index);
    }
  }
  /**
   * Clears te underlying collection.
   */
  void clear(){
    if(isMap){
      this.map.clear();
    }else{
      this.list.clear();
    }
  }
}