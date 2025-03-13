package dti.bftdti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class BFTDtiMessage<K, V> implements Serializable {
    private BFTDtiRequestType type;
    private K key;
    private V value;
    private HashSet<K> keySet;
    private int size;
    private boolean removed;

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

    public BFTDtiRequestType getType() {
        return type;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public void setKey(Object key) {
        this.key = (K) key;
    }

    @SuppressWarnings("unchecked")
    public void setValue(Object value) {
        this.value = (V) value;
    }

    public void setType(BFTDtiRequestType type) {
        this.type = type;
    }
}