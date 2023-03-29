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

import java.util.Date;
import java.util.EnumSet;

import com.turkerozturk.library.fat32.enums.DirectoryEntryAllocationStatus;
import com.turkerozturk.library.fat32.enums.EntryAttributes;
import com.turkerozturk.library.fat32.enums.EntryType;
import com.turkerozturk.library.fat32.enums.FsNodeFileSystemType;

/**
 *
 * @author u
 */
public class FsNode {

    private DirectoryEntryAllocationStatus allocationStatus;//byte
    private long localPositionId; //UInt32
    private long containerId; //UInt32
    private long firstDataClusterIdOfFile; //UInt32
    private String longName;
    private String shortName;
    private long sizeAsBytes; //UInt32
    private EntryType entryType;
    private Date createdDate;
    private Date writtenDate;
    private Date accessedDate;
    private int longNameChecksum;//byte
    private int calculatedChecksum;//byte
    private boolean deleted;

    /* asagidakileri ben ekledim */
    private String absolutePath;
    private String shortFullPath;
    private String name; // if possible it is longname, otherwise it is equal to shortname.
    private EnumSet<EntryAttributes> entryAttributes;
    private int directoryDepthLevel;
    private String parentPath;
    private byte[] rawEntry;
    private FsNodeFileSystemType fsNodeFileSystemType;
    private long size; //ftk imagerdaki gibi olcusu


    public FsNode() {
        this.fsNodeFileSystemType = FsNodeFileSystemType.INITIAL_DEFAULT_NODE;
    }

    

    /* bunu koymamizin sebebi eger swing api jtree kullanirsan bu sınıfı userobject olarak eklediginde agactaki node etiketinin gorunmesi icin bir toString metodu olmasi gerekiyor. */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * first character of the short name. if the entry is deleted, it is not
     * first character. Then it is equal to 0xe5. if the entry is empty, it is
     * not first character. Then it is equal to 0x00.
     *
     * @return
     */
    public DirectoryEntryAllocationStatus getAllocationStatus() {
        return allocationStatus;
    }

    public void setAllocationStatus(DirectoryEntryAllocationStatus allocationStatus) {
        this.allocationStatus = allocationStatus;
    }

    public long getLocalPositionId() {
        return localPositionId;
    }

    public void setLocalPositionId(long localPositionId) {
        this.localPositionId = localPositionId;
    }

    public long getContainerId() {
        return containerId;
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public long getFirstDataClusterIdOfFile() {
        return firstDataClusterIdOfFile;
    }

    public void setFirstDataClusterIdOfFile(long firstDataClusterIdOfFile) {
        this.firstDataClusterIdOfFile = firstDataClusterIdOfFile;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public long getSizeAsBytes() {
        return sizeAsBytes;
    }

    public void setSizeAsBytes(long sizeAsBytes) {
        this.sizeAsBytes = sizeAsBytes;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getWrittenDate() {
        return writtenDate;
    }

    public void setWrittenDate(Date writtenDate) {
        this.writtenDate = writtenDate;
    }

    public Date getAccessedDate() {
        return accessedDate;
    }

    public void setAccessedDate(Date accessedDate) {
        this.accessedDate = accessedDate;
    }

    public int getLongNameChecksum() {
        return longNameChecksum;
    }

    public void setLongNameChecksum(int longNameChecksum) {
        this.longNameChecksum = longNameChecksum;
    }

    public int getCalculatedChecksum() {
        return calculatedChecksum;
    }

    public void setCalculatedChecksum(int calculatedChecksum) {
        this.calculatedChecksum = calculatedChecksum;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getShortFullPath() {
        return shortFullPath;
    }

    public void setShortFullPath(String shortFullPath) {
        this.shortFullPath = shortFullPath;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnumSet<EntryAttributes> getEntryAttributes() {
        return entryAttributes;
    }

    public void setEntryAttributes(EnumSet<EntryAttributes> entryAttributes) {
        this.entryAttributes = entryAttributes;
    }

    public int getDirectoryDepthLevel() {
        return directoryDepthLevel;
    }

    public void setDirectoryDepthLevel(int directoryDepthLevel) {
        this.directoryDepthLevel = directoryDepthLevel;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public byte[] getRawEntry() {
        return rawEntry;
    }

    public void setRawEntry(byte[] rawEntry) {
        this.rawEntry = rawEntry;
    }

    public FsNodeFileSystemType getFsNodeFileSystemType() {
        return fsNodeFileSystemType;
    }

    public void setFsNodeFileSystemType(FsNodeFileSystemType fsNodeFileSystemType) {
        this.fsNodeFileSystemType = fsNodeFileSystemType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    
    
    
}

