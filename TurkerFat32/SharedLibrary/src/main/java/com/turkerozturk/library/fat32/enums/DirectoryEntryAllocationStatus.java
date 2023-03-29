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
 * DirectoryEntryAllocationStatus
 * bu kisa dosya adinin ilk karakteri ile alakali bir enum  olup, 
 * eger directory entry ise, ilk karaktere bakarak silinmis olup olmadigini(0xe5) anlamak mumkun olur.
 * eger uzun dosya adi parcasi ise, ilk karakterde uzun dosyanin kacinci parcasi oldugunu anlamak mumkun olur. normalde en ust entry
 * LAST_LONG_ENTRY dir, onun altndaki entryler en buyuk sayidan kucuge dogru satir satir LONG_NAME_ENTRY_NO_xx olabilirler,
 * onun da altindaki satir o long namein sahibi olan directory entrynin kendisi olur.
 * eger ilk karakter 00 ise bos bir entrydir ve ayni zamanda sonrasinda gelen entrylerin de bos olduklari anlamini tasir.
 * eger ilk karakter 05 ise japonca ile alakali normal bir directory entry oldugu anlasilir, bizim programda normal islem gorur.
 * eger bu enumda yok ise, o zaman buyuk bir ihtimalle kisa dosya adinin bir harfidir. burada olmadigi icin null doner.
 * @author u
 */
public enum DirectoryEntryAllocationStatus {
    ALLOCATED_ENTRY(-1, "Allocated File Or Folder"),

    // This is for empty entry, bundan sonra diger tum entrylerin de 0 oldugu anlamindadir, yani directory data clusterin kalan kismi TODO slackdir.
    EMPTY_ENTRY(0x00, "Empty Entry"),
    //These below are for Directory Entry Structure, 
    JAPAN_KANJI_INDICATOR(0x05, "Japan Kanji Indicator"), //yani bunu gorunce normal entry olarak islem yapmak kisaca hicbirsey yapmamak simdilik yeterlidir.
    DELETED_ENTRY(0xe5, "Deleted File Or Folder"), // dosyanin silinmis oldugunu gosterir yani bu entrynin gosterdigi data clusterid ve fat tablosunda takip edecegi diger cluster idleri TODO bize problem cikarabilir.
    // These below are for Long Name Entry
    LAST_LONG_ENTRY(0x40, "Last Long Name Entry"),
    LONG_NAME_ENTRY_NO_01(0x01, "Long Name Entry No 01"),
    LONG_NAME_ENTRY_NO_02(0x02, "Long Name Entry No 02"),
    LONG_NAME_ENTRY_NO_03(0x03, "Long Name Entry No 03"),
    LONG_NAME_ENTRY_NO_04(0x04, "Long Name Entry No 04"),
    LONG_NAME_ENTRY_NO_05(0x05, "Long Name Entry No 05"),
    LONG_NAME_ENTRY_NO_06(0x06, "Long Name Entry No 06"),
    LONG_NAME_ENTRY_NO_07(0x07, "Long Name Entry No 07"),
    LONG_NAME_ENTRY_NO_08(0x08, "Long Name Entry No 08"),
    LONG_NAME_ENTRY_NO_09(0x09, "Long Name Entry No 09"),
    LONG_NAME_ENTRY_NO_0A(0x0A, "Long Name Entry No 10"),
    LONG_NAME_ENTRY_NO_0B(0x0B, "Long Name Entry No 11"),
    LONG_NAME_ENTRY_NO_0C(0x0C, "Long Name Entry No 12"),
    LONG_NAME_ENTRY_NO_0D(0x0D, "Long Name Entry No 13"),
    LONG_NAME_ENTRY_NO_0E(0x0E, "Long Name Entry No 14"),
    LONG_NAME_ENTRY_NO_0F(0x0F, "Long Name Entry No 15"),
    LONG_NAME_ENTRY_NO_10(0x10, "Long Name Entry No 16"),
    LONG_NAME_ENTRY_NO_11(0x11, "Long Name Entry No 17"),
    LONG_NAME_ENTRY_NO_12(0x12, "Long Name Entry No 18"),
    LONG_NAME_ENTRY_NO_13(0x13, "Long Name Entry No 19"),
    LONG_NAME_ENTRY_NO_14(0x14, "Long Name Entry No 20"),
    LONG_NAME_ENTRY_NO_15(0x15, "Long Name Entry No 21"),
    LONG_NAME_ENTRY_NO_16(0x16, "Long Name Entry No 22"),
    LONG_NAME_ENTRY_NO_17(0x17, "Long Name Entry No 23"),
    LONG_NAME_ENTRY_NO_18(0x18, "Long Name Entry No 24"),
    LONG_NAME_ENTRY_NO_19(0x19, "Long Name Entry No 25"),
    LONG_NAME_ENTRY_NO_1A(0x1A, "Long Name Entry No 26"),
    LONG_NAME_ENTRY_NO_1B(0x1B, "Long Name Entry No 27"),
    LONG_NAME_ENTRY_NO_1C(0x1C, "Long Name Entry No 28"),
    LONG_NAME_ENTRY_NO_1D(0x1D, "Long Name Entry No 29"),
    LONG_NAME_ENTRY_NO_1E(0x1E, "Long Name Entry No 30"),
    LONG_NAME_ENTRY_NO_1F(0x1F, "Long Name Entry No 31"),
    LONG_NAME_ENTRY_NO_20(0x20, "Long Name Entry No 32"),
    LONG_NAME_ENTRY_NO_21(0x21, "Long Name Entry No 33"),
    LONG_NAME_ENTRY_NO_22(0x22, "Long Name Entry No 34"),
    LONG_NAME_ENTRY_NO_23(0x23, "Long Name Entry No 35"),
    LONG_NAME_ENTRY_NO_24(0x24, "Long Name Entry No 36"),
    LONG_NAME_ENTRY_NO_25(0x25, "Long Name Entry No 37"),
    LONG_NAME_ENTRY_NO_26(0x26, "Long Name Entry No 38"),
    LONG_NAME_ENTRY_NO_27(0x27, "Long Name Entry No 39"),
    LONG_NAME_ENTRY_NO_28(0x28, "Long Name Entry No 40"),
    LONG_NAME_ENTRY_NO_29(0x29, "Long Name Entry No 41"),
    LONG_NAME_ENTRY_NO_2A(0x2A, "Long Name Entry No 42"),
    LONG_NAME_ENTRY_NO_2B(0x2B, "Long Name Entry No 43"),
    LONG_NAME_ENTRY_NO_2C(0x2C, "Long Name Entry No 44"),
    LONG_NAME_ENTRY_NO_2D(0x2D, "Long Name Entry No 45"),
    LONG_NAME_ENTRY_NO_2E(0x2E, "Long Name Entry No 46"),
    LONG_NAME_ENTRY_NO_2F(0x2F, "Long Name Entry No 47"),
    LONG_NAME_ENTRY_NO_30(0x30, "Long Name Entry No 48"),
    LONG_NAME_ENTRY_NO_31(0x31, "Long Name Entry No 49"),
    LONG_NAME_ENTRY_NO_32(0x32, "Long Name Entry No 50"),
    LONG_NAME_ENTRY_NO_33(0x33, "Long Name Entry No 51"),
    LONG_NAME_ENTRY_NO_34(0x34, "Long Name Entry No 52"),
    LONG_NAME_ENTRY_NO_35(0x35, "Long Name Entry No 53"),
    LONG_NAME_ENTRY_NO_36(0x36, "Long Name Entry No 54"),
    LONG_NAME_ENTRY_NO_37(0x37, "Long Name Entry No 55"),
    LONG_NAME_ENTRY_NO_38(0x38, "Long Name Entry No 56"),
    LONG_NAME_ENTRY_NO_39(0x39, "Long Name Entry No 57"),
    LONG_NAME_ENTRY_NO_3A(0x3A, "Long Name Entry No 58"),
    LONG_NAME_ENTRY_NO_3B(0x3B, "Long Name Entry No 59"),
    LONG_NAME_ENTRY_NO_3C(0x3C, "Long Name Entry No 60"),
    LONG_NAME_ENTRY_NO_3D(0x3D, "Long Name Entry No 61"),
    LONG_NAME_ENTRY_NO_3E(0x3E, "Long Name Entry No 62"),
    LONG_NAME_ENTRY_NO_3F(0x3F, "Long Name Entry No 63");

    public final int value;
    private final String asReadableText;

    private DirectoryEntryAllocationStatus(int value, String asReadableText) {
        this.value = value;
        this.asReadableText = asReadableText;
    }

    private static final Map<Integer, DirectoryEntryAllocationStatus> _map = new HashMap<Integer, DirectoryEntryAllocationStatus>();

    static {
        for (DirectoryEntryAllocationStatus enumVariableName : DirectoryEntryAllocationStatus.values()) {
            _map.put(enumVariableName.value, enumVariableName);
        }
    }

    public static DirectoryEntryAllocationStatus from(int value) {
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
