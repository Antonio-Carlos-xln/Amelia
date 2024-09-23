package com.antoniocarlos.amelia;
/**
 * This interface allows resolving of names between containers (lists) and plain objects according to some naming convention.
 */
interface KeyResolver{
  /**
   * This method tells if a key follows the name convention for lists or not.
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's related to a list or not).
   */
  public boolean canResolve(String key);
  /**
   * This method tells whether a key follows the specific naming convention of being a list's element or not.
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's a list element or not).
   */
  public boolean canResolveIndex(String key);
  /**
   * This method tells whether a key follows the specific naming convention of being a list or not.
   *
   * @param key the key to tested.
   * @return a boolean indicating whether the key can be resolved or not (if it's a list or not).
   */
  public boolean isContainer(String key);
  /**
   * This method retrieves the index element out of a list's element.
   *
   * @param key the key to be parsed.
   * @return the present index.
   */
  public int resolveIndex(String key);
  /**
   * This method retrieves the name element out of a list's element.
   *
   * @param key the key to be parsed.
   * @return the present key.
   */
  public String cleanKey(String key);
}