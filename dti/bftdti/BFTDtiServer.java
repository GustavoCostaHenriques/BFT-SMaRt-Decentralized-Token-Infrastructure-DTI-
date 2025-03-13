/**
 * BFT Dti implementation (server side).
 *
 */
package dti.bftdti;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BFTDtiServer<K, V> extends DefaultSingleRecoverable {

    private TreeMap<Integer,Coin> coinReplicaMap;
    
    private TreeMap<Integer,NFT> NFTReplicaMap;

    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    // The constructor passes the id of the server to the super class
    public BFTDtiServer(int id) {
        // turn-on BFT-SMaRt'replica
        new ServiceReplica(id, this, this);

        System.out.println("BFT-DTI Server started");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Use: java BFTDtiServer <server id>");
            System.exit(-1);
        }
        new BFTDtiServer<Integer, String>(Integer.parseInt(args[0]));
    }

    @Override
    public byte[] appExecuteOrdered(byte[] command, MessageContext msgCtx) {
        //TODO: Implement the logic of the DTI server
        return new byte[0];
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        //TODO: Implement the logic of the DTI server
        return new byte[0];
    }

    @Override
    public byte[] getSnapshot() {
        //TODO: Implement the logic of the DTI server
        return new byte[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void installSnapshot(byte[] state) {
        //TODO: Implement the logic of the DTI server
    }
}