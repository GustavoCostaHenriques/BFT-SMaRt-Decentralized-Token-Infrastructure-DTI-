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

import java.util.TreeMap;
import java.util.ArrayList;
import java.util.List;

public class BFTDtiServer<K, V> extends DefaultSingleRecoverable {

    private int coinId = 1;
    private int nftId = 1;

    private TreeMap<Integer, Coin> CoinReplicaMap;

    private TreeMap<Integer, NFT> NFTReplicaMap;

    private final Logger logger = LoggerFactory.getLogger("bftsmart");
    // The constructor passes the id of the server to the super class
    public BFTDtiServer(int id) {
        CoinReplicaMap = new TreeMap<>();
        NFTReplicaMap = new TreeMap<>();
        // turn-on BFT-SMaRt'replica
        new ServiceReplica(id, this, this);

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
        try{
            BFTDtiMessage<K,V> response = new BFTDtiMessage<>();
            BFTDtiMessage<K,V> request = BFTDtiMessage.fromBytes(command);
            BFTDtiRequestType cmd = request.getType();
            int senderId = msgCtx.getSender();

            logger.info("Ordered execution of a {} request from {}", cmd, senderId);
            
            switch(cmd){

                case MINT:
                    Coin coin = new Coin(coinId,senderId, request.getcoinValue());
                    CoinReplicaMap.put(coinId, coin);
                    
                    response.setCoinId(coinId);
                    this.coinId++;

                    return BFTDtiMessage.toBytes(response);
                case SPEND:
                    ArrayList<Coin> Coins = new ArrayList<>(CoinReplicaMap.values());
                    ArrayList<Coin> CoinsOfTheUser = new ArrayList<>();
                    ArrayList<Coin> CoinsToSpend = new ArrayList<>();

                    for (Coin allCoins : Coins) {
                        if (allCoins.getOwner() == senderId) {
                            CoinsOfTheUser.add(allCoins);
                        }
                    }
                    if(CoinsOfTheUser == null) {
                        response.setCoinId(-1);
                        return BFTDtiMessage.toBytes(response);
                    } else {
                        for(Coin coinWithTheRightId : CoinsOfTheUser) {
                            for(int id : request.getCoinsToSpend()) {
                                if(coinWithTheRightId.getId() == id) {
                                    CoinsToSpend.add(coinWithTheRightId);
                                }
                            }
                        }
                    }

                    boolean repeatedIds = false;
                    // check for repeated ids in the Coins to spend array
                    for(int i = 0; i < CoinsToSpend.size(); i++) {
                        for(int j = 0; j < CoinsToSpend.size(); j++) {
                            if(j != i) {
                                if(CoinsToSpend.get(i).getId() == CoinsToSpend.get(j).getId()) {
                                    repeatedIds = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(repeatedIds) {
                        response.setCoinId(-2);
                        return BFTDtiMessage.toBytes(response);
                    }

                    if(CoinsToSpend.size() == 0) {
                        response.setCoinId(-3);
                        return BFTDtiMessage.toBytes(response);
                    }

                    float totalValue = 0;
                    for(Coin coinToSpend : CoinsToSpend) {
                        totalValue += coinToSpend.getValue();
                    }

                    float remainingValue = totalValue - request.getcoinValue();
                    if(remainingValue < 0) {
                        response.setCoinId(-4);
                        return BFTDtiMessage.toBytes(response);
                    }

                    if(remainingValue == 0) {
                        // remove the coins from the map
                        for(Coin coinToSpend : CoinsToSpend) {
                            CoinReplicaMap.remove(coinToSpend.getId());
                        }

                        // create a new coin for the receiver
                        Coin newCoinForReceiver = new Coin(coinId, request.getRecieverId(), request.getcoinValue());
                        CoinReplicaMap.put(coinId, newCoinForReceiver);
                        this.coinId++;
                        response.setCoinId(0);
                        return BFTDtiMessage.toBytes(response);
                    }

                    // check the receiverid (can not be (0,1,2,3) and it can not be the sender)
                    if((request.getRecieverId() < 4 && request.getRecieverId() > -1 ) || request.getRecieverId() == senderId) {
                        response.setCoinId(-5);
                        return BFTDtiMessage.toBytes(response);
                    }

                    // remove the coins from the map
                    for(Coin coinToSpend : CoinsToSpend) {
                        CoinReplicaMap.remove(coinToSpend.getId());
                    }

                    // create a new coin with the remaining value for the sender
                    Coin newCoin = new Coin(coinId, senderId, remainingValue);
                    CoinReplicaMap.put(coinId, newCoin);
                    response.setCoinId(coinId);
                    this.coinId++;

                    // create a new coin for the receiver
                    Coin newCoinForReceiver = new Coin(coinId, request.getRecieverId(), request.getcoinValue());
                    CoinReplicaMap.put(coinId, newCoinForReceiver);
                    this.coinId++;

                    return BFTDtiMessage.toBytes(response);

                case MINT_NFT:
                    ArrayList<NFT> allNFTS = new ArrayList<>();

                    boolean found = false;
                    for(NFT nft : NFTReplicaMap.values()) {
                        if(nft.getName().equalsIgnoreCase(request.getName())) {
                            found = true;
                            break;
                        }
                    }
                    
                    if(found) {
                        response.setNftId(-1);
                        return BFTDtiMessage.toBytes(response);
                    }
                    NFT nft = new NFT(nftId, senderId, request.getName(), request.getURI(), request.getnftValue());
                    NFT oldNFT = NFTReplicaMap.put(nftId, nft);
                    
                    response.setNftId(nftId);
                    this.nftId++;

                    return BFTDtiMessage.toBytes(response);

                case SET_NFT_PRICE:
                    NFT nftToChange = null;
                    for (NFT nftinMap : NFTReplicaMap.values()) {
                        if (nftinMap.getId() == (request.getNftId())) {
                            nftToChange = nftinMap;
                            break;
                        }
                    }
                    if(nftToChange != null && nftToChange.getOwner() == senderId) {
                        nftToChange.setValue(request.getnftValue());
                        NFTReplicaMap.put(nftToChange.getId(), nftToChange);
                        response.setConfirmation(1);
                    } else {
                        response.setConfirmation(0);
                    }
                    return BFTDtiMessage.toBytes(response);

                case BUY_NFT:
                    NFT nftToBuy = NFTReplicaMap.get(request.getNftId());
                    ArrayList<Coin> CoinsBuy = new ArrayList<>(CoinReplicaMap.values());
                    ArrayList<Coin> CoinsOfTheUserBuy = new ArrayList<>();
                    ArrayList<Coin> CoinsToSpendForNFT = new ArrayList<>();

                    for (Coin allCoins : CoinsBuy) {
                        if (allCoins.getOwner() == senderId) {
                            CoinsOfTheUserBuy.add(allCoins);
                        }
                    }
                    if(CoinsOfTheUserBuy == null) {
                        response.setCoinId(-1);
                        return BFTDtiMessage.toBytes(response);
                    } else {
                        for(Coin coinWithTheRightId : CoinsOfTheUserBuy) {
                            for(int id : request.getCoinsToSpend()) {
                                if(coinWithTheRightId.getId() == id) {
                                    CoinsToSpendForNFT.add(coinWithTheRightId);
                                }
                            }
                        }
                    }

                    if(nftToBuy == null) {
                        response.setCoinId(-2);
                        return BFTDtiMessage.toBytes(response);
                    }

                    boolean repeatedIdsNFT = false;
                    // check for repeated ids in the Coins to spend array
                    for(int i = 0; i < CoinsToSpendForNFT.size(); i++) {
                        for(int j = 0; j < CoinsToSpendForNFT.size(); j++) {
                            if(j != i) {
                                if(CoinsToSpendForNFT.get(i).getId() == CoinsToSpendForNFT.get(j).getId()) {
                                    repeatedIdsNFT = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(repeatedIdsNFT) {
                        response.setCoinId(-3);
                        return BFTDtiMessage.toBytes(response);
                    }

                    // check if the nft already belongs to the sender
                    if(nftToBuy.getOwner() == senderId) {
                        response.setCoinId(-4);
                        return BFTDtiMessage.toBytes(response);
                    }

                    

                    float totalValueForNFT = 0;
                    for(Coin coinNFT : CoinsToSpendForNFT) {
                        totalValueForNFT += coinNFT.getValue();
                    }

                    if(totalValueForNFT < nftToBuy.getValue()) {
                        response.setCoinId(-5);
                        return BFTDtiMessage.toBytes(response);
                    }

                    // remove the coins from the map
                    for(Coin coinNFTrm : CoinsToSpendForNFT) {
                        CoinReplicaMap.remove(coinNFTrm.getId());
                    }

                    // create a new coin with the remaining value for the sender
                    float remainingValueForNFT = totalValueForNFT - nftToBuy.getValue();
                    
                    if(remainingValueForNFT > 0) {
                        Coin newCoinWithRemainingValue = new Coin(coinId, senderId, remainingValueForNFT);
                        CoinReplicaMap.put(coinId, newCoinWithRemainingValue);
                        response.setCoinId(coinId);
                        this.coinId++;
                    }

                    // remove the NFT from the map
                    NFTReplicaMap.remove(nftToBuy.getId());

                    // set the owner of the NFT to the sender
                    nftToBuy.setOwner(senderId);

                    // add the NFT to the map
                    NFTReplicaMap.put(nftToBuy.getId(), nftToBuy);

                    return BFTDtiMessage.toBytes(response);

                // No need for order cases
                case MY_COINS:
                    ArrayList<Coin> CoinsOfTheUser2 = new ArrayList<>();

                    for(Coin coin2 : CoinReplicaMap.values()) {
                        if(coin2.getOwner() == senderId) {
                            CoinsOfTheUser2.add(coin2);
                        }
                    }
                
                    if(CoinsOfTheUser2!= null) {
                        response.setCoins(CoinsOfTheUser2);
                    } 
                    return BFTDtiMessage.toBytes(response);

                case MY_NFTS:
                    ArrayList<NFT> NFTsOfTheUser = new ArrayList<>();
                    for(NFT nft2: NFTReplicaMap.values()) {
                        if(nft2.getOwner() == senderId) {
                            NFTsOfTheUser.add(nft2);
                        }
                    }
                
                    if(NFTsOfTheUser != null) {
                        response.setNFTs(NFTsOfTheUser);
                    } 
                    return BFTDtiMessage.toBytes(response);
                    
                case SEARCH_NFT:
                    ArrayList<NFT> NFTsOfTheUser2 = new ArrayList<>();
                    String name = request.getName();
                    for(NFT nft2: NFTReplicaMap.values()) {
                        if(nft2.getName().contains(name)) {
                            NFTsOfTheUser2.add(nft2);
                        }
                    }
                    if(NFTsOfTheUser2 != null) {
                        response.setSearchedNFT(NFTsOfTheUser2);
                    }
                     
                    return BFTDtiMessage.toBytes(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to execute ordered command");
            return new byte[0];
        }
        return null;
    }

    @Override
    public byte[] appExecuteUnordered(byte[] command, MessageContext msgCtx) {
        // read-only operations can be defined here to be invoked without running
        // consensus
        try{
            BFTDtiMessage<K, V> response = new BFTDtiMessage<>();
            BFTDtiMessage<K, V> request = BFTDtiMessage.fromBytes(command);
            BFTDtiRequestType cmd = request.getType();
            int senderId = msgCtx.getSender();

            logger.info("Unordered execution of a {} request from {}", cmd, senderId);


            switch (cmd) {
                case MY_COINS:
                    ArrayList<Coin> CoinsOfTheUser = new ArrayList<>();

                    for(Coin coin : CoinReplicaMap.values()) {
                        if(coin.getOwner() == senderId) {
                            CoinsOfTheUser.add(coin);
                        }
                    }
                
                    if(CoinsOfTheUser != null) {
                        response.setCoins(CoinsOfTheUser);
                    } 
                    return BFTDtiMessage.toBytes(response);

                case MY_NFTS:
                    ArrayList<NFT> NFTsOfTheUser = new ArrayList<>();
                    for(NFT nft : NFTReplicaMap.values()) {
                        if(nft.getOwner() == senderId) {
                            NFTsOfTheUser.add(nft);
                        }
                    }
                
                    if(NFTsOfTheUser != null) {
                        response.setNFTs(NFTsOfTheUser);
                    } 
                    return BFTDtiMessage.toBytes(response);
                    
                case SEARCH_NFT:
                    ArrayList<NFT> NFTsOfTheUser2 = new ArrayList<>();
                    String name = request.getName();
                    for(NFT nft2: NFTReplicaMap.values()) {
                        if(nft2.getName().contains(name)) {
                            NFTsOfTheUser2.add(nft2);
                        }
                    }
                    if(NFTsOfTheUser2 != null) {
                        response.setSearchedNFT(NFTsOfTheUser2);
                    }
                     
                    return BFTDtiMessage.toBytes(response);
        }
        } catch (Exception e) {
            logger.error("Failed to execute unordered command");
            e.printStackTrace();
            return new byte[0];
        }
        return null;
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