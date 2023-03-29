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


package com.turkerozturk.library.fat32.helpers;

/*
 * Turker ozturk
 */

public class HexViewer {



	// http://www.gubatron.com/blog/2009/04/18/how-to-make-your-own-quick-dirty-hex-file-viewer-in-java/
	public static String show(byte[] bytes) {

		StringBuilder view = new StringBuilder();

		String part1 = "";
		String part2 = "";

		for (int j = 0; j < bytes.length; j++) {
			int tmpByte = Byte.toUnsignedInt(bytes[j]);
			String paddingZero = (tmpByte < 16) ? "0" : "";
			part1 += paddingZero + Integer.toHexString(tmpByte) + " ";
			part2 += ((tmpByte >= 33 && tmpByte <= 126) ? (char) tmpByte : '.')
					+ "  ";

		}

		view.append(part1 + "\r\n" + part2);

		return view.toString();

	}
	
	public static String show(byte[] bytes, boolean withoutAsciiStrings) {

		StringBuilder view = new StringBuilder();

		String part1 = "";
		String part2 = "";
		
		
		for (int j = 0; j < bytes.length; j++) {
			int tmpByte = Byte.toUnsignedInt(bytes[j]);
			String paddingZero = (tmpByte < 16) ? "0" : "";
			part1 += paddingZero + Integer.toHexString(tmpByte) + " ";
			
			if (withoutAsciiStrings) {
			}
			else {
				part2 += ((tmpByte >= 33 && tmpByte <= 126) ? (char) tmpByte : '.')
					+ "  ";
			}
		}

		view.append(part1 + "\r\n" + part2);

		return view.toString();

	}

}
