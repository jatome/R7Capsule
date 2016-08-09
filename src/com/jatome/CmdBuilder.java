/** Author: JATOME (José A. Tomé)
 ** Description:
 ------
 */
package com.jatome;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdBuilder {

    private MyLogger log = new MyLogger();

    //Constructor
    public CmdBuilder(){
    }

    //Build command line to make compression with 7zip
    // returns: List<String> type with command line.
    public List<String> cmdBuildCompress(HashMap<String,String> hmProfile){

        List<String> cmd = new ArrayList<String>(); //Lista de Strings para almacenar cada uno de los elementos de la línea de comandos.
        cmd.add("cmd");
        cmd.add("/c");
        String[] splitCmdLine = hmProfile.get("compressCmdLine").split("\\s+");
        for (int i=0; i<splitCmdLine.length; i++){
            if (splitCmdLine[i].equals("compressTarget")) {
                splitCmdLine[i] = hmProfile.get("compressTarget");
            }
            if (splitCmdLine[i].equals("compressSource")) {
                splitCmdLine[i] = hmProfile.get("compressSource");
            }
            cmd.add(splitCmdLine[i]);
        }
        return cmd;
    }


    // Construye línea comando para realizar la transferencia de datos con rsync
    public List<String> cmdBuildTransfer(HashMap<String,String> hmProfile){
        List<String> cmd = new ArrayList<String>();
        cmd.add("cmd");
        cmd.add("/c");
        String[] splitCmdLine = hmProfile.get("transferCmdLine").split("\\s+");
        System.out.println(splitCmdLine.length);
        for (int i=0; i<splitCmdLine.length; i++){
            cmd.add(splitCmdLine[i]);
        }
        return cmd;
    }


    //Lanza comandos contra la línea de comandos
    //Launch command string
    // cmd --> command string to execute
    // dir --> directory where execute the command
    public void cmdLauncher(List<String> cmd, String dir){
        try {
            String commandline = "";
            for (String s : cmd){
                commandline = commandline + s + " ";
                //System.out.print(s+" ");
            }
            commandline.trim();
            log.WriteLog("Command line: " + commandline);
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(new File(dir));
            pb.redirectErrorStream(true);
            pb.inheritIO(); //redirect io external app to this app
            Process prc = pb.start(); //start external app
            InputStream is = prc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine()) !=null){
                System.out.print(line+"\b\r");
                System.out.flush();
            }
            prc.waitFor();
        }
        catch (IOException | InterruptedException e1) {
            System.out.println("QUE PAZA" + " --> " + e1);
        }
    }

    //Procesa la unidad en función si se ha especificado un nombre de volumen o una letra de unidad
    public String driveParser(String path){

        String rpath = path;
        if (path.indexOf("@") == 0) { //If path starts by @, it specify the volume name of the drive
            int index = path.indexOf(":");
            String mylabel = path.substring(1,index);
            File[] letterDrives = getDriveLetters(); //it gets all the drive letters installed in the system
            for (File d : letterDrives){//iterate letter drives looking for the label of the volume
                String label = getDriveVolumeName(d);
                if (label.equals(mylabel)){ //it has found the driver with the volume name specified
                    rpath = d.toString()+path.substring(index+2);
                    break;
                }
            }
            //volume name not found
            //log.WriteLog("Error. Drive with the Volume Name specified not found.");
            //System.exit(1);
        }
        return rpath;
    }

    /**
     *
     * @return --> File[] -- A list of existing drive letters
     */
    public File[] getDriveLetters(){
        return File.listRoots();
    }

    //Retorn the volume name a the storage drive.
    //Parameters: driveLetter --> (String) storage drive letter. The correct format would be: LETTER:\
    //                            example: C:\
    public String getDriveVolumeName(File drive){

        String driveLetter = drive.toString();
        driveLetter = driveLetter.substring(0,3); //Get only 3 first characters

        if (!Character.isLetter(driveLetter.charAt(0))){ //first character only letter
            log.WriteLog("Error in getDriveVolumeName: no valid parameter");
            return "";
        }
        if (!driveLetter.endsWith(":\\")){ //after the letter only :\
            log.WriteLog("Error in getDriveVolumeName: no valid parameter");
            return "";
        }

        FileSystemView view = FileSystemView.getFileSystemView();
        //File drive = new File(driveLetter);
        String label = view.getSystemDisplayName(drive); //Return the volume label + the drive letter. ex: LABELNAME (C:)
        if (label == null) { return ""; }
        label = label.trim();
        if (label == null || label.length() < 1) { return ""; }
        int index = label.lastIndexOf(" (");
        if (index > 0) {
            label = label.substring(0, index);
        }
        //System.out.println(label);
        return label;
    }
}
