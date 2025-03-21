package dti.bftdti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class BFTDtiMessage<K, V> implements Serializable {
    private BFTDtiRequestType type;
    private K key;
    private String name;
    private String URI;
    private float coinValue;
    private float nftValue;
    private V value;
    private int [] coinsToSpend;
    private String[] info;
    private int coinId;
    private int nftId;
    private int receiverId;
    private int confirmation;
    private ArrayList<NFT> searchedNFT;
    private int[] coinIds;
    private ArrayList<Coin> coins = new ArrayList<>();
    private ArrayList<NFT> nfts = new ArrayList<>();

    public BFTDtiMessage() {
    }

    public static <K, V> byte[] toBytes(BFTDtiMessage<K, V> message) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(message);

        objOut.flush();
        byteOut.flush();

        return byteOut.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <K, V> BFTDtiMessage<K, V> fromBytes(byte[] rep) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(rep);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        return (BFTDtiMessage<K, V>) objIn.readObject();
    }
    public int getRecieverId(){
        return this.receiverId; 
    }

    public int [] getCoinsToSpend(){
        return this.coinsToSpend;
    }

    public String[] getInfo(){
        return this.info;
    }

    public String getName(){
        return this.name;
    }

    public String getURI(){
        return this.URI;
    }

    public BFTDtiRequestType getType() {
        return type;
    }

    public Float getcoinValue(){
        return this.coinValue;
    }

    public Float getnftValue(){
        return this.nftValue;
    }

    public ArrayList<Coin> getCoins(){
        return this.coins;
    }

    public ArrayList<NFT> getNFTs(){
        return this.nfts;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public int getCoinId() {
        return coinId;
    }

    public int getNftId() {
        return nftId;
    }

    public int getConfirmation() {
        return confirmation;
    }
    public ArrayList<NFT> getSearchedNFT(){
        return this.searchedNFT;
    }

    @SuppressWarnings("unchecked")
    public void setKey(Object key) {
        this.key = (K) key;
    }

    public void setReceiverId(int id){
        this.receiverId = id;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (V) value;
    }

    public void setCoinsToSpend(int [] coins){
        this.coinsToSpend = coins;
    }

    public void setType(BFTDtiRequestType type) {
        this.type = type;
    }

    public void setCoinId(int coinId) {
        this.coinId = coinId;
    }

    public void setNftId(int nftId) {
        this.nftId = nftId;
    }
    public void setInfo(String[] s){
        this.info = s;
    }
    
    public void setConfirmation(int confirmation) {
        this.confirmation = confirmation;
    }

    public void setcoinValue(Float coinValue){
        this.coinValue = coinValue;
    }

    public void setnftValue(Float nftValue){
        this.nftValue = nftValue;
    }

    public void setURI(String URI){
        this.URI = URI;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public void setCoins(ArrayList<Coin> coins){
        this.coins = coins;
    }

    public void setNFTs(ArrayList<NFT> nfts){
        this.nfts = nfts;
    }

    public void setSearchedNFT(ArrayList<NFT> nfts){
        this.searchedNFT = nfts;
    }

    public void setCoinIds(int[] coinIds) {
    this.coinIds = coinIds;
}
}