package com.njnu.kai.java.btc;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.bitcoinj.crypto.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class HDAddressTest {

    public static void entry() {
        System.out.println("\n----------    HDAddressTest entry    ----------");

        MainNetParams net = MainNetParams.get();
        DeterministicKey keyFromSeed = HDKeyDerivation.createMasterPrivateKey("abcdefghijk".getBytes());
        System.out.printf("keyFromSeed %s\n", keyFromSeed.toString());
        System.out.printf("derive0 %s\n", keyFromSeed.derive(0));
        System.out.printf("derive1 %s\n", keyFromSeed.derive(1));
        String masterKey = "xprv9s21ZrQH143K4EKMS3q1vbJo564QAbs98BfXQME6nk8UCrnXnv8vWg9qmtup3kTug96p5E3AvarBhPMScQDqMhEEm41rpYEdXBL8qzVZtwz";
        DeterministicKey keyFromStr = DeterministicKey.deserializeB58(masterKey, net);
        System.out.printf("keyFromStr %s\npublicKeyAsHex=%s\n", keyFromStr, keyFromStr.getPublicKeyAsHex());
//        DeterministicKey derive0 = keyFromStr.derive(0); // hardened
        DeterministicKey derive0 = HDKeyDerivation.deriveChildKey(keyFromStr, new ChildNumber(0, false));
        DumpedPrivateKey dumpedKey = derive0.getPrivateKeyEncoded(net);
        LegacyAddress address1_1 = LegacyAddress.fromKey(net, dumpedKey.getKey());
        LegacyAddress address1_2 = LegacyAddress.fromKey(net, derive0);
        LegacyAddress address1_3 = LegacyAddress.fromPubKeyHash(net, derive0.getPubKeyHash());
        System.out.printf("\nderive0 %s\nwif=%s\naddress=%s %s %s\nP2PKH pubkey=%s\nP2PKH privKey=%s\nP2WPKH pubkey=%s\nP2WPKH privKey=%s\n"
                , derive0, derive0.getPrivateKeyAsWiF(net), address1_1.toBase58(), address1_2.toBase58(), address1_3.toBase58()
                , derive0.serializePubB58(net), derive0.serializePrivB58(net)
                , derive0.serializePubB58(net, Script.ScriptType.P2WPKH), derive0.serializePrivB58(net, Script.ScriptType.P2WPKH));
//        DeterministicKey derive1 = keyFromStr.derive(1);
        DeterministicKey derive1 = HDKeyDerivation.deriveChildKey(keyFromStr, new ChildNumber(1, false));
        System.out.printf("\nderive1 %s\npubkey=%s\nprivKey=%s\nP2WPKH pubkey=%s\nP2WPKH privKey=%s\n"
                , derive1, derive1.serializePubB58(net), derive1.serializePrivB58(net)
                , derive1.serializePubB58(net, Script.ScriptType.P2WPKH), derive1.serializePrivB58(net, Script.ScriptType.P2WPKH));

        DeterministicKey keyFromPub = DeterministicKey.deserializeB58(derive0.serializePubB58(net), net);
        DeterministicKey derive0_99 = HDKeyDerivation.deriveChildKeyFromPublic(keyFromPub, new ChildNumber(99, false), HDKeyDerivation.PublicDeriveMode.NORMAL);
        LegacyAddress address099_1 = LegacyAddress.fromKey(net, derive0_99);
        LegacyAddress address099_2 = LegacyAddress.fromPubKeyHash(net, derive0_99.getPubKeyHash());
        System.out.printf("\nkeyFromPub derive0_99 %s %s\n", address099_1.toBase58(), address099_2.toBase58());

    }

}

//public class HDAddressTest {
//    /**
//     * @param @param  word 助记词
//     * @param @param  passphrase 密码
//     * @param @param  childNum 生成的hd钱包数量
//     * @param @param  params
//     * @param @return 参数
//     * @return List<HDWallet>    返回类型
//     * @throws IOException
//     * @throws FileNotFoundException
//     * @throws
//     * @Title: createHDWalletByPATH
//     */
//    public static List<HDWallet> createHDWalletByPATH(String word, String passphrase, int[] childNum) throws FileNotFoundException, IOException, UnreadableWalletException {
//        List<HDWallet> wallet = new ArrayList<HDWallet>();
//        try {
//            DeterministicSeed deterministicSeed = new DeterministicSeed(word, null, passphrase, 0L);
//            DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
//            DeterministicKey main = deterministicKeyChain.getKeyByPath(HDUtils.parsePath("44H/0H"), true);
//            DeterministicHierarchy tree = new DeterministicHierarchy(main);
//            DeterministicKey rootKey = tree.getRootKey();
////            LOG.info("### [BTC] childs privKey , pubKey , address start ###");
//            for (int i = childNum[0], len = childNum[1]; i < len; i++) {
//                DeterministicKey deriveChildKey = HDKeyDerivation.deriveChildKey(rootKey, new ChildNumber(i));
//                wallet.add(new HDWallet(deriveChildKey.getPathAsString(), deriveChildKey.getPrivateKeyAsWiF(params), Base58.encode(deriveChildKey.getPubKey()), ECKey.fromPrivate(deriveChildKey.getPrivKey()).toAddress(params).toBase58()));
//            }
//
////            LOG.info("### [BTC] childs privKey , pubKey , address end ###");
//        } catch (UnreadableWalletException e) {
//            e.printStackTrace();
//        }
//        return wallet;
//    }
//
//
//    /**
//     * @param @param  passphrase
//     * @param @param  params
//     * @param @return
//     * @param @throws IOException    参数
//     * @return String    返回类型
//     * @throws
//     * @Title: generateMnemonic
//     */
//    public static String generateMnemonic(String passphrase) throws IOException {
//        StringBuilder words = new StringBuilder();
//        SecureRandom secureRandom = new SecureRandom();
//        DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase);
//
//        for (String str : ds.getMnemonicCode()) {
//            words.append(str).append(" ");
//        }
//        return words.toString().trim();
//    }
//
//
//    /**
//     * @param @param  publicKey
//     * @param @return 参数
//     * @return String    返回类型
//     * @throws
//     * @Title: generateAddress   根据公钥生成地址
//     */
//    public static String generateAddress(String publicKey) {
//        //1. 计算公钥的 SHA-256 哈希值
//        byte[] sha256Bytes = HashUtils.sha256(Base58.decode(publicKey));
//        //2. 取上一步结果，计算 RIPEMD-160 哈希值
//        RIPEMD160Digest digest = new RIPEMD160Digest();
//        digest.update(sha256Bytes, 0, sha256Bytes.length);
//        byte[] ripemd160Bytes = new byte[digest.getDigestSize()];
//        digest.doFinal(ripemd160Bytes, 0);
//        //3. 取上一步结果，前面加入地址版本号（主网版本号“0x00”）
//        byte[] networkID = new BigInteger("00", 16).toByteArray();
//        byte[] extendedRipemd160Bytes = HashUtils.add(networkID, ripemd160Bytes);
//        //4. 取上一步结果，计算 SHA-256 哈希值
//        byte[] oneceSha256Bytes = HashUtils.sha256(extendedRipemd160Bytes);
//        //5. 取上一步结果，再计算一下 SHA-256 哈希值
//        byte[] twiceSha256Bytes = HashUtils.sha256(oneceSha256Bytes);
//        //6. 取上一步结果的前4个字节（8位十六进制）
//        byte[] checksum = new byte[4];
//        System.arraycopy(twiceSha256Bytes, 0, checksum, 0, 4);
//        //7. 把这4个字节加在第5步的结果后面，作为校验
//        byte[] binaryAddressBytes = HashUtils.add(extendedRipemd160Bytes, checksum);
//        //8. 把结果用 Base58 编码算法进行一次编码
//        return Base58.encode(binaryAddressBytes);
//    }
//
//    /**
//     * 验证地址是否合法
//     *
//     * @param address
//     * @return
//     */
//    public static boolean verifyAddress(String address) {
//        if (address.length() < 26 || address.length() > 35) {
//            return false;
//        }
//        byte[] decoded = HashUtils.decodeBase58To25Bytes(address);
//        if (null == decoded) {
//            return false;
//        }
//        // 验证校验码
//        byte[] hash1 = HashUtils.sha256(Arrays.copyOfRange(decoded, 0, 21));
//        byte[] hash2 = HashUtils.sha256(hash1);
//
//        return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
//    }
//
//    public static void entry() {
//        String s = null;//生成助记次
//        try {
//            s = generateMnemonic("xx");
//            int[] a = {1, 10};//根据助记词生成childID={1-10}的钱包地址
//            List<HDWallet> walls = createHDWalletByPATH(s, "123457", a);
//            for (HDWallet hdWallet : walls) {
//                System.out.println(hdWallet.getPubKey());
//                System.out.println(hdWallet.getPrivKey());
//                System.out.println(hdWallet.getAddress());
//                System.out.println("----------------------");
//            }
//
//        } catch (Exception e) {
//            System.out.printf("found exception: " + e);
//        }
//    }
//}
