package CKKS;

import data.EncodedText;
import modules.Complex;
import modules.FastFourierTransform;
import modules.Polynomial;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

public class Encoder {

    private BigInteger polyDegree;
    private MathContext mc;
    private FastFourierTransform fft;

    public Encoder(Parameters params, MathContext mathContext){
        polyDegree = params.getPolynomialDegree();
        mc = mathContext;
        fft = new FastFourierTransform(polyDegree.multiply(BigInteger.TWO).intValue(), mc);
    }

    public EncodedText encode(ArrayList<Complex> values, BigInteger scaling){
        int totVals = values.size();
        int totEncodedVals = totVals<<1;

        ArrayList<Complex> embeddingInverseValues = fft.embeddingInverse(values);

        BigInteger[] msgReal = new BigInteger[totEncodedVals/2];
        BigInteger[] msgImag = new BigInteger[totEncodedVals/2];
        BigDecimal scalingBD = new BigDecimal(scaling);
        BigDecimal half = new BigDecimal("0.5");
        for (int i = 0; i < totVals; i++) {
            msgReal[i] = embeddingInverseValues.get(i).real().multiply(scalingBD).add(half).toBigInteger();
            msgImag[i] = embeddingInverseValues.get(i).imag().multiply(scalingBD).add(half).toBigInteger();
        }

        // Merges list of real and imaginary numbers
        BigInteger[] message = Stream.concat(Arrays.stream(msgReal), Arrays.stream(msgImag))
                .toArray(BigInteger[]::new);

        return new EncodedText(new Polynomial(BigInteger.valueOf(message.length), message),scaling);
    }

    public ArrayList<Complex> decode(EncodedText text){
        BigInteger[] txtCoefficients = text.getPolynomial().getCoefficients();
        BigDecimal scalingFactorBD = new BigDecimal(text.getScalingFactor());
        int encodedLength = txtCoefficients.length;
        int totVals = encodedLength >> 1;

        BigDecimal[] txtCoeffBD = new BigDecimal[txtCoefficients.length];
        for (int i = 0; i < txtCoefficients.length; i++) {
            txtCoeffBD[i] = new BigDecimal(txtCoefficients[i]);
        }

        ArrayList<Complex> vals = new ArrayList<>();
        for (int i = 0; i < totVals; i++) {
            vals.add(new Complex(txtCoeffBD[i].divide(scalingFactorBD,mc),
                    txtCoeffBD[i+totVals].divide(scalingFactorBD,mc)));
        }

        return fft.embedding(vals);
    }

}
