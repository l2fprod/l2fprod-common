/* Created by JReleaseInfo AntTask from Open Source Competence Group */
/* Creation date Sun Jul 01 10:50:18 CEST 2018 */
package com.l2fprod.common;

import java.util.Date;

/**
 * This class provides information gathered from the build environment.
 * 
 * @author JReleaseInfo AntTask
 */
public class Version {


   /** buildDate (set during build process to 1530435018289L). */
   private static Date buildDate = new Date(1530435018289L);

   /**
    * Get buildDate (set during build process to Sun Jul 01 10:50:18 CEST 2018).
    * @return Date buildDate
    */
   public static final Date getBuildDate() { return buildDate; }


   /** project (set during build process to "l2fprod-common"). */
   private static String project = new String("l2fprod-common");

   /**
    * Get project (set during build process to "l2fprod-common").
    * @return String project
    */
   public static final String getProject() { return project; }


   /** year (set during build process to "2005-2009"). */
   private static String year = new String("2005-2009");

   /**
    * Get year (set during build process to "2005-2009").
    * @return String year
    */
   public static final String getYear() { return year; }


   /** version (set during build process to "7.4-SNAPSHOT"). */
   private static String version = new String("7.4-SNAPSHOT");

   /**
    * Get version (set during build process to "7.4-SNAPSHOT").
    * @return String version
    */
   public static final String getVersion() { return version; }


   /** buildTimestamp (set during build process to "07/01/2018 10:50 AM"). */
   private static String buildTimestamp = new String("07/01/2018 10:50 AM");

   /**
    * Get buildTimestamp (set during build process to "07/01/2018 10:50 AM").
    * @return String buildTimestamp
    */
   public static final String getBuildTimestamp() { return buildTimestamp; }

}
