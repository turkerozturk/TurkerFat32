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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.turkerozturk.library.fat32.Fat32Parser;
import com.turkerozturk.library.fat32.FsNode;
import com.turkerozturk.library.fat32.enums.EntryAttributes;
import com.turkerozturk.library.fat32.helpers.HexViewer;

/**
 *şimdi bu metodu evde iyice incele.
 * @author u
 */
public class DirectoryClusterChain {
    /*
        private static final int END_OF_CLUSTER_MARK = 0x0FFFFFFF;

     public ArrayList<Long> getFatChain(long ClusterID) { //UInt32
        ArrayList<Long> fatChain = new ArrayList<>(); //UInt32

        long C = ClusterID; //UInt32
        while (C >= 2 && C != END_OF_CLUSTER_MARK && C <= reservedArea.getFat32PartitionBootSector().getFatSz32AsValidClusterNumberCapacity()) {
            //System.out.println("FATZİNCİRİ: " + C);
            fatChain.add(C);
            C = NextRelativeRing(C);
        }

        return fatChain;
    }
    */
    /*
        public List<FsNodeTemplate> GetDirectory(long ClusterID) { //throws IOException  { //UInt32

        ArrayList<FsNodeTemplate> Elemanlar = new ArrayList<>();

        try {

            String LongNamePartial = "";
            int KontrolToplami = 0;

            long ContainerID = 0;//UInt32

            ArrayList<Long> ChainRings = fat32Parser.getFatChain(ClusterID);//UInt32

            if (ChainRings.size() > 0) {
                byte[] ChainBytes = new byte[ChainRings.size() * (int) fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte()];
                for (int i = 0; i < ChainRings.size(); i++) {
                    long memory = fat32Parser.getLogicalDiskImageFile().getFilePointer();
                    long RelativeClusterID = ChainRings.get(i);//UInt32

                    long AbsoluteClusterID = RelativeClusterID - 2; //UInt32
                    long ClusterAddress = fat32Parser.getReservedArea().getFat32PartitionBootSector().getDataOffsetAsByte() + (AbsoluteClusterID * fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte()); //UInt64
                    System.out.println("degisken: " + ClusterAddress);

                    fat32Parser.getLogicalDiskImageFile().seek(ClusterAddress);//UInt64
                    byte[] ClusterBytes = new byte[(int) fat32Parser.getReservedArea().getFat32PartitionBootSector().getClusterSizeAsByte()];
                    fat32Parser.getLogicalDiskImageFile().read(ClusterBytes);
                    //   System.out.println(memory);

                    if (RelativeClusterID != 2) {
                        if (((EntryAttributes.from(ClusterBytes[32 + 11]).value & EntryAttributes.LONG_NAME_MASK.value) != EntryAttributes.LONG_NAME.value)) {
                            if (ClusterBytes[33] != 0x2E) {
                                break;
                            }
                        }
                    }

                    //source, source ofset based 0, destination, destination ofset based 0, count of bytes
                    //System.Buffer.BlockCopy(ClusterBytes, 0, ChainBytes, ClusterBytes.length * i, ClusterBytes.length);
                    ChainBytes = Arrays.copyOfRange(ClusterBytes, ClusterBytes.length * i, ClusterBytes.length * i + ClusterBytes.length);//TODO son parameterede +1 ?
                    // Parser.DataStream.seek(memory);
                }

                for (long EntryPositionIDInCluster = 0; EntryPositionIDInCluster < (fat32Parser.getReservedArea().getFat32PartitionBootSector().getEntryCountInOneCluster() * ChainRings.size()); EntryPositionIDInCluster += 1) //UInt32
                {
                    //byte[] OneEntry = Checker.CopyBytes(ChainBytes, Parser.DirEntrySizeAsByte * (int)EntryPositionIDInCluster, Parser.DirEntrySizeAsByte);
                    byte[] OneEntry = Arrays.copyOfRange(ChainBytes, Fat32Parser.DIRECTORY_ENTRY_SIZE_AS_BYTE * (int) EntryPositionIDInCluster, Fat32Parser.DIRECTORY_ENTRY_SIZE_AS_BYTE * (int) EntryPositionIDInCluster + Fat32Parser.DIRECTORY_ENTRY_SIZE_AS_BYTE);//TODO son parameterede +1 ?
                    System.out.println(EntryPositionIDInCluster + "---------------------------------------------------------------------------------");

                    System.out.println(HexViewer.show(OneEntry));

                    if (AreAllBytesZero(OneEntry) == true) {
                    } else {
                        FsNode FSNode = new FsNode();

                        Integer directoryAttributesValue = OneEntry[11] & UBYTEMASK;

                        if (directoryAttributesValue == null) {
                            System.out.println(EntryPositionIDInCluster + "rrr: " + OneEntry[11]);

                        }

                        boolean comparison = (EntryAttributes.from(OneEntry[11]).value & EntryAttributes.LONG_NAME_MASK.value) == EntryAttributes.LONG_NAME.value;

                        if (comparison) {
                            FSNode = fat32Parser.ConvertLdirToFsNode(OneEntry);
                            LongNamePartial = FSNode.getLongName() + LongNamePartial;
                            KontrolToplami = FSNode.getLongNameChecksum();
                            continue;
                        } else {
                            FSNode = fat32Parser.ConvertDirToFsNode(OneEntry);
                            //??????? asagidaki sacma degil mi
                            if (OneEntry[0] != 0xE5) {
                                FSNode.setLongName(LongNamePartial);
                            } else {
                                FSNode.setLongName(LongNamePartial);
                            }

                            LongNamePartial = "";
                            if (FSNode.getAllocationStatus() == 0x05) {
                            }
                            switch (FSNode.getEntryType()) {
                                case DOTDOTDIRECTORY:
                                    ContainerID = FSNode.getDataStartId();

                                    break;

                                case DOTDIRECTORY:
                                    break;

                                case DIRECTORY:
                                    break;

                            }
                        }

                        FSNode.setLocalPositionId(EntryPositionIDInCluster);
                        FSNode.setLocalPositionId(EntryPositionIDInCluster);
                        FSNode.setContainerId(ContainerID);

                        fat32Parser.fsNodesInChain.add(FSNode);
                        Elemanlar.add(FSNode);

                    }
                }
            }

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return Elemanlar;

    }
*/
    
    
}
