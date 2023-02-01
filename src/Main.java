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

    // To run you must import the big-math library located in the "lib" folder
    public static void main(String[] args) {
        // Parameters
        BigInteger polynomialDegree = new BigInteger("16");
        int bitPrimeSize = 500;
        int millerRabinIterations = 30;

        BigInteger moduloBig = BigInteger.TWO.pow(2000);
        BigInteger moduloSmall = BigInteger.TWO.pow(1200);
        BigInteger scalingFactor = BigInteger.TWO.pow(40);

        MathContext mc = new MathContext(100);

        Parameters params = new Parameters(polynomialDegree,moduloBig, moduloSmall, scalingFactor, bitPrimeSize,
                 millerRabinIterations, mc);

        // Key Generation
        KeyGenerator keyGen = new KeyGenerator(params);
        SecretKey secretKey = keyGen.getSecretKey();
        PublicKey publicKey = keyGen.getPublicKey();
        PublicKey relinearizationKey = keyGen.getRelinearizationKey();

        // Creating vectors
        ArrayList<Complex> vector0 = new ArrayList<>();
        ArrayList<Complex> vector1 = new ArrayList<>();

        BigDecimal twoBD = new BigDecimal("2");
        BigDecimal threeBD = new BigDecimal("3");

        // vector0 = [2, 2+3j, 3, 3j, 2, 2, 3, 3j]
        vector0.add(new Complex(twoBD, BigDecimal.ZERO));
        vector0.add(new Complex(twoBD, threeBD));
        vector0.add(new Complex(threeBD, BigDecimal.ZERO));
        vector0.add(new Complex(BigDecimal.ZERO,threeBD));
        vector0.add(new Complex(twoBD, BigDecimal.ZERO));
        vector0.add(new Complex(twoBD, threeBD));
        vector0.add(new Complex(threeBD, BigDecimal.ZERO));
        vector0.add(new Complex(BigDecimal.ZERO,threeBD));

        // vector1 = [2, 2, 3, 2+2j, 2, 2, 3, 2+2j]
        vector1.add(new Complex(twoBD, BigDecimal.ZERO));
        vector1.add(new Complex(twoBD, BigDecimal.ZERO));
        vector1.add(new Complex(threeBD, BigDecimal.ZERO));
        vector1.add(new Complex(twoBD,twoBD));
        vector1.add(new Complex(twoBD, BigDecimal.ZERO));
        vector1.add(new Complex(twoBD, BigDecimal.ZERO));
        vector1.add(new Complex(threeBD, BigDecimal.ZERO));
        vector1.add(new Complex(twoBD,twoBD));

        // Encoding
        Encoder encoder = new Encoder(params, mc);
        Plaintext vector0Encoded = encoder.encode(vector0, scalingFactor);
        Plaintext vector1Encoded = encoder.encode(vector1, scalingFactor);

        // Encrypting
        Encryption encryptor = new Encryption(params, publicKey, secretKey);
        Ciphertext val0Cipher = encryptor.encrypt(vector0Encoded);
        Ciphertext val1Cipher = encryptor.encrypt(vector1Encoded);

        // Evaluation
        Evaluation evaluation = new Evaluation(params);
        Ciphertext val0Val1 = evaluation.multiplyCiphertext(val0Cipher, val1Cipher, relinearizationKey);

        // Decryption
        Decryptor decryptor = new Decryptor(params, secretKey);
        Plaintext val0Val1Encoded = decryptor.decrypt(val0Val1);

        // Decoding
        ArrayList<Complex> val0Val1Decoded = encoder.decode(val0Val1Encoded);

        // Rounding operation
        ArrayList<Complex> val0Val1DecodedRoundedA = new ArrayList<>();
        MathContext m = new MathContext(2);
        for (int i = 0; i < val0Val1Decoded.size(); i++) {
            val0Val1DecodedRoundedA.add(new Complex(val0Val1Decoded.get(i).real().round(m), val0Val1Decoded.get(i).imag().round(m)));
        }

        System.out.println(val0Val1DecodedRoundedA);
    }

}
