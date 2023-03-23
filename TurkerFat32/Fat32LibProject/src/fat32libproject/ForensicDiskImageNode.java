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


package fat32libproject;

/**
 *
 * @author u
 */
public class ForensicDiskImageNode {
    
    private int bytesPerSector;
    private int sectorCount;
    private String sourcePath;
    private String ImageType;
    
     @Override
    public String toString() {
        return this.getSourcePath();
    }

    public int getBytesPerSector() {
        return bytesPerSector;
    }

    public void setBytesPerSector(int bytesPerSector) {
        this.bytesPerSector = bytesPerSector;
    }

    public int getSectorCount() {
        return sectorCount;
    }

    public void setSectorCount(int sectorCount) {
        this.sectorCount = sectorCount;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getImageType() {
        return ImageType;
    }

    public void setImageType(String ImageType) {
        this.ImageType = ImageType;
    }
    
    
}
