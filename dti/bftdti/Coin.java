/**
 * BFT Dti implementation (Coin object creation).
 *
 */
package dti.bftdti;

import java.io.Serializable;

public class Coin implements Serializable{
    
    private int id;
    private int owner;
    private float value;

    public Coin(int id, int owner, float value){
        this.id = id;
        this.owner = owner;
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}