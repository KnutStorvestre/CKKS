package modules;

import java.math.BigInteger;

public class NumberTheoreticalTransform {

    private BigInteger polynomialDegree;
    private BigInteger modulo;
    private BigInteger[] rootsOfUnity;
    private BigInteger rootOfUnity;
    private NumberTheory numTheory;
    private BigInteger rootOfUnityInverse;
    private BigInteger[] rootsOfUnityInverse;
    private int[] reversedBits;


    public NumberTheoreticalTransform(BigInteger polyDeg, BigInteger mod, NumberTheory numberTheory){
        polynomialDegree = polyDeg;
        modulo = mod;
        rootOfUnity = numberTheory.rootOfUnity(BigInteger.TWO.multiply(polynomialDegree), modulo);
        numTheory = numberTheory;
        
        int polynomialDegreeInt = polynomialDegree.intValue();
        
        rootsOfUnity = new BigInteger[polynomialDegreeInt];
        rootsOfUnity[0] = BigInteger.ONE;
        for (int i = 1; i < polynomialDegreeInt; i++) {
            rootsOfUnity[i] = rootsOfUnity[i-1].multiply(rootOfUnity).mod(modulo);
        }

        rootOfUnityInverse = numberTheory.moduloInverse(rootOfUnity, modulo);
        rootsOfUnityInverse = new BigInteger[polynomialDegreeInt];
        rootsOfUnityInverse[0] = BigInteger.ONE;
        for (int i = 1; i < polynomialDegreeInt; i++) {
            rootsOfUnityInverse[i] = (rootsOfUnityInverse[i-1].multiply(rootOfUnityInverse)).mod(modulo);
        }

        reversedBits = new int[polynomialDegreeInt];
        int length = (int) (Math.log(polynomialDegreeInt) / Math.log(2));
        for (int i = 0; i < polynomialDegreeInt; i++) {
            reversedBits[i] = reverseBits(i, length) % polynomialDegreeInt;
        }
    }

    public BigInteger[] numberTheoreticalTransform(BigInteger[] coefficients, BigInteger[] rootsOfUnity){
        int totCoefficients = coefficients.length;

        BigInteger[] coeffsBitReversed = new BigInteger[totCoefficients];

        int width = (int) (Math.log(totCoefficients) / Math.log(2));
        for (int i = 0; i < coeffsBitReversed.length; i++) {
            coeffsBitReversed[i] = coefficients[reverseBits(i,width)];
        }

        int idxEven;
        int idxOdd;
        int rootsOfUnityIndex;
        BigInteger factor;
        BigInteger butterflyPlus;
        BigInteger butterflyMinus;
        for (int i = 1; i < width+1; i++) {
            for (int j = 0; j < totCoefficients; j += 1 << i) {
                for (int k = 0; k < (1 << (i - 1)); k++) {
                    idxEven = j + k;
                    idxOdd = j + k + (1 << (i-1));

                    rootsOfUnityIndex = (k << (1+width-i));
                    factor = (rootsOfUnity[rootsOfUnityIndex].multiply(coeffsBitReversed[idxOdd]).mod(modulo));
                    butterflyPlus = (coeffsBitReversed[idxEven].add(factor).mod(modulo));
                    butterflyMinus = (coeffsBitReversed[idxEven].subtract(factor).mod(modulo));
                    coeffsBitReversed[idxEven] = butterflyPlus;
                    coeffsBitReversed[idxOdd] = butterflyMinus;
                }
            }
        }

        return coeffsBitReversed;
    }

    public BigInteger[] fermatTheoreticalTransformInverse(BigInteger[] coefficients){
        int totCoefficients = coefficients.length;

        BigInteger[] toScaleDown = numberTheoreticalTransform(coefficients, rootsOfUnityInverse);
        BigInteger polynomialDegreeInverse = numTheory.moduloInverse(polynomialDegree, modulo);
        BigInteger[] result = new BigInteger[totCoefficients];
        for (int i = 0; i < totCoefficients; i++) {
            result[i] = toScaleDown[i].multiply(rootsOfUnityInverse[i].multiply(polynomialDegreeInverse)).mod(modulo);
        }

        return result;
    }

    public BigInteger[] forwardFastFourierTransform(BigInteger[] coefficients){
        int totCoefficients = coefficients.length;

        BigInteger[] inputFTT = new BigInteger[totCoefficients];

        for (int i = 0; i < totCoefficients; i++) {
            inputFTT[i] = coefficients[i].multiply(rootsOfUnity[i]).mod(modulo);
        }

        BigInteger[] forwardFastFourierTransformValues = numberTheoreticalTransform(inputFTT, rootsOfUnity);

        return forwardFastFourierTransformValues;
    }

    private int reverseBits(int value, int width){
        String binString =  Integer.toBinaryString(value);
        String forFormat = "%" + width + "s";
        String binaryString = String.format(forFormat, binString).replaceAll(" ", "0");
        String reversedBits = new StringBuilder(binaryString).reverse().toString();
        return Integer.parseInt(reversedBits,2);
    }

}
