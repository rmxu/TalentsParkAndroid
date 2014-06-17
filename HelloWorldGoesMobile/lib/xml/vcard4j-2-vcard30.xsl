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

<!-- 
 This XSLT transforms an XML representation of a vCard into a vCard 3.0.
 FIXME: encoding not printed. (it's normal in v3.0, I think)
 Note: Je ne sais pas si on peut ecrire une telle debilite en etant sain d'esprit.
 Note: I welcome any better idea to escape characters.
 Note: I assumed ALL values (single, list or structured) must be escape since in the
       RFC2426, the examples of several single values containing ',' are escaped.
 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="text" media-type="text/x-vcard"/>

  <xsl:variable name="fold-rfc2426" select="true()"/>
  <xsl:variable name="maximum-line-length" select="75"/>
  
  <xsl:template match="//addressBook">
    <xsl:apply-templates select="vcard"/>
  </xsl:template>

  <xsl:template match="vcard">
    <xsl:variable name="group" select="@group"/>
    <xsl:if test="string-length($group)>0">
      <xsl:value-of select="$group"/>
      <xsl:text>.</xsl:text>
    </xsl:if>
    <xsl:text>BEGIN:VCARD
</xsl:text>
    <xsl:apply-templates select="type"/>
    <xsl:if test="string-length($group)>0">
      <xsl:value-of select="$group"/>
      <xsl:text>.</xsl:text>
    </xsl:if>
    <xsl:text>END:VCARD

</xsl:text>
  </xsl:template>

  <xsl:template match="type">
    <xsl:variable name="group" select="@group"/>
    <xsl:if test="string-length($group)>0">
      <xsl:value-of select="$group"/>
      <xsl:text>.</xsl:text>
    </xsl:if>
    <xsl:value-of select="@name"/>
    <xsl:apply-templates select="parameter"/>
    <xsl:text>:</xsl:text>
    <!--<xsl:apply-templates select="value"/>-->
    <xsl:call-template name="fold-line">
      <xsl:with-param name="line">
	<xsl:apply-templates select="value"/>
      </xsl:with-param>
    </xsl:call-template>
    <xsl:text>
</xsl:text>
  </xsl:template>

  <xsl:template match="parameter">
    <xsl:text>;</xsl:text>
    <!-- TYPE is the default value and is not mandatory for v2.1 -->
    <!-- <xsl:if test="@name!='TYPE'"> -->
    <xsl:value-of select="@name"/>
    <xsl:text>=</xsl:text>
    <!-- </xsl:if> -->
    <xsl:value-of select="@value"/>
  </xsl:template>

  <xsl:template match="value">
    <xsl:choose>
      <xsl:when test="count(structuredItem)>0">
	<xsl:apply-templates select="structuredItem"/>
      </xsl:when>
      <xsl:when test="count(listItem)>0">
	<xsl:apply-templates select="listItem"/>
      </xsl:when>
      <xsl:otherwise>
	<xsl:apply-templates select="text"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="structuredItem">
    <xsl:apply-templates select="listItem"/>
    <xsl:if test="count(following-sibling::structuredItem)>0">
      <xsl:text>;</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="listItem">
    <xsl:apply-templates select="text"/>
    <xsl:if test="count(following-sibling::listItem)>0">
      <xsl:text>,</xsl:text>
    </xsl:if>
  </xsl:template>

  <xsl:template match="text">
    <xsl:call-template name="escape-string">
      <xsl:with-param name="string" select="text()"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="escape-string">
    <xsl:param name="string" />
    <xsl:call-template name="escape-coma">
      <xsl:with-param name="string">
	<xsl:call-template name="escape-semi-colon">
	  <xsl:with-param name="string">
	    <xsl:call-template name="escape-newline">
	      <xsl:with-param name="string">
		<xsl:call-template name="escape-backslash">
		  <xsl:with-param name="string" select="$string"/>
		</xsl:call-template>
	      </xsl:with-param>
	    </xsl:call-template>
	  </xsl:with-param>
	</xsl:call-template>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="escape-backslash">
    <xsl:param name="string" />
    <xsl:choose>
      <xsl:when test="contains($string,'\')">
	<xsl:value-of select="substring-before($string,'\')"/>
	<xsl:text>\\</xsl:text>
	<xsl:call-template name="escape-backslash">
	  <xsl:with-param name="string">
	    <xsl:value-of select="substring-after($string,'\')"/>
	  </xsl:with-param>
	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="escape-newline">
    <xsl:param name="string" />
    <xsl:choose>
      <xsl:when test="contains($string,'&#xA;')">
	<xsl:value-of select="substring-before($string,'&#xA;')"/>
	<xsl:text>\n</xsl:text>
	<xsl:call-template name="escape-newline">
	  <xsl:with-param name="string">
	    <xsl:value-of select="substring-after($string,'&#xA;')"/>
	  </xsl:with-param>
	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="escape-semi-colon">
    <xsl:param name="string" />
    <xsl:choose>
      <xsl:when test="contains($string,';')">
	<xsl:value-of select="substring-before($string,';')"/>
	<xsl:text>\;</xsl:text>
	<xsl:call-template name="escape-semi-colon">
	  <xsl:with-param name="string">
	    <xsl:value-of select="substring-after($string,';')"/>
	  </xsl:with-param>
	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="escape-coma">
    <xsl:param name="string" />
    <xsl:choose>
      <xsl:when test="contains($string,',')">
	<xsl:value-of select="substring-before($string,',')"/>
	<xsl:text>\,</xsl:text>
	<xsl:call-template name="escape-coma">
	  <xsl:with-param name="string">
	    <xsl:value-of select="substring-after($string,',')"/>
	  </xsl:with-param>
	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="$string"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="fold-line">
    <xsl:param name="line" />
    <xsl:choose>
      <xsl:when test="string-length($line)>$maximum-line-length">
	<xsl:value-of select="substring($line,1,$maximum-line-length)"/>
	<xsl:choose>
	  <xsl:when test="$fold-rfc2426">
	    <xsl:text>
 </xsl:text>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:text>=
</xsl:text>
	  </xsl:otherwise>
	</xsl:choose>
	<xsl:call-template name="fold-line">
	  <xsl:with-param name="line" select="substring($line,1+$maximum-line-length)"/>
	</xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="$line" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
