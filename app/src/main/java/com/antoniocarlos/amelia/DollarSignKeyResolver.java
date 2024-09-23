package com.antoniocarlos.amelia;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Concrete implementation of KeyResolver (@see KeyResolver). This follows rencapsulates the naming convention for lists and maps in the Amelia language.
 *
 */
class DollarSignKeyResolver implements KeyResolver{
  /**
   * Pattern for identifying an list index element.
   */
  private static final Pattern hasIndexAndIdentifier = Pattern.compile("^\\$(\\d*)_([a-zA-Z][a-zA-Z0-9_]*)$");
  /**
   * Pattern for identifying a list.
   */
  private static final Pattern hasIdentifier = Pattern.compile("^\\$([a-zA-Z][a-zA-Z0-9]*)$");
  /**
   * This method tells if a key follows the name convention for lists or not.@see KeyResolver#canResolve .
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's related to a list or not).
   */
  @Override
  public boolean canResolve(String key){
    Matcher hasIndex = hasIndexAndIdentifier.matcher(key);
    Matcher hasID = hasIdentifier.matcher(key);
    return hasIndex.matches() || hasID.matches();
  }
  /**
   * This method tells whether a key follows the specific naming convention of being a list's element or not.@see KeyResolver#canResolveIndex
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's a list element or not).
   */
  @Override
  public boolean canResolveIndex(String key){
    Matcher hasIndex = hasIndexAndIdentifier.matcher(key);
    if(!hasIndex.matches()){
      return false;
    }
    String m = hasIndex.group(1);
    if(m == null){
      return false;
    }
    return !m.isEmpty();
  }
  /**
   * This method tells whether a key follows the specific naming convention of being a list or not.@see KeyResolver#isContainer
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's a list or not).
   */
  @Override
  public boolean isContainer(String key){
    Matcher hasID = hasIdentifier.matcher(key);
    Matcher hasIndex = hasIndexAndIdentifier.matcher(key);
    return hasID.matches(); //&& !hasIndex.matches(); 
  }
  /**
   * This method retrieves the name element out of a list's element.@see KeyResolver#resolveIndex
   *
   * @param key the key to be parsed.
   * @return the present key.
   */
  @Override
  public int resolveIndex(String key){
    try{
      Matcher m = hasIndexAndIdentifier.matcher(key);
      m.matches();
      String capt = m.group(1);
      int index = Integer.parseInt(capt);
      return index;
    }catch(Exception e){
     throw new IllegalArgumentException("Attempted to resolve index of unresolvable key :" + key);
    }
  }
   /**
   * This method retrieves the name element out of a list's element. @see KeyResolver#cleanKey
   *
   * @param key the key to be parsed.
   * @return the present key.
   */
  @Override
  public String cleanKey(String key){
    Matcher hasID = hasIdentifier.matcher(key);
    Matcher hasIndex = hasIndexAndIdentifier.matcher(key);
    if(hasID.matches()){
      return hasID.group(1);
    }else if(hasIndex.matches()){
      return hasIndex.group(2);
    }
    throw new IllegalArgumentException("Attempted to clean unresolvable key");
  }
}