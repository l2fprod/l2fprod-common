<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>
    
  <xsl:template match="/">
    <assembly>
      <id>bin</id>
      <formats>
        <format>zip</format>
      </formats>
      <fileSets>
        <fileSet>
          <directory>.</directory>
          <outputDirectory></outputDirectory>
          <includes>
            <include>LICENSE.txt</include>
            <include>README.txt</include>
          </includes>
        </fileSet>
        <xsl:for-each select="components/component[@name!='all' and @name!='sandbox']">
          <fileSet>
            <directory>maven/<xsl:value-of select="@name"/>/target</directory>
            <outputDirectory>lib</outputDirectory>
            <includes>
              <include>*.jar</include>
            </includes>
          </fileSet>
          <fileSet>
            <directory>maven/<xsl:value-of select="@name"/>/target/filteredsrc</directory>
            <outputDirectory>src</outputDirectory>
            <includes>
              <include>**</include>
            </includes>
          </fileSet>
          <fileSet>
            <directory>src/java/<xsl:value-of select="@name"/></directory>
            <outputDirectory>src</outputDirectory>
            <includes>
              <include>**/*.gif</include>
              <include>**/*.jpg</include>
              <include>**/*.properties</include>
              <include>**/*.png</include>
            </includes>
          </fileSet>
        </xsl:for-each>
      </fileSets>
    </assembly>
  </xsl:template>

</xsl:stylesheet>
