package com.turkerozturk.console;

import static com.turkerozturk.library.fat32.helpers.OtherHelper.prettyPrintNode;

/***********************************************************************
 * Copyright 2023 Turker Ozturk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***********************************************************************/



import java.io.IOException;
import java.util.Map;

import com.turkerozturk.library.fat32.Fat32Partition;
import com.turkerozturk.library.fat32.FsNode;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author u
 */
public class Main {

    /* KULLANICIYA GÖSTERİLECEK TÜM METODLARIN TEST EDİLMESİ */
    public static void main(String args[]) throws IOException {

        /*  */
        //String logicalDiskImageUri = "D:\\fat32logical.img";
        String logicalDiskImageUri = "C:\\tmp\\fat32images\\USBLogicalFAT321GBFSF.dig";
        Fat32Partition partition = new Fat32Partition(logicalDiskImageUri);
        //y(partition);
        //x(partition);
        /* EXAMPLE */
        //partition.showPartitionBootsectorValues();
        /* EXAMPLE */
        //runPrintAllPathsInTheFileSystemExample(partition);
        /* EXAMPLE */
        //runPrintAllFilePathsInTheFileSystemExample(partition);
        /* EXAMPLE */
        //runPrintAllFolderPathsInTheFileSystemExample(partition);
        /* EXAMPLE */
        //runPrintAllFolderPathsInTheFileSystemExample(partition);
        /* EXAMPLE */
        //runPrintAllFolderPathsTreeInTheFileSystemExample(partition);
        /* EXAMPLE */
        //runFileInformationExample(partition);
        /* EXAMPLE */
        //runExtractFileExample(partition);
        /* EXAMPLE */
        //runFolderInformationExample(partition);
        /* EXAMPLE */
        //runGetDirectoryContentsExample(partition);
        /* EXAMPLE */
        //runExtractFolderExample(partition);

    }
    
     /* EXAMPLE */
    static void y(Fat32Partition partition) {
        Map<String, FsNode> m = partition.getFat32Parser().getDataArea().getDeletedPathMap();
        System.out.println("" + m.size());
        for (Map.Entry<String, FsNode> entry : m.entrySet()) {
            FsNode fsNode = entry.getValue();

          //  String firstClusterIdOfFileOrFolder = 

            // System.out.println(firstClusterIdOfFileOrFolder+" = " + fsNode.getShortName());
           // if (!fsNode.getEntryType().equals(EntryType.DOTDIRECTORY)) {// | !fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY)) {
                System.out.println(fsNode.getEntryType() + "\t " + fsNode.getFirstDataClusterIdOfFile() + "\t" + fsNode.getAbsolutePath() + "\t" + fsNode.isDeleted());
               // System.out.println(fsNode.getEntryType() + " " + firstClusterIdOfFileOrFolder + " = " + fsNode.getLongFullPath());
          

//  }

        }

    }


    /* EXAMPLE */
    /*
    static void x(Fat32Partition partition) {
        Map<Long, FsNode> m = partition.getFat32Parser().getDataArea().getMap();
        for (Map.Entry<Long, FsNode> entry : m.entrySet()) {
            Long firstClusterIdOfFileOrFolder = entry.getKey();
            FsNode fsNode = entry.getValue();

            // System.out.println(firstClusterIdOfFileOrFolder+" = " + fsNode.getShortName());
           // if (!fsNode.getEntryType().equals(EntryType.DOTDIRECTORY)) {// | !fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY)) {
                System.out.println(fsNode.getEntryType() + " " + firstClusterIdOfFileOrFolder + " = " + fsNode.getAbsolutePath());
          //  }

        }

    }
    */

    /* EXAMPLES */
    static void runPrintAllPathsInTheFileSystemExample(Fat32Partition partition) {

        /* Gives all full paths in the filesystem as String array */
        String[] allPaths = new String[partition.getAllPaths().size()];
        partition.getAllPaths().toArray(allPaths);//.getAllPaths.toArray(strings);        
        for (String path : allPaths) {
            System.out.println(path);
        }
        System.out.println("Total files and folders count: " + partition.getAllPaths().size());

    }

    static void runPrintAllFilePathsInTheFileSystemExample(Fat32Partition partition) {

        /* Gives all full paths in the filesystem as String array */
        String[] allPaths = new String[partition.getAllPaths().size()];
        partition.getAllPaths().toArray(allPaths);//.getAllPaths.toArray(strings);   
        long counter = 0;
        for (String pathString : allPaths) {
            if (!(pathString.endsWith("/.") | pathString.endsWith("/.."))) {
                counter++;

                System.out.println(pathString);

            }
        }
        System.out.println("Total files count: " + counter);

    }

    static void runPrintAllFolderPathsInTheFileSystemExample(Fat32Partition partition) {

        /* Gives all full paths in the filesystem as String array */
        String[] allPaths = new String[partition.getAllPaths().size()];
        partition.getAllPaths().toArray(allPaths);//.getAllPaths.toArray(strings);
        long counter = 0;
        for (String pathString : allPaths) {
            if ((pathString.endsWith("/.") | pathString.endsWith("/.."))) {
                counter++;

                System.out.println(pathString);

            }
        }
        System.out.println("Total folders count: " + counter);

    }

    static void runPrintAllFolderPathsTreeInTheFileSystemExample(Fat32Partition partition) {

        FsNode rootNode = partition.getRootNode();
        //FsNode rootNode = partition.getFileInformation(0L);
        prettyPrintNode(rootNode);

    }

    /*
    static void runFileInformationExample(Fat32Partition partition) {

        // Gives the file information if you know the full path of it //
        //TODO hata vermeyecek sekle geti.
        //String fileUri = "/TANGO/Canaro, Francisco/Candombe/17 Los Ejes De Mi Carreta.mp3";
        //String fileUri = "/TANGO/Canaro, Francisco/.";//TODO problemli
        String fileUri = "/";//TODO problemli

        FsNode fileInformation = partition.getFsNodeInformation(fileUri);
        prettyPrintNode(fileInformation);
        System.out.println("TEST 02 completed.");

    }
    */

    /*
    static void runFolderInformationExample(Fat32Partition partition) {

        // bir klasör hakkında bilgi çekilmesi //
        //String klasorUri = "/TANGO/Canaro, Francisco/Candombe/.";
        //String klasorUri = "/TANGO/Canaro, Francisco/Candombe/";
        String klasorUri = "/TANGO/Canaro, Francisco/Candombe";
        FsNode fileInformation = partition.getFsNodeInformation(klasorUri);
        prettyPrintNode(fileInformation);
        System.out.println("TEST 04 completed.");

    }
    */

    /*
    static void runExtractFileExample(Fat32Partition partition) throws IOException {

        // bir dosyanın extract edilmesi //
        String outputPath = "D:\\";
        String sourceUri = "/TANGO/Canaro, Francisco/Candombe/17 Los Ejes De Mi Carreta.mp3";
        FsNode fileInformation2 = partition.getFsNodeInformation(sourceUri);
        partition.extractFile(fileInformation2, outputPath);
        System.out.println("TEST 03 completed.");

    }
    */

    /**
     * TODO kontrol et tam düzgün çalışmıyor.
     *
     * @param partition
     * @throws IOException
     */
    /*
    static void runGetDirectoryContentsExample(Fat32Partition partition) throws IOException {

        // Gives Full paths of a directory contents, but not recursive. //
        //String directoryUri = "/";
        //String directoryUri = "/TANGO/Canaro, Francisco/Candombe";
        String directoryUri = "/TANGO";

        ArrayList<FsNode> directoryContents;
        try {
            directoryContents = partition.getDirectoryContents(directoryUri);
            for (Iterator<FsNode> it = directoryContents.iterator(); it.hasNext();) {
                FsNode fsNode = it.next();
                System.out.println("Content of directory: " + fsNode.getName());
            }
            System.out.println("File and Folder count in first level: " + directoryContents.size());

        } catch (IOException | PathNotExistException ex) {
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        System.out.println("TEST 05 completed.");
    }
    */

    /**
     * TODO düzgün çalışmıyor
     *
     * @param partition
     * @throws IOException
     */
    /*
    static void runExtractFolderExample(Fat32Partition partition) throws IOException {

        // extract the folder if you know the fullpaths. //
        String outputPath2 = "D:\\";
        String directoryUri2 = "/TANGO/Canaro, Francisco/Candombe";
        ArrayList<FsNode> directoryContents;
        try {
            directoryContents = partition.getDirectoryContents(directoryUri2);
            System.out.println("CONTENTS SIZE: " + directoryContents.size());
            for (Iterator<FsNode> it = directoryContents.iterator(); it.hasNext();) {
                FsNode fsNode = it.next();
                FsNode fileInformation4 = partition.getFsNodeInformation(fsNode.getShortFullPath());//TODO fullpath yap+
                prettyPrintNode(fsNode);
                //if (fileInformation4.getEntryType() == DirEntryType.FILE) {
                //   partition.extractFile(fileInformation4, outputPath2);
                //}
                //System.out.println("Content of directory: " + fsNode.getLongName());
            }

        } catch (IOException | PathNotExistException ex) {
            //Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }

        System.out.println("TEST 06 completed.");
    }
    */

}
