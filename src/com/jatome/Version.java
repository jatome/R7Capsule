package com.jatome;

/** Author: JATOME (José A. Tomé)
 ** Description:
 ------
 */
public class Version {
    private static final String appName = "R7Capsule";
    private static final String author = "José Antonio Tomé";
    private static final String dateRelease = "2016-07-29";
    private static final String version = "01.00";
    private static final String status = "alfa";

    public String getAppName(){
        return appName;
    }

    public String getAuthor(){
        return author;
    }

    public String getVersion(){
        return version;
    }

    public String getStatus(){
        return status;
    }

    public String getDateRelease(){ return dateRelease; }

    public String getCopyright(){
        return (appName + " v." + version + " (" + status + ")\nCopyright (c) 2016 by " + author + " (" + dateRelease + ")");
    }


}
