/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package se.kth.tablespoon.server.rest.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author henke
 */
public class TablespoonConfiguration extends Configuration {
  @NotEmpty
  private String template;
  
  @NotEmpty
  private String defaultName = "Stranger";
  
  @JsonProperty
  public String getTemplate() {
    return template;
  }
  
  @JsonProperty
  public void setTemplate(String template) {
    this.template = template;
  }
  
  @JsonProperty
  public String getDefaultName() {
    return defaultName;
  }
  
  @JsonProperty
  public void setDefaultName(String defaultName) {
    this.defaultName = defaultName;
  }
  
  public Template buildTemplate() {
    return new Template(template, defaultName);
  }
  
  
  
}
