package org.bitcoinj.core;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hash Engineering Solutions
 * Date: 5/3/14
 * To change this template use File | Settings | File Templates.
 */
public class CoinDefinition {

    public static final String coinName = "UralsCoin";
    public static final String coinTicker = "URALS";
    public static final String coinURIScheme = "urals";
    public static final String cryptsyMarketId = "";
    public static final String cryptsyMarketCurrency = "BTC";
    public static final String PATTERN_PRIVATE_KEY_START_UNCOMPRESSED = "[7]";
    public static final String PATTERN_PRIVATE_KEY_START_COMPRESSED = "[u]";

    public enum CoinPrecision {
        Coins,
        Millicoins,
    }
    public static final CoinPrecision coinPrecision = CoinPrecision.Coins;

    public static final String UNSPENT_API_URL = "https://chainz.cryptoid.info/urals/api.dws?q=unspent";
    public enum UnspentAPIType {
        BitEasy,
        Blockr,
        Abe,
        Cryptoid,
    };
    public static final UnspentAPIType UnspentAPI = UnspentAPIType.Cryptoid;

    public static final String BLOCKEXPLORER_BASE_URL_PROD = "http://exp.uralscoin.info:3001/";
    public static final String BLOCKEXPLORER_ADDRESS_PATH = "address/";
    public static final String BLOCKEXPLORER_TRANSACTION_PATH = "tx/";
    public static final String BLOCKEXPLORER_BLOCK_PATH = "block/";
    public static final String BLOCKEXPLORER_BASE_URL_TEST = "";

    public static final String DONATION_ADDRESS = "uaJyysvKv8sfvKxKfLiArdwnE4M3W8SufX";


    enum CoinHash {
        SHA256,
        scrypt,
        x11
    };
    public static final CoinHash coinPOWHash = CoinHash.x11;

    public static boolean checkpointFileSupport = false;

    public static final int TARGET_TIMESPAN = (int)(6 * 24 * 60 * 60);
    public static final int TARGET_SPACING = (int)(5 * 60);
    public static final int INTERVAL = TARGET_TIMESPAN / TARGET_SPACING;

    public static final int getIntervalCheckpoints() {
            return INTERVAL;

    }
    public static final int getTargetTimespan(int height, boolean testNet) {
            return TARGET_TIMESPAN;
    }

    public static int spendableCoinbaseDepth = 25;
    public static final long MAX_COINS = 525000000;

    public static final long DEFAULT_MIN_TX_FEE = 5000;
    public static final long DUST_LIMIT = 500;

    public static final long INSTANTX_FEE = 1000000;

    public static final int PROTOCOL_VERSION = 70082;
    public static final int MIN_PROTOCOL_VERSION = 70082;

    public static final int BLOCK_CURRENTVERSION = 3;
    public static final int MAX_BLOCK_SIZE = 10000000;

    public static final boolean supportsBloomFiltering = true;

    public static final int Port = 7444;
    public static final int TestPort = 17444;

    //
    //  Production
    //
    public static final int AddressHeader = 130;
    public static final int p2shHeader = 5;
    public static final boolean allowBitcoinPrivateKey = false;
    public static final int dumpedPrivateKeyHeader = 128;
    public static final long PacketMagic = 0xa3d5c2f9;

    // Forks
    static public long FORK_X17 = 1477958400; // Urals Hardfork, 11/01/2016 @ 12:00am (UTC)

    static public long genesisBlockDifficultyTarget = 0x1e0ffff0L;
    static public long genesisBlockTime = 1504224000L;
    static public long genesisBlockNonce = 25912842;
    static public String genesisHash = "00000bf56638a0f85009007e92cd848160d5211da779fd4e23f4bd22f0f5221a";
    static public String genesisMerkleRoot = "5bab319403ecce5eccd4715162fd72d35313064211ff16fccde441a12b2b93b8";
    static public int genesisBlockValue = 100;

      static public String genesisTxInBytes = "04ffff001d01044c4c32382f30362f323031373a20527573736961e2809973205061726c69616d656e742069732044697363757373696e6720746865204c6567616c697a6174696f6e206f6620426974636f696e2e";
    static public String genesisTxOutBytes = "04678afdb0fe5548271967f1a67130b7105cd6a828e03909a67962e0ea1f61deb649f6bc3f4cef38c4f35504e51ec112de5c384df7ba0b8d578a4c702b6bf11d5f";

    static public String[] dnsSeeds = new String[] {
        "162.213.250.203",
	"5.149.254.22",
	"5.149.254.11",
	"77.220.215.168",
	    
    };

    public static int minBroadcastConnections = 0;

    public static final Coin GetBlockReward(int height)
    {
        int nSubsidy;

        if (height == 1)
            nSubsidy = 2100000;
        else if (height < 239000) // main.cpp, (FORKX17_Main_Net-1000)
            nSubsidy = 50;
        else if (height < 7869744) // main.cpp, (FORKX17_Main_Net*33)-50256)
            nSubsidy = 25;
        else
            nSubsidy = 1; // should be 0.1 !!!   

        return Coin.valueOf(nSubsidy, 0);
    }

    public static int subsidyDecreaseBlockCount = 210000;

    public static BigInteger proofOfWorkLimit = Utils.decodeCompactBits(0x1e0fffffL);

    public static final String SATOSHI_KEY = "048240a8748a80a286b270ba126705ced4f2ce5a7847b3610ea3c06513150dade2a8512ed5ea86320824683fc0818f0ac019214973e677acd1244f6d0571fc5103";

    /** The string returned by getId() for the main, production network where people trade things. */
    public static final String ID_MAINNET = "org.urals.production";
    /** The string returned by getId() for the testnet. */
    public static final String ID_TESTNET = "org.urals.test";
    /** Unit test network. */
    public static final String ID_UNITTESTNET = "com.google.urals.unittest";

    public static void initCheckpoints(Map<Integer, Sha256Hash> checkpoints)
    {
        checkpoints.put(      0, Sha256Hash.wrap("00000bf56638a0f85009007e92cd848160d5211da779fd4e23f4bd22f0f5221a"));
        checkpoints.put(    100, Sha256Hash.wrap("00000d1135604f0f6391f3b16d05c348c8901cbace293ecfd84a5f535a4f8e18"));
        checkpoints.put(   3000, Sha256Hash.wrap("000004d7d9610d957b43a160fb90c4f56961e91509bf17f18c42ba2060273c36"));
        checkpoints.put(   5000, Sha256Hash.wrap("00000362aba9ec91f776f7d0d7e8e871ddb1af95b03dd62ce860fb15e0c14ba5"));
        checkpoints.put(  10000, Sha256Hash.wrap("000000577880073a284ac6ebf21b7e380c4ae6ab6f9d040c8472d6f1fda107da"));
/*        checkpoints.put(  29999, Sha256Hash.wrap("00000000ede644fcbdf8f8ce8c53bb15de5dfd5c32384c751fa4ef409992aa07"));
        checkpoints.put(  36222, Sha256Hash.wrap("0000000047c4338861a6b191570f07a23bd30c75c03a81ac5e5978053d946408"));
        checkpoints.put(  40599, Sha256Hash.wrap("00000000c2596d6bd49b08ab9d233cd7de97a01a7cde19d0e1a136a1f3904f3c"));
        checkpoints.put(  45512, Sha256Hash.wrap("00000000e2448d27f6360461739bcd25bf41d3767fc7c0e8c5e53a2db90eaf06"));
        checkpoints.put(  49478, Sha256Hash.wrap("00000000dbb3d6386ed45e335316a4f018e451bf60b12fdebbef680969a90acb"));
        checkpoints.put(  74910, Sha256Hash.wrap("000000002409374bcab8006f171b8c3eb4485220d94ae555b041ee24eb4d8434"));
        checkpoints.put(  84579, Sha256Hash.wrap("00000000372eebd8b26d135798ac04549dc32fdfb584710ed9edf2dcb1be6941"));
        checkpoints.put( 140602, Sha256Hash.wrap("0000000000b86fa0891a7241c71a0969439896b61abaf07e856eb0f49115b741"));
        checkpoints.put( 186158, Sha256Hash.wrap("0000000002d477cc1fea0438f7d477c0c993ae3762bc60efdbb8873275b385c7"));
        checkpoints.put( 212573, Sha256Hash.wrap("0000000da60270f8183780773064d689762b6ad6749296e9fc274c3dcecfc6f6"));
        checkpoints.put( 279122, Sha256Hash.wrap("00000002e14806c9d2aa7afeeb43df69b90ab8c6083b33b34fd9d62c57dac559"));
*/
    }

    //
    // TestNet - NOT TESTED
    //
    public static final boolean supportsTestNet = false;
    public static final int testnetAddressHeader = 111;
    public static final int testnetp2shHeader = 196;
    public static final long testnetPacketMagic = 0x4c1a2caf;
    public static final String testnetGenesisHash = "00000405073975ab683f4740808f51a2687a935788473b2621e8357c244649e0";
    static public long testnetGenesisBlockDifficultyTarget = (0x1e0ffff0L);
    static public long testnetGenesisBlockTime = 1505088000L;
    static public long testnetGenesisBlockNonce = (293736);

    static public String[] testnetDnsSeeds = new String[] {
            "162.213.250.203",
    };

    public static final String TESTNET_SATOSHI_KEY = "04517d8a699cb43d3938d7b24faaff7cda448ca4ea267723ba614784de661949bf632d6304316b244646dea079735b9a6fc4af804efb4752075b9fe2245e14e412";

    public static final String UNITTEST_ADDRESS = "";
    public static final String UNITTEST_ADDRESS_PRIVATE_KEY = "";

}
