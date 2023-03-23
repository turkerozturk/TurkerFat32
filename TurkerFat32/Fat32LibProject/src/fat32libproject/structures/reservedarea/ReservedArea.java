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


package fat32libproject.structures.reservedarea;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author u
 */
public class ReservedArea {
    
    RandomAccessFile logicalDiskImageFile;
    
    private static final int FAT32_BOOT_SECTOR_SIZE = 512;

    Fat32PartitionBootSector fat32PartitionBootSector;
    
    
    
    public ReservedArea (RandomAccessFile logicalDiskImageFile) {
        this.logicalDiskImageFile = logicalDiskImageFile;
    }    
    
    public void parsePartitionBootSector() throws IOException {
        this.logicalDiskImageFile.seek(0);
        byte[] BOOT_SECTOR_Buffer = new byte[FAT32_BOOT_SECTOR_SIZE];
        this.logicalDiskImageFile.read(BOOT_SECTOR_Buffer);
        this.fat32PartitionBootSector = new Fat32PartitionBootSector();
        this.fat32PartitionBootSector.ParseValues(BOOT_SECTOR_Buffer);
    }
    
   

    public Fat32PartitionBootSector getFat32PartitionBootSector() {
        return fat32PartitionBootSector;
    }

    public void setFat32PartitionBootSector(Fat32PartitionBootSector fat32PartitionBootSector) {
        this.fat32PartitionBootSector = fat32PartitionBootSector;
    }

}
