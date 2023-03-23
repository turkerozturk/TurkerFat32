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
public enum Ikon {
    UNDEFINED(0,"Undefined"),
    FOLDER(1,"Folder"),
    FILE(2,"File"),
    DELETED_FOLDER(3,"Deleted Folder"),
    DELETED_FILE(4,"Deleted File"),
    VOLUME(5,"Volume"),
    SELECTED(6,"Selected");
    
    public final int value;
	private final String asReadableText;

	private Ikon(int value, String asReadableText) {
		this.value = value;
		this.asReadableText = asReadableText;
	}

	private static final Map<Integer, Ikon> _map = new HashMap<Integer, Ikon>();
	static {
		for (Ikon enumVariableName : Ikon.values())
			_map.put(enumVariableName.value, enumVariableName);
	}

	public static Ikon from(int value) {
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
