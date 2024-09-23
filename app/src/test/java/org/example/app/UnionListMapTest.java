
package com.antoniocarlos.amelia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
                
class UnionListMapTest{

  @Test
  void addsAsMapTest(){
    List sb = Arrays.asList("a","b","c");
    UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,new DollarSignKeyResolver());
    u.add("number",1);
    assertThat(u.map)
      .contains(
        entry("number",1)
      );
    u.add("string","some-text");
    assertThat(u.map)
      .contains(
        entry("number",1),
        entry("string","some-text")
      );
    u.add("bool",true);
    assertThat(u.map)
      .contains(
        entry("number",1),
        entry("string","some-text"),
        entry("bool",true)
      );
    u.add("ob_ject",sb);
    assertThat(u.map)
      .contains(
        entry("number",1),
        entry("string","some-text"),
        entry("bool",true),
        entry("ob_ject",sb)
      );
    
  }

  @Test
  void addAsList(){
    UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,new DollarSignKeyResolver());
    u.add("$_a",1);
    u.add("$_a","Test");
    u.add("$_a",true);
    assertThat(u.list)
      .contains(1,"Test",true);
  }

  @Test
  void containsAsMap(){
    List sb = Arrays.asList("a","b","c");
    UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,new DollarSignKeyResolver());
    u.add("number",1);
    u.add("string","some-text");
    u.add("bool",true);
    u.add("ob_ject",sb);
    assertThat(u.contains("number"))
      .isTrue();
    assertThat(u.contains("string"))
      .isTrue();
    assertThat(u.contains("ob_ject"))
      .isTrue();
    assertThat(u.contains("ghghh"))
      .isFalse();
  }

  @Test
  void containsAsList(){
    UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,new DollarSignKeyResolver());
    u.add("$_a",1);
    u.add("$_a","Test");
    u.add("$_a",true);
    assertThat(u.contains("$0_a"))
      .isTrue();
    assertThat(u.contains("$1_a"))
      .isTrue();
    assertThat(u.contains("$2_a"))
      .isTrue();    
  }

  @Test
  void getAsMap(){
    List sb = Arrays.asList("a","b","c");
    UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,new DollarSignKeyResolver());
    u.add("number",1);
    u.add("string","some-text");
    u.add("bool",true);
    u.add("ob_ject",sb);
    assertThat(u.get("number"))
      .isEqualTo(1);
    assertThat(u.get("string"))
      .isEqualTo("some-text");
    assertThat(u.get("ob_ject"))
      .isEqualTo(sb);
    assertThat(u.get("ghghh"))
      .isEqualTo(null);
    assertThat(u.get("$ggg"))
      .isEqualTo(null);
  }

  @Test
  void getAsList(){
    UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,new DollarSignKeyResolver());
    u.add("$_a",1);
    u.add("$_a","Test");
    u.add("$_a",true);
    assertThat(u.get("$0_a"))
      .isEqualTo(1);
    assertThat(u.get("$1_a"))
      .isEqualTo("Test");
    assertThat(u.get("$2_a"))
      .isEqualTo(true);
    assertThatThrownBy(() -> u.get("key"))
      .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void removeAsMap(){
    List sb = Arrays.asList("a","b","c");
    UnionListMap u = new UnionListMap(UnionListMap.Type.MAP,new DollarSignKeyResolver());
    u.add("number",1);
    u.add("string","some-text");
    u.add("bool",true);
    u.add("$ob_ject",sb);
    u.remove("number");
    u.remove("string");
    u.remove("bool");
    u.remove("$ob_ject");
    assertThat(u.map)
      .isEmpty();
  }

  @Test
  void removeAsList(){
    UnionListMap u = new UnionListMap(UnionListMap.Type.LIST,new DollarSignKeyResolver());
    u.add("$_a",1);
    u.add("$_a","Test");
    u.add("$_a",true);
    assertThat(u.list)
      .containsExactly(1,"Test",true);
    u.remove("$2_a");
    u.remove("$1_a");
    u.remove("$0_a");    
    assertThat(u.list)
      .isEmpty();
  }

}