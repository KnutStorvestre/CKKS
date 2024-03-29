package data;


import java.math.BigInteger;

public class Plaintext {

    private Polynomial polynomial;
    private BigInteger scalingFactor;

    public Plaintext(Polynomial poly, BigInteger scaling){
        polynomial = poly;
        scalingFactor = scaling;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    public BigInteger getScalingFactor() {
        return scalingFactor;
    }

    /**
     * The value with the biggest degree is the value at the back
     * @return
     */
    @Override
    public String toString() {
        return "util.EncodedText{" +
                "polynomial=" + polynomial +
                ", scalingFactor=" + scalingFactor +
                '}';
    }

}
