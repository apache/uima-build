/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.uima.buildhelper;

import org.apache.maven.plugins.annotations.Parameter;

public class ParseSpec {
  
  /**
   * The name of the property to set
   * @since 1.0.0
   */
  @Parameter(required = true)
  private String name;
  
  /**
   * The format string to use
   * @since 1.0.0
   */
  @Parameter(required = true)
  private String format;

  public ParseSpec() {}
  
  public ParseSpec(String name, String format) {
    this.name = name;
    this.format = format;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

}
