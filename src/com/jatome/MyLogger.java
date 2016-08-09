package com.jatome;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.*;

/**
 * Author: JATOME (José A. Tomé)
 * email:
 */

public class MyLogger {

    private static final Logger logger =Logger.getLogger(MyLogger.class.getName());

    /**
     * Constructor
     *
     */
    public MyLogger(){
        //comprobar que ya ha sido definido logFile mediante el otro constructor
    }

    /**
     * Constructor
     * @param logFile --> String with path and log file name
     */
    public MyLogger(String logFile){
        try {

            TimeTools timeStamp = new TimeTools();
            //logger = Logger.getLogger(MyLogger.class.getName());
            FileHandler fh = new FileHandler(logFile);
            Handler ch = new ConsoleHandler();
            fh.setFormatter(new Formatter(){
                public String format(LogRecord record) {
                    return  "["+timeStamp.getTimeStamp()+"] "
                            + record.getLevel() + ": "
                            + record.getMessage() + "\n";
                }
            });
            logger.addHandler(fh); //Add file log to logger

            logger.setUseParentHandlers(false);
            ch.setFormatter(new Formatter(){
                public String format(LogRecord record) {
                    return  "["+timeStamp.getTimeStamp()+"] "
                            + record.getLevel() + ": "
                            + record.getMessage() + "\n";
                }
            });
            logger.addHandler(ch); //Add file log to logger
            Version ver = new Version();
            WriteLog("Log file "+logFile+" created by " + ver.getAppName() + " v." + ver.getVersion() + " (" + ver.getStatus() + ")");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Get the class MyLogger previously instanced with the constructor MyLogger(String logFile) by the main class.
     * The other classes will use use this method to get the class with the logFile already defined.
     */
    private MyLogger getLogFile(){
        return new MyLogger();
    }

    /**
     * Write a text in log file
     * @param text --> String to write in log file
     */
    public void WriteLog(String text){
        logger.log(Level.INFO, text);
    }
}
