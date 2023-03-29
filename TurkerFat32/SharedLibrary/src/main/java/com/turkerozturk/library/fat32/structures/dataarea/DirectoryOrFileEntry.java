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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;

import com.turkerozturk.library.fat32.FsNode;
import com.turkerozturk.library.fat32.enums.DirectoryEntryAllocationStatus;
import com.turkerozturk.library.fat32.enums.EntryAttributes;
import com.turkerozturk.library.fat32.enums.EntryType;
import com.turkerozturk.library.fat32.helpers.MsDosDateTime;
import com.turkerozturk.library.fat32.helpers.OtherHelper;

/**
 * Bir directory data clusterinda iki cesit entry tipi bulunur. Bunlardan biri
 * DirectoryOrFileEntry tipidir. Bir klasor veya dosyanin datasinin nereden
 * basladigini, kisa adinin ne oldugunu, klasor mu yoksa dosya mi oldugunu,
 * silinmis mi silinmemis mi oldugunu, kac culter yer kapladigini, olusturma
 * tarihi gibi tarihleri, dosyanin boyutu gibi bilgileri saklar. Bunlar hep 32
 * bytelik entry yapisindan elde edilirler.
 *
 * @author u
 */
public class DirectoryOrFileEntry {

    private String dirName;//11byte
    private EnumSet<EntryAttributes> entryAttributes;//byte
    private int dirNtRes;//byte
    private int dirCreatedtTimeTenth;//byte
    private int dirCreatedTime; //UInt16
    private int dirCreatedDate; //UInt16
    private int dirLastAccessedDate; //UInt16
    private int dirFstClusterHi; //UInt16
    private int dirWrittenTime; //UInt16
    private int dirWrittenDate; //UInt16
    private int dirFstClusterLo; //UInt16
    private long dirFileSize; //UInt32

    private String fileName;
    private String fileExtension;
    private Date createdDateTime;
    private Date accessedDateTime;
    private Date writtenDateTime;
    private long firstClusterNumber; //UInt32
    private DirectoryEntryAllocationStatus nameZeroOrAllocationStatus; //byte
    private int sfnChecksum; //byte
    private byte[] dirNameRaw;

    private final static long UINTMASK = 0xFFFFFFFFL;
    private final static int USHORTMASK = 0xFFFF;
    private final static short UBYTEMASK = 0xFF;
    private static final String DOT = ".";
    private static final String DOTDOT = "..";
    private static final String EXCLAMATION_MARK = "!";

    private final static int SHORT_FILE_NAME_LENGTH = 8;
    private final static int SHORT_FILE_EXTENSION_LENGTH = 3;
    private final static int SHORT_FILE_NAME_AND_EXTENSION_LENGTH = SHORT_FILE_NAME_LENGTH + SHORT_FILE_EXTENSION_LENGTH;
    private final static int FIRST_CHARACTER_OF_SHORTNAME_LENGTH = 1;

    public void calculateChecksum() {
        int sfnChecksumRaw = 0;
        for (int i = 0; i < SHORT_FILE_NAME_AND_EXTENSION_LENGTH; i++) {
            sfnChecksumRaw = ((((sfnChecksumRaw & 1) << 7) | ((sfnChecksumRaw & 0xfe) >> 1)) + dirNameRaw[i]);
            sfnChecksum = sfnChecksumRaw & UBYTEMASK;
        }
    }

    public void ParseValues(byte[] entryBytes) throws UnsupportedEncodingException {

        ByteBuffer byteBuffer = ByteBuffer.wrap(entryBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        dirNameRaw = new byte[SHORT_FILE_NAME_AND_EXTENSION_LENGTH];
        byteBuffer.get(dirNameRaw);
        calculateChecksum();//TODO ?
        int nameZeroOrAllocationStatusValue = dirNameRaw[0] & UBYTEMASK;
        this.nameZeroOrAllocationStatus = DirectoryEntryAllocationStatus.from(nameZeroOrAllocationStatusValue);
        //dirName = new String(Arrays.copyOfRange(dirNameRaw, 0, 7), java.nio.charset.StandardCharsets.UTF_8).trim();//.replaceAll("\u0020", "").replaceAll("\u0000", "").replaceAll("\ufffd", "").replaceAll("\uffff", "");//.replaceAll("\\P{Print}", "").trim();
        //fileExtension = new String(Arrays.copyOfRange(dirNameRaw, 8, 11), java.nio.charset.StandardCharsets.UTF_8).trim();//.replaceAll("\u0020", "").replaceAll("\u0000", "").replaceAll("\ufffd", "").replaceAll("\uffff", "");//.replaceAll("\\P{Print}", "").trim();

        dirName = new String(Arrays.copyOfRange(dirNameRaw, 0, SHORT_FILE_NAME_LENGTH)).trim();//.replaceAll("\u0020", "").replaceAll("\u0000", "").replaceAll("\ufffd", "").replaceAll("\uffff", "");//.replaceAll("\\P{Print}", "").trim();
        fileExtension = new String(Arrays.copyOfRange(dirNameRaw, SHORT_FILE_NAME_LENGTH, SHORT_FILE_NAME_AND_EXTENSION_LENGTH)).trim();//.replaceAll("\u0020", "").replaceAll("\u0000", "").replaceAll("\ufffd", "").replaceAll("\uffff", "");//.replaceAll("\\P{Print}", "").trim();

        if (!"".equals(fileExtension)) {
            dirName = dirName + DOT + fileExtension;

        }

        if (nameZeroOrAllocationStatus == DirectoryEntryAllocationStatus.DELETED_ENTRY) {
            dirName = EXCLAMATION_MARK + dirName.substring(FIRST_CHARACTER_OF_SHORTNAME_LENGTH);
        }

        fileName = dirName;

        int dirAttrByte = byteBuffer.get() & UBYTEMASK;
        this.entryAttributes = EntryAttributes.getFlags(dirAttrByte);

        dirNtRes = byteBuffer.get() & UBYTEMASK; //TODO bazi sistemlerde bu galiba silinen karakterin kondugu yer oluyormus.
        dirCreatedtTimeTenth = byteBuffer.get() & UBYTEMASK;

        MsDosDateTime createdDate = new MsDosDateTime();
        createdDate.setMilliSecondByteAsTenth(dirCreatedtTimeTenth);
        createdDate.setTimeWordLoByte(byteBuffer.get() & UBYTEMASK);
        createdDate.setTimeWordHiByte(byteBuffer.get() & UBYTEMASK);
        createdDate.setDateWordLoByte(byteBuffer.get() & UBYTEMASK);
        createdDate.setDateWordHiByte(byteBuffer.get() & UBYTEMASK);
        createdDateTime = createdDate.Parse();

        MsDosDateTime accessedDate = new MsDosDateTime();
        accessedDate.setDateWordLoByte(byteBuffer.get() & UBYTEMASK);
        accessedDate.setDateWordHiByte(byteBuffer.get() & UBYTEMASK);
        accessedDateTime = accessedDate.Parse();

        dirFstClusterHi = byteBuffer.getShort() & USHORTMASK;

        MsDosDateTime writtenDate = new MsDosDateTime();
        writtenDate.setTimeWordLoByte(byteBuffer.get() & UBYTEMASK);
        writtenDate.setTimeWordHiByte(byteBuffer.get() & UBYTEMASK);
        writtenDate.setDateWordLoByte(byteBuffer.get() & UBYTEMASK);
        writtenDate.setDateWordHiByte(byteBuffer.get() & UBYTEMASK);
        writtenDateTime = writtenDate.Parse();

        dirFstClusterLo = byteBuffer.getShort() & USHORTMASK;
        firstClusterNumber = dirFstClusterHi * 65536 + dirFstClusterLo;// & UINTMASK;
        dirFileSize = byteBuffer.getInt() & UINTMASK;

    }

    /* 32 bytelik entry bir dosya veya ContentsOfDirectory entrysi ise o dosya veya ContentsOfDirectory bilgilerini parse eder ve FsNode olarak geri doner. */
    public static FsNode convertDirectoryEntryToFsNode(byte[] thirtyTwoBytes) throws UnsupportedEncodingException {
        FsNode fsNode = new FsNode();
        DirectoryOrFileEntry directoryOrFileEntry = new DirectoryOrFileEntry();
        directoryOrFileEntry.ParseValues(thirtyTwoBytes);
        int hi = directoryOrFileEntry.getDirFstClusterHi();//ushort
        int lo = directoryOrFileEntry.getDirFstClusterLo();//ushort
        //long FirstClusterNumberOfCurrentFile = ConvertHiAndLoWordToUInt(hi, lo);//UInt32

        //Integer number = directoryOrFileEntry.getDirAttr().value;
        //System.out.println("burası DirectoryOrFileEntry.java: " + directoryOrFileEntry.entryAttributes );
        if (directoryOrFileEntry.entryAttributes.contains(EntryAttributes.DIRECTORY) | directoryOrFileEntry.entryAttributes.contains(EntryAttributes.VOLUME_ID)) {
            //switch (EntryAttributes.from(directoryOrFileEntry.getDirAttr().value & (EntryAttributes.DIRECTORY.value | EntryAttributes.VOLUME_ID.value))) {
            /*
            case UNKNOWN:
                if (directoryOrFileEntry.getNameZeroOrAllocationStatus() != DirectoryEntryAllocationStatus.EMPTY_ENTRY) {
                    fsNode.setEntryType(EntryType.FILE);
                } else {
                    fsNode.setEntryType(EntryType.UNKNOWN);
                }
                break;
             */
            if (directoryOrFileEntry.entryAttributes.contains(EntryAttributes.DIRECTORY)) {

                switch (directoryOrFileEntry.getDirName().trim()) {
                    case DOT:
                        fsNode.setEntryType(EntryType.DOTDIRECTORY);
                        break;

                    case DOTDOT:
                        fsNode.setEntryType(EntryType.DOTDOTDIRECTORY);
                        break;

                    default:
                        fsNode.setEntryType(EntryType.DIRECTORY);
                        break;
                }

            } else if (directoryOrFileEntry.entryAttributes.contains(EntryAttributes.VOLUME_ID)) {

                fsNode.setEntryType(EntryType.VOLUME);

            } else {

                fsNode.setEntryType(EntryType.INVALID);

            }
        } else {
            fsNode.setEntryType(EntryType.FILE);

        }

        if (directoryOrFileEntry.getNameZeroOrAllocationStatus() != DirectoryEntryAllocationStatus.DELETED_ENTRY) {
            fsNode.setDeleted(false);
        } else {
            fsNode.setDeleted(true);
        }

        fsNode.setFirstDataClusterIdOfFile(OtherHelper.ConvertHiAndLoWordToUInt(hi, lo));
        fsNode.setShortName(directoryOrFileEntry.getFileName());
        fsNode.setName(fsNode.getShortName());
        fsNode.setCalculatedChecksum(directoryOrFileEntry.getSfnChecksum());
        //fsNode.setLongName("");
        fsNode.setSizeAsBytes(directoryOrFileEntry.getDirFileSize());
                
        long allocatedClusterSize = fsNode.getSizeAsBytes() / 1024;
        fsNode.setSize(allocatedClusterSize);

                
        
        fsNode.setCreatedDate(directoryOrFileEntry.getCreatedDateTime());
        fsNode.setWrittenDate(directoryOrFileEntry.getWrittenDateTime());
        fsNode.setAccessedDate(directoryOrFileEntry.getAccessedDateTime());
        fsNode.setAllocationStatus(directoryOrFileEntry.getNameZeroOrAllocationStatus());

        //TODO asagidakine kodda ihtiyac yok. Sadece pretty print yaparak raw degeri karsilastirmak icin.
        // bu sebeple comment edilebilir cunku her node icin 32 byte yer kaplar.
        fsNode.setRawEntry(thirtyTwoBytes);
        return fsNode;
    }

    /**
     * Short Name of the directory or file
     *
     * @return
     */
    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public int getDirNtRes() {
        return dirNtRes;
    }

    public void setDirNtRes(int dirNtRes) {
        this.dirNtRes = dirNtRes;
    }

    /**
     * milliseconds of created date in ten milliseconds resolution
     *
     * @return
     */
    public int getDirCreatedtTimeTenth() {
        return dirCreatedtTimeTenth;
    }

    public void setDirCreatedtTimeTenth(int dirCreatedtTimeTenth) {
        this.dirCreatedtTimeTenth = dirCreatedtTimeTenth;
    }

    /**
     * hour minute second of created date
     *
     * @return
     */
    public int getDirCreatedTime() {
        return dirCreatedTime;
    }

    public void setDirCreatedTime(int dirCreatedTime) {
        this.dirCreatedTime = dirCreatedTime;
    }

    /**
     * year month day of created date
     *
     * @return
     */
    public int getDirCreatedDate() {
        return dirCreatedDate;
    }

    public void setDirCreatedDate(int dirCreatedDate) {
        this.dirCreatedDate = dirCreatedDate;
    }

    /**
     * year month day of accessed date there is no hour minute second of
     * accessed date in FAT32 filesystem. therefore they are 00:00:00
     *
     * @return
     */
    public int getDirLastAccessedDate() {
        return dirLastAccessedDate;
    }

    public void setDirLastAccessedDate(int dirLastAccessedDate) {
        this.dirLastAccessedDate = dirLastAccessedDate;
    }

    /**
     * high bytes of first data clusterid of this entry using with low bytes,
     * gives the clusterId
     *
     * @return
     */
    public int getDirFstClusterHi() {
        return dirFstClusterHi;
    }

    public void setDirFstClusterHi(int dirFstClusterHi) {
        this.dirFstClusterHi = dirFstClusterHi;
    }

    /**
     * hour minute second of written date
     *
     * @return
     */
    public int getDirWrittenTime() {
        return dirWrittenTime;
    }

    public void setDirWrittenTime(int dirWrittenTime) {
        this.dirWrittenTime = dirWrittenTime;
    }

    /**
     * year month day of written date
     *
     * @return
     */
    public int getDirWrittenDate() {
        return dirWrittenDate;
    }

    public void setDirWrittenDate(int dirWrittenDate) {
        this.dirWrittenDate = dirWrittenDate;
    }

    /**
     * low bytes of first data clusterid of this entry
     *
     * @return
     */
    public int getDirFstClusterLo() {
        return dirFstClusterLo;
    }

    public void setDirFstClusterLo(int dirFstClusterLo) {
        this.dirFstClusterLo = dirFstClusterLo;
    }

    /**
     * size of data
     *
     * @return
     */
    public long getDirFileSize() {
        return dirFileSize;
    }

    public void setDirFileSize(long dirFileSize) {
        this.dirFileSize = dirFileSize;
    }

    /**
     * name of file or directory without extension
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * extension of file or directory without name
     *
     * @return
     */
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * File or folder creation date
     *
     * @return
     */
    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    /**
     * File or folder accessed date
     *
     * @return
     */
    public Date getAccessedDateTime() {
        return accessedDateTime;
    }

    public void setAccessedDateTime(Date accessedDateTime) {
        this.accessedDateTime = accessedDateTime;
    }

    /**
     * File or folder written date
     *
     * @return
     */
    public Date getWrittenDateTime() {
        return writtenDateTime;
    }

    public void setWrittenDateTime(Date writtenDateTime) {
        this.writtenDateTime = writtenDateTime;
    }

    /**
     * data of the file or folder starts from this cluster also using this
     * cluster Id at fat table, we are collecting the other cluster numbers of
     * the data chain to obtain directory entries or file data.
     *
     * @return
     */
    public long getFirstClusterNumber() {
        return firstClusterNumber;
    }

    public void setFirstClusterNumber(long firstClusterNumber) {
        this.firstClusterNumber = firstClusterNumber;
    }

    /**
     * The first character of the shortname indicates sometimes the status of
     * the file or directory. We can understand from here whether the file or
     * folder is in deleted status or not.
     *
     * @return
     */
    public DirectoryEntryAllocationStatus getNameZeroOrAllocationStatus() {
        return nameZeroOrAllocationStatus;
    }

    public void setNameZeroOrAllocationStatus(DirectoryEntryAllocationStatus nameZeroOrAllocationStatus) {
        this.nameZeroOrAllocationStatus = nameZeroOrAllocationStatus;
    }

    /**
     * checksum of short name. its caltulation formula obtained from internet.
     *
     * @return
     */
    public int getSfnChecksum() {
        return sfnChecksum;
    }

    public void setSfnChecksum(int sfnChecksum) {
        this.sfnChecksum = sfnChecksum;
    }

    /**
     * short name in raw format. we are using other variable to show shortname,
     * if it is deleted we replace the first char with ! character there. but
     * here, it is in raw format.
     *
     * @return
     */
    public byte[] getDirNameRaw() {
        return dirNameRaw;
    }

    public void setDirNameRaw(byte[] dirNameRaw) {
        this.dirNameRaw = dirNameRaw;
    }

}
