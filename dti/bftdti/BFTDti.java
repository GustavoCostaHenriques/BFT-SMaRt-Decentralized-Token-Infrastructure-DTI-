/**
 * BFT Dti implementation (client side).
 *
 */
package dti.bftdti;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bftsmart.tom.ServiceProxy;

public class BFTDti<K, V> {
    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    private final ServiceProxy serviceProxy;

    public BFTDti(int id) {
        serviceProxy = new ServiceProxy(id);
    }
    
    public ArrayList<Coin> myCoins() {
        byte[] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.MY_COINS);
            
            // invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(BFTDtiMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_COINS request");
            return null;
        }

        if(rep.length == 0) {
            return null;
        }
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getCoins();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_COINS request");
            return null;
        }
    }

    public int mint(Float value){
        byte [] rep;
        try{
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.MINT);
            request.setcoinValue(value);

            // invokes BFT-Smart
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        } catch(IOException e){
            e.printStackTrace();
            logger.error("Failed to send MINT request");
            return -1;
        }
        if (rep.length == 0 ){
            return -1;
        }
        try {
            BFTDtiMessage <K,V> response = BFTDtiMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            ex.printStackTrace();
            logger.error("Failed to deserialized response of MINT request");
            return -1;
        } 
                
    }
    public int spend(String[] coinsId, int receiverId, float value){
        byte [] rep;
        try{
            BFTDtiMessage<K,V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.SPEND);
            request.setCoinsToSpend(stringArrayToIntArray(coinsId));
            request.setcoinValue(value);
            request.setReceiverId(receiverId);

            // invokes BFT-Smart
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        }  catch (IOException e){
            logger.error("Failed to send SPEND request");
            return -1;
        }  

        if (rep.length == 0 ){
            return -1;
        }


        try {
            BFTDtiMessage <K,V> response = BFTDtiMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of SPEND request");
            return -1;
        }
    }

    public ArrayList<NFT> myNFTs(){
        byte[] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.MY_NFTS);

            // invokes BFT-SMaRt
            rep = serviceProxy.invokeUnordered(BFTDtiMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send MY_NFTS request");
            return null;
        }

        if (rep.length == 0) {
            return null;
        }
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getNFTs();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialized response of MY_NFTS request");
            return null;
        }
    }

    public int mintNFT(String name , String url ,Float value){
        byte [] rep;
        try{
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.MINT_NFT);
            request.setName(name);
            request.setURI(url);
            request.setnftValue(value);

            // invokes BFT-Smart
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        } catch(IOException e){
            logger.error("Failed to send MINT_NFT request");
            return -1;
        }
        if (rep.length == 0 ){
            return -1;
        }
        try {
            BFTDtiMessage <K,V> response = BFTDtiMessage.fromBytes(rep);
            return response.getNftId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("TODO ERROR MSG");
            return -1;
        }     
    }

    public int setNFTPrice(int nftId, float newValue) {
        byte[] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.SET_NFT_PRICE);
            //change the value of the Nft with name to newValue
            request.setNftId(nftId);        
            request.setnftValue(newValue);

            // invokes BFT-Smart
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send SET_NFT_PRICE request");
            return -1;
        }
        if (rep.length == 0) {
            return -1;
        }
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getConfirmation();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialize response of SET_NFT_PRICE request");
            return -1;
        }
    }

    public ArrayList<NFT> searchNFT(String name){
        byte [] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.SEARCH_NFT);
            request.setName(name);

            rep = serviceProxy.invokeUnordered(BFTDtiMessage.toBytes(request));
        } catch (Exception e) {
            logger.error("Failed to send SEARCH_NFT request");
            return null;
        }
        if (rep.length == 0) {
            return null;
        }   
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getSearchedNFT();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialize response of SEARCH_NFT request");
            return null;
        }
    }

    public int buyNFT(int nftId, String[] coinIds) {
        byte[] rep;
        try {
            BFTDtiMessage<K, V> request = new BFTDtiMessage<>();
            request.setType(BFTDtiRequestType.BUY_NFT);
            request.setNftId(nftId);
            request.setCoinsToSpend(stringArrayToIntArray(coinIds));

            // invokes BFT-SMaRt
            rep = serviceProxy.invokeOrdered(BFTDtiMessage.toBytes(request));
        } catch (IOException e) {
            logger.error("Failed to send BUY_NFT request");
            return -1;
        }
        if (rep.length == 0) {
            return -1;
        }
        try {
            BFTDtiMessage<K, V> response = BFTDtiMessage.fromBytes(rep);
            return response.getCoinId();
        } catch (ClassNotFoundException | IOException ex) {
            logger.error("Failed to deserialize response of BUY_NFT request");
            return -1;
        }
    }

    private int [] stringArrayToIntArray(String [] s){
        int[] numbers = new int[s.length];
        for(int i = 0; i < s.length; i++){
            numbers[i] = Integer.parseInt(s[i]);
        }
        return numbers;
    }
}
