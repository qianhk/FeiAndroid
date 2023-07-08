//package com.njnu.kai.java.btc;
//
//import java.math.BigInteger;
//import java.nio.ByteBuffer;
//import java.util.Arrays;
//import java.util.stream.Stream;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.ArrayUtils;
//
//
///**
// * @author DHing
// * @ClassName: HashUtils
// */
//public class HashUtils {
//
//
//    /**
//     * 加密字符集合
//     */
//    private static final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
//
//    /**
//     * 使用 sha256 算法加密
//     *
//     * @param input
//     * @return
//     */
//    public static String sha256Hex(String input) {
//        return DigestUtils.sha256Hex(input);
//    }
//
//    /**
//     * 使用 sha256 hash 算法加密，返回一个 64 位的字符串 hash
//     *
//     * @param input
//     * @return
//     */
//    public static String sha256Hex(byte[] input) {
//        return DigestUtils.sha256Hex(input);
//    }
//
//    public static byte[] sha256(String input) {
//        return DigestUtils.sha256(input);
//    }
//
//    public static byte[] sha256(byte[] input) {
//        return DigestUtils.sha256(input);
//    }
//
//    /**
//     * 两个byte[]数组相加
//     *
//     * @param data1
//     * @param data2
//     * @return
//     */
//    public static byte[] add(byte[] data1, byte[] data2) {
//
//        byte[] result = new byte[data1.length + data2.length];
//        System.arraycopy(data1, 0, result, 0, data1.length);
//        System.arraycopy(data2, 0, result, data1.length, data2.length);
//
//        return result;
//    }
//
//    /**
//     * 将多个字节数组合并成一个字节数组
//     *
//     * @param bytes
//     * @return
//     */
//    public static byte[] merge(byte[]... bytes) {
//        Stream<Byte> stream = Stream.of();
//        for (byte[] b : bytes) {
//            stream = Stream.concat(stream, Arrays.stream(ArrayUtils.toObject(b)));
//        }
//        return ArrayUtils.toPrimitive(stream.toArray(Byte[]::new));
//    }
//
//    /**
//     * long 类型转 byte[]
//     *
//     * @param val
//     * @return
//     */
//    public static byte[] toBytes(long val) {
//        return ByteBuffer.allocate(Long.BYTES).putLong(val).array();
//    }
//
//
//    /**
//     * 使用 Base58 把地址解码成 25 字节
//     *
//     * @param input
//     * @return
//     */
//    public static byte[] decodeBase58To25Bytes(String input) {
//
//        BigInteger num = BigInteger.ZERO;
//        for (char t : input.toCharArray()) {
//            int p = ALPHABET.indexOf(t);
//            if (p == -1) {
//                return null;
//            }
//            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
//        }
//
//        byte[] result = new byte[25];
//        byte[] numBytes = num.toByteArray();
//        System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
//        return result;
//    }
//}
