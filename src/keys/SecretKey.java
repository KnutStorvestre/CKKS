package keys;

import data.Polynomial;

import java.math.BigInteger;

public class SecretKey {

    private BigInteger polynomialDegree;
    private Polynomial polynomial;

    public SecretKey(BigInteger polyDegree, Polynomial poly){
        polynomialDegree = polyDegree;
        polynomial = poly;
    }

    public BigInteger getPolynomialDegree() {
        return polynomialDegree;
    }

    public Polynomial getPoly(){
        return polynomial;
    }

    @Override
    public String toString() {
        return "SecretKey{" +
                "polynomialDegree=" + polynomialDegree +
                ", polynomial=" + polynomial +
                '}';
    }

}
