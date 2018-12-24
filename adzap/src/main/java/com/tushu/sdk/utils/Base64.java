package com.tushu.sdk.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author wangyi
 *
 * @time 2015年5月26日  下午2:33:34
 */
public class Base64 {
    private static final String ALGORITHM_DES = "DES/ECB/PKCS5Padding";
    private static final String DES_KEY = "screen*&-lock";


    /**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException
     *             异常
     */
    public static byte[] encodeByDES(String data) {
        return encodeByDES(data.getBytes());
    }

    public static String encodeByDES2String(String data) {
        // return encode(encodeByDES(data.getBytes()));
        return new String(encodeByDES(data.getBytes()));
    }

    /**
     * DES算法，加密
     * 
     * @param data
     *            待加密字符串
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException
     *             异常
     */
    public static byte[] encodeByDES(byte[] data) {
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * DES算法，解密
     * 
     * @param data
     *            待解密字符串
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    public static byte[] decodeByDES(byte[] data) {
        try {
            DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            SecretKey secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    public static String Encrypt(String originalStr) {
        String result = null;
        byte[] tmpOriginalStr = null;
        try {
            if (!originalStr.equalsIgnoreCase("")) {
                tmpOriginalStr = originalStr.getBytes("utf-8");
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                DESKeySpec dks = new DESKeySpec(DES_KEY.getBytes("utf-8"));
                SecretKey secretKey = keyFactory.generateSecret(dks);
//                IvParameterSpec param = new IvParameterSpec(DES_KEY_CHANNEL.getBytes("utf-8"));
                Cipher cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] tmpEncypt = cipher.doFinal(tmpOriginalStr);
                if (tmpEncypt != null) {
                    result = Base64.encode(tmpEncypt);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return result;
    }  

    public static String MD5(String src)  {
        try {
            return MD5(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String MD5(byte[] src) {
        // md5 ("abc") = 900150983cd24fb0d6963f7d28e17f72
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(src);// src.getBytes("UTF-8")
            byte[] dst = messageDigest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < dst.length; i++) {
                String hex = Integer.toHexString(0xFF & dst[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // private static final char[] legalChars =
    // "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    public static String encode(byte[] data) {
        return base64encode(data);
        /*
         * int start = 0; int len = data.length; StringBuffer buf = new
         * StringBuffer(data.length * 3 / 2); int end = len - 3; int i = start;
         * int n = 0; while (i <= end) { int d = ((((int) data[i]) & 0x0ff) <<
         * 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) &
         * 0x0ff); buf.append(legalChars[(d >> 18) & 63]);
         * buf.append(legalChars[(d >> 12) & 63]); buf.append(legalChars[(d >>
         * 6) & 63]); buf.append(legalChars[d & 63]); i += 3; if (n++ >= 14) { n
         * = 0; buf.append(" "); } } if (i == start + len - 2) { int d =
         * ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) <<
         * 8); buf.append(legalChars[(d >> 18) & 63]); buf.append(legalChars[(d
         * >> 12) & 63]); buf.append(legalChars[(d >> 6) & 63]);
         * buf.append("="); } else if (i == start + len - 1) { int d = (((int)
         * data[i]) & 0x0ff) << 16; buf.append(legalChars[(d >> 18) & 63]);
         * buf.append(legalChars[(d >> 12) & 63]); buf.append("=="); } return
         * buf.toString();
         */
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
            }
    }

    public static String decode(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            return "";
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
        }
        return new String(decodedBytes);
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;
        int len = s.length();
        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;
            if (i == len)
                break;
            int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12)
                    + (decode(s.charAt(i + 2)) << 6) + (decode(s.charAt(i + 3)));
            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);
            i += 4;
        }
    }

    private static String base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static int base64DecodeChars[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60,
            61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31,
            32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1,
            -1, -1, -1 };

    // 客户端Base64编码
    public static String base64encode(byte[] str) {
        int i, len;
        int c1, c2, c3;

        len = str.length;
        i = 0;
        StringBuffer out = new StringBuffer();
        while (i < len) {
            c1 = str[i++] & 0xff;
            if (i == len) {
                out.append(base64EncodeChars.charAt(c1 >> 2));
                out.append(base64EncodeChars.charAt((c1 & 0x3) << 4));
                out.append("==");
                break;
            }
            c2 = str[i++];
            if (i == len) {
                out.append(base64EncodeChars.charAt(c1 >> 2));
                out.append(base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4)));
                out.append(base64EncodeChars.charAt((c2 & 0xF) << 2));
                out.append("=");
                break;
            }
            c3 = str[i++];
            out.append(base64EncodeChars.charAt(c1 >> 2));
            out.append(base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4)));
            out.append(base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6)));
            out.append(base64EncodeChars.charAt(c3 & 0x3F));
        }
        return out.toString();
    }

    // 客户端Base64解码
    /*
     * public String base64decode(byte[] str) { int c1, c2, c3, c4; int i, len,
     * out;
     * 
     * len = str.length; i = 0; StringBuffer out =new StringBuffer(); while(i <
     * len) { // c1 do { c1 = base64DecodeChars[str[i++] & 0xff]; } while(i <
     * len && c1 == -1); if(c1 == -1) break;
     * 
     * // c2 do { c2 = base64DecodeChars[str[i++] & 0xff]; } while(i < len && c2
     * == -1); if(c2 == -1) break;
     * 
     * out += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));
     * 
     * // c3 do { c3 = str.charCodeAt(i++) & 0xff; if(c3 == 61) return out; c3 =
     * base64DecodeChars[c3]; } while(i < len && c3 == -1); if(c3 == -1) break;
     * 
     * out += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));
     * 
     * // c4 do { c4 = str[i++] & 0xff; if(c4 == 61) return out; c4 =
     * base64DecodeChars[c4]; } while(i < len && c4 == -1); if(c4 == -1) break;
     * out += String.fromCharCode(((c3 & 0x03) << 6) | c4); } return out; }
     */

    
    public static byte[] decode(byte[] data) {
        int len = data.length;
        ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
        int i = 0;
        int b1, b2, b3, b4;

        while (i < len) {

            /* b1 */
            do {
                b1 = base64DecodeChars[data[i++]];
            } while (i < len && b1 == -1);
            if (b1 == -1) {
                break;
            }

            /* b2 */
            do {
                b2 = base64DecodeChars[data[i++]];
            } while (i < len && b2 == -1);
            if (b2 == -1) {
                break;
            }
            buf.write((b1 << 2) | ((b2 & 0x30) >>> 4));

            /* b3 */
            do {
                b3 = data[i++];
                if (b3 == 61) {
                    return buf.toByteArray();
                }
                b3 = base64DecodeChars[b3];
            } while (i < len && b3 == -1);
            if (b3 == -1) {
                break;
            }
            buf.write(((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2));

            /* b4 */
            do {
                b4 = data[i++];
                if (b4 == 61) {
                    return buf.toByteArray();
                }
                b4 = base64DecodeChars[b4];
            } while (i < len && b4 == -1);
            if (b4 == -1) {
                break;
            }
            buf.write(((b3 & 0x03) << 6) | b4);
        }
        return buf.toByteArray();
    }

}
