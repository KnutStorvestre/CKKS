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
        BigInteger polynomialDegree = new BigInteger("8");
        // TODO explain this!!
        int taylorIterations = 6;
        // The minimum bit size of the primes
        // TODO write about why its smart to have big primes
        int bitPrimeSize = 60;
        // Miller-Rabin is an algorithm to test if a number is prime
        // having a high number of miller rabin iterations makes it less to get false positives
        // Uses the fermants little theorem and that the only square roots of 1 modulo n are 1 and −1.
        // https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
        // 1024 safe prime iterations is 40
        // Do we really need safe primes? Explain why not
        // https://stackoverflow.com/questions/6325576/how-many-iterations-of-rabin-miller-should-i-use-for-cryptographic-safe-primes#:~:text=Only%202%20iterations%2C%20assuming%202,80%20as%20a%20negligibly%20probability.
        int millerRabinIterations = 30;

        //TODO explain why I use these exponent values and why they have to be to the power of two
        BigInteger moduloBig = BigInteger.TWO.pow(1200);
        BigInteger moduloSmall = BigInteger.TWO.pow(600);
        BigInteger scalingFactor = BigInteger.TWO.pow(30);

        // How many decimals you want to keep for each BigDecimal calculation
        MathContext mc = new MathContext(100);

        // TODO explain why we use parameters
        Parameters params = new Parameters(polynomialDegree,moduloBig, moduloSmall, scalingFactor, bitPrimeSize,
                taylorIterations, millerRabinIterations, mc);

        KeyGenerator keyGen = new KeyGenerator(params);
        SecretKey secretKey = keyGen.getSecretKey();
        PublicKey publicKey = keyGen.getPublicKey();
        PublicKey relinearizationKey = keyGen.getRelinearizationKey();


        // This is just for testing the input will be handled by the GUI
        ArrayList<Complex> values0 = new ArrayList<>();
        ArrayList<Complex> values1 = new ArrayList<>();

        BigDecimal twoBD = new BigDecimal("2");
        BigDecimal threeBD = new BigDecimal("3");

        //[(2,0),(2,3),(3,0),(0,3)]
        values0.add(new Complex(twoBD, BigDecimal.ZERO));
        values0.add(new Complex(twoBD, threeBD));
        values0.add(new Complex(threeBD, BigDecimal.ZERO));
        values0.add(new Complex(BigDecimal.ZERO,threeBD));

        //[(2,0),(2,0),(3,0),(2,2)]
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
        Ciphertext val0Val1 = evaluation.multiply(val0Cipher, val1Cipher, relinearizationKey);

        Decryptor decryptor = new Decryptor(params, secretKey);
        EncodedText val0Val1Encoded = decryptor.decrypt(val0Val1);
        ArrayList<Complex> val0Val1Decoded = encoder.decode(val0Val1Encoded);

        /*
        EncodedText val0Val1Decoded = decryptor.decrypt(val0Val1);

        ArrayList<Complex> values0Decoded;
        ArrayList<Complex> values0DecodedRounded = new ArrayList<>();
        values0Decoded = encoder.decode(val0Val1Decoded);
        */

        // Super temporary
        // TODO implement it in gui that you can choose precision of result
        ArrayList<Complex> val0Val1DecodedRounded = new ArrayList<>();
        MathContext m = new MathContext(20);
        for (int i = 0; i < val0Val1Decoded.size(); i++) {
            val0Val1DecodedRounded.add(new Complex(val0Val1Decoded.get(i).real().round(m), val0Val1Decoded.get(i).imag().round(m)));
        }
        System.out.println(val0Val1DecodedRounded);
    }

}
