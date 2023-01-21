package data;


import java.math.BigInteger;

public class Ciphertext {

    private Polynomial poly0;
    private Polynomial poly1;
    private BigInteger scaling;
    private BigInteger modulo;

    public Ciphertext(Polynomial p0, Polynomial p1, BigInteger scalingVal, BigInteger mod){
        poly0 = p0;
        poly1 = p1;
        scaling = scalingVal;
        modulo = mod;
    }

    public Polynomial getPoly0() {
        return poly0;
    }

    public Polynomial getPoly1() {
        return poly1;
    }

    public BigInteger getScaling() {
        return scaling;
    }

    public BigInteger getModulo() {
        return modulo;
    }

    @Override
    public String toString() {
        return "Ciphertext{" +
                "poly0=" + poly0 +
                ", poly1=" + poly1 +
                ", scaling=" + scaling +
                ", modulo=" + modulo +
                '}';
    }

}
