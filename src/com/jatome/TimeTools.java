package com.jatome;

/**
 * Author: JATOME (José A. Tomé)
 * email:
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Class TimeTools
 * Methods related to time
 */
public class TimeTools {

    private long chronoStartms;
    private long chronoStopms;

    /**
     * @return String with date and time with the format: yyyy/MM/dd HH:mm:ss
     */
    public String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    /**
    * Put the actual system time in milliseconds in the private field chronoStartms
    */
    public void chronoStart() {
        chronoStartms = System.currentTimeMillis();
    }

    /**
     * Put the actual system time in milliseconds in the private field chronoEndms
     */
    public void chronoStop() {
        chronoStopms = System.currentTimeMillis();
    }

    /**
     * @return: String with differential time chronoEndms-chronoStartms in the following format:
     *          Days:Hours:Minutes:Seconds:Milliseconds
     */
    public String getChronoElapseTime() {

        long diffms = chronoStopms-chronoStartms;
        /*double aux = (double)(diffms/3600000); //to hours
        int hours = (int)aux;
        aux = (aux-hours)*60; //to minutes
        int minutes = (int)aux;
        aux = (aux-minutes)*60; //to seconds
        int seconds = (int)aux;
        int milliseconds = (int)(aux-seconds)*1000; //to milliseconds
*/
        System.out.println(diffms);

        int days = (int)TimeUnit.MILLISECONDS.toDays(diffms); //to integer days
        int aux = days*24;
        int hours = (int)TimeUnit.MILLISECONDS.toHours(diffms); //remaining hours
        aux = aux + hours*60;
        int minutes = (int)(TimeUnit.MILLISECONDS.toMinutes(diffms)-aux); //remaining minutes
        aux = aux + minutes*60;
        int seconds = (int)(TimeUnit.MILLISECONDS.toSeconds(diffms)-aux); //remaining seconds
        aux = aux + seconds*1000;
        int milliseconds = (int)(diffms-aux); //remaining milliseconds
        return String.format("%02d Days : %02d Hours : %02d Minutes : %02d Seconds : %03d Milliseconds", days, hours, minutes, seconds, milliseconds);
    }

}
