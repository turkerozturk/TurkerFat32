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


package fat32libproject.structures.dataarea;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import fat32libproject.FsNode;
import fat32libproject.enums.EntryAttributes;
import fat32libproject.enums.DirectoryEntryAllocationStatus;
import fat32libproject.enums.EntryType;
import fat32libproject.helpers.OtherHelper;
import static fat32libproject.structures.dataarea.DataArea.UBYTEMASK;
import fat32libproject.structures.fatarea.FatTable;
import fat32libproject.structures.reservedarea.Fat32PartitionBootSector;

/**
 * TODO directory data clusterlarinin da slack bölgeleri var.
 *
 * @author u
 */
public class Directory {

    private static final int DIRECTORY_ENTRY_SIZE_AS_BYTE = 32;

    private final RandomAccessFile logicalDiskImageFile;
    private final Fat32PartitionBootSector fat32PartitionBootSector;
    private final FatTable fatTable;

    boolean ignoreDotAndDotDotDirectoryEntries;

    /**
     * Because a directory consists of one or more clusters, this variable is an
     * array of cluster numbers.
     */
    private ArrayList<Long> clusterIdsInChain;

    /**
     * The entries in directory.
     */
    ArrayList<Object> childNodes = new ArrayList<>();

    /**
     * TODO bunu şimdilik sadece parametre olarak gelen baslangic cluster id
     * sini almak icin kullaniyoruz fakat aslinda diger alanlarini da doldurup
     * oyle almak daha iyi olur.
     *
     */
    FsNode fsNodeOfDirectory;

    /**
     *
     * @param logicalDiskImageFile
     * @param fat32PartitionBootSector
     * @param fatTable
     * @param fsNode
     * @throws IOException
     */
    public Directory(RandomAccessFile logicalDiskImageFile, Fat32PartitionBootSector fat32PartitionBootSector, FatTable fatTable, FsNode fsNode, boolean ignoreDotAndDotDotDirectoryEntries) throws IOException {
        this.logicalDiskImageFile = logicalDiskImageFile;
        this.fatTable = fatTable;
        this.fat32PartitionBootSector = fat32PartitionBootSector;
        this.fsNodeOfDirectory = fsNode;
        this.ignoreDotAndDotDotDirectoryEntries = ignoreDotAndDotDotDirectoryEntries;
        //OtherHelper.prettyPrintNode(this.fsNodeOfDirectory);
        //System.out.println("Directory.java satir 62: " + this.fsNodeOfDirectory.getLongFullPath());
        //System.out.println(this.fsNodeOfDirectory.getLongFullPath()); //  + " | (Directory.java)" );

        this.parseDirectoryContents();
    }

    //public ArrayList<FsNodeTemplate> parseDirectoryContents(long ClusterID) { //throws IOException  { //UInt32
    /**
     * "." is the directory itself ".." is the parent directory Only root
     * directory hasn't got "." and ".." directories.
     *
     * @return all nodes of directory without following subdirectories
     * @throws java.io.IOException
     */
    private ArrayList<Object> parseDirectoryContents() throws IOException { //throws IOException  { //UInt32

        //System.out.println("clusterıd bu" + fsNodeOfDirectory.getFirstDataClusterIdOfFile());
        this.clusterIdsInChain = this.fatTable.getAChainOfClusterIdsIFromFatTable(fsNodeOfDirectory.getFirstDataClusterIdOfFile());//UInt32

        String longNamePartial = "";
        int kontrolToplami = 0;

        long containerId = 0;//UInt32

        long clusterSizeAsByte = this.fat32PartitionBootSector.getClusterSizeAsByte();

        if (clusterIdsInChain.size() > 0) {

            /* once directory entrylerin kapladigi yer kadar byte ayiriyoruz */
            byte[] totalBytesInTheChain = new byte[clusterIdsInChain.size() * (int) clusterSizeAsByte];

            /* sonra eger clusteridlerde problem olmadigi surece onlarin clusterlarindaki byte degerlerini bizim totalBytesInTheChain degiskenine yukluyoruz.*/
            for (int i = 0; i < clusterIdsInChain.size(); i++) {
                //long memory = this.logicalDiskImageFile.getFilePointer();
                long relativeClusterId = clusterIdsInChain.get(i);//UInt32
                long absoluteClusterId = relativeClusterId - 2; //UInt32
                long clusterAddress = this.getFat32PartitionBootSector().getDataOffsetAsByte() + (absoluteClusterId * clusterSizeAsByte); //UInt64
                //System.out.println("degisken: " + clusterAddress);

                this.getLogicalDiskImageFile().seek(clusterAddress);//UInt64
                byte[] clusterBytes = new byte[(int) clusterSizeAsByte];
                this.getLogicalDiskImageFile().read(clusterBytes);
                //   System.out.println(memory);

                /* burada donguden gerekirse cikmak icin bir kural var 
                * Açıklaması şu: root directory cluster zinciri hariç, 
                * diğer tüm directory cluster zincirlerinin 
                * ilk 32 bytelık entrysi her zaman klasörün kendisine ait entrydir yani DOT dur yani entrynin içinde yazan clusterid numarası kendisinin idsidir.
                * İkinci 32 bytelık entrysi ise yine root hariç, DOTDOT dur yani ikinci entrynin içinde yazan cluster id bir üst dizinin idsir. yani ebeveyninin.
                * ben neden DOTDOT entrynin long name flagı içerip içermediğini kontrol ediyorum bilmiyorum ama burada öyle yapmışım.
                * long name içermiyorsa ve kısa adın ilk harfi yani allocation statusu 0x2e  değilse döngüden çıkıyoruz.
                * TODO neden çıkıyoruz?
                * 
                 */
                if (relativeClusterId != 2) {
                    if (!EntryAttributes.getFlags(clusterBytes[32 + 11]).contains(EntryAttributes.LONG_NAME)) {
                        if (clusterBytes[33] != 0x2E) {
                            break;
                        }
                    }
                }

                //source, source ofset based 0, destination, destination ofset based 0, count of bytes
                //System.Buffer.BlockCopy(clusterBytes, 0, totalBytesInTheChain, clusterBytes.length * i, clusterBytes.length);
                totalBytesInTheChain = Arrays.copyOfRange(clusterBytes, clusterBytes.length * i, clusterBytes.length * i + clusterBytes.length);//TODO son parameterede +1 ?
                // Parser.DataStream.seek(memory);
            }

            /* sonra da az once yuklemis oldugumuz bir veya birkac clusterin bytelarini 32ser 32ser parselleyip entrylerimizi elde ediyoruz */
            final long entryCountInOneCluster = this.getFat32PartitionBootSector().getEntryCountInOneCluster();
            for (long entryPositionIdInCluster = 0; entryPositionIdInCluster < (entryCountInOneCluster * clusterIdsInChain.size()); entryPositionIdInCluster += 1) //UInt32
            {
                //byte[] oneEntry = Checker.CopyBytes(totalBytesInTheChain, Parser.DirEntrySizeAsByte * (int)entryPositionIdInCluster, Parser.DirEntrySizeAsByte);
                byte[] oneEntry = Arrays.copyOfRange(totalBytesInTheChain, DIRECTORY_ENTRY_SIZE_AS_BYTE * (int) entryPositionIdInCluster, DIRECTORY_ENTRY_SIZE_AS_BYTE * (int) entryPositionIdInCluster + DIRECTORY_ENTRY_SIZE_AS_BYTE);//TODO son parameterede +1 ?
                //System.out.println(entryPositionIdInCluster + "---------------------------------------------------------------------------------");

                //System.out.println(HexViewer.show(oneEntry));
                if (OtherHelper.areAllBytesZero(oneEntry) == true) {
                    // bu 32 bytelik kisim bos olduguna gore bir entry alloce edilmemis demek.
                } else {
                    FsNode fsNode;

                    Integer directoryAttributesValue = oneEntry[11] & UBYTEMASK;
                    //  System.out.println(directoryAttributesValue + " " + EntryAttributes.getFlags(directoryAttributesValue));
                    //boolean isLongNameEntry = EntryAttributes.getFlags(directoryAttributesValue).contains(EntryAttributes.LONG_NAME_MASK);
                    boolean isLongNameEntry = EntryAttributes.getFlags(directoryAttributesValue).contains(EntryAttributes.LONG_NAME);

                    // System.out.println(isLongNameEntry + " " + directoryAttributesValue + " " + EntryAttributes.getFlags(directoryAttributesValue));
                    //boolean isLongNameEntry = (EntryAttributes.from(directoryAttributesValue).value & EntryAttributes.LONG_NAME_MASK.value) == EntryAttributes.LONG_NAME.value;
                    if (isLongNameEntry) {
                        fsNode = PartOfLongNameEntry.convertLongNameEntryToFsNode(oneEntry);
                        longNamePartial = fsNode.getLongName() + longNamePartial;
                        //System.out.println(longNamePartial);
                        kontrolToplami = fsNode.getLongNameChecksum();
                        continue;
                    } else {
                        fsNode = DirectoryOrFileEntry.convertDirectoryEntryToFsNode(oneEntry);
                        //??????? asagidaki sacma degil mi
                        /*
                        if (oneEntry[0] != DirectoryEntryAllocationStatus.DELETED_ENTRY.value) {
                            fsNode.setLongName(longNamePartial);
                        } else {
                            fsNode.setLongName(longNamePartial);
                        }
                         */
                        fsNode.setLongName(longNamePartial);
                        if (!"".equals(fsNode.getLongName())) {
                            fsNode.setName(fsNode.getLongName());
                        } else {
                            fsNode.setName(fsNode.getShortName());

                        }

                        longNamePartial = "";

                        if (fsNode.getAllocationStatus() == DirectoryEntryAllocationStatus.JAPAN_KANJI_INDICATOR) {
                            // TODO what indicates 0x05
                        }
                        /**
                         * TEST SİL *
                         */

                        //https://stackoverflow.com/questions/23056324/why-does-java-allow-null-value-to-be-assigned-to-an-enum
                        if (fsNode.getEntryType() == null) {
                            //  System.out.println("Directory.java NULL: " + fsNode.getFirstDataClusterIdOfFile() + ", " + fsNode.getLongName());
                            //   System.out.println("*******************");
                            //    OtherHelper.prettyPrintNode(fsNode);
                            //    System.out.println("---------------------");
                        } else {
                            // System.out.println("Directory.java NULL DEĞİL" + fsNode.getFirstDataClusterIdOfFile() + ", " + fsNode.getShortName());
                            switch (fsNode.getEntryType()) {

                                case DOTDOTDIRECTORY:
                                    containerId = fsNode.getFirstDataClusterIdOfFile();
                                    break;
                                case DOTDIRECTORY:
                                    break;
                                case DIRECTORY:
                                    break;
                                case VOLUME:
                                    break;

                            }

                        }
                        /**
                         * TEST SİL *
                         */
                        // System.out.println(this.fsNodeOfDirectory.getLongFullPath() + "/" + fsNode.getName()); //  + " | (Directory.java)" );

                        /**/
                    }

                    /* 
                    * Bu duzeltmenin sebebi, 32 bytlik entrynin ilk harfi eger entry directory or file entry tipinde ise silinip silinmedigini veya bos oldugunu anlamaya yariyorken,
                    * eger entry part of long file name entry ise long file parcalarindan hangisinin sira numarasi veya son olup olmadigini anlamaya yaramasi
                    * ve bunlarin tamamini ayni enumda tanimlamamizdan oturu, long filename degilse o zaman enum olarak kisa adin ilk harfinin long filenamee karsilik dusen enumunu yazip cirkin gorunmesin diye,
                    * allocation satus enumuna allocated diye kafamdan bir deger ekledim ve degerini -1 yaptim, yani olmasi gerektigi sekilde orada bir kisa dosya adi varsa yani o dosya adi bos degilse o entry allocated durumundadir.
                    * TODO yalniz rootun ismi yok, belki onda sacma gorunur diyecgim ama pardon onda da volume id anlaminda adi, yine de adi yoksa yani olmayabilir,
                    * o zaman volume id ise gibi bir kontrol koymak daha gerekir asagiya;
                     */
                    if (!"".equals(fsNode.getName()) | fsNode.getEntryType() != EntryType.VOLUME) {
                        fsNode.setAllocationStatus(DirectoryEntryAllocationStatus.ALLOCATED_ENTRY);
                    }

                    fsNode.setLocalPositionId(entryPositionIdInCluster);
                    fsNode.setContainerId(containerId);

                 //   if (!ignoreDotAndDotDotDirectoryEntries & (fsNode.getEntryType().equals(EntryType.DOTDIRECTORY) | fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY))) {
                    if (this.ignoreDotAndDotDotDirectoryEntries & (fsNode.getEntryType().equals(EntryType.DOTDIRECTORY) | fsNode.getEntryType().equals(EntryType.DOTDOTDIRECTORY))) {
                        //
                    } else {
                        childNodes.add(fsNode);
                    }

                }
            }
        }

        return childNodes;

    }

    private RandomAccessFile getLogicalDiskImageFile() {
        return logicalDiskImageFile;
    }

    /*
    public void setLogicalDiskImageFile(RandomAccessFile logicalDiskImageFile) {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }
     */
    private Fat32PartitionBootSector getFat32PartitionBootSector() {
        return fat32PartitionBootSector;
    }

    /*
    public void setFat32PartitionBootSector(Fat32PartitionBootSector fat32PartitionBootSector) {
        this.fat32PartitionBootSector = fat32PartitionBootSector;
    }

    public ArrayList<Long> getClusterIdsInChain() {
        return clusterIdsInChain;
    }

    public void setClusterIdsInChain(ArrayList<Long> clusterIdsInChain) {
        this.clusterIdsInChain = clusterIdsInChain;
    }
     */
    public ArrayList<Object> getChildren() {
        return childNodes;
    }
    /*
    public void setDirectoryContents(ArrayList<FsNode> childNodes) {
        this.childNodes = childNodes;
    }

    public FsNode getFsNodeOfDirectory() {
        return fsNodeOfDirectory;
    }

    public void setFsNodeOfDirectory(FsNode fsNodeOfDirectory) {
        this.fsNodeOfDirectory = fsNodeOfDirectory;
    }
     */
}
