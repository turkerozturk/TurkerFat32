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

/**
 *
 * @author u
 */
public class FileSystemInformationNode {
    
    private String volumeName;
    private String fileSystemType;
    private long clusterSize;
    private long clusterCount;
    private long freeClusterCount;
    private String volumeSerialNumber;
    private boolean utcTimeStamps;
    
     @Override
    public String toString() {
        return this.getVolumeName() + " [" + this.getFileSystemType() + "]";
    }

    public long getClusterSize() {
        return clusterSize;
    }

    public void setClusterSize(long clusterSize) {
        this.clusterSize = clusterSize;
    }

    public long getClusterCount() {
        return clusterCount;
    }

    public void setClusterCount(long clusterCount) {
        this.clusterCount = clusterCount;
    }

    public long getFreeClusterCount() {
        return freeClusterCount;
    }

    public void setFreeClusterCount(long freeClusterCount) {
        this.freeClusterCount = freeClusterCount;
    }

    public String getVolumeSerialNumber() {
        return volumeSerialNumber;
    }

    public void setVolumeSerialNumber(String volumeSerialNumber) {
        this.volumeSerialNumber = volumeSerialNumber;
    }

    public boolean isUtcTimeStamps() {
        return utcTimeStamps;
    }

    public void setUtcTimeStamps(boolean utcTimeStamps) {
        this.utcTimeStamps = utcTimeStamps;
    }

    public String getVolumeName() {
        return volumeName;
    }

    public void setVolumeName(String volumeName) {
        this.volumeName = volumeName;
    }

    public String getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(String fileSystemType) {
        this.fileSystemType = fileSystemType;
    }
    
    
    
}
