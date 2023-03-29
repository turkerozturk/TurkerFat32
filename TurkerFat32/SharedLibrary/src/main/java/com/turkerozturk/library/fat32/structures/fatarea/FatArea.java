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


package com.turkerozturk.library.fat32.structures.fatarea;

import java.io.IOException;
import java.io.RandomAccessFile;

import com.turkerozturk.library.fat32.structures.reservedarea.Fat32PartitionBootSector;

/**
 *
 * @author u
 */
public class FatArea {

    private long fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes;
    private int totalCountOfFatTables;
    long oneFatTableSizeAsByte;
    long fatSz32AsValidClusterNumberCapacity;
    


    private FatTable[] fatTables;

    //private byte[] fatBytes;
    private RandomAccessFile logicalDiskImageFile;

    public FatArea(RandomAccessFile logicalDiskImageFile) throws IOException {
        this.logicalDiskImageFile = logicalDiskImageFile;

    }

    //public void parseFatTables(long fatOffsetAsByte, long fatSz32AsByte, byte bpbNumberOfFatTables) throws IOException {
    public void parseFatTables(Fat32PartitionBootSector fat32PartitionBootSector) throws IOException {

        this.fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes = fat32PartitionBootSector.getFatOffsetAsByte();
        this.oneFatTableSizeAsByte = fat32PartitionBootSector.getFatSz32AsByte();
        this.totalCountOfFatTables = fat32PartitionBootSector.getBpbNumberOfFatTables();
        this.fatSz32AsValidClusterNumberCapacity = fat32PartitionBootSector.getFatSz32AsValidClusterNumberCapacity();

        /* */
        fatTables = new FatTable[this.totalCountOfFatTables];
        for (int fatTableIndex = 0; fatTableIndex < this.totalCountOfFatTables; fatTableIndex++) {
            long fatTableStartOffset = (long) (this.fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes + this.oneFatTableSizeAsByte * fatTableIndex);
            logicalDiskImageFile.seek((long) fatTableStartOffset);
            byte[] fatBytes = new byte[(int) this.oneFatTableSizeAsByte]; //Int32 
            logicalDiskImageFile.read(fatBytes);
            //System.out.println("fatbytessize" + fatBytes[4079615]);
            FatTable fatTable = new FatTable();
            fatTable.parseFatTable(fatBytes, this.fatSz32AsValidClusterNumberCapacity);
            fatTables[fatTableIndex] = fatTable;
        }

        /* */
    }
    
     /*  eski ismi bringfatchain idi. verilen başlangıç clusteridsini fat tablosunda index olarak kullanır ve değerinde sonraki clusteridsi yazdığı müddetçe hepsini toplayıp geri döner.
    Bu metod sayesinde bir dosya veya dizinin kapladığı tüm clusterların idlerini öğrenmiş oluruz. Başlangıç clusteridsini buraya getirirken ise root hep 2den başladığı için biliyoruz ve devamınde entrylerin içinde başlangıç clusteridsi datastartid olark bulunuyor.
  */
    /*
    public ArrayList<Long> getAChainOfClusterIdsIFromFatTable(long startingClusterIdInFatTable) { //UInt32
        ArrayList<Long> chainOfClusterIdsInFatTable = new ArrayList<>(); //UInt32

        long CurrentRelativeRing = startingClusterIdInFatTable; //UInt32
        while (CurrentRelativeRing >= 2 && CurrentRelativeRing != END_OF_CLUSTER_MARK && CurrentRelativeRing <= this.fatSz32AsValidClusterNumberCapacity) {
            //System.out.println("FATZİNCİRİ: " + CurrentRelativeRing);
            chainOfClusterIdsInFatTable.add(CurrentRelativeRing);
           // System.out.println("CurrentRelativeRing: " + CurrentRelativeRing);

            CurrentRelativeRing = NextRelativeRing(CurrentRelativeRing);
        }

        return chainOfClusterIdsInFatTable;
    }
    */
    

    /*
    public long NextRelativeRing(long currentRelativeRing) {  //UInt32
        if ((currentRelativeRing != END_OF_CLUSTER_MARK) && (currentRelativeRing >= 2) && (currentRelativeRing <= this.fatSz32AsValidClusterNumberCapacity)) {

            long result = getFatTables()[0].getEntries()[(int) currentRelativeRing]; // TODO ben default olarak fattable 0 kullaniyorum ama snra bunu parametreli yap. cunku bu sekilde diger fat tablolari varsa programda bir ise yaramadan duruyorlar cunku secilme imkani sunulmamis.
           // System.out.println("result2: " + result);
            return result;

        } else {
            return currentRelativeRing; // bu şekilde iyi değil. FAT corrupt ise problem olur. BringCorruptFATChain adında başka bir fonk yaz sonra.
        }

    }
    
    */

    /*
    public void loadFAT2Memory(Long fatNumber) throws IOException {
        if (fatNumber == null) {
            fatNumber = 0L;
        }
        long adres = (long) (this.fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes + this.oneFatTableSizeAsByte * fatNumber);
        logicalDiskImageFile.seek((long) adres);
        fatBytes = new byte[(int) this.oneFatTableSizeAsByte]; //Int32 
        logicalDiskImageFile.read(fatBytes);
    }
     */
    public long getFatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes() {
        return fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes;
    }

    public void setFatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes(long fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes) {
        this.fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes = fatAreaStartOffsetFromTheBeginningOfTheLogicalDiskImageFileAsBytes;
    }

    public long getTotalCountOfFatTables() {
        return totalCountOfFatTables;
    }

    public void setTotalCountOfFatTables(int totalCountOfFatTables) {
        this.totalCountOfFatTables = totalCountOfFatTables;
    }

    public long getOneFatTableSizeAsByte() {
        return oneFatTableSizeAsByte;
    }

    public void setOneFatTableSizeAsByte(long oneFatTableSizeAsByte) {
        this.oneFatTableSizeAsByte = oneFatTableSizeAsByte;
    }

    public FatTable[] getFatTables() {
        return fatTables;
    }

    public void setFatTables(FatTable[] fatTables) {
        this.fatTables = fatTables;
    }

    /*
    public byte[] getFatBytes() {
        return fatBytes;
    }

    public void setFatBytes(byte[] fatBytes) {
        this.fatBytes = fatBytes;
    }
     */
    public RandomAccessFile getLogicalDiskImageFile() {
        return logicalDiskImageFile;
    }

    public void setLogicalDiskImageFile(RandomAccessFile logicalDiskImageFile) {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }

}
