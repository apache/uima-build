/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.uima.buildhelper;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Parse the current time value into multiple properties. Each execution gets
 * the current date and time, and sets 0 or more properties such as
 * currentTime.year, currentTime.month, etc.
 * 
 * Users specify the property name and the format of the parsing; multiple sets
 * of these can be configured to represent the same time.
 * 
 * @goal parse-date-time
 * @phase validate
 */
public class ParseDateTime extends AbstractMojo {
  /**
   * Collection of parseSpecs. Each parseSpec has a name - the property name,
   * and a format - see "Usage"
   * 
   * @Parameter
   * @required
   * @since 1.0.0
   */
  private ParseSpec[] parseSpecs;
  
  /**
   * The Maven project to analyze.
   * 
   * @Parameter ( expression = "${project}" )
   * @required
   * @readonly
   */
  private MavenProject project;

  public void execute() throws MojoExecutionException {
   
    Date [] now = new Date[] {new Date()};
    
    for (int i = 0; i < parseSpecs.length; i++) {
      ParseSpec ps = parseSpecs[i]; 
      String v = MessageFormat.format("{0,date," + ps.getFormat() + "}", now);
      if (getLog().isDebugEnabled()) {
        getLog().debug("Setting property " +
            ps.getName() +
            " with format " +
            ps.getFormat() +
            " to value '" +
            v +
            "'");
      }
      project.getProperties().setProperty(ps.getName(), v);
    }  
  }
}
