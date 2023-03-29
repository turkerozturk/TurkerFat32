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


package com.turkerozturk.library.fat32.structures.reservedarea;

//import static turkerfat32project.Fat32Parser.DIRECTORY_ENTRY_SIZE_AS_BYTE;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

import com.turkerozturk.library.fat32.helpers.MsDosDateTime;

/**
 *
 * @author u
 */
public class Fat32PartitionBootSector {

    //asagidakiler yapida var.
    private byte[] bsJmpBoot;
    private String bsOemName;
    private int bpbBytesPerSector;//u16
    private int bpbSectorsPerCluster;//u8
    private int bpbReservedSectorCount;//u16
    private int bpbNumberOfFatTables;//u8
    private int bpbRootEntCount;//u16
    private int bpbTotalSectors16;//u16
    private int bpbMedia;//u8
    private int bpbFatSize16;//u16
    private int bpbSectorsPerTrack;//u16
    private int bpbNumberOfHeads;//u16
    private long bpbHiddenSectors;//u32
    private long bpbTotalSectors32;//u32
    private long bpbFatSize32;//u32
    private int bpbExtFlags;//u16
    private int bpbFileSystemVersion;//u16
    private long bpbRootCluster;//u32
    private int bpbFileSystemInfo;//u16
    private int bpbBackupBootSector;//u16
    private byte[] bpbReserved;
    private int bsDriveNumber;//u8
    private int bsReserved1;//u8
    private int bsBootSig;//u8
    private long bsVolumeId;
    private String bsVolumeLab;
    private String bsFileSystemType;
    private byte[] bsExecutableCode;
    private int bsEndingFlag;//u16

    //asagidakiler yukaridakilerden hesaplandi
    Date volumeCreatedDate;
    private long fatOffsetAsByte; //UInt64
    private long rootDirFatOffsetAsByte; //UInt64
    private long dataOffsetAsByte; //UInt64
    private long clusterSizeAsByte; //UInt32 
    private long totalDataSectors; //UInt64
    private long totalCountOfClusters; //UInt64
    private long totalFatAreaSize; //UInt64
    private long totalDataAreaSize; //UInt64
    private long fatSz32AsByte; //UInt64

    private long entryCountInOneCluster; //UInt64
    private long fatSz32AsValidClusterNumberCapacity; //UInt64

    private static final int DIRECTORY_ENTRY_SIZE_AS_BYTE = 32;
    private static final int SIZE_OF_AN_INTEGER = 4;
    private static final int SIZE_OF_FIRST_TWO_USELESS_INTEGER_VALUE_OF_FAT_TABLE = SIZE_OF_AN_INTEGER * 2;
    private final static long UINTMASK = 0xFFFFFFFFL;
    private final static int USHORTMASK = 0xFFFF;
    private final static short UBYTEMASK = 0xFF;

    public void ParseValues(byte[] partitionBootRecordBytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(partitionBootRecordBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        bsJmpBoot = new byte[3];
        byteBuffer.get(bsJmpBoot);
        //System.arraycopy(partitionBootRecordBytes, 0, bsJmpBoot, 0, 3);
        byte[] bsOemNameBytes = new byte[8];
        byteBuffer.get(bsOemNameBytes);
        bsOemName = new String(bsOemNameBytes).trim();
        bpbBytesPerSector = byteBuffer.getShort() & USHORTMASK;//( (partitionBootRecordBytes[12] << 8 | partitionBootRecordBytes[11]) ) ; //UInt16
        bpbSectorsPerCluster = byteBuffer.get() & UBYTEMASK;//partitionBootRecordBytes[13];
        bpbReservedSectorCount = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[15] << 8 | partitionBootRecordBytes[14]); //UInt16
        bpbNumberOfFatTables = byteBuffer.get() & UBYTEMASK;// partitionBootRecordBytes[16];
        bpbRootEntCount = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[18] << 8 | partitionBootRecordBytes[17]); //UInt16
        bpbTotalSectors16 = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[20] << 8 | partitionBootRecordBytes[19]); //UInt16
        bpbMedia = byteBuffer.get() & UBYTEMASK;// partitionBootRecordBytes[21];
        bpbFatSize16 = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[23] << 8 | partitionBootRecordBytes[22]); //UInt16
        bpbSectorsPerTrack = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[25] << 8 | partitionBootRecordBytes[24]); //UInt16
        bpbNumberOfHeads = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[27] << 8 | partitionBootRecordBytes[26]); //UInt16
        bpbHiddenSectors = byteBuffer.getInt() & UINTMASK;//( partitionBootRecordBytes[31] << 24 | partitionBootRecordBytes[30] << 16 | partitionBootRecordBytes[29] << 8 | partitionBootRecordBytes[28]); //UInt32
        bpbTotalSectors32 = byteBuffer.getInt() & UINTMASK;// (partitionBootRecordBytes[35] << 24 | partitionBootRecordBytes[34] << 16 | partitionBootRecordBytes[33] << 8 | partitionBootRecordBytes[32]); //UInt32
        bpbFatSize32 = byteBuffer.getInt() & UINTMASK;// (partitionBootRecordBytes[39] << 24 | partitionBootRecordBytes[38] << 16 | partitionBootRecordBytes[37] << 8 | partitionBootRecordBytes[36]); //UInt32
        bpbExtFlags = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[41] << 8 | partitionBootRecordBytes[40]); //UInt16
        bpbFileSystemVersion = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[43] << 8 | partitionBootRecordBytes[42]); //UInt16
        bpbRootCluster = byteBuffer.getInt() & UINTMASK;// (partitionBootRecordBytes[47] << 24 | partitionBootRecordBytes[46] << 16 | partitionBootRecordBytes[45] << 8 | partitionBootRecordBytes[44]); //UInt32
        bpbFileSystemInfo = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[49] << 8 | partitionBootRecordBytes[48]); //UInt16
        bpbBackupBootSector = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[51] << 8 | partitionBootRecordBytes[50]); //UInt16
        bpbReserved = new byte[12];
        byteBuffer.get(bpbReserved);
        bsDriveNumber = byteBuffer.get() & UBYTEMASK;// partitionBootRecordBytes[64];
        bsReserved1 = byteBuffer.get() & UBYTEMASK;// partitionBootRecordBytes[65];
        bsBootSig = byteBuffer.get() & UBYTEMASK;// partitionBootRecordBytes[66];
        byte[] bsVolumeIdBytes = new byte[4];
        byteBuffer.get(bsVolumeIdBytes);
        int bsVolumeIdLoBytes = (bsVolumeIdBytes[3] << 8 | bsVolumeIdBytes[2]);
        int bsVolumeIdHiBytes = (bsVolumeIdBytes[1] << 8 | bsVolumeIdBytes[0]);
        ByteBuffer bBufferOnlyForVolumeId = ByteBuffer.wrap(bsVolumeIdBytes);
        bBufferOnlyForVolumeId.order(ByteOrder.LITTLE_ENDIAN);
        bsVolumeId = bBufferOnlyForVolumeId.getInt() & UINTMASK;
        byte[] bsVolumeLabBytes = new byte[11];
        byteBuffer.get(bsVolumeLabBytes);
        bsVolumeLab = new String(bsVolumeLabBytes);
        byte[] bsFileSystemTypeBytes = new byte[8];
        byteBuffer.get(bsFileSystemTypeBytes);
        bsFileSystemType = new String(bsFileSystemTypeBytes).trim();
        bsExecutableCode = new byte[420];
        byteBuffer.get(bsExecutableCode);
        bsEndingFlag = byteBuffer.getShort() & USHORTMASK;// (partitionBootRecordBytes[510] << 8 | partitionBootRecordBytes[511]); //UInt16

        calculateFormulasRelatedWithTheFAT32Partition();

        MsDosDateTime dt = new MsDosDateTime();
        dt.setDateWordHiByte(bsVolumeIdHiBytes);
        dt.setDateWordLoByte(bsVolumeIdLoBytes);
        volumeCreatedDate = dt.Parse();
    }

    private void calculateFormulasRelatedWithTheFAT32Partition() {
        fatOffsetAsByte = (this.getBpbReservedSectorCount() * this.getBpbBytesPerSector());
        rootDirFatOffsetAsByte = (fatOffsetAsByte + this.getBpbRootCluster() * 4);
        dataOffsetAsByte = (this.getBpbReservedSectorCount() + (this.getBpbFatSize32() * this.getBpbNumberOfFatTables())) * this.getBpbBytesPerSector();
        clusterSizeAsByte = (this.getBpbBytesPerSector() * this.getBpbSectorsPerCluster());
        totalDataSectors = (this.getBpbTotalSectors32() - (this.getBpbReservedSectorCount() + (this.getBpbFatSize32() * this.getBpbNumberOfFatTables())));
        totalCountOfClusters = (totalDataSectors / this.getBpbSectorsPerCluster());
        totalFatAreaSize = ((this.getBpbFatSize32() * this.getBpbNumberOfFatTables()) * this.getBpbBytesPerSector());
        totalDataAreaSize = ((this.getBpbTotalSectors32() - (this.getBpbReservedSectorCount() + (this.getBpbFatSize32() * this.getBpbNumberOfFatTables()))) * this.getBpbBytesPerSector());
        fatSz32AsByte = (this.getBpbFatSize32() * this.getBpbBytesPerSector());
        entryCountInOneCluster = this.clusterSizeAsByte / DIRECTORY_ENTRY_SIZE_AS_BYTE;
        fatSz32AsValidClusterNumberCapacity = (fatSz32AsByte / SIZE_OF_AN_INTEGER) - SIZE_OF_FIRST_TWO_USELESS_INTEGER_VALUE_OF_FAT_TABLE; //UInt64

    }

    /**
     * Jump Code + NOP
     *
     * @return
     */
    public byte[] getBsJmpBoot() {
        return bsJmpBoot;
    }

    public void setBsJmpBoot(byte[] bsJmpBoot) {
        this.bsJmpBoot = bsJmpBoot;
    }

    /**
     * OEM Name (Probably MSWIN4.1)
     *
     * @return
     */
    public String getBsOemName() {
        return bsOemName;
    }

    public void setBsOemName(String bsOemName) {
        this.bsOemName = bsOemName;
    }

    /**
     * Bytes Per Sector
     *
     * @return
     */
    public int getBpbBytesPerSector() {
        return bpbBytesPerSector;
    }

    public void setBpbBytesPerSector(int bpbBytesPerSector) {
        this.bpbBytesPerSector = bpbBytesPerSector;
    }

    /**
     * Sectors Per Cluster
     *
     * @return
     */
    public int getBpbSectorsPerCluster() {
        return bpbSectorsPerCluster;
    }

    public void setBpbSectorsPerCluster(int bpbSectorsPerCluster) {
        this.bpbSectorsPerCluster = bpbSectorsPerCluster;
    }

    /**
     * Reserved Sectors
     *
     * @return
     */
    public int getBpbReservedSectorCount() {
        return bpbReservedSectorCount;
    }

    public void setBpbReservedSectorCount(int bpbReservedSectorCount) {
        this.bpbReservedSectorCount = bpbReservedSectorCount;
    }

    /**
     * Number of Copies of FAT
     *
     * @return
     */
    public int getBpbNumberOfFatTables() {
        return bpbNumberOfFatTables;
    }

    public void setBpbNumberOfFatTables(int bpbNumberOfFatTables) {
        this.bpbNumberOfFatTables = bpbNumberOfFatTables;
    }

    /**
     * Maximum Root DirectoryEntries (N/A for FAT32)
     *
     * @return
     */
    public int getBpbRootEntCount() {
        return bpbRootEntCount;
    }

    public void setBpbRootEntCount(int bpbRootEntCount) {
        this.bpbRootEntCount = bpbRootEntCount;
    }

    /**
     * Number of Sectors inPartition Smaller than 32MB (N/A for FAT32)
     *
     * @return
     */
    public int getBpbTotalSectors16() {
        return bpbTotalSectors16;
    }

    public void setBpbTotalSectors16(int bpbTotalSectors16) {
        this.bpbTotalSectors16 = bpbTotalSectors16;
    }

    /**
     * Media Descriptor (F8h forHard Disks)
     *
     * @return
     */
    public int getBpbMedia() {
        return bpbMedia;
    }

    public void setBpbMedia(int bpbMedia) {
        this.bpbMedia = bpbMedia;
    }

    /**
     * Sectors Per FAT in Older FATSystems (N/A for FAT32)
     *
     * @return
     */
    public int getBpbFatSize16() {
        return bpbFatSize16;
    }

    public void setBpbFatSize16(int bpbFatSize16) {
        this.bpbFatSize16 = bpbFatSize16;
    }

    /**
     * Sectors Per Track
     *
     * @return
     */
    public int getBpbSectorsPerTrack() {
        return bpbSectorsPerTrack;
    }

    public void setBpbSectorsPerTrack(int bpbSectorsPerTrack) {
        this.bpbSectorsPerTrack = bpbSectorsPerTrack;
    }

    /**
     * Number of Heads
     *
     * @return
     */
    public int getBpbNumberOfHeads() {
        return bpbNumberOfHeads;
    }

    public void setBpbNumberOfHeads(int bpbNumberOfHeads) {
        this.bpbNumberOfHeads = bpbNumberOfHeads;
    }

    /**
     * Number of Hidden Sectors inPartition
     *
     * @return
     */
    public long getBpbHiddenSectors() {
        return bpbHiddenSectors;
    }

    public void setBpbHiddenSectors(long bpbHiddenSectors) {
        this.bpbHiddenSectors = bpbHiddenSectors;
    }

    /**
     * Number of Sectors inPartition
     *
     * @return
     */
    public long getBpbTotalSectors32() {
        return bpbTotalSectors32;
    }

    public void setBpbTotalSectors32(long bpbTotalSectors32) {
        this.bpbTotalSectors32 = bpbTotalSectors32;
    }

    /**
     * Number of Sectors Per FAT
     *
     * @return
     */
    public long getBpbFatSize32() {
        return bpbFatSize32;
    }

    public void setBpbFatSize32(long bpbFatSize32) {
        this.bpbFatSize32 = bpbFatSize32;
    }

    /**
     * Flags (Bits 0-4 IndicateActive FAT Copy) (Bit 7 Indicates whether FAT
     * Mirroringis Enabled or Disabled ) (If FATMirroring is Disabled, the FAT
     * Information is onlywritten to the copy indicated by bits 0-4)
     *
     * @return
     */
    public int getBpbExtFlags() {
        return bpbExtFlags;
    }

    public void setBpbExtFlags(int bpbExtFlags) {
        this.bpbExtFlags = bpbExtFlags;
    }

    /**
     * Version of FAT32 Drive (HighByte = Major Version, Low Byte = Minor
     * Version)
     *
     * @return
     */
    public int getBpbFileSystemVersion() {
        return bpbFileSystemVersion;
    }

    public void setBpbFileSystemVersion(int bpbFileSystemVersion) {
        this.bpbFileSystemVersion = bpbFileSystemVersion;
    }

    /**
     * Cluster Number of the Startof the Root Directory
     *
     * @return
     */
    public long getBpbRootCluster() {
        return bpbRootCluster;
    }

    public void setBpbRootCluster(long bpbRootCluster) {
        this.bpbRootCluster = bpbRootCluster;
    }

    /**
     * Sector Number of the FileSystem Information Sector (See Structure
     * Below)(Referenced from the Start of the Partition)
     *
     * @return
     */
    public int getBpbFileSystemInfo() {
        return bpbFileSystemInfo;
    }

    public void setBpbFileSystemInfo(int bpbFileSystemInfo) {
        this.bpbFileSystemInfo = bpbFileSystemInfo;
    }

    /**
     * Sector Number of the BackupBoot Sector (Referenced from the Start of the
     * Partition)
     *
     * @return
     */
    public int getBpbBackupBootSector() {
        return bpbBackupBootSector;
    }

    public void setBpbBackupBootSector(int bpbBackupBootSector) {
        this.bpbBackupBootSector = bpbBackupBootSector;
    }

    /**
     * Reserved
     *
     * @return
     */
    public byte[] getBpbReserved() {
        return bpbReserved;
    }

    public void setBpbReserved(byte[] bpbReserved) {
        this.bpbReserved = bpbReserved;
    }

    /**
     * Logical Drive Number ofPartition
     *
     * @return
     */
    public int getBsDriveNumber() {
        return bsDriveNumber;
    }

    public void setBsDriveNumber(int bsDriveNumber) {
        this.bsDriveNumber = bsDriveNumber;
    }

    /**
     * Unused (Could be High Byteof Previous Entry)
     *
     * @return
     */
    public int getBsReserved1() {
        return bsReserved1;
    }

    public void setBsReserved1(int bsReserved1) {
        this.bsReserved1 = bsReserved1;
    }

    /**
     * Extended Signature (29h)
     *
     * @return
     */
    public int getBsBootSig() {
        return bsBootSig;
    }

    public void setBsBootSig(int bsBootSig) {
        this.bsBootSig = bsBootSig;
    }

    /**
     * Serial Number of Partition
     *
     * @return
     */
    public long getBsVolumeId() {
        return bsVolumeId;
    }

    public void setBsVolumeId(long bsVolumeId) {
        this.bsVolumeId = bsVolumeId;
    }

    /**
     * Volume Name of Partition
     *
     * @return
     */
    public String getBsVolumeLab() {
        return bsVolumeLab;
    }

    public void setBsVolumeLab(String bsVolumeLab) {
        this.bsVolumeLab = bsVolumeLab;
    }

    /**
     * FAT Name (FAT32)
     *
     * @return
     */
    public String getBsFileSystemType() {
        return bsFileSystemType;
    }

    public void setBsFileSystemType(String bsFileSystemType) {
        this.bsFileSystemType = bsFileSystemType;
    }

    /**
     * Executable Code
     *
     * @return
     */
    public byte[] getBsExecutableCode() {
        return bsExecutableCode;
    }

    public void setBsExecutableCode(byte[] bsExecutableCode) {
        this.bsExecutableCode = bsExecutableCode;
    }

    /**
     * Boot Record Signature (55hAAh)
     *
     * @return
     */
    public int getBsEndingFlag() {
        return bsEndingFlag;
    }

    public void setBsEndingFlag(int bsEndingFlag) {
        this.bsEndingFlag = bsEndingFlag;
    }

    /**
     *
     * @return
     */
    public long getFatOffsetAsByte() {
        return fatOffsetAsByte;
    }

    public void setFatOffsetAsByte(long fatOffsetAsByte) {
        this.fatOffsetAsByte = fatOffsetAsByte;
    }

    /**
     *
     * @return
     */
    public long getRootDirFatOffsetAsByte() {
        return rootDirFatOffsetAsByte;
    }

    public void setRootDirFatOffsetAsByte(long rootDirFatOffsetAsByte) {
        this.rootDirFatOffsetAsByte = rootDirFatOffsetAsByte;
    }

    /**
     *
     * @return
     */
    public long getDataOffsetAsByte() {
        return dataOffsetAsByte;
    }

    public void setDataOffsetAsByte(long dataOffsetAsByte) {
        this.dataOffsetAsByte = dataOffsetAsByte;
    }

    /**
     *
     * @return
     */
    public long getClusterSizeAsByte() {
        return clusterSizeAsByte;
    }

    public void setClusterSizeAsByte(long clusterSizeAsByte) {
        this.clusterSizeAsByte = clusterSizeAsByte;
    }

    /**
     *
     * @return
     */
    public long getTotalDataSectors() {
        return totalDataSectors;
    }

    public void setTotalDataSectors(long totalDataSectors) {
        this.totalDataSectors = totalDataSectors;
    }

    /**
     *
     * @return
     */
    public long getTotalCountOfClusters() {
        return totalCountOfClusters;
    }

    public void setTotalCountOfClusters(long totalCountOfClusters) {
        this.totalCountOfClusters = totalCountOfClusters;
    }

    /**
     *
     * @return
     */
    public long getTotalFatAreaSize() {
        return totalFatAreaSize;
    }

    public void setTotalFatAreaSize(long totalFatAreaSize) {
        this.totalFatAreaSize = totalFatAreaSize;
    }

    /**
     *
     * @return
     */
    public long getTotalDataAreaSize() {
        return totalDataAreaSize;
    }

    public void setTotalDataAreaSize(long totalDataAreaSize) {
        this.totalDataAreaSize = totalDataAreaSize;
    }

    /**
     *
     * @return
     */
    public long getFatSz32AsByte() {
        return fatSz32AsByte;
    }

    public void setFatSz32AsByte(long fatSz32AsByte) {
        this.fatSz32AsByte = fatSz32AsByte;
    }

    /**
     *
     * @return
     */
    public long getEntryCountInOneCluster() {
        return entryCountInOneCluster;
    }

    public void setEntryCountInOneCluster(long entryCountInOneCluster) {
        this.entryCountInOneCluster = entryCountInOneCluster;
    }

    /**
     *
     * @return
     */
    public long getFatSz32AsValidClusterNumberCapacity() {
        return fatSz32AsValidClusterNumberCapacity;
    }

    public void setFatSz32AsValidClusterNumberCapacity(long fatSz32AsValidClusterNumberCapacity) {
        this.fatSz32AsValidClusterNumberCapacity = fatSz32AsValidClusterNumberCapacity;
    }

    /**
     * It is Volume Id converted to MsDOS Date
     * @return
     */
    public Date getVolumeCreatedDate() {
        return volumeCreatedDate;
    }

    public void setVolumeCreatedDate(Date volumeCreatedDate) {
        this.volumeCreatedDate = volumeCreatedDate;
    }

}
