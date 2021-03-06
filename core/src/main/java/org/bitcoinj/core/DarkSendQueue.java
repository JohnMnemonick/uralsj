/**
 * Copyright 2014 Hash Engineering Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bitcoinj.core;

import org.uralsj.DarkSend;
import org.uralsj.DarkSendSigner;
import org.uralsj.MasterNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import static org.bitcoinj.core.Utils.int64ToByteStreamLE;
import static org.bitcoinj.core.Utils.uint32ToByteStreamLE;

public class DarkSendQueue extends Message implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(DarkSendQueue.class);

    TransactionInput vin;
    long time;
    int denom;
    boolean ready;
    byte[] vchSig;

    private transient int optimalEncodingMessageSize;

    DarkCoinSystem system;

    DarkSendQueue() {
        denom = 0;
        vin = null;
        time = 0;
        vchSig = null;
        ready = false;

        this.system = null;
    }

    DarkSendQueue(NetworkParameters params, byte[] bytes)
    {
        super(params, bytes, 0);
    }

    DarkSendQueue(NetworkParameters params, byte[] bytes, int cursor) {
        super(params, bytes, cursor);
    }

    @Override
    protected void parseLite() throws ProtocolException {
        if (parseLazy && length == UNKNOWN_LENGTH) {
            //If length hasn't been provided this tx is probably contained within a block.
            //In parseRetain mode the block needs to know how long the transaction is
            //unfortunately this requires a fairly deep (though not total) parse.
            //This is due to the fact that transactions in the block's list do not include a
            //size header and inputs/outputs are also variable length due the contained
            //script so each must be instantiated so the scriptlength varint can be read
            //to calculate total length of the transaction.
            //We will still persist will this semi-light parsing because getting the lengths
            //of the various components gains us the ability to cache the backing bytearrays
            //so that only those subcomponents that have changed will need to be reserialized.

            //parse();
            //parsed = true;
            length = calcLength(payload, offset);
            cursor = offset + length;
        }
    }

    protected static int calcLength(byte[] buf, int offset) {
        /*VarInt varint;
        // jump past version (uint32)
        int cursor = offset;
        cursor += 4; //denom
        //vin
        cursor += 36;
        varint = new VarInt(buf, cursor);
        long scriptLen = varint.value;
        // 4 = length of sequence field (unint32)
        cursor += scriptLen + 4 + varint.getOriginalSizeInBytes();
        //time
        cursor += 8;
        //ready
        cursor += 1;
        //vchSig
        varint = new VarInt(buf, cursor);
        long size = varint.value;
        cursor += varint.getOriginalSizeInBytes();
        cursor += size;

        return cursor - offset;
        */
        return 0;
    }

    @Override
    void parse() throws ProtocolException {
        if (parsed)
            return;

        /*cursor = offset;

        denom = (int) readUint32();
        optimalEncodingMessageSize = 4;

        vin = new TransactionInput(params, null, payload, cursor);
        optimalEncodingMessageSize += vin.getMessageSize();

        time = readInt64();
        optimalEncodingMessageSize += 4;

        byte[] readyByte = readBytes(1);
        ready = readyByte[0] != 0 ? true : false;
        optimalEncodingMessageSize += 1;

        vchSig = readByteArray();
        optimalEncodingMessageSize += vchSig.length;

        length = cursor - offset;
        */

    }

    @Override
    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {


        uint32ToByteStreamLE(denom, stream);

        vin.bitcoinSerialize(stream);

        int64ToByteStreamLE(time, stream);

        stream.write(new VarInt(ready ? 1 : 0).encode());

        stream.write(new VarInt(vchSig.length).encode());
        stream.write(vchSig);
    }

    long getOptimalEncodingMessageSize() {
        if (optimalEncodingMessageSize != 0)
            return optimalEncodingMessageSize;
        maybeParse();
        if (optimalEncodingMessageSize != 0)
            return optimalEncodingMessageSize;
        optimalEncodingMessageSize = getMessageSize();
        return optimalEncodingMessageSize;
    }

    boolean getAddress(PeerAddress address) {
        for (MasterNode mn : system.masternode.vecMasternodes) {
            if (mn.vin == vin) {
                address = mn.address;
                return true;
            }
        }
        return false;
    }

    int getProtocolVersion() {
        for (MasterNode mn : system.masternode.vecMasternodes) {
            if (mn.vin == vin) {
                return mn.protocolVersion;
            }
        }
        return 0;
    }

    boolean Sign() {
        if (!DarkCoinSystem.fMasterNode) return false;

        String strMessage = vin.toString() + denom + time + ready;

        ECKey eckey2;
        StringBuilder errorMessage = new StringBuilder();

        if ((eckey2 = DarkSendSigner.setKey(params, system.strMasterNodePrivKey, errorMessage)) == null) {
            log.warn("CDarksendQueue():Relay - ERROR: Invalid masternodeprivkey: '%s'\n", errorMessage);
            return false;
        }

        if ((vchSig = DarkSendSigner.signMessage(strMessage, errorMessage, eckey2)) == null) {
            log.warn("CDarksendQueue():Relay - Sign message failed");
            return false;
        }

        if (!DarkSendSigner.verifyMessage(eckey2, vchSig, strMessage, errorMessage)) {
            log.warn("CDarksendQueue():Relay - Verify message failed");
            return false;
        }

        return true;
    }

    boolean Relay() {

       // LOCK(cs_vNodes);
        for(Peer peer : system.peerGroup.getConnectedPeers())
        {
            //TODO:
            // always relay to everyone
            peer.sendMessage(this);
            //pnode -> PushMessage("dsq", ( * this));
        }

        return true;
    }

    boolean IsExpired()
    {
        return (Utils.currentTimeSeconds() - time) > DarkSend.DARKSEND_QUEUE_TIMEOUT;// 120 seconds
    }

    boolean CheckSignature()
    {
        for(MasterNode mn : system.masternode.vecMasternodes) {

            if(mn.vin == vin) {
                String strMessage = vin.toString() + denom + time + ready;

                StringBuilder errorMessage = new StringBuilder();
                if(!DarkSendSigner.verifyMessage(ECKey.fromPublicOnly(mn.pubkey2.getBytes()), vchSig, strMessage, errorMessage)){
                    log.error("CDarksendQueue::CheckSignature() - Got bad masternode address signature %s \n", vin.toString());
                    return false;
                }

                return true;
            }
        }

        return false;
    }

}
