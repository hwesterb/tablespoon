/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.server.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jersey.repackaged.com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @author henke
 */
public class Saying {
  private long id;
  
  @Length(max = 3)
  private String content;
  
  public Saying() {
    // Jackson deserialization
  }
  
  public Saying(long id, String content) {
    this.id = id;
    this.content = content;
  }
  
  @JsonProperty
  public long getId() {
    return id;
  }
  
  @JsonProperty
  public String getContent() {
    return content;
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("content", content)
        .toString();
  }
}