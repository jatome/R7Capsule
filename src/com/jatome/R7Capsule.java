/**
 * Author: JATOME (José A. Tomé)
 * Email: jatomfe@gmail.com
 * Description: It reads an xml file whith the configuration to use 7zip to make a compressed backup and rsync to
 *              transfer this backup to another location
 *
 */

//**

//------
//** Version: 01.00

package com.jatome;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class R7Capsule {

    private MyLogger log = new MyLogger();
    private final int NUMARGS = 2;
    //args[0] --> xml profiles file
    //args[1] --> profile to execute from xml file
    //args[2] -->
    public static void main(String[] args) {

        //>>Show about
        Version ver = new Version();
        System.out.println(ver.getCopyright() + "\n");
        //>>Test arguments

        //>>Crear clase de Backup
        R7Capsule bck = new R7Capsule();
        CmdBuilder cmd = new CmdBuilder();
        FileSystemTools fst = new FileSystemTools();
        String logpath = fst.getHomePath()+"\\.R7Capsule";
        fst.createFolder(logpath);
        MyLogger log = bck.initLogFile(logpath+"\\r7.log");
        bck.testAppArguments(args);
        TimeTools chrono = new TimeTools();

        //>>Starting
        log.WriteLog(">>>> STARTING BACKUP...");
        File xmlfile = new File(args[0]); //it gets profile file path
        String profileID = args[1];
        ProfileParser pParser = new ProfileParser (xmlfile, profileID);
        HashMap<String,String> hmProfileContent = new HashMap<String,String>();

        hmProfileContent = pParser.readXML(); // Lectura del fichero XML que contine los perfiles

        String currentdir = System.getProperty("user.dir"); //it gets current system directory

        if (hmProfileContent.get("compressEnable").equals("true")) {//compression is enabled
            log.WriteLog(">>>> Starting Compression...");
            //System.out.println("\nSTARTING COMPRESSION... \n");
            String compressSource = cmd.driveParser(hmProfileContent.get("compressSource"));
            hmProfileContent.put("compressSource", compressSource); //Substitute volume name by drive letter if necessary
            log.WriteLog("Source: "+compressSource);
            String compressTarget = cmd.driveParser(hmProfileContent.get("compressTarget"));
            hmProfileContent.put("compressTarget", compressTarget); //Substitute volume name by drive letter if necessary
            log.WriteLog("Target: "+compressTarget);
            List<String> cmdcompress = cmd.cmdBuildCompress(hmProfileContent); //Se construllen los elementos de la línea de comandos

            chrono.chronoStart();
            cmd.cmdLauncher(cmdcompress, currentdir); //Se lanza el comando de compresión
            chrono.chronoStop();
            System.out.println(chrono.getChronoElapseTime());
            log.WriteLog("Elapsed Time of Compression: " + chrono.getChronoElapseTime());
            log.WriteLog(">>>> Compression finished.");
        }
        if (hmProfileContent.get("transferEnable").equals("true")) {//transfer is enabled
            System.out.println("\nSTARTING DATA TRANSFER...\n");
            List<String> cmdtransfer = cmd.cmdBuildTransfer(hmProfileContent);
            cmd.cmdLauncher(cmdtransfer, currentdir);
        }
        //>>Generates compression command line with 7zip

        //>>Verificar espacio libre en destino y si cabe lo que se ha de enviar.

        //>>Generar linea comandos rsync

        //>>Presentar información sobre el proceso y despedirse :-)
        log.WriteLog(">>>> BACKUP FINISHED.");
    }

    /**
     * Init log file creating a log file.
     * @param path --> String with the path and the log file name
     */
    private MyLogger initLogFile(String path){
        Locale.setDefault(new Locale("EN")); //Automatic text in log file will be in english
        TimeTools tt = new TimeTools();
        int idxdot = path.lastIndexOf(".");
        String p = path.substring(0,idxdot);
        p = p + "_[" + tt.getTimeStamp() + "]" + path.substring(idxdot,path.length()); //insert timestamp in log filename
        return (new MyLogger (p));
    }

    private void testAppArguments(String[] args){
        if (args.length != NUMARGS){
            log.WriteLog("ERROR. Incorrect number of arguments.");
            System.exit(1);
        }
    }

}


