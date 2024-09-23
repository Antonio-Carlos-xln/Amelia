package com.antoniocarlos.amelia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;               
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class responsible for listening for the antlr parsing events, passing the relevant ones to the Delegate.
 */
class AmeliaParseListener extends AmeliaBaseListener{
  /**
   * Delegate instance.
   */
  Delegate delegate;
  /**
   * State variable meant for stepped construction of a "with" statement.
   */
  UnionListMap props;
  /**
   * Constructor that accepts the delegate instance.
   *
   * @param a delegate instance.
   */
  public AmeliaParseListener(Delegate delegate){
    this.delegate = delegate;
  }
  /**
   * Getter method for the delegate.
   *
   * @return current delegate instance.
   *
   */
  public Delegate getDelegate(){
    return this.delegate;
  }
  /**
   * Event of entering the "root" rule of the input document.
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
  @Override
  public void enterRoot(AmeliaParser.RootContext ctx){
  }
  /**
   * Event of leaving the "root" rule of the input document (end of the document). Simply delegates the event to the onEndOfInput method of the delegate instance.
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
    @Override
  public void exitRoot(AmeliaParser.RootContext ctx){
    delegate.onEndOfInput();
  }
  /**
   * Helper method for processing composed IDs.
   * 
   * @param id A single (possibly composed) id (e.g. Foo of Bar of Baz).
   * @return a list with all the referenced ids.
   *
   */
  private List<String> processID(String id){
    List<String> ids = Arrays.asList(id.split("of"));
    return ids
      .stream()
      .map(s -> s.trim())
      .collect(Collectors.toList());
    
  }
  /**
   * Event of leaving the "let" rule of the input document. It passess the processed data to the onLet method of the delegate instance (@see Delegate#onLet).
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
  @Override
  public void exitLet(AmeliaParser.LetContext ctx){
      delegate.onLet(processID(ctx.val().getText()), props);
      props.clear();
  }
  /**
   * Event of leaving the "set" rule of the input document. It passes the processed data to the onSet method of the delegate instance (@see onSet).
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
  @Override
  public void exitSet(AmeliaParser.SetContext ctx){
      boolean refVal = ctx.val(0) instanceof AmeliaParser.GIDContext;
      Object v  = processValueType(ctx.val(0));
      delegate.onSet(processID(ctx.val(1).getText()),v,refVal);
    }
  /**
   * Event of leaving the "delete" rule of the input document.It passess the processed data to the onDelete method of the delegate instance (@see Delegate#onDelete).
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
  @Override
  public void exitDelete(AmeliaParser.DeleteContext ctx){
      delegate.onDelete(processID(ctx.val().getText()));
  }
  /**
   * Event of entering the "let" rule of the input document. It starts the collection of the props.
   * 
   * @param ctx parsing context provided by antlr.
   *
   */
  @Override
  public void enterLet(AmeliaParser.LetContext ctx) {
    props = new UnionListMap(UnionListMap.Type.MAP,  new DollarSignKeyResolver());
  }
  /**
   * Helper method to process the data of a "val" rule.
   * 
   * @param ctx parsing context for  "val" rule provided by antlr.
   * @return the object in the right primitive boxxing type or a string containing a single (possibly compose) id.
   *
   */
  private Object processValueType(AmeliaParser.ValContext val){
    Object v = null;
    if(val instanceof AmeliaParser.IntContext){
      v = Integer.parseInt(val.getText());
    }else if(val instanceof AmeliaParser.FloatContext){
      v = Double.parseDouble(val.getText());
    }else if(val instanceof AmeliaParser.StringContext){
      v = val.getText().replace("\"","");
    }else if(val instanceof AmeliaParser.GIDContext){
      v = val.getText();      
    }
    return v;
  }
 /**
  * Event of leaving the "assign" rule of the input document. It passess the processed data to the onEntry method of the delegate instance (@see Delegate#onEntry).
  * 
  * @param ctx parsing context provided by antlr.
  *
  */
  @Override
  public void exitAssign(AmeliaParser.AssignContext ctx) {
    AmeliaParser.ValContext val = ctx.val();
    boolean refVal = val instanceof AmeliaParser.GIDContext;
    List<String> ids= processID(ctx.ID().getText());
    Object v = processValueType(val);
    delegate.onEntry(props,ids, v,refVal);
  }  

}