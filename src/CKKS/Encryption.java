package CKKS;

import data.Ciphertext;
import data.EncodedText;
import keys.PublicKey;
import keys.SecretKey;
import modules.ChineseRemainderTheorem;
import modules.NumberTheory;
import modules.Polynomial;

import java.math.BigInteger;

public class Encryption {

    private BigInteger polynomialDegree;
    private BigInteger smallMod;
    private BigInteger bigMod;
    private PublicKey publicKey;
    private SecretKey secretKey;
    private NumberTheory numTheory;
    private ChineseRemainderTheorem crtParameters;

    public Encryption(Parameters params, PublicKey pubKey, SecretKey secKey){
        polynomialDegree = params.getPolynomialDegree();
        smallMod = params.getSmallModulo();
        bigMod = params.getBigModulo();
        publicKey = pubKey;
        secretKey = secKey;
        crtParameters = params.getCrt();
        numTheory = new NumberTheory();
    }

    public Ciphertext encrypt(EncodedText encodedText){
        Polynomial pubKey0 = publicKey.getP0();
        Polynomial pubKey1 = publicKey.getP1();

        BigInteger[] randomDiscreteTriangleList0 = numTheory.randomTriangle(polynomialDegree.intValue());
        BigInteger[] randomDiscreteTriangleList1 = numTheory.randomTriangle(polynomialDegree.intValue());
        BigInteger[] randomDiscreteTriangleList2 = numTheory.randomTriangle(polynomialDegree.intValue());

        Polynomial randomPoly = new Polynomial(polynomialDegree, randomDiscreteTriangleList0);
        Polynomial errorPoly0 = new Polynomial(polynomialDegree, randomDiscreteTriangleList1);
        Polynomial errorPoly1 = new Polynomial(polynomialDegree, randomDiscreteTriangleList2);

        Polynomial cipherPolynomial0 = pubKey0.multiplicationCRT(randomPoly, crtParameters);
        System.out.println(randomPoly);
        System.out.println(cipherPolynomial0);
        cipherPolynomial0 = errorPoly0.additionMod(cipherPolynomial0,smallMod);
        cipherPolynomial0 = cipherPolynomial0.additionMod(encodedText.getPolynomial(), smallMod);
        cipherPolynomial0 = cipherPolynomial0.moduloSmall(smallMod);

        Polynomial cipherPolynomial1 = pubKey1.multiplicationCRT(randomPoly,crtParameters);
        cipherPolynomial1 = errorPoly1.additionMod(cipherPolynomial1, smallMod);
        cipherPolynomial1 = cipherPolynomial1.moduloSmall(smallMod);

        return new Ciphertext(cipherPolynomial0, cipherPolynomial1, encodedText.getScalingFactor(), smallMod);
    }

}
