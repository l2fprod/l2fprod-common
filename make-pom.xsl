<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:lxslt="http://xml.apache.org/xslt"
  xmlns:redirect="org.apache.xalan.xslt.extensions.Redirect"
  xmlns:xalan="http://xml.apache.org/xalan"
  extension-element-prefixes="redirect"
  version="1.0">

  <xsl:output
    method="xml"
    indent="yes"
    xalan:indent-amount="2"/>

  <xsl:param name="VERSION" select="'6.3-SNAPSHOT'"/>

  <xsl:template match="/">
    <xsl:for-each select="components/component[@name!='all' and @name!='sandbox']">
      <redirect:write select="concat('maven/', @name, '/pom.xml')">
        <xsl:apply-templates select="."/>
      </redirect:write>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="component">
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

      <!--
           the parent is the same for all submodule
           -->
      <parent>
        <artifactId>l2fprod-common-all</artifactId>
        <groupId>com.l2fprod.common</groupId>
        <version><xsl:value-of select="$VERSION"/></version>
        <relativePath>../../pom.xml</relativePath>
      </parent>
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.l2fprod.common</groupId>
      <artifactId>l2fprod-common-<xsl:value-of select="@name"/></artifactId>
      <name><xsl:value-of select="@name"/></name>
      <version><xsl:value-of select="$VERSION"/></version>
      <description><xsl:value-of select="@name"/></description>
      <build>
        <sourceDirectory>target/filteredsrc</sourceDirectory>
        <resources>
          <resource>
            <directory>../../src/java/<xsl:value-of select="@name"/></directory>
            <includes>
              <include>**/*.gif</include>
              <include>**/*.jpg</include>
              <include>**/*.png</include>
              <include>**/*.properties</include>
            </includes>
          </resource>
        </resources>
        <plugins>
          <plugin>
            <artifactId>maven-antrun-plugin</artifactId>
            <executions>
              <execution>
                <phase>process-sources</phase>
                <goals>
                  <goal>run</goal>
                </goals>
                <configuration>
                  <tasks>
                    <filter token="VERSION">
                      <xsl:attribute name="value">${project.version}</xsl:attribute>
                    </filter>
                    <filter value="L2FProd.com Common Components" token="PROJECT.FULLNAME" />
                    <filter value="l2fprod-common" token="PROJECT.SHORTNAME" />
                    <filter value="2005-2006" token="YEAR" />
                    <mkdir>
                      <xsl:attribute name="dir">${basedir}/target/filteredsrc</xsl:attribute>
                    </mkdir>
                    <copy filtering="on">
                      <xsl:attribute name="todir">${basedir}/target/filteredsrc</xsl:attribute>
                      <fileset includes="**/*.java">
                        <xsl:attribute name="dir">../../src/java/${project.name}</xsl:attribute>
                      </fileset>
                    </copy>
                  </tasks>
                </configuration>
              </execution>
            </executions>
          </plugin>
          <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
              <archive>
                <compress>true</compress>
                <!--
                     <addMavenDescriptor>false</addMavenDescriptor>
                     -->
                <manifest>
                  <mainClass><xsl:value-of select="@mainclass"/></mainClass>
                  <addExtensions>false</addExtensions>
                  <addClasspath>true</addClasspath>
                </manifest>
                <manifestSections>
                  <xsl:for-each select="bean">
                    <manifestSection>
                      <name><xsl:value-of select="@name"/></name>
                      <manifestEntries>
                        <Java-Bean>True</Java-Bean>
                      </manifestEntries>
                    </manifestSection>
                  </xsl:for-each>
                </manifestSections>
              </archive>
            </configuration>
          </plugin>
        </plugins>
      </build>
      <dependencies>
        <xsl:for-each select="depend">
          <dependency>
            <groupId>com.l2fprod.common</groupId>
            <artifactId>l2fprod-common-<xsl:value-of select="."/></artifactId>
            <version><xsl:value-of select="$VERSION"/></version>
            <scope>compile</scope>
          </dependency>
        </xsl:for-each>
        <xsl:for-each select="external-depend">
          <dependency>
            <groupId><xsl:value-of select="@groupId"/></groupId>
            <artifactId><xsl:value-of select="@artifactId"/></artifactId>
            <version><xsl:value-of select="@version"/></version>
            <scope>compile</scope>
          </dependency>
        </xsl:for-each>
      </dependencies>
    </project>
  </xsl:template>

</xsl:stylesheet>
