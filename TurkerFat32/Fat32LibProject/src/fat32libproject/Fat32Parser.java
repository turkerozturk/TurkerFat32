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


package fat32libproject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import fat32libproject.structures.dataarea.DataArea;
import fat32libproject.structures.fatarea.FatArea;
import fat32libproject.structures.reservedarea.ReservedArea;

/**
 *
 * @author u
 */
public class Fat32Parser {

    private String logicalDiskImageUri;
    private RandomAccessFile logicalDiskImageFile;
    private ReservedArea reservedArea;
    private FatArea fatArea;
    private DataArea dataArea;

    private ArrayList<Long> firstClusterNumberOfParentDirectories;//UInt32

    private static final int LONG_NAME_ORDER_MASK = 0x40;
    private static final int DEFAULT_SELECTED_FAT_TABLE_NUMBER = 0;

    //private static final String DOT = ".";
    //private static final String DOTDOT = "..";
    
  

    private ArrayList<Long> dirQueue = new ArrayList<>(); //UInt32
    private String longNameStack;
    private int longNameStackChecksum; //byte
    private int longNameStackOrder; //byte


    /* icinde bulunulan dizin seviyesini kontrol ederek */
    private int dirLevel;

    Fat32Parser(RandomAccessFile logicalDiskImageFile) {
        /* dosyayi aciyoruz */
        this.logicalDiskImageFile = logicalDiskImageFile;
    }

    void init() throws FileNotFoundException, IOException {


        /* fat32 sisteminin ilk bolgesi olan reserve bolgeyi parse ediyoruz, dosya sistemi hakkinda bilgileri aliyoruz  */
        this.reservedArea = new ReservedArea(logicalDiskImageFile);
        this.reservedArea.parsePartitionBootSector();

        /* fat32 sisteminin fat bolgesindeki fat tablolarini parse ediyoruz
           bu tablolar sayesinde verdigimiz cluster ID nin devaminda varsa diger clusteridlerini
           ogrenebilecegiz */
        this.fatArea = new FatArea(logicalDiskImageFile);
        this.fatArea.parseFatTables(reservedArea.getFat32PartitionBootSector());

        /* TODO daha asagilari yazmadik */
 /* Data bolgesinde dosya parcalari ve dizinlerin bilgileri var, bize dizinlerin bilgileri lazim. 
           Bu sebeple root clusterinin icindeki dizin entrylerinin baslangic clusteridlerini takip ederek tum dosya ve dizin bilgilerini parselliyoruz */
        this.dataArea = new DataArea(logicalDiskImageFile);
        this.dataArea.parseAllEntries(this.reservedArea.getFat32PartitionBootSector(), this.fatArea, DEFAULT_SELECTED_FAT_TABLE_NUMBER); //// TODO ben default olarak fattable 0 kullaniyorum ama snra bunu parametreli yap. cunku bu sekilde diger fat tablolari varsa programda bir ise yaramadan duruyorlar cunku secilme imkani sunulmamis.

        // makeCalculationsOnFSNodesInChain();
    }



    int getDirLevel() {
        return dirLevel;
    }

    void setDirLevel(int dirLevel) {
        this.dirLevel = dirLevel;
    }

    ArrayList<Long> getFirstClusterNumberOfParentDirectories() {
        return firstClusterNumberOfParentDirectories;
    }

    void setFirstClusterNumberOfParentDirectories(ArrayList<Long> firstClusterNumberOfParentDirectories) {
        this.firstClusterNumberOfParentDirectories = firstClusterNumberOfParentDirectories;
    }

    ArrayList<Long> getDirQueue() {
        return dirQueue;
    }

    void setDirQueue(ArrayList<Long> dirQueue) {
        this.dirQueue = dirQueue;
    }

    String getLongNameStack() {
        return longNameStack;
    }

    void setLongNameStack(String longNameStack) {
        this.longNameStack = longNameStack;
    }

    int getLongNameStackChecksum() {
        return longNameStackChecksum;
    }

    void setLongNameStackChecksum(int longNameStackChecksum) {
        this.longNameStackChecksum = longNameStackChecksum;
    }

    int getLongNameStackOrder() {
        return longNameStackOrder;
    }

    void setLongNameStackOrder(int longNameStackOrder) {
        this.longNameStackOrder = longNameStackOrder;
    }

    static int getLongNameOrderMask() {
        return LONG_NAME_ORDER_MASK;
    }

    String getLogicalDiskImageUri() {
        return logicalDiskImageUri;
    }

    void setLogicalDiskImageUri(String logicalDiskImageUri) {
        this.logicalDiskImageUri = logicalDiskImageUri;
    }

    RandomAccessFile getLogicalDiskImageFile() {
        return logicalDiskImageFile;
    }

    void setLogicalDiskImageFile(RandomAccessFile logicalDiskImageFile) {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }

    ReservedArea getReservedArea() {
        return reservedArea;
    }

    void setReservedArea(ReservedArea reservedArea) {
        this.reservedArea = reservedArea;
    }

    FatArea getFatArea() {
        return fatArea;
    }

    void setFatArea(FatArea fatArea) {
        this.fatArea = fatArea;
    }

    public DataArea getDataArea() {
        return dataArea;
    }

    void setDataArea(DataArea dataArea) {
        this.dataArea = dataArea;
    }
    
    

}

/* burada donguden gerekirse cikmak icin bir kural var */
 /*
                    if (relativeClusterId != 2) {
                        if (((DirAttr.from(clusterBytes[32 + 11]).value & DirAttr.LONG_NAME_MASK.value) != DirAttr.LONG_NAME.value)) {
                            if (clusterBytes[33] != 0x2E) {
                                break;
                            } else {
                                   //source, source ofset based 0, destination, destination ofset based 0, count of bytes
                    //System.Buffer.BlockCopy(clusterBytes, 0, totalBytesInTheChain, clusterBytes.length * i, clusterBytes.length);
                   // totalBytesInTheChain = Arrays.copyOfRange(clusterBytes, clusterBytes.length * i, clusterBytes.length * i + clusterBytes.length);//TODO son parameterede +1 ?
                    // Parser.DataStream.seek(memory);
                    System.arraycopy(clusterBytes,0,totalBytesInTheChain,clusterBytes.length * i, clusterBytes.length);
                            }
                        }
                    }
 */

 /*
        byte[] demBytes = null; //instead of null, specify your bytes here. 

        File outputFile = new File("LOCATION TO FILE");

        try ( FileOutputStream outputStream = new FileOutputStream(outputFile); ) {

            outputStream.write(demBytes);  //write the bytes and your done. 

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
 */
//this.
        // return file;
