import util.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

/**
 * @author hongkai.qian
 * @version 1.0.0
 * @since 15-10-27
 */
public class TestInflate {


    private static byte[] unZipByte2(byte[] data) {
        final Inflater inflater = new Inflater();
        inflater.setInput(data);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        inflater.finished();

        byte[] result = new byte[1024];
        int inflateLength;
        try {
            do {
                inflateLength = inflater.inflate(result);
                byteArrayOutputStream.write(result, 0, inflateLength);
            } while (inflateLength > 0);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {

        } finally {
            inflater.end();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static byte[] unZipByte(byte[] data) {
        final Inflater inflater = new Inflater();
        inflater.setInput(data);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);

        byte[] result = new byte[1024];
        for (int idx = result.length - 1; idx >= 0; --idx) {
            result[idx] = 0;
        }
        int inflateLength;
        try {
            while (!inflater.finished()) {
                inflateLength = inflater.inflate(result);
                byteArrayOutputStream.write(result, 0, inflateLength);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.err.println("unzip Error: " + e.toString());
        } finally {
            inflater.end();
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void byte2HexStr(StringBuilder builder, byte b) {
        String stmp = Integer.toHexString(b & 0xFF);
        builder.append((stmp.length() == 1) ? "0" + stmp : stmp);
        builder.append(" ");
    }


    public static String constructByteStr(byte[] bytes) {
        if (bytes == null) {
            return "null";
        } else {
            final StringBuilder builder = new StringBuilder();
            final int length = bytes.length;

            if (length < 60) {
                for (int idx = 0; idx < length; ++idx) {
                    byte2HexStr(builder, bytes[idx]);
                }
            } else {
                for (int idx = 0; idx < 20; ++idx) {
                    byte2HexStr(builder, bytes[idx]);
                }
                builder.append(" ........  ");
                for (int idx = length - 20; idx < length; ++idx) {
                    byte2HexStr(builder, bytes[idx]);
                }
            }
            return builder.toString();
        }
    }


    public static void main(String[] args) {

//        final File file = new File("/Users/ttkai/");
//        final String[] list = file.list();
//        for (String s : list) {
//            System.out.println(s);
//        }

        final byte[] oriBytes = FileUtils.loadByte("/Users/ttkai/Documents/QQMusicCrack/3g_lyric_response_skip_5");


        final byte[] bytes = unZipByte(oriBytes);
        final String str1 = new String(bytes);
        System.out.println("str1=" + str1);

        System.out.println();

        final byte[] bytes2 = unZipByte2(oriBytes);
        final String str2 = new String(bytes2);
//        System.out.println("str2=" + str2);

        final Object oo = oriBytes;
        byte[] bbb = (byte[]) oo;
        System.out.println(bbb);
        System.out.println(constructByteStr(oriBytes));

//        Byte[] BBB = (Byte[]) oo;
//        System.out.println(BBB);

    }
}
