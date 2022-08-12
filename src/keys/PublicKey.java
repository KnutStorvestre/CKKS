package keys;

import modules.Polynomial;

import java.math.BigInteger;

public class PublicKey {

    private BigInteger polynomialDegree;
    private Polynomial p0;
    private Polynomial p1;

    public PublicKey(BigInteger polyDeg, Polynomial polynomial0, Polynomial polynomial1){
        polynomialDegree = polyDeg;
        p0 = polynomial0;
        p1 = polynomial1;
    }

    public Polynomial getP0() {
        return p0;
    }

    public Polynomial getP1() {
        return p1;
    }

}
