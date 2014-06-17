<?xml version="1.0"?>
<!-- 
  VCard4J is a VCard (RFC 2426) parser offering a Java API.
  Copyright (C) 2002-2003 Yann Duponchel. All rights reserved.
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  Lesser General Public License for more details.
  
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:variable name="show-n" select="true()"/>
  <xsl:variable name="show-nickname" select="true()"/>
  <xsl:variable name="show-tel" select="true()"/>
  <xsl:variable name="show-email" select="true()"/>
  <xsl:variable name="show-adr" select="true()"/>
  <xsl:variable name="show-bday" select="true()"/>

  <xsl:template match="/">
    <html>
      <head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
	<meta name="Author" content="duponchel@sourceforge.net (Yann)"/>
	<!-- <link rel="stylesheet" type="text/css" href="yann.css" title="style"/> -->
	<title>VCards</title>  
      </head>      
      <body text="#FFFFFF" bgcolor="#000000" link="#FFFFFF" alink="#FFFFFF" vlink="#FFFFFF">
      <font face="Arial, Helvetica"/>
	<div align="center">
	  <table>
	    <thead>
	      <tr bgcolor="#555555" align="center">
		<th>Name</th>
		<xsl:if test="$show-tel"><th>Telephone</th></xsl:if>
		<xsl:if test="$show-email"><th>E-Mail</th></xsl:if>
		<xsl:if test="$show-adr"><th>Address</th></xsl:if>
		<xsl:if test="$show-bday"><th>Birthday</th></xsl:if>
	      </tr>
	    </thead>
	    <tbody>
	      <xsl:apply-templates select="//vcard"/>
	      <!--
	      <xsl:for-each select="//vcard">
	      <xsl:sort select="type[@name='NICKNAME']/value/listItem/text">
	      <xsl:apply-templates select="."/>
	      </xsl:sort>
	      </xsl:for-each>
	      -->
	    </tbody>
	  </table>
	</div>
      </body> 
    </html> 
  </xsl:template>

  <xsl:template match="vcard">
    <tr bgcolor="#333333">

      <!-- Name, nickname, logo -->
      <td>
	<xsl:call-template name="N"/>
      </td>
      
      <!-- Telephone -->
      <xsl:if test="$show-tel">
	<td>
	  <xsl:call-template name="TEL"/>
	</td>
      </xsl:if>

      <!-- E-Mail -->
      <xsl:if test="$show-email">
	<td>
	  <xsl:call-template name="EMAIL"/>
	</td>
      </xsl:if>
      
      <!-- Address -->
      <xsl:if test="$show-adr">
	<td>
	  <xsl:call-template name="ADR"/>
	</td>
      </xsl:if>

      <!-- Birthday -->
      <xsl:if test="$show-bday">
	<td>
	  <xsl:value-of select="type[@name='BDAY']/value/text/text()"/>
	</td>
      </xsl:if>
    </tr>
  </xsl:template>
  
  <!-- Name, nickname, logo -->
  <xsl:template name="N">
    <table>
      <xsl:variable name="logo" select="type[@name='LOGO' and parameter/@name='VALUE' and parameter/@value='uri']/value/text/text()"/>
      <xsl:if test="string-length($logo)>0">
	<tr>
	  <td>
	    <img src="{$logo}" alt="" border="0"/>
	  </td>
	</tr>
      </xsl:if>
      <tr>
	<td>
	  <xsl:choose>
	    <xsl:when test="$show-n and $show-nickname">
	      <xsl:value-of select="type[@name='N']/value/structuredItem[2]/listItem/text/text()"/>
	      <xsl:text> </xsl:text>
	      <xsl:value-of select="type[@name='N']/value/structuredItem[1]/listItem/text/text()"/>
	      <xsl:if test="count(type[@name='NICKNAME']/value/listItem/text)>0">
	        <xsl:text> (</xsl:text>
	        <xsl:value-of select="type[@name='NICKNAME']/value/listItem/text/text()"/>
	        <xsl:text>)</xsl:text>
	      </xsl:if>
	    </xsl:when>
	    <xsl:when test="not($show-n) and $show-nickname and count(type[@name='NICKNAME']/value/listItem/text)>0">
	      <xsl:value-of select="type[@name='NICKNAME']/value/listItem/text/text()"/>
	    </xsl:when>
	    <xsl:otherwise>
	      <xsl:value-of select="type[@name='N']/value/structuredItem[2]/listItem/text/text()"/>
	      <xsl:text> </xsl:text>
	      <xsl:value-of select="type[@name='N']/value/structuredItem[1]/listItem/text/text()"/>
	    </xsl:otherwise>
	  </xsl:choose>
	</td>
      </tr>
    </table>
  </xsl:template>

  <!-- Telephone -->
  <xsl:template name="TEL">
    <table>
      <xsl:for-each select="type[@name='TEL']">
	<tr>
	  <td>
	    <xsl:value-of select="value/text/text()"/>
	    <xsl:if test="count(parameter[@name='TYPE'])>0">
	      <xsl:text> (</xsl:text>
	      <xsl:for-each select="parameter[@name='TYPE']">
		<xsl:value-of select="@value"/>
		<xsl:choose>
		  <xsl:when test="count(following-sibling::parameter[@name='TYPE'])>0">
		    <xsl:text>, </xsl:text>
		  </xsl:when>
		  <xsl:otherwise>
		    <xsl:text>)</xsl:text>
		  </xsl:otherwise>
		</xsl:choose>
	      </xsl:for-each>
	    </xsl:if>
	  </td>
	</tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <!-- E-Mail -->
  <xsl:template name="EMAIL">
    <table>
      <xsl:for-each select="type[@name='EMAIL']">
	<tr>
	  <td>
	    <xsl:variable name="mailto" select="value/text/text()"/>
	    <a href="mailto:{$mailto}">
	      <xsl:value-of select="$mailto"/>
	    </a>
	    <xsl:if test="count(parameter[@name='TYPE' and @value!='INTERNET'])>0">
	      <xsl:text> (</xsl:text>
	      <xsl:for-each select="parameter[@name='TYPE' and @value!='INTERNET']">
		<xsl:value-of select="@value"/>
		<xsl:choose>
		  <xsl:when test="count(following-sibling::parameter[@name='TYPE' and @value!='INTERNET'])>0">
		    <xsl:text>, </xsl:text>
		  </xsl:when>
		  <xsl:otherwise>
		    <xsl:text>)</xsl:text>
		  </xsl:otherwise>
		</xsl:choose>
	      </xsl:for-each>
	    </xsl:if>
	  </td>
	</tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <!-- Address -->
  <xsl:template name="ADR">
    <table>
      <xsl:for-each select="type[@name='ADR']">
	<tr>
	  <td>
	    <xsl:value-of select="value/structuredItem[1]/listItem/text/text()"/>
	    <xsl:text> </xsl:text>
	    <xsl:value-of select="value/structuredItem[2]/listItem/text/text()"/>
	    <xsl:text> </xsl:text>
	    <xsl:value-of select="value/structuredItem[3]/listItem/text/text()"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="value/structuredItem[4]/listItem/text/text()"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="value/structuredItem[6]/listItem/text/text()"/>
	    <xsl:text>, </xsl:text>
	    <xsl:value-of select="value/structuredItem[7]/listItem/text/text()"/>
	    <xsl:if test="count(parameter[@name='TYPE'])>0">
	      <xsl:text> (</xsl:text>
	      <xsl:for-each select="parameter[@name='TYPE']">
		<xsl:value-of select="@value"/>
		<xsl:choose>
		  <xsl:when test="count(following-sibling::parameter[@name='TYPE'])>0">
		    <xsl:text>, </xsl:text>
		  </xsl:when>
		  <xsl:otherwise>
		    <xsl:text>)</xsl:text>
		  </xsl:otherwise>
		</xsl:choose>
	      </xsl:for-each>
	    </xsl:if>
	  </td>
	</tr>
      </xsl:for-each>
    </table>
  </xsl:template>

</xsl:stylesheet>
