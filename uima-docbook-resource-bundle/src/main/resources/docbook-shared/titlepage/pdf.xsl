<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" exclude-result-prefixes="exsl" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- This stylesheet was created by template/titlepage.xsl; do not edit it by hand. -->

<xsl:template name="book.titlepage.recto">
  <xsl:choose>
    <xsl:when test="d:bookinfo/d:title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:title"/>
    </xsl:when>
    <xsl:when test="d:info/d:title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:title"/>
    </xsl:when>
    <xsl:when test="d:title">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:title"/>
    </xsl:when>
  </xsl:choose>

  <xsl:choose>
    <xsl:when test="d:bookinfo/d:subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:subtitle"/>
    </xsl:when>
    <xsl:when test="d:info/d:subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:subtitle"/>
    </xsl:when>
    <xsl:when test="d:subtitle">
      <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:subtitle"/>
    </xsl:when>
  </xsl:choose>

  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:othercredit"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:othercredit"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:mediaobject"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:mediaobject"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:releaseinfo"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:releaseinfo"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:revision"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:revision"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:revhistory"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:revhistory"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:abstract"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:abstract"/>
</xsl:template>

<xsl:template name="book.titlepage.verso">
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:bookinfo/d:copyright"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:info/d:copyright"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:bookinfo/d:legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:info/d:legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:bookinfo/d:pubdate"/>
  <xsl:apply-templates mode="book.titlepage.verso.auto.mode" select="d:info/d:pubdate"/>
</xsl:template>

<xsl:template name="book.titlepage.separator">
</xsl:template>

<xsl:template name="book.titlepage">
  <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:variable name="recto.content">
      <xsl:call-template name="book.titlepage.before.recto"/>
      <xsl:call-template name="book.titlepage.recto"/>
    </xsl:variable>
    <xsl:variable name="recto.elements.count">
      <xsl:choose>
        <xsl:when test="function-available('exsl:node-set')"><xsl:value-of select="count(exsl:node-set($recto.content)/*)"/></xsl:when>
        <xsl:when test="contains(system-property('xsl:vendor'), 'Apache Software Foundation')">
          <!--Xalan quirk--><xsl:value-of select="count(exsl:node-set($recto.content)/*)"/></xsl:when>
        <xsl:otherwise>1</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:if test="(normalize-space($recto.content) != '') or ($recto.elements.count &gt; 0)">
      <fo:block><xsl:copy-of select="$recto.content"/></fo:block>
    </xsl:if>
    <xsl:variable name="verso.content">
      <xsl:call-template name="book.titlepage.before.verso"/>
      <xsl:call-template name="book.titlepage.verso"/>
    </xsl:variable>
    <xsl:variable name="verso.elements.count">
      <xsl:choose>
        <xsl:when test="function-available('exsl:node-set')"><xsl:value-of select="count(exsl:node-set($verso.content)/*)"/></xsl:when>
        <xsl:when test="contains(system-property('xsl:vendor'), 'Apache Software Foundation')">
          <!--Xalan quirk--><xsl:value-of select="count(exsl:node-set($verso.content)/*)"/></xsl:when>
        <xsl:otherwise>1</xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:if test="(normalize-space($verso.content) != '') or ($verso.elements.count &gt; 0)">
      <fo:block><xsl:copy-of select="$verso.content"/></fo:block>
    </xsl:if>
    <xsl:call-template name="book.titlepage.separator"/>
  </fo:block>
</xsl:template>

<xsl:template match="*" mode="book.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="book.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="d:title" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="20pt" space-before="15pt" font-weight="bold" font-family="{$title.fontset}" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:call-template name="division.title">
<xsl:with-param name="node" select="ancestor-or-self::d:book[1]"/>
</xsl:call-template>
</fo:block>
</xsl:template>

<xsl:template match="d:subtitle" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" text-align="center" font-size="18pt" space-before="13.5pt" font-family="{$title.fontset}" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:corpauthor" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" font-size="14pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:authorgroup" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" font-size="14pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:author" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" font-size="14pt" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:othercredit" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:mediaobject" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="2em" space-after="2em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:releaseinfo" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:revision" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:revhistory" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:abstract" mode="book.titlepage.recto.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.recto.style" space-before="0.5em" text-align="start" margin-left="0.5in" margin-right="0.5in" font-family="{$body.fontset}" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:copyright" mode="book.titlepage.verso.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="0.5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:legalnotice" mode="book.titlepage.verso.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.verso.style" text-align="start" margin-left="0.5in" margin-right="0.5in" font-family="{$body.fontset}" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
</fo:block>
</xsl:template>

<xsl:template match="d:pubdate" mode="book.titlepage.verso.auto.mode">
<fo:block xsl:use-attribute-sets="book.titlepage.verso.style" space-before="3.5em" xmlns:fo="http://www.w3.org/1999/XSL/Format">
<xsl:apply-templates select="." mode="book.titlepage.verso.mode"/>
</fo:block>
</xsl:template>

</xsl:stylesheet>
