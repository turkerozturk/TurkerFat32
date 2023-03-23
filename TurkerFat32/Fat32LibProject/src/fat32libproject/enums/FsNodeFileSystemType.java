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
 *
 * @author u
 */
public enum FsNodeFileSystemType {
    
    INITIAL_DEFAULT_NODE(0, "Initial Default Node"),
    PHYSICAL_PARTITION_NODE(1, "Physical Partition"),
    LOGICAL_PARTITION_NODE(2, "Logical Partition"),
    FAT32_NODE(3, "FAT32 Node"),
    FAT32_SLACK_NODE(4, "FAT32 Slack Node"),
    FAT32_UNUSED_SPACE_NODE(5, "FAT32 Unused Space Node");

    
    public final int value;
    private final String asReadableText;

    private FsNodeFileSystemType(int value, String asReadableText) {
        this.value = value;
        this.asReadableText = asReadableText;
    }

    private static final Map<Integer, FsNodeFileSystemType> _map = new HashMap<Integer, FsNodeFileSystemType>();

    static {
        for (FsNodeFileSystemType enumVariableName : FsNodeFileSystemType.values()) {
            _map.put(enumVariableName.value, enumVariableName);
        }
    }

    public static FsNodeFileSystemType from(int value) {
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
