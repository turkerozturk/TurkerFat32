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


package com.turkerozturk.library.fat32.structures.dataarea;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.turkerozturk.library.fat32.FsNode;
import com.turkerozturk.library.fat32.enums.DirectoryEntryAllocationStatus;
import com.turkerozturk.library.fat32.enums.EntryType;
import com.turkerozturk.library.fat32.structures.fatarea.FatArea;
import com.turkerozturk.library.fat32.structures.fatarea.FatTable;
import com.turkerozturk.library.fat32.structures.reservedarea.Fat32PartitionBootSector;

/**
 *
 * @author u
 */
public class DataArea {

    //DataCluster[] dataClusters;
    private RandomAccessFile logicalDiskImageFile;
    private Fat32PartitionBootSector fat32PartitionBootSector;
    private FatArea fatArea;
    private FatTable fatTable;
    private static final int DEFAULT_SELECTED_FAT_TABLE_NUMBER = 0;

    private long rootClusterId;
    private long rootClusterOffset;
    private long rootDirectoryCountOfEntries;

    final static long UINTMASK = 0xFFFFFFFFL;
    final static int USHORTMASK = 0xFFFF;
    final static short UBYTEMASK = 0xFF;

    private static final String PATH_SEPARATOR = "/";

    private int directoryDepthLevel;// -1 = root diye başlattım;
    
    private FsNode rootNode;
    
   private boolean ignoreDirectoryEntries = true;

    //** TODO Bu degiskenleri kullanmayacaksan sil. **//
    //private ArrayList<Long> dirQueue = new ArrayList<>(); //UInt32
    //private String longNameStack;
    //private int longNameStackChecksum; //byte
    //private int longNameStackOrder; //byte    
    /* bunu belki geri donusler icin kullanabiliriz.*/
    //ArrayList<Long> firstClusterNumberOfParentDirectories = new ArrayList<>();//UInt32
    //private int dirLevel;
    /* icinde bulunulan ContentsOfDirectory seviyesini kontrol ederek */
    //private LinkedList<String> currentShortFullPath = new LinkedList<>();
    //private ArrayList<String> shortFullPaths = new ArrayList<>();
    //    LinkedList<String> breadCrumbs = new LinkedList<>();
    /**
     * *
     */
    /* zincirdeki tüm ContentsOfDirectory entryleri buna yükleneceği için bir zincir bittiğinde içini boşaltmak mantıklı olur veya daha iç bir sınıfa taşımak.*/
    public ArrayList<FsNode> fsNodesInChain = new ArrayList<>();

    // private ArrayList<FsNodeTemplate> allFsNodesInFileSystem = new ArrayList();
    /* bu asagidakini kullanmayi sonra birakacagim cunku ayni clusteridsi birden fazla tekrar edince sonuncusu kaydoluyor sadece mape*/
    //private Map<Long, FsNode> map = new HashMap<>();

    /* 2017-11-15 itibariyle bunu gelistirmeye basliyorum cunku unique olan tek set path*/
    private Map<String, FsNode> pathMap = new HashMap<>();
    private Map<String, FsNode> deletedPathMap = new HashMap<>();

    private String workingDirectoryPath = "";//[root]";

  //  private final LinkedList<String> wpath = new LinkedList<>();

    private final LinkedList<FsNode> directoryQueue = new LinkedList<>();

    public DataArea(RandomAccessFile logicalDiskImageFile) throws IOException {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }

    /* burada senden bekledigim, tum entryleri toplayip bir global FSNode arraylistine atman.
     bir de bunlarin hepsinin tamyollarinin oldugu bir liste daha yapman.*/
    public void parseAllEntries(Fat32PartitionBootSector fat32PartitionBootSector, FatArea fatArea, int fatTableNumber) throws IOException {
        this.setFat32PartitionBootSector(fat32PartitionBootSector);
        this.setFatArea(fatArea);
        this.setFatTable(fatArea.getFatTables()[DEFAULT_SELECTED_FAT_TABLE_NUMBER]);

        //this.wpath.add(PATH_SEPARATOR);
        this.rootClusterId = this.fat32PartitionBootSector.getBpbRootCluster();
        this.rootClusterOffset = this.fat32PartitionBootSector.getRootDirFatOffsetAsByte();
        this.rootDirectoryCountOfEntries = this.fat32PartitionBootSector.getBpbRootEntCount();

        /* en ust ContentsOfDirectory bu */
        //long currentFirstClusterId = this.rootClusterId;
        //firstClusterNumberOfParentDirectories.add(currentFirstClusterId);
        // STARTED PREPARING ROOT FSNODE
        /**
         * root nodeyi biz elle set edip gonderiyoruz. TODO fakat sonradan elde
         * edilen bilgilerle bu odeyi update etmeliyiz. *
         */
        this.directoryDepthLevel = 0;
        this.rootNode = this.prepareRootPath();

        //this.map.put(fsNode.getFirstDataClusterIdOfFile(), fsNode);// TODO root nodeyi nasıl ekleyeceğiz bu işe yaramadı.
        // STARTED PARSING OF ALL FOLDERS AND SUBFOLDERS
        //int stopAt = 30;
        //int stopper = 0;
        directoryQueue.add(rootNode);
        while (!directoryQueue.isEmpty()) {
            /*
            if (stopper > stopAt) {
                break;
            } else {
                stopper++;
            }
             */
            
            this.directoryWalk(directoryQueue.getFirst(), this.ignoreDirectoryEntries);
            directoryQueue.removeFirst();
        }

        //this.directoryWalk(fsNode); //2017-11-17
        //recursiveGetDirectories(fsNode); // problemli olduğundan bunu ellemeyip başka bir metod ile devam edeceğim.
    }
    
    private FsNode prepareRootPath() {
        FsNode fsNode = new FsNode();
        fsNode.setFirstDataClusterIdOfFile(this.rootClusterId);//(this.rootClusterId);//661557, 601577
        fsNode.setAllocationStatus(DirectoryEntryAllocationStatus.ALLOCATED_ENTRY);
        fsNode.setShortFullPath(workingDirectoryPath);
        fsNode.setAbsolutePath(workingDirectoryPath);
        fsNode.setName("[root]" + " (" + this.fat32PartitionBootSector.getBsVolumeLab() + ")");
        //fsNode.setEntryType(EntryType.VOLUME);
        fsNode.setEntryType(EntryType.DIRECTORY); // VOLUME idi. DIRECTORY olarak değiştirdim. Program akışında guideki if thende önemli. Tekrar değiştirmeye karar verirsen dikkatli ol. önce dene.
        fsNode.setDirectoryDepthLevel(directoryDepthLevel);
        return fsNode;
    }

    public ArrayList<Object> directoryWalk(FsNode fsNodeOfParentDirectory, boolean ignoreDotAndDotDotDirectoryEntries) throws IOException {

        Directory directory = new Directory(this.logicalDiskImageFile, fat32PartitionBootSector, fatTable, fsNodeOfParentDirectory, ignoreDotAndDotDotDirectoryEntries);
        ArrayList<Object> contentsOfDirectory = directory.getChildren();
        for (Object object : contentsOfDirectory) {
            FsNode fsNode = (FsNode) object;
            //TODO problem olasiligini indirmek icin . ve .. entrylerini atladim
            //sonucta windows gezgininde de gorunmuyorlar. Ama msdosda gorunuyorlar.
            //ben windows gezginindeki gibi gostermemeyi tercih ettim.
            if (fsNode.getEntryType().equals(EntryType.DOTDIRECTORY) | fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY)) {
                continue;
            }
            //FsNode bilgilerini aldiktan sonra üzerine kendi belirlediğim değişkenleri ekliyorum burada:
            // hangi klasörün içinde olduğu, klasörün derinlik seviyesi falan gibi.

            fsNode.setDirectoryDepthLevel(fsNodeOfParentDirectory.getDirectoryDepthLevel());
            fsNode.setShortFullPath(fsNodeOfParentDirectory.getShortFullPath() + PATH_SEPARATOR + fsNode.getShortName());
            fsNode.setAbsolutePath(fsNodeOfParentDirectory.getAbsolutePath() + PATH_SEPARATOR + fsNode.getName());//((!"".equals(fsNode.getLongName())) ? fsNode.getLongName() : fsNode.getShortName()));
            fsNode.setParentPath(fsNodeOfParentDirectory.getAbsolutePath());

            /* */
 /* BU TAM DOGRU GOSTERIYOR FTK IMAGERDAKI GIBI*/
            System.out.println("["+fsNode.getDirectoryDepthLevel()+"."+fsNode.getLocalPositionId()+"]"+fsNode.getParentPath() + "/" + fsNode.getName());
            //System.out.println(fsNode.getParentPath() + "/" + fsNode.getName());
            /* */
 /* eğer node bir klasör ise o zaman o klasörün alt klasör ve dosyalarına dalabiliriz. 
            fakat bunu recursionla yapmak hoşuma gitmiyor. 
            o yüzden fsnodeler arasında directory olanları içlerine girmeden geçici bir listeye not edip 
            bulunduğumuz klasördeki fsnodeler bitene kadar devam edip ondan sonra o not ettiğimiz 
            dizinlerin içlerini parse etme yöntemine gideceğiz.
            ayrıca root dizini hariç tüm dizinlerin içinde ilk entry yani . dot kendisini, 
            ve ikinci entry yani .. dot parent klasörü gösterdiğinden, onların da içlerine gitmeyeceğiz. 
            benim programımda EntryType.DIRECTORY olan fsnode entryleri sadece bildiğimiz normal klasörlerdir.
            dot ve dotdot klasörler için DOTDIRECTORY ve DOTDOTDIRECTORY EntryType içerisinde ayrıca vardır. 
            O yüzden if conditionunda sadece DRECTORY Mİ değil mi diye bakmak yeterlidir. */
            if (fsNode.getEntryType() == EntryType.DIRECTORY) {

                // silinmiş klasörlerde takip yapmıyoruz, FTK imager da yapmıyor, yoksa sakatlık çıkar.
                if (!fsNode.isDeleted()) {
                    fsNode.setDirectoryDepthLevel(fsNodeOfParentDirectory.getDirectoryDepthLevel() + 1);
                    directoryQueue.add(fsNode);
                }
            }

            // MOST IMPORTANT LINE. ADDING FSNode to global pathMap.
            if (!fsNode.isDeleted()) {
                pathMap.put(fsNode.getAbsolutePath(), fsNode);
            } else {
                deletedPathMap.put(fsNode.getAbsolutePath(), fsNode);
            }

            /*
            if (fsNode.getEntryType() != EntryType.DOTDIRECTORY) {
                if (fsNode.getEntryType() != EntryType.DOTDOTDIRECTORY) {
                    String s = fsNode.getEntryType().toString().substring(0, Math.min(fsNode.getEntryType().toString().length(), 7));
                  //  System.out.println("[DataArea.recursiveGetDirectories metodu] \t" + "\t" + fsNode.getFirstDataClusterIdOfFile() + "\t" + s + "\t" + fsNode.getDirectoryDepthLevel() + "\t" + String.join("", wpath) + fsNode.getName() + "\t" + fsNode.isDeleted() + "\t" + fsNode.getAbsolutePath() + "\t" + fsNode.getName() + "\t, parent:" + fsNode.getParentPath() );
                }
            }
             */
        }

        return contentsOfDirectory;
    }

    /**
     * bu çalışıyor ama problemli. bazı şeyler eksik veya yerleri. o yüzden
     * kullanmıyoruz. bunun yerine başka metod geliştirmeye başladım.
     *
     *
     * @return
     */
    /*
    public ArrayList<FsNode> recursiveGetDirectories(FsNode fsNodeOfParentDirectory) throws IOException {

       
        Directory directory = new Directory(this.logicalDiskImageFile, fat32PartitionBootSector, fatTable, fsNodeOfParentDirectory);
        ArrayList<FsNode> ContentsOfDirectory = directory.getChildren();
       

        for (FsNode fsNode : ContentsOfDirectory) {

            fsNode.setShortFullPath(fsNodeOfParentDirectory.getShortFullPath() + PATH_SEPARATOR + fsNode.getShortName());
            fsNode.setAbsolutePath(fsNodeOfParentDirectory.getAbsolutePath() + PATH_SEPARATOR + fsNode.getName());//((!"".equals(fsNode.getLongName())) ? fsNode.getLongName() : fsNode.getShortName()));

            if (fsNode.getEntryType() == EntryType.DIRECTORY) {

                if (!firstClusterNumberOfParentDirectories.contains(fsNode.getFirstDataClusterIdOfFile())) {

                    firstClusterNumberOfParentDirectories.add(fsNode.getFirstDataClusterIdOfFile());
                    // TEST SİL //
 // yani önceden mape kaydedilip de override olan var mı ona bakıyoruz. //
                    if (this.map.containsKey(fsNode.getFirstDataClusterIdOfFile())) {
                        System.out.println("KEY: " + fsNode.getFirstDataClusterIdOfFile());
                    }
                    // TEST SİL //
                    this.map.put(fsNode.getFirstDataClusterIdOfFile(), fsNode);
                    wpath.add(fsNode.getName() + PATH_SEPARATOR);
                    //System.out.println(fsNode.getEntryType() + "\t\t\t" + fsNode.getFirstDataClusterIdOfFile() + "\t\t" + String.join("", wwpath));

                    fsNode.setDirectoryDepthLevel(directoryDepthLevel);
                    this.recursiveGetDirectories(fsNode); //RECURSION
                    this.directoryDepthLevel -= 1;

                    wpath.removeLast();
                }
            } else {
                // System.out.println(fsNode.getEntryType() + "\t\t\t" + fsNode.getFirstDataClusterIdOfFile() + "\t\t" + String.join("", wwpath) + fsNode.getName());

                this.map.put(fsNode.getFirstDataClusterIdOfFile(), fsNode);
                // 2017-11-15 //
 // yeni pathmap denemem//
                String ourPath = fsNodeOfParentDirectory.getAbsolutePath() + PATH_SEPARATOR + fsNode.getName();
                this.pathMap.put(ourPath, fsNode);

                String s = fsNode.getEntryType().toString().substring(0, Math.min(fsNode.getEntryType().toString().length(), 7));
                System.out.println("[DataArea.recursiveGetDirectories metodu] \t" + "\t" + fsNode.getFirstDataClusterIdOfFile() + "\t" + s + "\t" + fsNode.getDirectoryDepthLevel() + "\t" + String.join("", wpath) + fsNode.getName());


                
            }

        }

        return ContentsOfDirectory;
    }
     */
 /*
    private void makeCalculationsOnFSNodesInChain() {
        longNameStack = "";
        for (int i = 0; i < fsNodesInChain.size(); i++) {
            if (fsNodesInChain.get(i).getEntryType() == EntryType.LONGFILENAME) {
                longNameStack = fsNodesInChain.get(i).getLongName() + longNameStack;
            } else {
                fsNodesInChain.get(i).setLongName(longNameStack);
                longNameStack = "";
            }
        }
    }
     */
    public RandomAccessFile getLogicalDiskImageFile() {
        return logicalDiskImageFile;
    }

    public void setLogicalDiskImageFile(RandomAccessFile logicalDiskImageFile) {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }

    /*
    public DataCluster[] getDataClusters() {
        return dataClusters;
    }

    public void setDataClusters(DataCluster[] dataClusters) {
        this.dataClusters = dataClusters;
    }
     */
    public Fat32PartitionBootSector getFat32PartitionBootSector() {
        return fat32PartitionBootSector;
    }

    public void setFat32PartitionBootSector(Fat32PartitionBootSector fat32PartitionBootSector) {
        this.fat32PartitionBootSector = fat32PartitionBootSector;
    }

    public FatArea getFatArea() {
        return fatArea;
    }

    public void setFatArea(FatArea fatArea) {
        this.fatArea = fatArea;
    }

    public long getRootClusterId() {
        return rootClusterId;
    }

    public void setRootClusterId(long rootClusterId) {
        this.rootClusterId = rootClusterId;
    }

    public long getRootClusterOffset() {
        return rootClusterOffset;
    }

    public void setRootClusterOffset(long rootClusterOffset) {
        this.rootClusterOffset = rootClusterOffset;
    }

    public long getRootDirectoryCountOfEntries() {
        return rootDirectoryCountOfEntries;
    }

    public void setRootDirectoryCountOfEntries(long rootDirectoryCountOfEntries) {
        this.rootDirectoryCountOfEntries = rootDirectoryCountOfEntries;
    }

    /*
    public LinkedList<String> getCurrentShortFullPath() {
        return currentShortFullPath;
    }

    public void setCurrentShortFullPath(LinkedList<String> currentShortFullPath) {
        this.currentShortFullPath = currentShortFullPath;
    }

    public ArrayList<String> getShortFullPaths() {
        return shortFullPaths;
    }

    public void setShortFullPaths(ArrayList<String> shortFullPaths) {
        this.shortFullPaths = shortFullPaths;
    }
     */
    public ArrayList<FsNode> getFsNodesInChain() {
        return fsNodesInChain;
    }

    public void setFsNodesInChain(ArrayList<FsNode> fsNodesInChain) {
        this.fsNodesInChain = fsNodesInChain;
    }

    /*
    public ArrayList<Long> getDirQueue() {
        return dirQueue;
    }

    public void setDirQueue(ArrayList<Long> dirQueue) {
        this.dirQueue = dirQueue;
    }
     */
 /*
    public String getLongNameStack() {
        return longNameStack;
    }

    public void setLongNameStack(String longNameStack) {
        this.longNameStack = longNameStack;
    }

    public int getLongNameStackChecksum() {
        return longNameStackChecksum;
    }

    public void setLongNameStackChecksum(int longNameStackChecksum) {
        this.longNameStackChecksum = longNameStackChecksum;
    }

    public int getLongNameStackOrder() {
        return longNameStackOrder;
    }

    public void setLongNameStackOrder(int longNameStackOrder) {
        this.longNameStackOrder = longNameStackOrder;
    }
     */
 /*
    public int getDirLevel() {
        return dirLevel;
    }

    public void setDirLevel(int dirLevel) {
        this.dirLevel = dirLevel;
    }
     */
 /*
    public ArrayList<Long> getFirstClusterNumberOfParentDirectories() {
        return firstClusterNumberOfParentDirectories;
    }

    public void setFirstClusterNumberOfParentDirectories(ArrayList<Long> firstClusterNumberOfParentDirectories) {
        this.firstClusterNumberOfParentDirectories = firstClusterNumberOfParentDirectories;
    }
     */
 /*
    public Map<Long, FsNode> getMap() {
        return map;
    }

    public void setMap(Map<Long, FsNode> map) {
        this.map = map;
    }
     */
    public String getWorkingDirectoryPath() {
        return workingDirectoryPath;
    }

    public void setWorkingDirectoryPath(String workingDirectoryPath) {
        this.workingDirectoryPath = workingDirectoryPath;
    }

    public FatTable getFatTable() {
        return fatTable;
    }

    public void setFatTable(FatTable fatTable) {
        this.fatTable = fatTable;
    }

    public Map<String, FsNode> getPathMap() {
        return pathMap;
    }

    public void setPathMap(Map<String, FsNode> pathMap) {
        this.pathMap = pathMap;
    }

    public int getDirectoryDepthLevel() {
        return directoryDepthLevel;
    }

    public void setDirectoryDepthLevel(int directoryDepthLevel) {
        this.directoryDepthLevel = directoryDepthLevel;
    }

    public Map<String, FsNode> getDeletedPathMap() {
        return deletedPathMap;
    }

    public void setDeletedPathMap(Map<String, FsNode> deletedPathMap) {
        this.deletedPathMap = deletedPathMap;
    }

    public FsNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(FsNode rootNode) {
        this.rootNode = rootNode;
    }
    
    

}
