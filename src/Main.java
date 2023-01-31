import CKKSOperations.*;
import data.Ciphertext;
import data.Plaintext;
import keys.PublicKey;
import keys.SecretKey;
import data.Complex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // The number of complex numbers in each vector
        BigInteger polynomialDegree = new BigInteger("16");
        int bitPrimeSize = 500;
        int millerRabinIterations = 30;

        BigInteger moduloBig = BigInteger.TWO.pow(2000);
        BigInteger moduloSmall = BigInteger.TWO.pow(1200);
        BigInteger scalingFactor = BigInteger.TWO.pow(40);

        MathContext mc = new MathContext(100);

        Parameters params = new Parameters(polynomialDegree,moduloBig, moduloSmall, scalingFactor, bitPrimeSize,
                 millerRabinIterations, mc);

        KeyGenerator keyGen = new KeyGenerator(params);
        SecretKey secretKey = keyGen.getSecretKey();
        PublicKey publicKey = keyGen.getPublicKey();
        PublicKey relinearizationKey = keyGen.getRelinearizationKey();

        ArrayList<Complex> values0 = new ArrayList<>();
        ArrayList<Complex> values1 = new ArrayList<>();

        BigDecimal twoBD = new BigDecimal("2");
        BigDecimal threeBD = new BigDecimal("3");

        //[(2,0),(2,3),(3,0),(0,3),(2,0),(2,0),(3,0),(2,2)]
        values0.add(new Complex(twoBD, BigDecimal.ZERO));
        values0.add(new Complex(twoBD, threeBD));
        values0.add(new Complex(threeBD, BigDecimal.ZERO));
        values0.add(new Complex(BigDecimal.ZERO,threeBD));
        values0.add(new Complex(twoBD, BigDecimal.ZERO));
        values0.add(new Complex(twoBD, threeBD));
        values0.add(new Complex(threeBD, BigDecimal.ZERO));
        values0.add(new Complex(BigDecimal.ZERO,threeBD));

        //[(2,0),(2,0),(3,0),(2,2),(2,0),(2,0),(3,0),(2,2)]
        values1.add(new Complex(twoBD, BigDecimal.ZERO));
        values1.add(new Complex(twoBD, BigDecimal.ZERO));
        values1.add(new Complex(threeBD, BigDecimal.ZERO));
        values1.add(new Complex(twoBD,twoBD));
        values1.add(new Complex(twoBD, BigDecimal.ZERO));
        values1.add(new Complex(twoBD, BigDecimal.ZERO));
        values1.add(new Complex(threeBD, BigDecimal.ZERO));
        values1.add(new Complex(twoBD,twoBD));

        Encoder encoder = new Encoder(params, mc);
        Plaintext val0Encoded = encoder.encode(values0, scalingFactor);
        Plaintext val1Encoded = encoder.encode(values1, scalingFactor);

        Encryption encryper = new Encryption(params, publicKey, secretKey);
        Ciphertext val0Cipher = encryper.encrypt(val0Encoded);
        Ciphertext val1Cipher = encryper.encrypt(val1Encoded);

        Evaluation evaluation = new Evaluation(params);
        Ciphertext val0Val1 = evaluation.additionCiphertext(val0Cipher, val1Cipher);

        Decryptor decryptor = new Decryptor(params, secretKey);
        Plaintext val0Val1Encoded = decryptor.decrypt(val0Val1);
        ArrayList<Complex> val0Val1Decoded = encoder.decode(val0Val1Encoded);

        Plaintext a = evaluation.multiplyEncodedText(val0Encoded,val1Encoded);
        ArrayList<Complex> aDecoded = encoder.decode(a);

        ArrayList<Complex> bDecoded = evaluation.multiplicationMessage(values0,values1);

        ArrayList<Complex> val0Val1DecodedRoundedA = new ArrayList<>();
        ArrayList<Complex> val0Val1DecodedRoundedB = new ArrayList<>();
        MathContext m = new MathContext(2);
        for (int i = 0; i < val0Val1Decoded.size(); i++) {
            val0Val1DecodedRoundedA.add(new Complex(aDecoded.get(i).real().round(m), aDecoded.get(i).imag().round(m)));
            val0Val1DecodedRoundedB.add(new Complex(bDecoded.get(i).real().round(m), bDecoded.get(i).imag().round(m)));
        }

        System.out.println(val0Val1DecodedRoundedA);
    }

}
