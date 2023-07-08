package com.njnu.kai.java.btc;

import org.bitcoinj.core.*;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;

public class HitAddress implements Runnable {
    public long mJsCount;

    @Override
    public void run() {
        mJsCount = 0;
        MainNetParams net = MainNetParams.get();
        String masterKey = "xprv9s21ZrQH143K4EKMS3q1vbJo564QAbs98BfXQME6nk8UCrnXnv8vWg9qmtup3kTug96p5E3AvarBhPMScQDqMhEEm41rpYEdXBL8qzVZtwz";
        DeterministicKey keyFromStr = DeterministicKey.deserializeB58(masterKey, net);
        DeterministicKey derive0 = HDKeyDerivation.deriveChildKey(keyFromStr, new ChildNumber(0, false));
        String serializePubB58 = derive0.serializePubB58(net);
        while (true) {
            ++mJsCount;
            DeterministicKey keyFromPub = DeterministicKey.deserializeB58(serializePubB58, net);
            DeterministicKey derive0_99 = HDKeyDerivation.deriveChildKeyFromPublic(keyFromPub, new ChildNumber(99, false), HDKeyDerivation.PublicDeriveMode.NORMAL);
            LegacyAddress address099_1 = LegacyAddress.fromKey(net, derive0_99);
            LegacyAddress address099_2 = LegacyAddress.fromPubKeyHash(net, derive0_99.getPubKeyHash());
            String addressPubHash = BtcAddressTest.get3AddressPubHash(derive0_99.getPubKeyHash());
//            System.out.printf("keyFromPub derive0_99 %s %s\n", address099_1.toBase58(), address099_2.toBase58());
        }
//        DeterministicKey derive0 = keyFromStr.derive(0); // hardened
    }

//    @Override
//    public void run() {
//        mJsCount = 0;
//        MainNetParams network = MainNetParams.get();
//        while (true) {
//            ++mJsCount;
//
//            byte[] priKey1 = Utils.HEX.decode("0c28fca386c7a227600b2fe50b7cae11ec86d3bf1fbe471be89827e19d72aa1d");
//            ECKey ecKey = ECKey.fromPrivate(priKey1); //slow
//            byte[] pubKeyHash = ecKey.getPubKeyHash();
//
//            LegacyAddress address1 = LegacyAddress.fromPubKeyHash(network, pubKeyHash);
//            String base58 = address1.toBase58();
////            System.out.println("1开头的地址1：" + base58);
//
//            String address = BtcAddressTest.get3AddressPubHash(pubKeyHash);
////            System.out.println("3开头的地址1：" + address);
//
//            SegwitAddress segwitAddress = SegwitAddress.fromHash(network, pubKeyHash);
//            String bech32 = segwitAddress.toBech32();
////            System.out.println("bc1开头的地址：" + bech32);
//        }
//    }
}
