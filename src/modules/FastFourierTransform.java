package modules;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;

public class FastFourierTransform {

    private int indices;
    private ArrayList<Complex> rootsOfUnity;
    private ArrayList<Complex> rootsOfUnityInverse;
    private int[] reversedBits;
    private int[] rotationGroup;
    private final static BigDecimal PI = new BigDecimal("3.141592653589793");
    private MathContext mc;

    public FastFourierTransform(int numIndices, MathContext mathContext){
        indices = numIndices;
        rootsOfUnity = new ArrayList<>(numIndices);
        rootsOfUnityInverse = new ArrayList<>(numIndices);
        mc = mathContext;

        // Roots of unity and roots of unity inverse
        BigDecimal angle;
        for (int i = 0; i < numIndices; i++) {
            //angle = 2*Math.PI * i / numIndices;
            angle = (((new BigDecimal("2")).multiply(PI)).multiply(new BigDecimal(i))).divide(new BigDecimal(numIndices),mc);
            BigDecimal negativeOneBD = new BigDecimal("-1");
            rootsOfUnity.add(i, new Complex(BigDecimalMath.cos(angle, mc), BigDecimalMath.sin(angle, mc)));
            rootsOfUnityInverse.add(i, new Complex(BigDecimalMath.cos(angle.multiply(negativeOneBD), mc),
                    BigDecimalMath.sin(angle.multiply(negativeOneBD), mc)));
        }

        // Total bits to be reversed and reversed bits
        int totReversedBits = numIndices/4;
        int width = (int)(Math.log(totReversedBits)/Math.log(2));
        reversedBits = new int[totReversedBits];
        for (int i = 0; i < totReversedBits; i++) {
            reversedBits[i] = reversedBits(i, width);
        }

        rotationGroup = new int[totReversedBits];
        rotationGroup[0] = 1;
        for (int i = 1; i < totReversedBits; i++) {
            rotationGroup[i] = (5*rotationGroup[i-1] % numIndices);
        }
    }


    public ArrayList<Complex> embedding(ArrayList<Complex> values){
        int totCoefficients = values.size();
        ArrayList<Complex> valuesReversed = new ArrayList<>(totCoefficients);
        for (int i = 0; i < totCoefficients; i++) {
            valuesReversed.add(i,new Complex(values.get(i).real(),values.get(i).imag()));
        }

        int log2TotCoeffs = (int) (Math.log(totCoefficients) / Math.log(2));
        for (int i = 0; i < values.size(); i++) {
            int idxDeepCopy = reversedBits(i, log2TotCoeffs);
            valuesReversed.set(i,  values.get(idxDeepCopy));
        }

        int indexMod;
        int space;
        int idxEven;
        int idxOdd;
        int rootOfUnityIndex;
        Complex factor;
        Complex butterflyPluss;
        Complex butterflyMinus;
        for (int i = 1; i < log2TotCoeffs + 1; i++) {
            indexMod = 1 << (i + 2);
            space = (int) (indices / indexMod);
            for (int j = 0; j < totCoefficients; j += 1 << i) {
                for (int k = 0; k < (1 << (i - 1)); k++) {
                    idxEven = j + k;
                    idxOdd = j + k + (1 << (i-1));
                    rootOfUnityIndex = (rotationGroup[k] % indexMod) * space;

                    factor = rootsOfUnity.get(rootOfUnityIndex).times(valuesReversed.get(idxOdd));

                    butterflyPluss = valuesReversed.get(idxEven).plus(factor);
                    butterflyMinus = valuesReversed.get(idxEven).minus(factor);

                    valuesReversed.set(idxEven, butterflyPluss);
                    valuesReversed.set(idxOdd, butterflyMinus);
                }
            }
        }
        return valuesReversed;
    }

    public ArrayList<Complex> embeddingInverse(ArrayList<Complex> values){
        int totCoefficients = values.size();

        ArrayList<Complex> valuesDeepCopy = new ArrayList<>(totCoefficients);
        for (int i = 0; i < totCoefficients; i++) {
            valuesDeepCopy.add(i,new Complex(values.get(i).real(),values.get(i).imag()));
        }

        double v = Math.log(totCoefficients) / Math.log(2);
        int logTotCoefficients = (int) v;

        int indexMod;
        int space;
        int idxEven;
        int idxOdd;
        int rootOfUnityIndex;
        Complex butterflyPluss;
        Complex butterflyMinus;
        for (int i = logTotCoefficients; i > 0; i--) {
            indexMod = (int) Math.pow(2,(i+2));
            space = (int) (indices / indexMod);
            for (int j = 0; j < totCoefficients; j += 1 << i) {
                for (int k = 0; k < (1 << (i - 1)); k++) {
                    idxEven = j + k;
                    idxOdd = j + k + (1 << (i-1));
                    rootOfUnityIndex = (rotationGroup[k] % indexMod) * space;

                    butterflyPluss = valuesDeepCopy.get(idxEven).plus(valuesDeepCopy.get(idxOdd));
                    butterflyMinus = valuesDeepCopy.get(idxEven).minus(valuesDeepCopy.get(idxOdd));
                    butterflyMinus = butterflyMinus.times(rootsOfUnityInverse.get(rootOfUnityIndex));

                    valuesDeepCopy.set(idxEven, butterflyPluss);
                    valuesDeepCopy.set(idxOdd, butterflyMinus);
                }
            }
        }

        ArrayList<Complex> scalingDownCoefficients = new ArrayList<>(totCoefficients);
        for (int i = 0; i < totCoefficients; i++) {
            scalingDownCoefficients.add(new Complex(BigDecimal.ZERO,BigDecimal.ZERO));
        }

        for (int i = 0; i < valuesDeepCopy.size(); i++) {
            int idxDeepCopy = reversedBits(i,(int) (Math.log(totCoefficients) / Math.log(2)));
            scalingDownCoefficients.set(i,  valuesDeepCopy.get(idxDeepCopy));
        }


        BigDecimal totCoefficientsBD = new BigDecimal(totCoefficients);
        BigDecimal a;
        BigDecimal b;
        for (int i = 0; i < totCoefficients; i++) {
            a = scalingDownCoefficients.get(i).real().divide(totCoefficientsBD,mc);
            b = scalingDownCoefficients.get(i).imag().divide(totCoefficientsBD,mc);
            scalingDownCoefficients.set(i, new Complex(a,b));
        }

        return scalingDownCoefficients;
    }

    private int reversedBits(int value, int numBits){
        String binVal = Integer.toBinaryString(value);
        String zerosBitLength = new String(new char[numBits-binVal.length()]).replace('\0', '0');
        String binValLength = zerosBitLength+binVal;
        String binValReversed = new StringBuilder(binValLength).reverse().toString();
        return Integer.parseInt(binValReversed,2);
    }

}
