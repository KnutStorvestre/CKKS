package GUI;

import data.Ciphertext;
import data.Plaintext;
import data.Complex;

import java.util.ArrayList;

public class Vector {

    private ArrayList<Complex> vector;
    private Plaintext encoded;
    private Ciphertext encrypted;

    /**
     *
     * @param level 0 = vector, 1 = encodedText, 2 = Ciphertext
     */
    public void printVector(int level){
        if (level == 0){
            System.out.println(vector);
        } else if (level == 1) {
            System.out.println(encoded);
        }
        else {
            System.out.println(encrypted);
        }
    }

    public void setVector(ArrayList<Complex> vector) {
        this.vector = vector;
    }

    public void setEncoded(Plaintext encoded) {
        this.encoded = encoded;
    }

    public void setEncrypted(Ciphertext encrypted) {
        this.encrypted = encrypted;
    }

    public ArrayList<Complex> getVector() {
        return vector;
    }

    public Plaintext getEncoded() {
        return encoded;
    }

    public Ciphertext getEncrypted() {
        return encrypted;
    }

}
