package com.jatome;

import java.io.File;

/**
 * Created by Casita on 28/07/2016.
 */
public class FileSystemTools {

    /**
     * @return --> home path of my windows user
     */
    public String getHomePath(){
        return System.getProperty("user.home");
    }


    /**
     * Description: create a new folder if it doesn't exist
     * @param path --> String with folder to create
     * @return --> boolean true if path created or false otherwise
     */
    public boolean createFolder(String path){
        return (new File(path).mkdirs());
    }
}
