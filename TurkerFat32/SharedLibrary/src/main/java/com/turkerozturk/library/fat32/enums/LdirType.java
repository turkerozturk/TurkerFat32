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


package com.turkerozturk.library.fat32.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author u
 */
public enum LdirType {
    	LFN(0, "LFN");
	
	public final int value;
	private final String asReadableText;

	private LdirType(int value, String asReadableText) {
		this.value = value;
		this.asReadableText = asReadableText;
	}

	private static final Map<Integer, LdirType> _map = new HashMap<Integer, LdirType>();
	static {
		for (LdirType enumVariableName : LdirType.values())
			_map.put(enumVariableName.value, enumVariableName);
	}

	public static LdirType from(int value) {
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
