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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Check if the requested artifact is already in the .m2 repository
 *   if so, just return
 * else, download the requested artifact from archive.apache.org/dist/uima/
 * 
 * @goal copy-from-apache-dist
 * @phase validate
 *
 */
public class CopyFromApacheDist extends AbstractMojo {
  
  /**
   * Group Id
   * @parameter default-value="${project.groupId}"
   */
  private String groupId;

  /**
   * Artifact Id
   * @parameter default-value="uimaj"  
   */
  private String artifactId;

  /**
   * Version, e.g. 2.4.0
   * @parameter
   * @required
   */
  private String version = null;

  /**
   * Type 
   *
   * @parameter default-value="zip"
   */
  private String type;

  /**
   * Classifier
   *
   * @parameter default-value="bin"
   */
  private String classifier;

  /**
   * Repository
   *
   * @parameter default-value="${settings.localRepository}"
   */
  private String repository;
  
  public void execute() throws MojoExecutionException {
     //  repoloc / org/apache/uima  / artifactId / version / artifactId - version - classifier . type
    String targetInLocalFileSystem = String.format("%s/%s/%s/%s/%s-%s%s.%s",
        repository,
        groupId.replace('.', '/'),
        artifactId,
        version,
        artifactId,
        version,
        (classifier.length() > 0) ? ("-" + classifier) : "",
        type);    
    
    File targetFile = new File(targetInLocalFileSystem);
    if (targetFile.exists()) {
      System.out.format("copy-from-apache-dist returning, file %s exists%n", targetInLocalFileSystem);
      return;
    }
     // http://archive.apache.org/dist/uima/  artifactId - version / artifactId  - version - classifier . type
    String remoteLocation = String.format("http://archive.apache.org/dist/uima/%s-%s/%s-%s%s.%s",
        artifactId,
        version,
        artifactId,
        version,
        (classifier.length() > 0) ? ("-" + classifier) : "",
        type);

    // read remote file
    URL remoteURL = null;
    try {
      remoteURL = new URL(remoteLocation);
    } catch (MalformedURLException e) {
      throw new MojoExecutionException("Bad URL internally: " + remoteLocation, e);
    }
    
    HttpURLConnection remoteConnection = null;
    InputStream is = null;
    try {
      remoteConnection = (HttpURLConnection) remoteURL.openConnection();
      // discovered by trial and error - 
      // without setUseCaches(false), the read/write loop seems to terminate early
      //   and only copy a portion of the file  (running on IBM Java 6)
      remoteConnection.setUseCaches(false);  // insure get original file
      is =  remoteConnection.getInputStream();
    } catch (IOException e) {
      throw new MojoExecutionException("While reading remote location " + remoteLocation, e);
    }

    FileOutputStream os = null;
    try {
      targetFile.getParentFile().mkdirs();
      os = new FileOutputStream(targetFile);
    } catch (FileNotFoundException e) {
      throw new MojoExecutionException("While creating local file in location " + targetFile.getAbsolutePath(), e);    
    }

    System.out.format("copy-from-apache-dist file %s to %s%n", remoteLocation, targetInLocalFileSystem);
    
    byte[] buf = new byte[1024*1024];  // buffer size
    // still getting partial transfers.  
    // trying: when receive a negative for bytes read, retry 3 times with a .5 second delay
    for (int retries = 0; retries < 3; retries ++) {
      while(true) {
        int bytesRead;
        try {
          bytesRead = is.read(buf);
        } catch (IOException e) {
          throw new MojoExecutionException("While reading remote file in location " + remoteLocation, e);    
        }
        if (bytesRead < 0) {
          try {
            Thread.sleep(500); //wait 1/2 a second
          } catch (InterruptedException e) {
          }  
          break;
        }
        if (retries > 0) {
          System.out.format("retrying read successful after %d retries%n", retries);
        }
        retries = 0; // reset the retry counter... 
        try {
          os.write(buf, 0, bytesRead);
        } catch (IOException e) {
          throw new MojoExecutionException("While writing target file in location " + targetFile.getAbsolutePath(), e);    
       }
      }
    }
    try {
      os.close();
    } catch (IOException e) {
      throw new MojoExecutionException("While closing target file in location " + targetFile.getAbsolutePath(), e);    
    }
    try {
      is.close();
    } catch (IOException e) {
      throw new MojoExecutionException("While closing remote file in location " + remoteLocation, e);    
    }
  }
  
//  public static void main(String[] args) throws IOException {
//    String remoteLocation = "http://archive.apache.org/dist/uima/uimaj-2.4.0/uimaj-2.4.0-bin.zip.asc";
//    // read remote file
//    URL remoteURL = null;
//    try {
//      remoteURL = new URL(remoteLocation);
//    } catch (MalformedURLException e) {
//   //   throw new MojoExecutionException("Bad URL internally: " + remoteLocation, e);
//    }
//    
//    InputStream is = null;
//    try {
//      is =  remoteURL.openStream();
//    } catch (IOException e) {
//      e.printStackTrace();
//     // throw new MojoExecutionException("While reading remote location " + remoteLocation, e);
//    }
//    FileOutputStream os = null;
//    try {
//      os = new FileOutputStream("c:/temp/t");
//    } catch (FileNotFoundException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    byte[] buf = new byte[1024*1024];  // 1 meg buffer size
//    while(true) {
//      int bytesRead = is.read(buf);
//      if (bytesRead < 0) {
//        break;
//      }
//      os.write(buf, 0, bytesRead);
//    }
//    os.close();
//    is.close();
//    
//  }
}
