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


package fat32libproject.structures.fatarea;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;


/**
 *
 * @author u
 */
public class FatTable {

    private Long[] entries;
    private long sizeAsBytes;
    private int totalCountOfEntries;
    
    //private Map<long, long[]> c;

    final static long UINTMASK = 0xFFFFFFFFL;
    final static int USHORTMASK = 0xFFFF;
    final static short UBYTEMASK = 0xFF;
    final static int SIZE_OF_INT_VARIABLE = 4;

    
    private long fatSz32AsValidClusterNumberCapacity;
    
    private static final int END_OF_CLUSTER_MARK = 0x0FFFFFFF;

    

    public FatTable() {

    }

    public void parseFatTable(byte[] tableBytes, long fatSz32AsValidClusterNumberCapacity) {

        this.fatSz32AsValidClusterNumberCapacity = fatSz32AsValidClusterNumberCapacity;

        this.sizeAsBytes = tableBytes.length;
        this.totalCountOfEntries = (int) (this.sizeAsBytes / SIZE_OF_INT_VARIABLE);
        entries = new Long[this.totalCountOfEntries];
        ByteBuffer byteBuffer = ByteBuffer.wrap(tableBytes);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < this.totalCountOfEntries; i++) {

            this.entries[i] = byteBuffer.getInt() & UINTMASK;

        }
        
        
        this.parseChains();
    }

    
     private void parseChains() {

     }
     
       /*  eski ismi bringfatchain idi. verilen başlangıç clusteridsini fat tablosunda index olarak kullanır ve değerinde sonraki clusteridsi yazdığı müddetçe hepsini toplayıp geri döner.
    Bu metod sayesinde bir dosya veya dizinin kapladığı tüm clusterların idlerini öğrenmiş oluruz. Başlangıç clusteridsini buraya getirirken ise root hep 2den başladığı için biliyoruz ve devamınde entrylerin içinde başlangıç clusteridsi datastartid olark bulunuyor.
  */
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
    
    
     public long NextRelativeRing(long currentRelativeRing) {  //UInt32
        if ((currentRelativeRing != END_OF_CLUSTER_MARK) && (currentRelativeRing >= 2) && (currentRelativeRing <= this.fatSz32AsValidClusterNumberCapacity)) {

            //long result = getFatTables()[0].getEntries()[(int) currentRelativeRing]; // TODO ben default olarak fattable 0 kullaniyorum ama snra bunu parametreli yap. cunku bu sekilde diger fat tablolari varsa programda bir ise yaramadan duruyorlar cunku secilme imkani sunulmamis.
            long result = this.getEntries()[(int) currentRelativeRing]; // TODO ben default olarak fattable 0 kullaniyorum ama snra bunu parametreli yap. cunku bu sekilde diger fat tablolari varsa programda bir ise yaramadan duruyorlar cunku secilme imkani sunulmamis.
           

            // System.out.println("result2: " + result);
            return result;

        } else {
            return currentRelativeRing; // bu şekilde iyi değil. FAT corrupt ise problem olur. BringCorruptFATChain adında başka bir fonk yaz sonra.
        }

    }

    public long getSizeAsBytes() {
        return sizeAsBytes;
    }

    public void setSizeAsBytes(long sizeAsBytes) {
        this.sizeAsBytes = sizeAsBytes;
    }

    public int getTotalCountOfEntries() {
        return totalCountOfEntries;
    }

    public void setTotalCountOfEntries(int totalCountOfEntries) {
        this.totalCountOfEntries = totalCountOfEntries;
    }

    public Long[] getEntries() {
        return entries;
    }

    public void setEntries(Long[] entries) {
        this.entries = entries;
    }

    public long getFatSz32AsValidClusterNumberCapacity() {
        return fatSz32AsValidClusterNumberCapacity;
    }

    public void setFatSz32AsValidClusterNumberCapacity(long fatSz32AsValidClusterNumberCapacity) {
        this.fatSz32AsValidClusterNumberCapacity = fatSz32AsValidClusterNumberCapacity;
    }

   

}
