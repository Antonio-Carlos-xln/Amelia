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
                
class DollarSignKeyResolverTest{
  DollarSignKeyResolver kr = new DollarSignKeyResolver();
  @Test
  void canResolveTest(){
    assertThat(kr.canResolve("$"))
      .isEqualTo(false);
    assertThat(kr.canResolve("$a"))
      .isEqualTo(true);
    assertThat(kr.canResolve("$s_"))
      .isEqualTo(false);
    assertThat(kr.canResolve("$9_"))
      .isEqualTo(false);
    assertThat(kr.canResolve("$6_list"))
      .isEqualTo(true);
    assertThat(kr.canResolve("$list"))
      .isEqualTo(true);
    assertThat(kr.canResolve("$2"))
      .isEqualTo(false);
  }
  @Test
  void canResolveIndex(){
    assertThat(kr.canResolveIndex("$"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$a"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$s_"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$_s"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$9_"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$6_list"))
      .isEqualTo(true);
    assertThat(kr.canResolveIndex("$list"))
      .isEqualTo(false);
    assertThat(kr.canResolveIndex("$2"))
      .isEqualTo(false);
  }
  @Test
  void isContainer(){
    assertThat(kr.isContainer("$"))
      .isEqualTo(false);
    assertThat(kr.isContainer("$a"))
      .isEqualTo(true);
    assertThat(kr.isContainer("$s_"))
      .isEqualTo(false);
    assertThat(kr.isContainer("$9_"))
      .isEqualTo(false);
    assertThat(kr.isContainer("$6_list"))
      .isEqualTo(false);
    assertThat(kr.isContainer("$list"))
      .isEqualTo(true);
    assertThat(kr.isContainer("$2"))
      .isEqualTo(false);
  }
  @Test
  void resolveIndex(){
    assertThatThrownBy(() -> kr.resolveIndex("$"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> kr.resolveIndex("$a"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> kr.resolveIndex("$s_"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> kr.resolveIndex("$9_"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThat(kr.resolveIndex("$6_list"))
      .isEqualTo(6);
    assertThat(kr.resolveIndex("$0_a"))
      .isEqualTo(0);
    assertThat(kr.resolveIndex("$1_a"))
      .isEqualTo(1);
    assertThat(kr.resolveIndex("$2_a"))
      .isEqualTo(2);
    assertThatThrownBy(() -> kr.resolveIndex("$list"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> kr.resolveIndex("$2"))
      .isInstanceOf(IllegalArgumentException.class);
  }
  @Test
  void cleanKey(){
    assertThatThrownBy(() -> kr.cleanKey("$"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThat(kr.cleanKey("$a"))
      .isEqualTo("a");
    assertThatThrownBy(() -> kr.cleanKey("$s_"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> kr.cleanKey("$9_"))
      .isInstanceOf(IllegalArgumentException.class);
    assertThat(kr.cleanKey("$6_list"))
      .isEqualTo("list");
    assertThat(kr.cleanKey("$list"))
      .isEqualTo("list");
    assertThatThrownBy(() -> kr.cleanKey("$2"))
      .isInstanceOf(IllegalArgumentException.class);      
  }
}
                                                                            