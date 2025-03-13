/**
 * BFT Dti implementation (client side).
 *
 */
package dti.bftdti;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.ServiceProxy;

public class BFTDti<K, V> {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;

    public BFTDti(int id) {
        serviceProxy = new ServiceProxy(id);
    }

    public myCoins(int id) {
        byte[] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.MY_COINS);
            request.setKey(id);

            // invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_COINS request");
            return null;
        }

        if(rep.length == 0) {
            return null;
        }
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getValue();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_COINS request");
            return null;
        }
    }
}
