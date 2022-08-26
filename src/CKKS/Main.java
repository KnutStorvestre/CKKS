package CKKS;

import data.Ciphertext;
import data.EncodedText;
import keys.PublicKey;
import keys.SecretKey;
import modules.Complex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // The number of complex numbers in each vector
        // Why does the number need to have a value of 2^x
        BigInteger polynomialDegree = new BigInteger("16");
        // TODO explain this!!
        // The minimum bit size of the primes
        // TODO write about why its smart to have big primes
        int bitPrimeSize = 500;
        // Miller-Rabin is an algorithm to test if a number is prime
        // having a high number of miller rabin iterations makes it less to get false positives
        // Uses the fermants little theorem and that the only square roots of 1 modulo n are 1 and −1.
        // https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
        // 1024 safe prime iterations is 40
        int millerRabinIterations = 30;

        //TODO explain why I use these exponent values and why they have to be to the power of two
        BigInteger moduloBig = BigInteger.TWO.pow(2000);
        BigInteger moduloSmall = BigInteger.TWO.pow(1200);
        BigInteger scalingFactor = BigInteger.TWO.pow(40);

        // How many decimals you want to keep for each BigDecimal calculation
        MathContext mc = new MathContext(100);

        // TODO explain why we use parameters
        Parameters params = new Parameters(polynomialDegree,moduloBig, moduloSmall, scalingFactor, bitPrimeSize,
                 millerRabinIterations, mc);

        KeyGeneratorCKKS keyGen = new KeyGeneratorCKKS(params);
        SecretKey secretKey = keyGen.getSecretKey();
        PublicKey publicKey = keyGen.getPublicKey();
        PublicKey relinearizationKey = keyGen.getRelinearizationKey();

        // This is just for testing the input will be handled by the GUI
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
        EncodedText val0Encoded = encoder.encode(values0, scalingFactor);
        EncodedText val1Encoded = encoder.encode(values1, scalingFactor);

        Encryption encryper = new Encryption(params, publicKey, secretKey);
        Ciphertext val0Cipher = encryper.encrypt(val0Encoded);
        Ciphertext val1Cipher = encryper.encrypt(val1Encoded);

        Evaluation evaluation = new Evaluation(params);
        Ciphertext val0Val1 = evaluation.multiplyCiphertext(val0Cipher, val1Cipher, relinearizationKey);

        Decryptor decryptor = new Decryptor(params, secretKey);
        EncodedText val0Val1Encoded = decryptor.decrypt(val0Val1);
        ArrayList<Complex> val0Val1Decoded = encoder.decode(val0Val1Encoded);

        System.out.println(values0);
        System.out.println(values1);
        //ArrayList<Complex> val0AddVal1 = evaluation.multiplicationPlaintext(values0,values1);
        // ArrayList<Complex> val0AddVal1 = evaluation.di;

        //Ciphertext Val0subVal1 = evaluation.subtractionCiphertext(val0Cipher, val1Cipher);
        //EncodedText Val0subVal1Encoded = decryptor.decrypt(Val0subVal1);

        System.out.println(val0Encoded);
        System.out.println(val1Encoded);
        //EncodedText a = evaluation.additionEncodedText(val0Encoded,val1Encoded);

        EncodedText a = evaluation.multiplyEncodedText(val0Encoded,val1Encoded);
        ArrayList<Complex> aDecoded = encoder.decode(a);

        ArrayList<Complex> bDecoded = evaluation.multiplicationPlaintext(values0,values1);

        // Super temporary
        // TODO implement it in gui that you can choose precision of result
        ArrayList<Complex> val0Val1DecodedRoundedA = new ArrayList<>();
        ArrayList<Complex> val0Val1DecodedRoundedB = new ArrayList<>();
        MathContext m = new MathContext(2);
        for (int i = 0; i < val0Val1Decoded.size(); i++) {
            val0Val1DecodedRoundedA.add(new Complex(aDecoded.get(i).real().round(m), aDecoded.get(i).imag().round(m)));
            val0Val1DecodedRoundedB.add(new Complex(bDecoded.get(i).real().round(m), bDecoded.get(i).imag().round(m)));
        }

        // TODO maybe I should call it message instead of plaintext?
        //[4.8400000000102378363 - 1.9120108007382472227E-12i, 4.8399999999782648704 + 6.5999999999516917752i, 9.0000000000287107034 - 2.7699349525023601149E-11i, -6.5999999999909436812 + 6.5999999999890232488i, 4.8399999999988511127 + 1.0576313537326018433E-11i, 4.8400000000013308178 + 6.6000000000409852753i, 9.0000000000898044078 - 1.4418420864109633241E-11i, -6.5999999998924296149 + 6.6000000000662016292i]
        System.out.println(val0Val1DecodedRoundedA);
        System.out.println(val0Val1DecodedRoundedB);
    }

}
