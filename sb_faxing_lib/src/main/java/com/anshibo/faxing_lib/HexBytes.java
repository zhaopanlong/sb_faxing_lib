package com.anshibo.faxing_lib;

public class HexBytes {

    public static byte[] hex2Bytes(String src) {
        byte[] res = new byte[src.length() / 2];
        char[] chs = src.toCharArray();
        for (int i = 0, c = 0; i < chs.length; i += 2, c++) {
            res[c] = (byte) (Integer.parseInt(new String(chs, i, 2), 16));
        }
        return res;
    }

    public static String bytes2Hex(byte[] src, int len) {
        char[] res = new char[len * 2];
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < len; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

    public static String intToHex(int i) {
        String s = Integer.toHexString(i);
        char[] chars = s.toCharArray();
        String result ;
        switch (s.length()){
            case 1:
                return "000"+s;
            case 2:
                return "00"+s;

            case 3:
                return "0"+s;

            case 4:
                return s;

        }
        return s;
    }

    public static String intToHex2(int i) {
        String s = Integer.toHexString(i);

        return "0" + s;
    }

    public static String desToHex(int num, int length) {
        String str = Integer.toHexString(num).toUpperCase();

        for (int i = 0; i < length && str.length() < length; ++i) {
            str = "0" + str;
        }

        return str;
    }
}

