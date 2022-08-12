import ch.obermuhlner.math.big.BigDecimalMath;
import modules.ChineseRemainderTheorem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class Parameters {

    private final BigInteger polynomialDegree;
    private final BigInteger bigModulo;
    private final BigInteger smallModulo;
    private final BigInteger scalingFactor;
    private final int primeBitSize;
    private final int taylorIterations;
    private final int millerRabinIterations;
    private final MathContext mathContext;
    private ChineseRemainderTheorem crt;

    public Parameters(BigInteger polyDeg, BigInteger bigMod, BigInteger smallMod, BigInteger scaling,
                      int primeBitSizeNum, int taylorIterationsNum, int millerRabinIterationsNum, MathContext mc){
        polynomialDegree = polyDeg;
        bigModulo = bigMod;
        smallModulo = smallMod;
        scalingFactor = scaling;
        primeBitSize = primeBitSizeNum;
        taylorIterations = taylorIterationsNum;
        millerRabinIterations = millerRabinIterationsNum;
        mathContext = mc;

        // TODO explain why I am using these values to calculate total primes that I am going to use!
        BigDecimal bigModLog2 = BigDecimalMath.log2(new BigDecimal(bigModulo), mathContext);
        BigDecimal polyDegLog2 = BigDecimalMath.log2(new BigDecimal(polynomialDegree), mc);
        int totPrimes = (new BigDecimal("2")).add(polyDegLog2).add((new BigDecimal("4").multiply(bigModLog2)).divide(new BigDecimal(primeBitSize), mc)).intValue();

        crt = new ChineseRemainderTheorem(polyDeg, totPrimes, primeBitSize, millerRabinIterations);
    }

    public BigInteger getBigModulo() {
        return bigModulo;
    }

    public BigInteger getSmallModulo() {
        return smallModulo;
    }

    public BigInteger getScalingFactor() {
        return scalingFactor;
    }

    public ChineseRemainderTheorem getCrt() {
        return crt;
    }

    public BigInteger getPolynomialDegree() {
        return polynomialDegree;
    }


}
