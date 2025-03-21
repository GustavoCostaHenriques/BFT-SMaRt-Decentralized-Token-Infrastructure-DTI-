/**
 * BFT Dti implementation (NFT object creation).
 *
 */
package dti.bftdti;

import java.io.Serializable;

public class NFT implements Serializable{

    private int id;
    private int owner;
    private String name;
    private String URI;
    private float value;

    public NFT(int id, int owner, String name, String URI, float value) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.URI = URI;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
    public boolean compare(String name){
        return (this.name.equals(name));
    }
    public String toString(){
        return this.id + this.name + this.URI + this.owner + this.value;
    }
}

