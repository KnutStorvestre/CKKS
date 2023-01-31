package CKKSOperations;

import data.Ciphertext;
import data.Plaintext;
import keys.SecretKey;
import modules.ChineseRemainderTheorem;
import data.Polynomial;

import java.math.BigInteger;

public class Decryptor {

    private BigInteger polyDeg;
    private ChineseRemainderTheorem crt;
    private SecretKey secretKey;

    public Decryptor(Parameters params, SecretKey sk){
        polyDeg = params.getPolynomialDegree();
        crt = params.getCrt();
        secretKey = sk;
    }

    public Plaintext decrypt(Ciphertext cipher){
        Polynomial cipherPoly0 = cipher.getPoly0();
        Polynomial cipherPoly1 = cipher.getPoly1();
        BigInteger cipherMod = cipher.getModulo();

        Polynomial encodedPolynomial = cipherPoly1.multiplicationCRT(secretKey.getPoly(), crt);
        encodedPolynomial = cipherPoly0.additionMod(encodedPolynomial, cipherMod);
        encodedPolynomial = encodedPolynomial.moduloSmall(cipherMod);

        return new Plaintext(encodedPolynomial, cipher.getScaling());
    }

}
