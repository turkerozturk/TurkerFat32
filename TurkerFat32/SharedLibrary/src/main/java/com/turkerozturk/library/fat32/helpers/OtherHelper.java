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


package com.turkerozturk.library.fat32.helpers;

import com.turkerozturk.library.fat32.FsNode;
import com.turkerozturk.library.fat32.structures.reservedarea.Fat32PartitionBootSector;

/**
 *
 * @author u
 */
public class OtherHelper {

    private static final int D65536 = 65536;

    public static boolean areAllBytesZero(byte[] Bytes) {
        for (Byte b : Bytes) {
            if (b != 0) {
                return false;
            }
        }

        return true;
    }

    public static long ConvertHiAndLoWordToUInt(int HiWord, int LoWord) //uint
    {
        long formul = (HiWord * D65536) + LoWord;
        return formul;
    }

    /**
     * Prints the porperties of the FsNode with its values.
     *
     * @param fsNode Node to be printed
     */
    public static String prettyPrintNode(FsNode fsNode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getAllocationStatus: " + fsNode.getAllocationStatus() + "\r\n");
        stringBuilder.append("getLocalPositionId: " + fsNode.getLocalPositionId() + "\r\n");
        stringBuilder.append("getContainerId: " + fsNode.getContainerId() + "\r\n");
        stringBuilder.append("getFirstDataClusterIdOfFile: " + fsNode.getFirstDataClusterIdOfFile() + "\r\n");
        stringBuilder.append("getLongName: " + fsNode.getLongName() + "\r\n");
        stringBuilder.append("getShortName: " + fsNode.getShortName() + "\r\n");
        stringBuilder.append("getSizeAsBytes: " + fsNode.getSizeAsBytes() + "\r\n");
        stringBuilder.append("getEntryType: " + fsNode.getEntryType() + "\r\n");
        stringBuilder.append("getCreatedDate: " + fsNode.getCreatedDate() + "\r\n");
        stringBuilder.append("getWrittenDate: " + fsNode.getWrittenDate() + "\r\n");
        stringBuilder.append("getAccessedDate: " + fsNode.getAccessedDate() + "\r\n");
        stringBuilder.append("getLongNameChecksum: " + fsNode.getLongNameChecksum() + "\r\n");
        stringBuilder.append("getCalculatedChecksum: " + fsNode.getCalculatedChecksum() + "\r\n");
        stringBuilder.append("isDeleted: " + (fsNode.isDeleted() ? "Yes" : "No") + "\r\n");
        stringBuilder.append("getLongFullPath: " + fsNode.getAbsolutePath() + "\r\n");
        stringBuilder.append("getShortFullPath: " + fsNode.getShortFullPath() + "\r\n");
        stringBuilder.append("getName: " + fsNode.getName() + "\r\n");
        stringBuilder.append("getFolderDepth: " + fsNode.getDirectoryDepthLevel() + "\r\n");
        stringBuilder.append("getRawEntry: \r\n" + HexViewer.show(fsNode.getRawEntry(), false) + "\r\n");
        return stringBuilder.toString();
    }

    public static String prettyPrintPartitionBootSector(Fat32PartitionBootSector fat32PartitionBootSector) {
        StringBuilder stringBuilder = new StringBuilder();
stringBuilder.append("getBsJmpBoot: " + fat32PartitionBootSector.getBsJmpBoot() + "\r\n");
        stringBuilder.append("getBsOemName: " + fat32PartitionBootSector.getBsOemName() + "\r\n");
        stringBuilder.append("getBpbBytesPerSector: " + fat32PartitionBootSector.getBpbBytesPerSector() + "\r\n");
        stringBuilder.append("getBpbSectorsPerCluster: " + fat32PartitionBootSector.getBpbSectorsPerCluster() + "\r\n");
        stringBuilder.append("getBpbReservedSectorCount: " + fat32PartitionBootSector.getBpbReservedSectorCount() + "\r\n");
        stringBuilder.append("getBpbNumberOfFatTables: " + fat32PartitionBootSector.getBpbNumberOfFatTables() + "\r\n");
        stringBuilder.append("getBpbRootEntCount: " + fat32PartitionBootSector.getBpbRootEntCount() + "\r\n");
        stringBuilder.append("getBpbTotalSectors16: " + fat32PartitionBootSector.getBpbTotalSectors16() + "\r\n");
        stringBuilder.append("getBpbMedia: " + fat32PartitionBootSector.getBpbMedia() + "\r\n");
        stringBuilder.append("getBpbFatSize16: " + fat32PartitionBootSector.getBpbFatSize16() + "\r\n");
        stringBuilder.append("getBpbSectorsPerTrack: " + fat32PartitionBootSector.getBpbSectorsPerTrack() + "\r\n");
        stringBuilder.append("getBpbNumberOfHeads: " + fat32PartitionBootSector.getBpbNumberOfHeads() + "\r\n");
        stringBuilder.append("getBpbHiddenSectors: " + fat32PartitionBootSector.getBpbHiddenSectors() + "\r\n");
        stringBuilder.append("getBpbTotalSectors32: " + fat32PartitionBootSector.getBpbTotalSectors32() + "\r\n");
        stringBuilder.append("getBpbFatSize32: " + fat32PartitionBootSector.getBpbFatSize32() + "\r\n");
        stringBuilder.append("getBpbExtFlags: " + fat32PartitionBootSector.getBpbExtFlags() + "\r\n");
        stringBuilder.append("getBpbFileSystemVersion: " + fat32PartitionBootSector.getBpbFileSystemVersion() + "\r\n");
        stringBuilder.append("getBpbRootCluster: " + fat32PartitionBootSector.getBpbRootCluster() + "\r\n");
        stringBuilder.append("getBpbFileSystemInfo: " + fat32PartitionBootSector.getBpbFileSystemInfo() + "\r\n");
        stringBuilder.append("getBpbBackupBootSector: " + fat32PartitionBootSector.getBpbBackupBootSector() + "\r\n");
        stringBuilder.append("getBpbReserved: " + fat32PartitionBootSector.getBpbReserved() + "\r\n");
        stringBuilder.append("getBsDriveNumber: " + fat32PartitionBootSector.getBsDriveNumber() + "\r\n");
        stringBuilder.append("getBsReserved1: " + fat32PartitionBootSector.getBsReserved1() + "\r\n");
        stringBuilder.append("getBsBootSig: " + fat32PartitionBootSector.getBsBootSig() + "\r\n");
        stringBuilder.append("getBsVolumeId: " + fat32PartitionBootSector.getBsVolumeId() + "\r\n");
        stringBuilder.append("getBsVolumeLab: " + fat32PartitionBootSector.getBsVolumeLab() + "\r\n");
        stringBuilder.append("getBsFileSystemType: " + fat32PartitionBootSector.getBsFileSystemType() + "\r\n");
        stringBuilder.append("getBsExecutableCode: " + fat32PartitionBootSector.getBsExecutableCode() + "\r\n");
        stringBuilder.append("getBsEndingFlag: " + fat32PartitionBootSector.getBsEndingFlag() + "\r\n");
        stringBuilder.append("getFatOffsetAsByte: " + fat32PartitionBootSector.getFatOffsetAsByte() + "\r\n");
        stringBuilder.append("getRootDirFatOffsetAsByte: " + fat32PartitionBootSector.getRootDirFatOffsetAsByte() + "\r\n");
        stringBuilder.append("getDataOffsetAsByte: " + fat32PartitionBootSector.getDataOffsetAsByte() + "\r\n");
        stringBuilder.append("getClusterSizeAsByte: " + fat32PartitionBootSector.getClusterSizeAsByte() + "\r\n");
        stringBuilder.append("getTotalDataSectors: " + fat32PartitionBootSector.getTotalDataSectors() + "\r\n");
        stringBuilder.append("getTotalCountOfClusters: " + fat32PartitionBootSector.getTotalCountOfClusters() + "\r\n");
        stringBuilder.append("getTotalFatAreaSize: " + fat32PartitionBootSector.getTotalFatAreaSize() + "\r\n");
        stringBuilder.append("getTotalDataAreaSize: " + fat32PartitionBootSector.getTotalDataAreaSize() + "\r\n");
        stringBuilder.append("getFatSz32AsByte: " + fat32PartitionBootSector.getFatSz32AsByte() + "\r\n");
        stringBuilder.append("getEntryCountInOneCluster: " + fat32PartitionBootSector.getEntryCountInOneCluster() + "\r\n");
        stringBuilder.append("getFatSz32AsValidClusterNumberCapacity: " + fat32PartitionBootSector.getFatSz32AsValidClusterNumberCapacity() + "\r\n");
        stringBuilder.append("getVolumeCreatedDate: " + fat32PartitionBootSector.getVolumeCreatedDate() + "\r\n");
        return stringBuilder.toString();

    }
    
    
    /**
     * Prints the help sentences of the FsNode
     *
     * @param fsNode Node to be printed
     */
    public static String prettyPrintNodeHelp() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FsNode is our class to store parsed entries. Each entry is 32 bytes."
                + "It can be a directory entry or a long filename entry." + "\r\n");
        stringBuilder.append("getAllocationStatus: " + "If it is not an empty entry, it is allocated. The first byte determines this." + "\r\n");
        stringBuilder.append("getLocalPositionId: " + "Entries are located in directory entry clusters. Each are 32 bytes long. For example if an entry position shows 3, then 32 x 3 = 96. Its raw bytes started at 96. byte offset of that cluster." + "\r\n");
        stringBuilder.append("getContainerId: " + "Parent folder id. If it is 0, its parent is root folder." + "\r\n");
        stringBuilder.append("getFirstDataClusterIdOfFile: " + "Because file data or folder directory entries can allocate one or more clusters, it shows the first datacluster id of them. Using this id, we can learn the rest of cluster ids in the chain from fat table and then we can extract a file or learn the directory contents." + "\r\n");
        stringBuilder.append("getLongName: " + "If a file or folder has longname(more than 8+3 characters), after prepared by other methods, we are storing it here. Please read other documentation about longname and its entries." + "\r\n");
        stringBuilder.append("getShortName: " + "the first 8 + 3 bytes of the 32 byte entry. Trim 8 and 3 bytes then concatanate them with . character." + "\r\n");
        stringBuilder.append("getSizeAsBytes: " + "The real size of a file or folder. If it is a file, we can get separate the file and its slack space from its chain or clusters." + "\r\n");
        stringBuilder.append("getEntryType: " + "(TODO improve it) Whether it is a file or directory or dot or dotdot directory etc." + "\r\n");
        stringBuilder.append("getCreatedDate: " + "msdos datetime. Created date. It has also 10th of milliseconds accuracy." + "\r\n");
        stringBuilder.append("getWrittenDate: " + "msdos datetime.Written Date has no milliseconds value." + "\r\n");
        stringBuilder.append("getAccessedDate: " + "msdos datetime.Accessed date. It hasnt got hour minute second values. Therefore it shows them as 00:00:00. TODO maybe we need to get them from created or written date." + "\r\n");
        stringBuilder.append("getLongNameChecksum: " + "TODO calculate this checksum to make this parser more copmlete." + "\r\n");
        stringBuilder.append("getCalculatedChecksum: " + "TODO verify this calculated checksum and make it functional." + "\r\n");
        stringBuilder.append("isDeleted: " + "Whether it is deleted or not. We are not following deleted file or folders to not freeze the code. And they are showing sometimes allocated entry clusters. Therefore we cannot map the entries by firstclusterids of entry data. Because it is not uniue if we want to include the deleted entries." + "\r\n");
        stringBuilder.append("getLongFullPath: " + "We prepared this field to show the absolute fqdn full path without drive name and slashes are / and root represents / and if file or foldernames has longnames we used them, if not we used short ones, and we decided that the shortnames are all uppercase because shortnames are case insensitive." + "\r\n");
        stringBuilder.append("getShortFullPath: " + "Absolute full path like long full path, but all names are in shortname format." + "\r\n");
        stringBuilder.append("getName: " + "If it has longname it shows the longname, otherwise it shows the shortname." + "\r\n");
        stringBuilder.append("getFolderDepth: " + "It shows the depth level, how many parent directory it has." + "\r\n");
        stringBuilder.append("getRawEntry: " + "This is to check parsed information with raw entry data." + "\r\n");
        return stringBuilder.toString();
    }

}
