<?xml version="1.0"?>

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">

<xsl:output method="text"/>
<xsl:strip-space elements="*"/>

<xsl:template match="changes">
	<xsl:value-of select="@title"/>
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="release">

	What's new in version <xsl:value-of select="@version"/> - <xsl:value-of select="@date"/>

	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="action">
	* <xsl:value-of select="normalize-space(.)"/> <!--xsl:text> (</xsl:text><xsl:value-of select="@dev"/><xsl:text>)</xsl:text-->
	<xsl:if test="@due-to">
		<xsl:text> Thanks to </xsl:text>
			<link href="mailto:{@due-to-email}"><xsl:value-of select="@due-to"/></link>
		<xsl:text>.</xsl:text>
	</xsl:if>

	<xsl:if test="@fixes-bug">
		<xsl:text> Fixes </xsl:text>
			<link href="http://xml.apache.org/bugs/show_bug.cgi?id={@fixes-bug}">
			<xsl:text>bug </xsl:text><xsl:value-of select="@fixes-bug"/>
			</link>
		<xsl:text>.</xsl:text>
	</xsl:if>
</xsl:template>

<xsl:template match="devs">
	<!-- remove -->
</xsl:template>

<xsl:template match="ul/li">
	* <xsl:apply-templates/>
</xsl:template>

</xsl:stylesheet>
