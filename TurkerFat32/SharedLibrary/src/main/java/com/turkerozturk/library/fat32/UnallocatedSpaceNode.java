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

import com.turkerozturk.library.fat32.enums.EntryType;

/**
 *
 * @author u
 */
public class UnallocatedSpaceNode {
    
    private long absoluteStartOffset;    
    private long size;
    
    private String name;
    private EntryType entryType;

    public UnallocatedSpaceNode() {
        this.entryType = EntryType.UNALLOCATED_SPACE;
    }
    
    
    
    
    @Override
    public String toString() {
        return this.getAbsoluteStartOffset()+"";
    }

    public long getAbsoluteStartOffset() {
        return absoluteStartOffset;
    }

    public void setAbsoluteStartOffset(long absoluteStartOffset) {
        this.absoluteStartOffset = absoluteStartOffset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }   

    public String getName() {
        name = this.getAbsoluteStartOffset() + "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }
    
    
    
}
