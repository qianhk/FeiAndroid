package com.njnu.kai.java.btc;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.core.SegwitAddress;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.MainNetParams;

public class HitAddress implements Runnable {
    public long mJsCount;

    @Override
    public void run() {
        mJsCount = 0;
        MainNetParams network = MainNetParams.get();
        while (true) {
            ++mJsCount;

            byte[] priKey1 = Utils.HEX.decode("0c28fca386c7a227600b2fe50b7cae11ec86d3bf1fbe471be89827e19d72aa1d");
            ECKey ecKey = ECKey.fromPrivate(priKey1); //slow
            byte[] pubKeyHash = ecKey.getPubKeyHash();

            LegacyAddress address1 = LegacyAddress.fromPubKeyHash(network, pubKeyHash);
            String base58 = address1.toBase58();
//            System.out.println("1开头的地址1：" + base58);

            String address = BtcAddressTest.get3AddressPubHash(pubKeyHash);
//            System.out.println("3开头的地址1：" + address);

            SegwitAddress segwitAddress = SegwitAddress.fromHash(network, pubKeyHash);
            String bech32 = segwitAddress.toBech32();
//            System.out.println("bc1开头的地址：" + bech32);
        }
    }
}
