package org.uralsj;

import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;

/**
 * Created by Eric on 2/8/2015.
 */
public class DarkSendBroadcastTransaction {
    Transaction tx;
    TransactionInput vin;
    byte [] vchSig;
    long sigTime;
}
