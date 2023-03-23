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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Eski ismi DirAttr idi.
 * @author u
 */
public enum EntryAttributes {
    //UNKNOWN(0x00, "Unknown"),
    READ_ONLY(0x01, "Read Only"),
    HIDDEN(0x02, "Hidden"),
    SYSTEM(0x4, "System"),
    VOLUME_ID(0x08, "Volume"),
    DIRECTORY(0x10, "Directory"),
    ARCHIVE(0x20, "Archive"),
    LONG_NAME(0x01 | 0x02 | 0x04 | 0x08, "Long Name"),
    LONG_NAME_MASK(0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20, "Long Name Mask");

    //http://eddmann.com/posts/using-bit-flags-and-enumsets-in-java/
    //public static final EnumSet<DirAttr> ALL_OPTS = EnumSet.allOf(EntryAttributes.class);
    public static final EnumSet<EntryAttributes> ALL_OPTS = EnumSet.allOf(EntryAttributes.class);

    //LONG_NAME(READ_ONLY | HIDDEN | SYSTEM | VOLUME_ID, ""),
    //LONG_NAME_MASK(READ_ONLY | HIDDEN | SYSTEM | VOLUME_ID | DIRECTORY | ARCHIVE, "");
    public final int value;
    private final String asReadableText;

    private EntryAttributes(int value, String asReadableText) {
        this.value = value;
        this.asReadableText = asReadableText;
    }

    private static final Map<Integer, EntryAttributes> _map = new HashMap<Integer, EntryAttributes>();

    static {
        for (EntryAttributes enumVariableName : EntryAttributes.values()) {
            _map.put(enumVariableName.value, enumVariableName);
        }
    }

    public static EntryAttributes from(int value) {
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
    
    ////http://javarevisited.blogspot.com.tr/2014/03/how-to-use-enumset-in-java-with-example.html
    //https://stackoverflow.com/questions/5346477/implementing-a-bitfield-using-java-enums
    /**
     * Bu metod sayesinde bit bayraklar iceren sayinin hangi bitlerinin set edildigini anlamis oluyoruz.
     * Cok onemli ornek bir metod. Bulana ve anlayana kadar epey vaktim gitti.
     * @param value
     * @return 
     */
    public static EnumSet<EntryAttributes> getFlags(long value) {

        EnumSet<EntryAttributes> flags = EnumSet.allOf(EntryAttributes.class);
        for (Iterator it = flags.iterator(); it.hasNext();) {
            EntryAttributes flag = (EntryAttributes) it.next();
            int flagValue = flag.value;
            if ((flagValue & value) != flagValue) {
                flags.remove(flag);
            }
        }
        return flags;
    }

}
