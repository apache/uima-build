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
  private static final int MAXRETRIES = 6;
  private static final int MINTOTALSIZE = 100;
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
    String remoteLocation = String.format("http://%s.apache.org/dist/uima/%s-%s/%s-%s%s.%s",
        "archive",
//        "www",
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
  
    FileOutputStream os = null;
    try {
      targetFile.getParentFile().mkdirs();
      os = new FileOutputStream(targetFile);
    } catch (FileNotFoundException e) {
      throw new MojoExecutionException("While creating local file in location " + targetFile.getAbsolutePath(), e);    
    }
    
    int totalSize = 0;
    int readSoFar = 0;

  retryLoop:
    for (int retry = 0; retry < MAXRETRIES; retry ++) {
    
      HttpURLConnection remoteConnection = null;
      InputStream is = null;
      
      try {
        remoteConnection = (HttpURLConnection) remoteURL.openConnection();
        if (readSoFar > 0) {
          String rangespec = String.format("bytes=%d-", readSoFar);
          System.out.format("Requesting range: %s%n", rangespec);
          remoteConnection.setRequestProperty("Range", rangespec);
        }
        if (totalSize == 0) { 
          totalSize = remoteConnection.getContentLength();
          if (totalSize < MINTOTALSIZE) {
            throw new MojoExecutionException(String.format("File size %d too small for %s%n", totalSize, remoteLocation));
          }
        }

        is =  remoteConnection.getInputStream();
//        if (readSoFar > 0) {
//          System.out.format("Skipping over %,d bytes read so far; this may take some time%n", readSoFar);
//          long skipped = is.skip(readSoFar);
//          if (skipped != readSoFar) {
//            System.out.format("Skipping only skipped %,d out of %,d; retrying", skipped, readSoFar);
//            continue retryLoop;
//          }
//        }
      } catch (IOException e) {
        throw new MojoExecutionException("While reading remote location " + remoteLocation, e);
      }
  
  
      System.out.format("copy-from-apache-dist file %s to %s%n", remoteLocation, targetInLocalFileSystem);
      System.out.format("%,12d of %,12d\r", readSoFar, totalSize);
      
      byte[] buf = new byte[1024*1024];  // buffer size
      while(true) {
        int bytesRead;
        try {
          bytesRead = is.read(buf);
        } catch (IOException e) {
          throw new MojoExecutionException("While reading remote file in location " + remoteLocation, e);    
        }
        if (bytesRead < 0 ) {
          if (readSoFar == totalSize) {
            break;
          }
          System.out.format("%n *** Premature EOF, %,12d read out of %,12d   Retry %d%n", readSoFar, totalSize, retry);
          try {
            is.close();
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          if (retry == (MAXRETRIES - 1)) {
            throw new MojoExecutionException("CopyFromApacheDist retry limit exceeded ");   
          }
          continue retryLoop;
        }
        retry = 0;  // reset retry count because we read some good bytes
        try {
          os.write(buf, 0, bytesRead);
        } catch (IOException e) {
          throw new MojoExecutionException("While writing target file in location " + targetFile.getAbsolutePath(), e);    
        }
        readSoFar = readSoFar + bytesRead;
        System.out.format("%,12d of %,12d\r", readSoFar, totalSize);
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
      break;  // out of retry loop
    }
    System.out.println("");
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
