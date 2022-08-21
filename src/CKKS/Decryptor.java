package CKKS;

import data.Ciphertext;
import data.EncodedText;
import keys.SecretKey;
import modules.ChineseRemainderTheorem;
import modules.Polynomial;

import java.math.BigInteger;

public class Decryptor {

    //TODO if encoder is in the same class as encoder this should maybe be a part of the same class as the encryption

    private BigInteger polyDeg;
    private ChineseRemainderTheorem crt;
    private SecretKey secretKey;

    public Decryptor(Parameters params, SecretKey sk){
        polyDeg = params.getPolynomialDegree();
        crt = params.getCrt();
        secretKey = sk;
    }

    public EncodedText decrypt(Ciphertext cipher){
        Polynomial cipherPoly0 = cipher.getPoly0();
        Polynomial cipherPoly1 = cipher.getPoly1();
        BigInteger cipherMod = cipher.getModulo();

        Polynomial encodedPolynomial = cipherPoly1.multiplicationCRT(secretKey.getPoly(), crt);
        encodedPolynomial = cipherPoly0.additionMod(encodedPolynomial, cipherMod);
        encodedPolynomial = encodedPolynomial.moduloSmall(cipherMod);

        return new EncodedText(encodedPolynomial, cipher.getScaling());
    }
}
