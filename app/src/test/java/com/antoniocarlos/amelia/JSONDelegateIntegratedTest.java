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
                
public class JSONDelegateIntegratedTest{
  DollarSignKeyResolver kr = new DollarSignKeyResolver();
    @Test
    void testNotNullMap(){
      String testString = """
         set 5 as VeryHigh
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("json");
      String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"VeryHigh\":5}");
    }
   
    @Test
    void testNotEmptyMap(){
      String testString = """
         set 5 as VeryHigh
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("json");
      String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"VeryHigh\":5}");
    }

    @Test
    void testNotEmptyMap2(){
      String testString = """
         set 5 as VeryHigh
         set 4 as high
         """;
      AmeliaInterpreter ai = new AmeliaInterpreter("json");
      String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"high\":4,\"VeryHigh\":5}");
    }
   
    @Test 
    void testNestedMapGenerationFromLet() {
        String testString = """
        let message 
                   with "client" as Emitter,
                   "company" as Receiver,
                   "VERYHIGH" as Priority
        """                ;
        AmeliaInterpreter ai = new AmeliaInterpreter("json");
        String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"message\":{\"Emitter\":\"client\",\"Priority\":\"VERYHIGH\",\"Receiver\":\"company\"}}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
  String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"VERYHIGH\":\"veryhigh\",\"message\":{\"Emitter\":\"client\",\"Priority\":\"veryhigh\",\"Receiver\":\"company\"}}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"message\":{\"Emitter\":\"client\",\"Priority\":\"veryhigh\",\"Receiver\":\"company\"},\"priority\":\"veryhigh\"}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"reason\":\"test\",\"message\":{\"Emitter\":\"client\",\"Priority\":\"veryhigh\",\"Receiver\":\"company\"},\"priority\":\"veryhigh\"}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
 String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
    String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"reason\":\"test\",\"message\":{\"Emitter\":\"client\",\"Priority\":\"low\",\"Receiver\":\"company\"}}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
    String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"reason\":\"test\",\"message\":{\"Emitter\":\"client\",\"Priority\":\"low\",\"Receiver\":\"company\"},\"reply\":{\"Emitter\":\"company\",\"Priority\":\"medium\",\"Receiver\":\"client\"}}");
  }

@Test
  void testNestedMapGenerationFromNestedMapsFromObjects() {
    String testString = """
        set 0 as x of position of object
        set 1.5 as y of position of object
        """                ;
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"object\":{\"position\":{\"x\":0,\"y\":1.5}}}");
  }

  @Test
  void testNestedMapGenerationFromNestedMapsFromListOfObjects() {
    String testString = """
        set 0 as x of $_position of $list
        set 1.5 as x of $_position of $list
        set 3.2 as x of $_position of $list
        """                ;
      AmeliaInterpreter ai = new AmeliaInterpreter("json");
     String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"list\":[{\"x\":0},{\"x\":1.5},{\"x\":3.2}]}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
 String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"list\":[{\"x\":0,\"y\":1.5},{\"x\":1.5,\"y\":0},{\"x\":1.5,\"y\":1.5}]}");
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
 String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"messages\":[{\"Emitter\":\"client\",\"Priority\":\"high\",\"Receiver\":\"company\"},{\"Emitter\":\"company\",\"Priority\":\"low\",\"Receiver\":\"client\"}]}");   
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
 String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"messages\":[{\"Emitter\":\"partner\",\"Priority\":\"high\",\"Receiver\":\"company\"},{\"Emitter\":\"company\",\"Priority\":\"medium\",\"Receiver\":\"client\"}]}");   
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
    AmeliaInterpreter ai = new AmeliaInterpreter("json");
String result = ai.<String>run(testString);
      assertThat(result)
         .isEqualTo("{\"messages\":[{\"Emitter\":\"client\",\"Priority\":\"high\",\"Receiver\":\"company\"}]}");
}


}
