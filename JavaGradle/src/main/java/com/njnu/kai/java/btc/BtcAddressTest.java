package com.njnu.kai.java.btc;

import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;

import java.util.ArrayList;
import java.util.List;

public class BtcAddressTest {

    public static String get3Address(String publicKey) {
        byte[] pubkeyByte = Utils.HEX.decode(publicKey);
        return BtcAddressTest.get3Address(pubkeyByte);
    }

    public static String get3Address(byte[] pubkeyByte) {
        byte[] bytes = Utils.sha256hash160(pubkeyByte);
        byte[] newBuf = new byte[bytes.length + 2];
        newBuf[0] = 0x0;
        newBuf[1] = 0x14;
        System.arraycopy(bytes, 0, newBuf, 2, bytes.length);
        byte[] againSha = Utils.sha256hash160(newBuf);
        return Base58.encodeChecked(0x5, againSha);
    }

    public static String get3AddressPubHash(byte[] pubhashBytes) {
        byte[] newBuf = new byte[pubhashBytes.length + 2];
        newBuf[0] = 0x0;
        newBuf[1] = 0x14;
        System.arraycopy(pubhashBytes, 0, newBuf, 2, pubhashBytes.length);
        byte[] againSha = Utils.sha256hash160(newBuf);
        return Base58.encodeChecked(0x5, againSha);
    }

    public static void entry() {
        System.out.println("\n----------    BtcAddressTest entry    ----------");

        MainNetParams network = MainNetParams.get();

        byte[] priKey1 = Utils.HEX.decode("0c28fca386c7a227600b2fe50b7cae11ec86d3bf1fbe471be89827e19d72aa1d");
        ECKey ecKey = ECKey.fromPrivate(priKey1);

        LegacyAddress address1 = LegacyAddress.fromKey(network, ecKey);
        System.out.println("1开头的地址1：" + address1.toBase58());

        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(network, "KwdMAjGmerYanjeui5SHS7JkmpZvVipYvB2LJGU1ZxJwYvP98617");
        ecKey = dumpedPrivateKey.getKey();
        LegacyAddress address1_1 = LegacyAddress.fromKey(network, ecKey);
        System.out.println("1开头的地址2：" + address1_1.toBase58());

        dumpedPrivateKey = DumpedPrivateKey.fromBase58(network, "KwdMAjGmerYanjeui5SHS7JkmpZvVipYvB2LJGU1ZxJwYvP98617");
        ecKey = dumpedPrivateKey.getKey();
        System.out.println("3开头的地址 isPubKeyCompressed:" + dumpedPrivateKey.isPubKeyCompressed());
        LegacyAddress address3 = LegacyAddress.fromScriptHash(network, ecKey.getPubKeyHash());
        System.out.println("3开头的地址：" + address3.toBase58() + " pubKey=" + ecKey.getPubKey());
//        address3 = LegacyAddress.fromBase58(network, "3MYDyiQrrL3nUvxTfULYxoYYL24kNaFH53");
//        System.out.println("3开头的地址：" + address3.toBase58());
        String publicKey = "03f028892bad7ed57d2fb57bf330e2fff579dc341a";
        System.out.println("3开头1：" + BtcAddressTest.get3Address(publicKey));

        byte[] pubkeyByte = Utils.HEX.decode(publicKey);
        byte[] xxxx = Utils.sha256hash160(pubkeyByte);
        address3 = LegacyAddress.fromScriptHash(network, xxxx);
        System.out.println("3开头2：" + address3.toBase58());

        System.out.println("3开头3：" + BtcAddressTest.get3Address(ecKey.getPubKey()));

        dumpedPrivateKey = DumpedPrivateKey.fromBase58(network, "KwdMAjGmerYanjeui5SHS7JkmpZvVipYvB2LJGU1ZxJwYvP98617");
        ecKey = dumpedPrivateKey.getKey();
        SegwitAddress segwitAddress = SegwitAddress.fromKey(network, ecKey);
        System.out.println("bc1开头的地址：" + segwitAddress.toBech32());

//        dumpedPrivateKey = DumpedPrivateKey.fromBase58(network
//                , "KwdMAjGmerYanjeui5SHS7JkmpZvVipYvB2LJGU1ZxJwYvP98617");
//        ecKey = dumpedPrivateKey.getKey();
//        segwitAddress = SegwitAddress.fromKey(network, ecKey);
//        System.out.println("bc1开头的地址2：" + segwitAddress.toBech32());

        int mThreadAmount = 6;
        List<HitAddress> hitArray = new ArrayList<>();
        for (int i = 0; i < mThreadAmount; ++i) {
            HitAddress hitAddress = new HitAddress();
            hitArray.add(hitAddress);
            Thread t = new Thread(hitAddress);
            t.start();
        }

        long lastTotalCount = 0;
        while (true) {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            long tmpTotalCount = lastTotalCount;
            lastTotalCount = 0;
            for (int i = 0; i < mThreadAmount; ++i) {
                HitAddress hitAddress = hitArray.get(i);
                lastTotalCount += hitAddress.mJsCount;
            }
            long curTotalCount = lastTotalCount - tmpTotalCount;
            long end = System.currentTimeMillis();
            float diff = (end - start) / 1000.f;
            long speed = (long) (curTotalCount / diff);

            System.out.printf("thread amount is %d, speed/s is %d, total is %d.\n",
                    mThreadAmount, speed, lastTotalCount);
        }
    }
}
