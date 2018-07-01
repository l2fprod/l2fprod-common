<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

  <xsl:output method="html"/>

  <xsl:template match="/">
    <project name="build-components" default="jar">

      <target name="init">
        <filter token="VERSION">
          <xsl:attribute name="value">${version}</xsl:attribute>
        </filter>
        <filter token="PROJECT.FULLNAME">
          <xsl:attribute name="value">${project.fullname}</xsl:attribute>
        </filter>
        <filter token="PROJECT.SHORTNAME">
          <xsl:attribute name="value">${project}</xsl:attribute>
        </filter>
        <filter token="YEAR">
          <xsl:attribute name="value">${year}</xsl:attribute>
        </filter>

        <mkdir>
          <xsl:attribute name="dir">${build.dir}/jars/<xsl:value-of select="@name"/>
          </xsl:attribute>
        </mkdir>
      </target>

      <!--
        Copy src
      -->
      <target name="src" depends="init">
        <xsl:for-each select="//component">
          <echo>
            Copying source code for <xsl:value-of select="@name"/>
          </echo>

          <!-- copy the src -->
          <mkdir>
            <xsl:attribute name="dir">${build.dir}/src/<xsl:value-of select="@name"/>
            </xsl:attribute>
          </mkdir>
          <!-- filtering for java files only -->
          <copy filtering="on">
            <xsl:attribute name="todir">${build.dir}/src/<xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:apply-templates select="fileset"/>
          </copy>
          <copy overwrite="true">
            <xsl:attribute name="todir">${build.dir}/src/<xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:apply-templates select="resources/fileset"/>
          </copy>
          <!-- images just get copied -->
        </xsl:for-each>        
      </target>
      
      <target name="jar" depends="src">
        <xsl:for-each select="//component">
          <echo>
            Now building component <xsl:value-of select="@name"/>
          </echo>

          <!-- compile what has been copied -->
          <mkdir>
            <xsl:attribute name="dir">${build.dir}/classes/<xsl:value-of select="@name"/>
            </xsl:attribute>
          </mkdir>
          <javac debug="on" optimize="off">
            <xsl:attribute name="srcdir">${build.dir}/src/<xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:attribute name="destdir">${build.dir}/classes/<xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:apply-templates select="classpath"/>
            <xsl:choose>
              <xsl:when test="@isAll='true'">
                <xsl:apply-templates select="//component/classpath"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="classpath"/>
              </xsl:otherwise>
            </xsl:choose>
          </javac>

          <!-- build the jar for this component -->
          <jar destfile="${jar}">
            <xsl:attribute name="destfile">${build.dir}/jars/l2fprod-common-<xsl:value-of select="@name"/>.jar</xsl:attribute>
            <fileset>
              <xsl:attribute name="dir">${build.dir}/classes/<xsl:value-of select="@name"/>
              </xsl:attribute>
              <include name="**/*.class"/>
            </fileset>
            <xsl:apply-templates select="resources/fileset"/>
            <manifest>
              <attribute name="Built-by">
                <xsl:attribute name="value">${user.name}</xsl:attribute>
              </attribute>
              <attribute name="Built-on">
                <xsl:attribute name="value">${build.time}</xsl:attribute>
              </attribute>
              <section name="com/l2fprod/common">
                <attribute name="Specification-Title">
                  <xsl:attribute name="value">${project.fullname}</xsl:attribute>
                </attribute>
                <attribute name="Specification-Version">
                  <xsl:attribute name="value">${version}</xsl:attribute>
                </attribute>
                <attribute name="Specification-Vendor" value="L2FProd.com"/>
                <attribute name="Implementation-Title">
                  <xsl:attribute name="value">${project.fullname}</xsl:attribute>
                </attribute>
                <attribute name="Implementation-Version">
                  <xsl:attribute name="value">${version}</xsl:attribute>
                </attribute>
                <attribute name="Implementation-Vendor" value="L2FProd.com"/>
              </section>
              <attribute name="Main-Class" value="{@mainclass}"/>
              <xsl:choose>
                <xsl:when test="@isAll='true'">
                  <xsl:for-each select="//bean">
                    <section name="{@name}">
                      <attribute name="Java-Bean" value="True"/>
                    </section>
                  </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:for-each select=".//bean">
                    <section name="{@name}">
                      <attribute name="Java-Bean" value="True"/>
                    </section>
                  </xsl:for-each>
                </xsl:otherwise>
              </xsl:choose>
            </manifest>
          </jar>
        </xsl:for-each>
      </target>

      <target name="javadoc" depends="init">
        <property name="dist.javadoc.dir">
          <xsl:attribute name="value">${dist.dir}/docs/api</xsl:attribute>
        </property>

        <mkdir>
          <xsl:attribute name="dir">${dist.javadoc.dir}</xsl:attribute>
        </mkdir>

        <javadoc
          private="false"
          protected="true"
          version="false" author="false" use="false"
          stylesheetfile="xdocs/javadoc.css"
          splitindex="true">
          <xsl:attribute name="windowtitle">${project.fullname} ${version} API</xsl:attribute>
          <xsl:attribute name="doctitle">${project.fullname} ${version}</xsl:attribute>
          <xsl:attribute name="bottom">Copyright (c) ${year} L2FProd.com. All Rights Reserved.</xsl:attribute>
          <xsl:attribute name="sourcepath">${build.dir}/src/all</xsl:attribute>
          <xsl:attribute name="destdir">${dist.javadoc.dir}</xsl:attribute>

          <fileset>
            <xsl:attribute name="dir">${build.dir}/src/all</xsl:attribute>
            <xsl:for-each select="//javadoc">
              <xsl:apply-templates select="include"/>
            </xsl:for-each>
          </fileset>
        </javadoc>
      </target>

    </project>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*[. != '']"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
