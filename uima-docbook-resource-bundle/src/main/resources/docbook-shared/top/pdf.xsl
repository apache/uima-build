<?xml version="1.0" encoding="UTF-8"?>

<!--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:d="http://docbook.org/ns/docbook"
                version='1.0'>
        
  <!-- relative urls are relative to this stylesheet location -->
  <!-- xsl:imports must be first in the file, and have lower precedence
       than following elements -->
    
      <!-- note that xsl:import elements must appear FIRST
       and their href strings must be literal (no variables allowed) -->
  
  <!-- import docbook system from the shared uima-docbook-tool project -->  
  <xsl:import href="urn:docbkx:stylesheet" />    
  
      
  <xsl:import href="../common/html-pdf.xsl" />
  
  <xsl:import href="../common/pdf.xsl" />
 
  <!-- The script will generate a title page -->
  <!-- this generated xsl file needs to be included here -->
  <xsl:include href="../titlepage/pdf.xsl"/>

</xsl:stylesheet>

