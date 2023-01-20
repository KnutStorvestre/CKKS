package modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Polynomial {

    private BigInteger polynomialDegree;
    private BigInteger[] coefficients;
    private int polynomialDegreeInt;

    public Polynomial(BigInteger polyDeg, BigInteger[] coeffs){
        polynomialDegree = polyDeg;
        coefficients = coeffs;
        polynomialDegreeInt = polynomialDegree.intValue();
    }

    public Polynomial multiplicationGaloidField(Polynomial poly, BigInteger coefficientModulus){
        BigInteger[] polyProduct = new BigInteger[poly.polynomialDegreeInt];
        Arrays.fill(polyProduct, BigInteger.ZERO);
        BigInteger[] polyCoefficients = poly.getCoefficients();

        int index;
        boolean isPos;
        BigInteger coefficient;
        for (int i = 0; i < 2 * polynomialDegreeInt - 1; i++) {
            index = i % polynomialDegreeInt;
            isPos = i < polynomialDegreeInt;
            coefficient = BigInteger.ZERO;
            for (int j = 0; j < polynomialDegreeInt; j++) {
                if (0 <= i - j){
                    if (i - j < polynomialDegreeInt){
                        coefficient = coefficient.add(coefficients[j].multiply(polyCoefficients[i-j]));
                    }
                }
            }

            if (isPos)
                polyProduct[index] = polyProduct[index].add(coefficient);
            else
                polyProduct[index] = polyProduct[index].subtract(coefficient);

            polyProduct[index] = polyProduct[index].mod(coefficientModulus);
        }

        return new Polynomial(polynomialDegree, polyProduct);
    }

    public Polynomial multiplicationCRT(Polynomial poly, ChineseRemainderTheorem crt){
        BigInteger[] crtPrimes = crt.getPrimes();
        int crtPrimesNum = crtPrimes.length;
        ArrayList<NumberTheoreticalTransform> ntts = crt.getNtts();

        Polynomial product;
        ArrayList<Polynomial> products = new ArrayList<>();
        for (int i = 0; i < crtPrimesNum; i++) {
            product = this.multiplicationNTTCRT(poly, crtPrimes[i], ntts.get(i));
            products.add(product);
        }

        ArrayList<BigInteger> values;
        BigInteger[] reconstructedValues = new BigInteger[polynomialDegreeInt];
        for (int i = 0; i < polynomialDegreeInt; i++) {
            values = new ArrayList<>();
            for (Polynomial tmpPoly : products) {
                values.add(tmpPoly.getCoefficients()[i]);
            }
            reconstructedValues[i] = crt.recover(values);
        }

        return new Polynomial(polynomialDegree, reconstructedValues).moduloSmall(crt.getModulo());
    }

    public Polynomial multiplicationNTTCRT(Polynomial poly, BigInteger prime, NumberTheoreticalTransform ntt){
        BigInteger[] thisForwardFTT = ntt.forwardFastFourierTransform(coefficients);
        BigInteger[] polyForwardFTT = ntt.forwardFastFourierTransform(poly.getCoefficients());

        BigInteger[] product = new BigInteger[polynomialDegreeInt];
        for (int i = 0; i < polynomialDegreeInt; i++) {
            product[i] = thisForwardFTT[i].multiply(polyForwardFTT[i]);
        }

        BigInteger[] fttInverse = ntt.fermatTheoreticalTransformInverse(product);

        return new Polynomial(polynomialDegree, fttInverse);
    }

    public Polynomial scalarModMultiplication(BigInteger scalar, BigInteger modulo){
        BigInteger[] scalarProductCoefficients = new BigInteger[polynomialDegreeInt];

        for (int i = 0; i < polynomialDegreeInt; i++) {
            scalarProductCoefficients[i] = (coefficients[i].multiply(scalar)).mod(modulo);
        }

        return new Polynomial(polynomialDegree, scalarProductCoefficients);
    }

    // Divides by scalar
    public Polynomial scalarDivide(BigInteger scalar){
        BigInteger[] coeffs = new BigInteger[coefficients.length];
        for (int i = 0; i < coefficients.length; i++) {
            coeffs[i] = coefficients[i].divide(scalar);
        }

        return new Polynomial(polynomialDegree, coeffs);
    }

    public Polynomial additionMod(Polynomial poly, BigInteger modulo){
        BigInteger[] sumCoefficients = new BigInteger[polynomialDegreeInt];
        Arrays.fill(sumCoefficients, new BigInteger("0"));

        BigInteger[] polyCoeffs = poly.getCoefficients();

        for (int i = 0; i < polynomialDegreeInt; i++) {
            sumCoefficients[i] = coefficients[i].add(polyCoeffs[i]).mod(modulo);
        }
        return new Polynomial(polynomialDegree, sumCoefficients);
    }

    public Polynomial subtractionMod(Polynomial poly, BigInteger modulo){
        BigInteger[] sumCoefficients = new BigInteger[polynomialDegreeInt];
        Arrays.fill(sumCoefficients, new BigInteger("0"));

        BigInteger[] polyCoeffs = poly.getCoefficients();

        for (int i = 0; i < polynomialDegreeInt; i++) {
            sumCoefficients[i] = coefficients[i].subtract(polyCoeffs[i]).mod(modulo);
        }
        return new Polynomial(polynomialDegree, sumCoefficients);
    }

    public Polynomial moduloSmall(BigInteger modulo){
        BigInteger[] modCoeffs =  new BigInteger[coefficients.length];
        BigInteger tmpVal;
        try {
            for (int i = 0; i < coefficients.length; i++) {
                modCoeffs[i] = coefficients[i].mod(modulo);
            }

            for (int i = 0; i < modCoeffs.length; i++) {
                if (modCoeffs[i].compareTo(modulo.divide(BigInteger.TWO)) > 0){
                    modCoeffs[i] = modCoeffs[i].subtract(modulo);
                }
            }
        }
        catch (Exception e){
            System.out.println("mod small failed!");
        }

        return new Polynomial(polynomialDegree, modCoeffs);
    }

    public BigInteger getPolynomialDegree() {
        return polynomialDegree;
    }

    public BigInteger[] getCoefficients() {
        return coefficients;
    }

    public int getPolynomialDegreeInt() {
        return polynomialDegreeInt;
    }

    @Override
    public String toString() {

        StringBuilder str = new StringBuilder();
        for (int i = polynomialDegreeInt-1; i >= 0; i--) {
            if (!coefficients[i].equals(BigInteger.ZERO)){
                if (!str.toString().equals("")){
                    str.append(" + ");
                }
                if (i == 0 || !coefficients[i].equals(BigInteger.ONE)){
                    str.append(coefficients[i].toString());
                }
                if (i != 0){
                    str.append("x");
                }
                if (i > 1){
                    str.append('^').append(Integer.toString(i));
                }
            }
        }

        return str.toString();
    }

}
