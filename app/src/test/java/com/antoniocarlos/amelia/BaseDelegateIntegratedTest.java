/*
 * This source file was generated by the Gradle 'init' task
 */
package com.antoniocarlos.amelia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.api.Assertions.entry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
                
public class BaseDelegateIntegratedTest{
  DollarSignKeyResolver kr = new DollarSignKeyResolver();
    @Test
    void testNotNullMap(){
      String testString = """
         set 5 as VeryHigh
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("base");
      UnionListMap m = ai.<UnionListMap>run(testString);
      assertThat(m)
         .isNotNull();
    }
   
    @Test
    void testNotEmptyMap(){
      String testString = """
         set 5 as VeryHigh
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("base");
      UnionListMap m = ai.<UnionListMap>run(testString);
      assertThat(m.get("VeryHigh"))
         .isEqualTo(5);
    }

    @Test
    void testNotEmptyMap2(){
      String testString = """
         set 5 as VeryHigh
         set 4 as high
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("base");
      UnionListMap m = ai.<UnionListMap>run(testString);
      assertThat(m.get("VeryHigh"))
         .isEqualTo(5);
      assertThat(m.get("high"))
         .isEqualTo(4);
    }
   
    @Test 
    void testNestedMapGenerationFromLet() {
        String testString = """
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "VERYHIGH" as Priority
        """                ;
        AmeliaInterpreter ai = new AmeliaInterpreter("base");
        UnionListMap m = ai.<UnionListMap>run(testString);
        UnionListMap mes = ((UnionListMap)m.get("message"));
        assertThat(mes.get("Emitter"))
         .isEqualTo("client");
        assertThat(mes.get("Receiver"))
         .isEqualTo("company");
        assertThat(mes.get("Priority"))
         .isEqualTo("VERYHIGH");
    }

  @Test 
  void testNestedMapGenerationFromSetAndLet() {
    String testString = """
        set "veryhigh" as VERYHIGH
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   VERYHIGH as Priority
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap mes = ((UnionListMap)m.get("message"));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("veryhigh");
     assertThat(m.get("VERYHIGH"))
       .isEqualTo("veryhigh");
  }
  @Test
  void testNestedMapGenerationFromLetAndSet() {
    String testString = """
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "veryhigh" as Priority
        set Priority of message as priority
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap mes = ((UnionListMap)m.get("message"));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("veryhigh");
    assertThat(m.get("priority"))
      .isEqualTo("veryhigh");
  }

    @Test
  void testNestedMapGenerationFromSetAndLetAndSet() {
    String testString = """
        set "test" as reason
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "veryhigh" as Priority
        set "veryhigh" as priority
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap mes = ((UnionListMap)m.get("message"));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("veryhigh");
    assertThat(m.get("priority"))
      .isEqualTo("veryhigh");
    assertThat(m.get("reason"))
      .isEqualTo("test");

  }
   
   
  @Test 
  void testNestedMapGenerationFromDeleteALet() {
    String testString = """
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "veryhigh" as Priority
        delete message
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    Map<String,Object> exp = new HashMap<String,Object>();
    assertThat(m.getMap())
       .isEmpty();
  }



  @Test
  void testNestedMapGenerationFromSetAtField() {
    String testString = """
        set "test" as reason
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "veryhigh" as Priority
        set "low" as Priority of message
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap mes = ((UnionListMap)m.get("message"));      
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("low");
    assertThat(m.get("reason"))
      .isEqualTo("test");
  }

  @Test
  void testNestedMapGenerationFromSwapOfFieldsWithSet() {
    String testString = """
        set "test" as reason
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "veryhigh" as Priority
        set "low" as Priority of message
        let reply
                with "" as Emitter,
                "" as Receiver,
                "veryhigh" as Priority
        set "medium" as Priority of reply
        set Receiver of message as Emitter of reply
        set Emitter of message as Receiver of reply
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap mes = ((UnionListMap)m.get("message"));
    UnionListMap rep = ((UnionListMap)m.get("reply"));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("low");
    assertThat(m.get("reason"))
      .isEqualTo("test");
    assertThat(rep.get("Emitter"))
      .isEqualTo("company");
    assertThat(rep.get("Receiver"))
      .isEqualTo("client");
    assertThat(rep.get("Priority"))
      .isEqualTo("medium");
  }

@Test
  void testNestedMapGenerationFromNestedMapsFromObjects() {
    String testString = """
        set 0 as x of position of object
        set 1.5 as y of position of object
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    assertThat(((UnionListMap)((UnionListMap)m.get("object")).get("position")).get("x"))
      .isEqualTo(0);
    assertThat(((UnionListMap)((UnionListMap)m.get("object")).get("position")).get("y"))
      .isEqualTo(1.5);
  }

  @Test
  void testNestedMapGenerationFromNestedMapsFromListOfObjects() {
    String testString = """
        set 0 as x of $_position of $list
        set 1.5 as x of $_position of $list
        set 3.2 as x of $_position of $list
        """                ;
      AmeliaInterpreter ai = new AmeliaInterpreter("base");
      UnionListMap m = ai.<UnionListMap>run(testString);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(0)).get("x"))
        .isEqualTo(0);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(1)).get("x"))
        .isEqualTo(1.5);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(2)).get("x"))
        .isEqualTo(3.2);
  }
  @Test
  void testNestedMapGenerationFromNestedMapsFromListOfObjectsOld() {
    String testString = """
        set 0 as x of $_position of $list
        set 1.5 as y of $0_position of $list
        set 1.5 as x of $_position of $list
        set 0 as y of $1_position of $list
        set 1.5 as x of $_position of $list
        set 1.5 as y of $2_position of $list
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(0)).get("x"))
        .isEqualTo(0);
   assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(0)).get("y"))
        .isEqualTo(1.5);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(1)).get("x"))
        .isEqualTo(1.5);
     assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(1)).get("y"))
        .isEqualTo(0);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(2)).get("x"))
        .isEqualTo(1.5);
      assertThat(((UnionListMap)((UnionListMap)m.get("list")).get(2)).get("y"))
        .isEqualTo(1.5);
  }

@Test
public void testListOfLets(){
  String testString = """
    let $_message of $messages 
                   with "client" as Emitter,
                        "company" as Receiver,
                        "high" as Priority
    let $_reply of $messages
             with "company" as Emitter,
                  "client" as Receiver,
                  "low" as Priority
        
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap list = ((UnionListMap)m.get("messages"));
    assertThat(((UnionListMap)list.get(0)))
        .isNotNull();
    assertThat(((UnionListMap)list.get(1)))
        .isNotNull();
    UnionListMap mes = ((UnionListMap)list.get(0));
    UnionListMap rep = ((UnionListMap)list.get(1));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("high");
    assertThat(rep.get("Emitter"))
      .isEqualTo("company");
    assertThat(rep.get("Receiver"))
      .isEqualTo("client");
    assertThat(rep.get("Priority"))
      .isEqualTo("low");
    
}

  @Test
public void testListOfLetsWithASet(){
  String testString = """
    let $_message of $messages 
                   with "client" as Emitter,
                        "company" as Receiver,
                        "high" as Priority
    let $_reply of $messages
             with "company" as Emitter,
                  "client" as Receiver,
                  "low" as Priority
    set "partner" as Emitter of $0_message of $messages
    set  "medium" as Priority of $1_message of $messages     
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap list = ((UnionListMap)m.get("messages"));
    assertThat(((UnionListMap)list.get(0)))
        .isNotNull();
    assertThat(((UnionListMap)list.get(1)))
        .isNotNull();
    UnionListMap mes = ((UnionListMap)list.get(0));
    UnionListMap rep = ((UnionListMap)list.get(1));
    assertThat(mes.get("Emitter"))
      .isEqualTo("partner");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("high");
    assertThat(rep.get("Emitter"))
      .isEqualTo("company");
    assertThat(rep.get("Receiver"))
      .isEqualTo("client");
    assertThat(rep.get("Priority"))
      .isEqualTo("medium");
    
}

 @Test
public void testListOfLetsWithADelete(){
  String testString = """
    let $_message of $messages 
                   with "client" as Emitter,
                        "company" as Receiver,
                        "high" as Priority
    let $_reply of $messages
             with "company" as Emitter,
                  "client" as Receiver,
                  "low" as Priority
     delete $1_message of $messages
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("base");
    UnionListMap m = ai.<UnionListMap>run(testString);
    UnionListMap list = ((UnionListMap)m.get("messages"));
    assertThat((list.getList().size()))
        .isEqualTo(1);
    UnionListMap mes = ((UnionListMap)list.get(0));
    assertThat(mes.get("Emitter"))
      .isEqualTo("client");
    assertThat(mes.get("Receiver"))
      .isEqualTo("company");
    assertThat(mes.get("Priority"))
      .isEqualTo("high");
}


}