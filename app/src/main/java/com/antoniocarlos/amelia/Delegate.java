package com.antoniocarlos.amelia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;               
/**
 * This interface is the actual consumer of the data of the input document, receiving only the relevant information from the data that was preprocessed.Here it's where the real processing and the postprocessing occurs.
 * It has a parametric type meant to represent the data type that the concrete implementations can produce.
 */
interface Delegate<T>{
 /**
  * This method gets invoked whenever a "let" statement is found at the input document. It's the main way of creating elements in the language model.
  * 
  * @param ids a list of individual ids.
  * @param propsI the collection of all passed props.
  *
  */ 
  void onLet(List<String> ids, UnionListMap props);
 /**
  * This method gets invoked once for every assignment inside a "with" block, and is responsible for collecting the props of a currently being created element. It can created nested elements, if the necessary.  
  * 
  * @param propsI the collection of all passed props.
  * @param ids a list of individual ids.
  * @param valI a value to be inserted at the currently being-build props.
  * @param refVal boolean that indicates whether or not the given value is a reference.
  *
  */
 
  void onEntry(UnionListMap props, List<String> ids, Object val, boolean refVal);
  /**
   * This method gets invoked whenever a "set" statement is found in the input document. It can also create nested elements if some of the quoted elements do not exist yet.
   * 
   * @param ids a list of individual ids.
   * @param valI the value to set the given property.
   * @param refVal whether or not the current val is a reference to other property of some object (including the global object).
   *
   */
  void onSet(List<String> ids, Object val, boolean refVal);
   /**
   * This method gets invoked whenever a "delete" instruction is found in the inout document and is responsable for performing such deletion.
   * 
   * @param ids a list of individual ids.
   *
   */
  void onDelete(List<String> ids);
   /**
   * This method gets invoked when the antlr emits the exit of rule "root", signaling that there's no more input to be parsed.
   * 
   */
  void onEndOfInput();
   /**
   * This method can be called by the client after the end of the input to get the produced data. A  config map is accepted for giving specific instructions on how to postprocess the data.
   * 
   * @param ids a list of individual ids.
   * @return The constructed data as result of procesing the input document.
   */
  T getData(Map<String, Object> config);
} 