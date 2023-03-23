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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import fat32libproject.FsNode;
import fat32libproject.enums.EntryAttributes;
import fat32libproject.enums.EntryType;
import fat32libproject.enums.DirectoryEntryAllocationStatus;
import fat32libproject.enums.LdirType;

/**
 * fatgen103.pdf
 * @author u
 */
public class PartOfLongNameEntry {
    
    /**
     * In order to meet the goals of locality-of-access and transparency, the long directory entry is defined as
* a short directory entry with a special attribute. As described previously, a long directory entry is just a
* regular directory entry in which the attribute field has a value of:
* ATTR_LONG_NAME ATTR_READ_ONLY |
* ATTR_HIDDEN |
* ATTR_SYSTEM |
* ATTR_VOLUME_ID
* A mask for determining whether an entry is a long-name sub-component should also be defined:
* ATTR_LONG_NAME_MASK ATTR_READ_ONLY |
* ATTR_HIDDEN |
* ATTR_SYSTEM |
* ATTR_VOLUME_ID |
* ATTR_DIRECTORY |
* ATTR_ARCHIVE
* When such a directory entry is encountered it is given special treatment by the file system. It is
* treated as part of a set of directory entries that are associated with a single short directory entry. Each
* long directory entry has the following structure:
     */

    private DirectoryEntryAllocationStatus lDirOrd; //byte
    private String lDirNameField1of3;
    private EntryAttributes lDirAttr;
    private LdirType lDirType;
    private int lDirChecksum; //byte
    private String lDirNameField2of3;
    private int lDirFstClusterLO; //UInt16
    private String lDirNameField3of3;

    //private final static long UINTMASK = 0xFFFFFFFFL;
    private final static int USHORTMASK = 0xFFFF;
    private final static short UBYTEMASK = 0xFF;
    
    private final static int LDIR_NAME_1_BYTES_SIZE = 10;
    private final static int LDIR_NAME_2_BYTES_SIZE = 12;
    private final static int LDIR_NAME_3_BYTES_SIZE = 4;

    public void parseValues(byte[] EntryBytes) throws UnsupportedEncodingException {
        /*
        lDirOrd = EntryBytes[0];
        lDirNameField1of3 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 1, 10)).Replace("\xFFFF", "").Replace("\x0000", "");
        lDirAttr = (EntryAttributes)EntryBytes[11]; lDirType = (lDirType)EntryBytes[12]; lDirChecksum = EntryBytes[13];
        lDirNameField2of3 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 14, 12)).Replace("\xFFFF", "").Replace("\x0000", "");
        lDirFstClusterLO = (UInt16)(EntryBytes[27] << 8 | EntryBytes[26]); lDirNameField3of3 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 28, 4)).Replace("\xFFFF", "").Replace("\x0000", "");
         */

        ByteBuffer byteBuffer = ByteBuffer.wrap(EntryBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);

       
            //https://stackoverflow.com/questions/11020893/java-removing-unicode-characters
            int lDirOrdValue = byteBuffer.get() & UBYTEMASK; //LDIR_Ord = EntryBytes[0];
            lDirOrd = DirectoryEntryAllocationStatus.from(lDirOrdValue);
            byte[] lDirName1Bytes = new byte[LDIR_NAME_1_BYTES_SIZE];
            byteBuffer.get(lDirName1Bytes);
            lDirNameField1of3 = new String(lDirName1Bytes, java.nio.charset.StandardCharsets.UTF_16LE);//.replaceAll("\u0020", "");//.replaceAll("\\P{Print}", "").trim();//	         lDirNameField1of3 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 1, 10)).Replace("\xFFFF", "").Replace("\x0000", "");
            int lDirTypeByte = byteBuffer.get() & UBYTEMASK;
            lDirType = LdirType.from(lDirTypeByte);// lDirType = (lDirType)EntryBytes[12];
            int lDirAttrByte = byteBuffer.get() & UBYTEMASK;
            lDirAttr = EntryAttributes.from(lDirAttrByte);//LDIR_Attr = (EntryAttributes)EntryBytes[11];
            lDirChecksum = byteBuffer.get() & UBYTEMASK; //LDIR_Chksum = EntryBytes[13];
            byte[] lDirName2Bytes = new byte[LDIR_NAME_2_BYTES_SIZE];
            byteBuffer.get(lDirName2Bytes);
            lDirNameField2of3 = new String(lDirName2Bytes, java.nio.charset.StandardCharsets.UTF_16LE);//.replaceAll("\u0020", "");//.replaceAll("\\P{Print}", "").trim();//LDIR_Name2 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 14, 12)).Replace("\xFFFF", "").Replace("\x0000", "");
            lDirFstClusterLO = byteBuffer.getShort() & USHORTMASK;//	         lDirFstClusterLO = (UInt16)(EntryBytes[27] << 8 | EntryBytes[26]);
            byte[] lDirName3Bytes = new byte[LDIR_NAME_3_BYTES_SIZE];
            byteBuffer.get(lDirName3Bytes);
            lDirNameField3of3 = new String(lDirName3Bytes, java.nio.charset.StandardCharsets.UTF_16LE);//.replaceAll("\u0020", "");//.replaceAll("\\P{Print}", "").trim();// lDirNameField3of3 = (System.Text.UnicodeEncoding.Unicode.GetString(EntryBytes, 28, 4)).Replace("\xFFFF", "").Replace("\x0000", "");
            // lDirNameField2of3 = lDirNameField2of3.substring(lDirNameField2of3.length() - 1);

        

    }
    
    /**
     * 32 bytelik entry bir uzun dosya ismi parcasi ise o ismi parse eder ve
     * bazi degiskenleri set eder ve FsNode olarak geri doner.
     *
     * @param thirtyTwoBytes uzun dosya isminin parcalarindan birini iceren 32
     * bytelik entry datasi
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static FsNode convertLongNameEntryToFsNode(byte[] thirtyTwoBytes) throws UnsupportedEncodingException {
        FsNode fsNode = new FsNode();
        PartOfLongNameEntry lDir = new PartOfLongNameEntry();
        lDir.parseValues(thirtyTwoBytes);
        fsNode.setLongNameChecksum(lDir.getlDirChecksum());
        String lfnPartial = (lDir.getlDirNameField1of3() + lDir.getlDirNameField2of3() + lDir.getlDirNameField3of3()).replaceAll("\u0000", "").replaceAll("\ufffd", "").replaceAll("\uffff", "");//TODO bu ne?
        //fsNode.setShortName(""); 
        fsNode.setLongName(lfnPartial);
        fsNode.setAllocationStatus(lDir.getlDirOrd());
        fsNode.setEntryType(EntryType.LONGFILENAME);
        return fsNode;
    }

    /**
     * The order of this entry in the sequence of long dir entries
* associated with the short dir entry at the end of the long dir set.
* If masked with 0x40 (LAST_LONG_ENTRY), this indicates the
* entry is the last long dir entry in a set of long dir entries. All valid
* sets of long dir entries must begin with an entry having this
* mask.
     * @return 
     */
    public DirectoryEntryAllocationStatus getlDirOrd() {
        return lDirOrd;
    }

    public void setlDirOrd(DirectoryEntryAllocationStatus lDirOrd) {
        this.lDirOrd = lDirOrd;
    }

    /**
     * Characters 1-5 of the long-name sub-component in this dir entry.
     * @return 
     */
    public String getlDirNameField1of3() {
        return lDirNameField1of3;
    }

    public void setlDirNameField1of3(String lDirNameField1of3) {
        this.lDirNameField1of3 = lDirNameField1of3;
    }

    /**
     * Attributes - must be ATTR_LONG_NAME
     * @return 
     */
    public EntryAttributes getlDirAttr() {
        return lDirAttr;
    }

    public void setlDirAttr(EntryAttributes lDirAttr) {
        this.lDirAttr = lDirAttr;
    }

    /**
     * If zero, indicates a directory entry that is a sub-component of a
    * long name. NOTE: Other values reserved for future extensions.
    * Non-zero implies other dirent types.
     * @return 
     */
    public LdirType getlDirType() {
        return lDirType;
    }

    public void setlDirType(LdirType lDirType) {
        this.lDirType = lDirType;
    }

    /**
     * Checksum of name in the short dir entry at the end of the long dir set.
     * @return 
     */
    public int getlDirChecksum() {
        return lDirChecksum;
    }

    public void setlDirChecksum(int lDirChecksum) {
        this.lDirChecksum = lDirChecksum;
    }

    /**
     * Characters 6-11 of the long-name sub-component in this dir entry.
     * @return 
     */
    public String getlDirNameField2of3() {
        return lDirNameField2of3;
    }

    public void setlDirNameField2of3(String lDirNameField2of3) {
        this.lDirNameField2of3 = lDirNameField2of3;
    }

    /**
     * Must be ZERO. This is an artifact of the FAT "first cluster" and
     * must be zero for compatibility with existing disk utilities. It's
     * meaningless in the context of a long dir entry.
     * @return 
     */
    public int getlDirFstClusterLO() {
        return lDirFstClusterLO;
    }

    public void setlDirFstClusterLO(int lDirFstClusterLO) {
        this.lDirFstClusterLO = lDirFstClusterLO;
    }

    /**
     * Characters 12-13 of the long-name sub-component in this dir entry.
     * @return 
     */
    public String getlDirNameField3of3() {
        return lDirNameField3of3;
    }

    public void setlDirNameField3of3(String lDirNameField3of3) {
        this.lDirNameField3of3 = lDirNameField3of3;
    }

}
