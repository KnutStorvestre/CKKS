package GUI;

import data.Ciphertext;
import data.EncodedText;
import modules.Complex;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Vector {

    private ArrayList<Complex> vector;
    private EncodedText encoded;
    private Ciphertext encrypted;

    public void setVector(ArrayList<Complex> vector) {
        this.vector = vector;
    }

    public void setEncoded(EncodedText encoded) {
        this.encoded = encoded;
    }

    public void setEncrypted(Ciphertext encrypted) {
        this.encrypted = encrypted;
    }

    public ArrayList<Complex> getVector() {
        return vector;
    }

    public EncodedText getEncoded() {
        return encoded;
    }

    public Ciphertext getEncrypted() {
        return encrypted;
    }
}
