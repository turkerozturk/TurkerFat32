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


package fat32libproject.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * eski ismi DirEntryType
 * bunlar directory entrynin hangi turden oldugunu gosterir. hepsini bilmiyorum
 * ama dosya mi yoksa normal/dot/dotdot dizinlerden biri mi yoksa uzun dosya adi
 * mi oldugunu anlamakta kullaniyoruz. digerleri ile karsilastim mi bilmiyorum.
 *
 * @author u
 */
public enum EntryType {
    ENUMUNILKDEGERI0KURALSIZ(0, ""),
    VOLUME(1, "Volume"),
    DIRECTORY(2, "Directory"),
    DOTDIRECTORY(3, "Dot Directory"),
    DOTDOTDIRECTORY(4, "Double Dot Directory"),
    FILE(5, "Regular File"),
    LONGFILENAME(6, "Long File Name"),
    UNALLOCATED_SPACE(7, "Unallocated Space"),
    ENDOFDIRECTORY(8, "End of Directory"),
    INVALID(9, "Invalid"),
    UNKNOWN(10, "Unknown"),
    ORPHAN(11, "Orphan"),
    DELETED(12, "Deleted"),
    FILESLACK(13, "File Slack"),
    UNALLOCATED_SPACE_CONTAINER(14, "Unallocated Space Root Folder"),
    FILESYSTEM_META_DATA(15, "Filesystem Metadata");


    
    public final int value;
    private final String asReadableText;

    private EntryType(int value, String asReadableText) {
        this.value = value;
        this.asReadableText = asReadableText;
    }

    private static final Map<Integer, EntryType> _map = new HashMap<Integer, EntryType>();

    static {
        for (EntryType enumVariableName : EntryType.values()) {
            _map.put(enumVariableName.value, enumVariableName);
        }
    }

    public static EntryType from(int value) {
        if (_map.containsKey(value)) {
            return _map.get(value);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return asReadableText.toString();
    }
}
