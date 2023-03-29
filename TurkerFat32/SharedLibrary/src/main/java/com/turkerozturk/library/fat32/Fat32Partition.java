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


package com.turkerozturk.library.fat32;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Map;

import com.turkerozturk.library.fat32.enums.EntryType;
import com.turkerozturk.library.fat32.exceptions.PathNotExistException;
import com.turkerozturk.library.fat32.helpers.OtherHelper;

/**
 *
 * @author u
 */
//public final class Fat32Partition {
public class Fat32Partition {

    private FsNode rootNode;

    private static final String PATH_SEPARATOR = "/";
    private static final String FILE_SLACK = ".FileSlack";

    private final RandomAccessFile logicalDiskImageFile;

    private Fat32Parser fat32Parser;
    Map<String, Long> allPathsAndIdsInFileSystem; //eskimek uzere
    Map<String, FsNode> allPathsAndFsNodesInFileSystem; //2017-11-16 itibari ile geliştiriliyor.

    ArrayList<String> allPaths;

    private static final String SLASHDOT = "/.";

    private static final int DEFAULT_SELECTED_FAT_TABLE_NUMBER = 0;

    public Fat32Partition(String logicalDiskImageUri) throws IOException {
        this.logicalDiskImageFile = new RandomAccessFile(logicalDiskImageUri, "r");
        fat32Parser = new Fat32Parser(this.logicalDiskImageFile);
        fat32Parser.setLogicalDiskImageUri(logicalDiskImageUri);
        fat32Parser.init();
        //this.mapAllPathsAndIds();//TODO bu su anda calisan ama problemli olan. cunku id numaralari overwrite oluyor.
        this.pathMapAll(); // 2017-11-16 TODO path, FsNode şeklinde maplemeliyiz ki unique olsun, sonra da this.mapAllPathsAndIds() metodunu devre dışı bırak.
        this.loadAllPaths();

        this.rootNode = this.fat32Parser.getDataArea().getRootNode();
        //System.out.println("AAA" + this.rootNode.getName());
        //this.rootNode = this.getFsNodeInformation(this.getFat32Parser().getReservedArea().getFat32PartitionBootSector().getBpbRootCluster() - 2);

    }

    /**
     *
     */
    /*
    private void mapAllPathsAndIds() {

        allPathsAndIdsInFileSystem = new HashMap<>();
        for (Map.Entry<Long, FsNode> entry : this.getFat32Parser().getDataArea().getMap().entrySet()) {
            Long firstClusterIdOfFileOrFolder = entry.getKey();
            FsNode fsNode = entry.getValue();
            //if (fsNode.getEntryType() != DirEntryType.DOTDIRECTORY && fsNode.getEntryType() != DirEntryType.DOTDOTDIRECTORY) {
            this.allPathsAndIdsInFileSystem.put(fsNode.getAbsolutePath(), firstClusterIdOfFileOrFolder);
            //System.out.println(fsNode.getAbsolutePath() + "\t" + firstClusterIdOfFileOrFolder);
            //}
        }
    }
     */
 /* 2017-11-16 yeni metod */
    private void pathMapAll() {
        /*
        allPathsAndFsNodesInFileSystem = new HashMap<>();
        for (Map.Entry<String, FsNode> entry : this.getFat32Parser().getDataArea().getPathMap().entrySet()) {
            String path = entry.getKey();
            FsNode fsNode = entry.getValue();
            //if (fsNode.getEntryType() != DirEntryType.DOTDIRECTORY && fsNode.getEntryType() != DirEntryType.DOTDOTDIRECTORY) {
            this.allPathsAndFsNodesInFileSystem.put(fsNode.getAbsolutePath(), fsNode);
            //System.out.println(fsNode.getAbsolutePath() + "\t" + firstClusterIdOfFileOrFolder);
            //}
        }
         */
        this.allPathsAndFsNodesInFileSystem = this.getFat32Parser().getDataArea().getPathMap();
    }

    private void loadAllPaths() {
        allPaths = new ArrayList<String>();//[this.allPathsAndIdsInFileSystem.size()];
        for (Map.Entry<String, FsNode> entry : this.allPathsAndFsNodesInFileSystem.entrySet()) {
            String pathString = entry.getKey();
            //if (!(pathString.endsWith("/.") | pathString.endsWith("/.."))) {

            allPaths.add(pathString);
            //}
        }
    }

    /*eski
        private void loadAllPaths() {
        allPaths = new ArrayList<>();//[this.allPathsAndIdsInFileSystem.size()];
        for (Map.Entry<String, Long> entry : this.allPathsAndIdsInFileSystem.entrySet()) {
            String pathString = entry.getKey();
            //if (!(pathString.endsWith("/.") | pathString.endsWith("/.."))) {

            allPaths.add(pathString);
            //}
        }
    }
     */
    public Fat32Parser getFat32Parser() {
        return fat32Parser;
    }

    void setFat32Parser(Fat32Parser fat32Parser) {
        this.fat32Parser = fat32Parser;
    }

    Map<String, Long> getAllPathsAndIdsInFileSystem() {
        return allPathsAndIdsInFileSystem;
    }

    void setAllPathsAndIdsInFileSystem(Map<String, Long> allPathsAndIdsInFileSystem) {
        this.allPathsAndIdsInFileSystem = allPathsAndIdsInFileSystem;
    }

    /**
     *
     * @param uri full path of file
     * @return information about the file. Name, size, created accessed written
     * dates, file flags, technical location in the partition etc.
     */
    /*
    public FsNode getFsNodeInformation(String uri) {

        long clusterId;
        if (uri == null | "".equals(uri) | "/".equals(uri) | "/.".equals(uri)) { //root cluster
            clusterId = this.getFat32Parser().getReservedArea().getFat32PartitionBootSector().getBpbRootCluster();
            return this.getFsNodeInformation(clusterId);
        } else if (uri.endsWith("/")) { //uri correction
            uri = uri + ".";
        } else if (uri.endsWith("/..")) { //nothing to do in case of parent folder.
            return null;
        }

        if (this.getAllPathsAndIdsInFileSystem().containsKey(uri)) { //normal
                                                    System.out.println("ddd" + uri);

            clusterId = this.getAllPathsAndIdsInFileSystem().get(uri);
            return this.getFsNodeInformation(clusterId);
        } else if (this.getAllPathsAndIdsInFileSystem().containsKey(uri + "/.")) { //uri correction
            clusterId = this.getAllPathsAndIdsInFileSystem().get(uri + "/.");
            return this.getFsNodeInformation(clusterId);
        }
        return null;
    }
     */
    /**
     *
     * @param firstClusterIdOfFileData beginning clusterId of file data. The
     * parser has all nodes mapped with beginning clusterids in the memory.
     * @return information about the file. Name, size, created accessed written
     * dates, file flags, technical location in the partition etc.
     */
    //public FsNode getFsNodeInformation(Long firstClusterIdOfFileData) {
    /*
    public FsNode getFsNodeInformation(String absolutePath) {

        //FsNode fsNode = this.fat32Parser.getDataArea().getMap().get(firstClusterIdOfFileData);
        FsNode fsNode = this.fat32Parser.getDataArea().getPathMap().get(absolutePath);

        //File file = new File("");
        //this.
        return fsNode;
    }
     */
    /**
     *
     * @param uri full path of directory
     * @return one level contents of given directory, files and folders
     * @throws IOException
     * @throws PathNotExistException
     */
    /*
    public ArrayList<FsNode> getDirectoryContents(String uri) throws IOException, PathNotExistException {
        ArrayList<FsNode> directoryContents = new ArrayList<>();
        FsNode directoryNode = getFsNodeInformation(uri);

        if (directoryNode != null) {
            if (directoryNode.getEntryType() == EntryType.DIRECTORY | directoryNode.getEntryType() == EntryType.DOTDIRECTORY) {
            //if (directoryNode.getEntryType() == EntryType.DIRECTORY | directoryNode.getEntryType() == EntryType.DOTDIRECTORY | directoryNode.getEntryType() == EntryType.DOTDOTDIRECTORY) {
    
                Directory directory = new Directory(logicalDiskImageFile, this.fat32Parser.getReservedArea().getFat32PartitionBootSector(), this.fat32Parser.getFatArea().getFatTables()[DEFAULT_SELECTED_FAT_TABLE_NUMBER], directoryNode);

                directoryContents = directory.getChildren();
            }
        } else {
            System.out.println("[Fat32Partition.getDirectoryContents] metodundayim");
        }

        //throw new PathNotExistException("Path not found: " + directoryUri);
        return directoryContents;

    }
     */
 /*
        public ArrayList<FsNode> getDirectoryContents(String uri) throws IOException, PathNotExistException {
        String directoryUri = uri + SLASHDOT;
        
        
        if (this.getAllPathsAndIdsInFileSystem().containsKey(directoryUri)) {
            long clusterId = this.getAllPathsAndIdsInFileSystem().get(directoryUri);
            FsNode directoryNode = this.getFsNodeInformation(clusterId);
            Directory directory = new Directory(logicalDiskImageFile, this.fat32Parser.getReservedArea().getFat32PartitionBootSector(), this.fat32Parser.getFatArea().getFatTables()[DEFAULT_SELECTED_FAT_TABLE_NUMBER], directoryNode);
            
            return directory.getChildren();
        }

        throw new PathNotExistException("Path not found: " + directoryUri);

    }
     */
    /**
     *
     * @param fsNode Node of the file
     * @param outputPathAsString path to extract the file
     * @throws IOException
     */
    /*
    public void extractFile(FsNode fsNode, String outputPath) throws IOException {
        this.fat32Parser.extractFile(fsNode, outputPath);
    }
     */
 /*  */
    public void extractFile(FsNode fsNode, String outputPathAsString) throws IOException {

        long clusterSizeAsByte = this.fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte();

        ArrayList<Long> clusterIdsInChain = this.fat32Parser.getFatArea().getFatTables()[DEFAULT_SELECTED_FAT_TABLE_NUMBER].getAChainOfClusterIdsIFromFatTable(fsNode.getFirstDataClusterIdOfFile());//UInt32

        // once directory entrylerin kapladigi yer kadar byte ayiriyoruz //
        long bytesLeft = fsNode.getSizeAsBytes();
        //byte[] totalBytesInTheChain = new byte[clusterIdsInChain.size() * (int) clusterSizeAsByte];
        //byte[] totalBytesInTheChain = new byte[(int) bytesLeft];

        String outputFileFullPath = outputPathAsString + PATH_SEPARATOR + fsNode.getName();
        File outputFile = new File(outputFileFullPath);

        try (FileOutputStream outputStream = new FileOutputStream(outputFile);) {
            // sonra eger clusteridlerde problem olmadigi surece onlarin clusterlarindaki byte degerlerini bizim totalBytesInTheChain degiskenine yukluyoruz.//
            for (int i = 0; i < clusterIdsInChain.size(); i++) {

                long relativeClusterId = clusterIdsInChain.get(i);//UInt32
                long absoluteClusterId = relativeClusterId - 2; //UInt32
                long clusterAddress = this.fat32Parser.getReservedArea().getFat32PartitionBootSector().getDataOffsetAsByte() + (absoluteClusterId * clusterSizeAsByte); //UInt64
                this.logicalDiskImageFile.seek(clusterAddress);//UInt64

                if (bytesLeft >= clusterSizeAsByte) {

                    byte[] dataBytes = new byte[(int) clusterSizeAsByte];
                    this.logicalDiskImageFile.read(dataBytes);
                    //System.arraycopy(dataBytes, 0, totalBytesInTheChain, (int) clusterSizeAsByte * i, (int) clusterSizeAsByte);
                    outputStream.write(dataBytes);  //write the bytes and your done. 

                    bytesLeft -= clusterSizeAsByte;

                } else {
                    byte[] dataBytes = new byte[(int) bytesLeft];
                    this.logicalDiskImageFile.read(dataBytes);
                    //System.arraycopy(dataBytes, 0, totalBytesInTheChain, (int) clusterSizeAsByte * i, (int) bytesLeft);
                    outputStream.write(dataBytes);  //write the bytes and your done. 

                    byte[] slackSpaceBytes = new byte[(int) clusterSizeAsByte - (int) bytesLeft];
                    this.logicalDiskImageFile.read(slackSpaceBytes);
                    String slackSpaceOutputFileFullPath = outputPathAsString + PATH_SEPARATOR + fsNode.getName() + FILE_SLACK;
                    File slackSpaceOutputFile = new File(slackSpaceOutputFileFullPath);
                    try (FileOutputStream slackSpaceOutputStream = new FileOutputStream(slackSpaceOutputFile);) {
                        slackSpaceOutputStream.write(slackSpaceBytes);  //write the bytes and your done. 

                    }

                    //System.out.println("son cluster" + totalBytesInTheChain.length);
                }

            } //for

            //       File outputFile = new File();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //https://stackoverflow.com/questions/9198184/setting-file-creation-timestamp-in-java
        this.setFileDateTimes(Paths.get(outputFileFullPath), fsNode);

    }

    /**
     * CALISAN FAKAT BIRKAC DOSYAYI HEAP SPACE DOLDURMASI YUZUNDEN EXTRACT
     * EDEMEYEN extractFile metodu yedegi: *
     */
    /*
    public void extractFile(FsNode fsNode, String outputPathAsString) throws IOException {

        long clusterSizeAsByte = this.fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte();

        ArrayList<Long> clusterIdsInChain = this.fat32Parser.getFatArea().getFatTables()[DEFAULT_SELECTED_FAT_TABLE_NUMBER].getAChainOfClusterIdsIFromFatTable(fsNode.getFirstDataClusterIdOfFile());//UInt32

        // once directory entrylerin kapladigi yer kadar byte ayiriyoruz //
        long bytesLeft = fsNode.getSizeAsBytes();
        //byte[] totalBytesInTheChain = new byte[clusterIdsInChain.size() * (int) clusterSizeAsByte];
        byte[] totalBytesInTheChain = new byte[(int) bytesLeft];

        // sonra eger clusteridlerde problem olmadigi surece onlarin clusterlarindaki byte degerlerini bizim totalBytesInTheChain degiskenine yukluyoruz.//
        for (int i = 0; i < clusterIdsInChain.size(); i++) {

            long relativeClusterId = clusterIdsInChain.get(i);//UInt32
            long absoluteClusterId = relativeClusterId - 2; //UInt32
            long clusterAddress = this.fat32Parser.getReservedArea().getFat32PartitionBootSector().getDataOffsetAsByte() + (absoluteClusterId * clusterSizeAsByte); //UInt64
            this.logicalDiskImageFile.seek(clusterAddress);//UInt64

            if (bytesLeft >= clusterSizeAsByte) {

                byte[] dataBytes = new byte[(int) clusterSizeAsByte];
                this.logicalDiskImageFile.read(dataBytes);
                System.arraycopy(dataBytes, 0, totalBytesInTheChain, (int) clusterSizeAsByte * i, (int) clusterSizeAsByte);
                bytesLeft -= clusterSizeAsByte;

            } else {
                byte[] dataBytes = new byte[(int) bytesLeft];
                this.logicalDiskImageFile.read(dataBytes);
                System.arraycopy(dataBytes, 0, totalBytesInTheChain, (int) clusterSizeAsByte * i, (int) bytesLeft);

                byte[] slackSpaceBytes = new byte[(int) clusterSizeAsByte - (int) bytesLeft];
                this.logicalDiskImageFile.read(slackSpaceBytes);

                //System.out.println("son cluster" + totalBytesInTheChain.length);
            }

        } //for
        String outputFileFullPath = outputPathAsString + PATH_SEPARATOR + fsNode.getName();
        File outputFile = new File(outputFileFullPath);
        //       File outputFile = new File();

        try (FileOutputStream outputStream = new FileOutputStream(outputFile);) {

            outputStream.write(totalBytesInTheChain);  //write the bytes and your done. 

        } catch (Exception e) {
            e.printStackTrace();
        }
        //outputFile.setLastModified(fsNode.getWrittenDate().getTime());
        BasicFileAttributeView attributes = Files.getFileAttributeView(Paths.get(outputFileFullPath), BasicFileAttributeView.class);
        FileTime creationFileTime = FileTime.fromMillis(fsNode.getCreatedDate().getTime());
        FileTime writtenFileTime = FileTime.fromMillis(fsNode.getWrittenDate().getTime());
        FileTime accessedFileTime = FileTime.fromMillis(fsNode.getAccessedDate().getTime());
        attributes.setTimes(creationFileTime, writtenFileTime, accessedFileTime);

        
    }
    
    
    
    
     */
    public void setFileDateTimes(Path preparedFsNodePath, FsNode fsNode) throws IOException {
        BasicFileAttributeView attributes = Files.getFileAttributeView(preparedFsNodePath, BasicFileAttributeView.class);

        FileTime creationFileTime = null;
        if (fsNode.getCreatedDate() != null) {
            creationFileTime = FileTime.fromMillis(fsNode.getCreatedDate().getTime());
        }

        FileTime writtenFileTime = null;
        if (fsNode.getWrittenDate() != null) {
            writtenFileTime = FileTime.fromMillis(fsNode.getWrittenDate().getTime());
        }

        FileTime accessedFileTime = null;
        if (fsNode.getAccessedDate() != null) {
            accessedFileTime = FileTime.fromMillis(fsNode.getAccessedDate().getTime());
        }
        attributes.setTimes(writtenFileTime, accessedFileTime, creationFileTime);
    }

    /**
     *
     * @param selectedFsNode The folder node which will be extracted from Fat32
     * image file.
     * @param outputPathAsString The user selected extraction Path in the
     * destination filesystem.
     * @throws IOException
     */
    public void extractFolder(FsNode selectedFsNode, String outputPathAsString) throws IOException {
        //System.out.println(selectedFsNode.getFirstDataClusterIdOfFile());       

        if (!selectedFsNode.isDeleted() & selectedFsNode.getEntryType().equals(EntryType.DIRECTORY)) {
            String preparedOutputPathAsString = outputPathAsString + PATH_SEPARATOR + selectedFsNode.getName();
            Path containerFsNodePreparedOutputPath = Paths.get(preparedOutputPathAsString);
            if (!Files.exists(containerFsNodePreparedOutputPath)) {
                Files.createDirectories(containerFsNodePreparedOutputPath);
            }
            this.setFileDateTimes(containerFsNodePreparedOutputPath, selectedFsNode);

            ArrayList<Object> contentsDirectory;

            contentsDirectory = this.getFat32Parser().getDataArea().directoryWalk(selectedFsNode, true);
            //klasorleri de mac tarihlerini verebilmek icin bastan bu yola basvuruyorum yani ayirip once klasorleri hallediyorum
            String preparedFsNodePathAsString;
            Path preparedFsNodePath;
            for (Object object : contentsDirectory) {
                FsNode fsNode = (FsNode) object;

                if (fsNode.getEntryType().equals(EntryType.DIRECTORY)) {
                    preparedFsNodePathAsString = preparedOutputPathAsString + PATH_SEPARATOR + fsNode.getName();
                    preparedFsNodePath = Paths.get(preparedFsNodePathAsString);
                    if (!Files.exists(preparedFsNodePath)) {
                        Files.createDirectory(preparedFsNodePath);
                        this.setFileDateTimes(preparedFsNodePath, fsNode);
                    }
                    extractFolder(fsNode, preparedOutputPathAsString);

                }
            }

            //BITTI TEK SEVIYE BOS KLASORLERIN OLUSTURULMASI
//------------------------------------------------------------------------------------------------------------
            for (Object object : contentsDirectory) {
                FsNode fsNode = (FsNode) object;

                if (fsNode.getEntryType().equals(EntryType.FILE)) {

                    preparedFsNodePathAsString = preparedOutputPathAsString;// + PATH_SEPARATOR + fsNode.getName();

                    File myFile = new File(preparedFsNodePathAsString);

                    Path myDir = Paths.get(myFile.getParent());
                    if (!Files.exists(myDir)) {
                        Files.createDirectories(myDir);
                    }
                    extractFile(fsNode, preparedFsNodePathAsString);

                }
                //   File myFile = new File( "C:/my folder/tree/apple.exe" );
// Now get the path
                //  String myDir = myFile.getParent();
                //  preparedFsNodePath.

            }
        }

    }

    /**
     *
     * @return all file and folder full paths in the filesystem as String array
     */
    public ArrayList<String> getAllPaths() {
        return allPaths;
    }

    void setAllPaths(ArrayList<String> allPaths) {
        this.allPaths = allPaths;
    }

    public FsNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(FsNode rootNode) {
        this.rootNode = rootNode;
    }

    public String showPartitionBootsectorValues() {
        return OtherHelper.prettyPrintPartitionBootSector(fat32Parser.getReservedArea().getFat32PartitionBootSector());
    }

    public Map<String, FsNode> getAllPathsAndFsNodesInFileSystem() {
        return allPathsAndFsNodesInFileSystem;
    }

    public void setAllPathsAndFsNodesInFileSystem(Map<String, FsNode> allPathsAndFsNodesInFileSystem) {
        this.allPathsAndFsNodesInFileSystem = allPathsAndFsNodesInFileSystem;
    }

    public void extractUnallocatedSpace(UnallocatedSpaceNode unallocatedSpaceNode, String outputPathAsString) throws IOException {

        long clusterSizeAsByte = this.fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte();
        String outputFileFullPath = outputPathAsString + PATH_SEPARATOR + unallocatedSpaceNode.getName();
        File outputFile = new File(outputFileFullPath);
        long clusterCount = unallocatedSpaceNode.getSize() / clusterSizeAsByte;
        try (FileOutputStream outputStream = new FileOutputStream(outputFile);) {
            long clusterAddress = unallocatedSpaceNode.getAbsoluteStartOffset();
            this.logicalDiskImageFile.seek(clusterAddress);
            
            byte[] dataBytes = new byte[(int) clusterSizeAsByte];
            for (long i = 0; i < clusterCount; i++) {
                this.logicalDiskImageFile.read(dataBytes);
                outputStream.write(dataBytes);     
            }
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
