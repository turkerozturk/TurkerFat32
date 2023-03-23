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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author u
 */
public enum DataClusterType {

DIRECTORY_ENTRIES_CLUSTER(0, "DIRECTORY_ENTRIES_CLUSTER"),
FILE_DATA_CLUSTER(0, "FILE_DATA_CLUSTER");

	
	public final int value;
	private final String asReadableText;

	private DataClusterType(int value, String asReadableText) {
		this.value = value;
		this.asReadableText = asReadableText;
	}

	private static final Map<Integer, DataClusterType> _map = new HashMap<Integer, DataClusterType>();
	static {
		for (DataClusterType enumVariableName : DataClusterType.values())
			_map.put(enumVariableName.value, enumVariableName);
	}

	public static DataClusterType from(int value) {
		if (_map.containsKey(value)) {
			return _map.get(value);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return asReadableText;
	}
}
