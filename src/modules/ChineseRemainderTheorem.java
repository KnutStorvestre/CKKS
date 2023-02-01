package modules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class ChineseRemainderTheorem {

    private BigInteger polynomialDegree;
    private int primeNum;
    private int primeBitSize;
    private BigInteger[] primes;
    private Random ran;
    private NumberTheory numTheory;
    private ArrayList<NumberTheoreticalTransform> ntts;
    private BigInteger modulo;
    private BigInteger[] crtValues;
    private BigInteger[] crtInverseValues;
    public ChineseRemainderTheorem(BigInteger polyDeg, int totalPrimes, int primeBitSizeNum, int millerRabinIterationsNum){
        polynomialDegree = polyDeg;
        primeNum = totalPrimes;
        primeBitSize = primeBitSizeNum;
        ran = new Random();
        numTheory = new NumberTheory();

        primes = generatePrimes(primeNum, primeBitSize, polynomialDegree, millerRabinIterationsNum);
        ntts = new ArrayList<>(totalPrimes);

        NumberTheoreticalTransform ntt;
        for (BigInteger tmpPrime : primes){
            ntt = new NumberTheoreticalTransform(polynomialDegree, tmpPrime, numTheory);
            ntts.add(ntt);
        }

        modulo = BigInteger.ONE;
        for (BigInteger prime : primes){
             modulo = modulo.multiply(prime);
        }

        // creates crt inverse values and crt values
        // crtVal = mod // prime[i]
        // crtInvVal = crtVal^(mod-2) % mod
        int numPrimes = primes.length;
        crtValues = new BigInteger[numPrimes];
        crtInverseValues = new BigInteger[numPrimes];
        for (int i = 0; i < numPrimes; i++) {
            crtValues[i] = modulo.divide(primes[i]);
            crtInverseValues[i] = numTheory.moduloInverse(crtValues[i], primes[i]);
        }
    }

    // Each of the primes generated are 1 (mod 2*polyDeg) = prime
    public BigInteger[] generatePrimes(int totPrimes, int primeBitSize, BigInteger polynomialDegree, int millerRabinIterations){
        BigInteger mod = BigInteger.TWO.multiply(polynomialDegree);
        BigInteger[] primes = new BigInteger[totPrimes];
        BigInteger posPrime;

        // This is the smalles possible prime that has more bits that primeBitSize
        posPrime = BigInteger.TWO.pow(primeBitSize).add(BigInteger.ONE);

        for (int i = 0; i < totPrimes;){
            if (millerRabinTest(posPrime, millerRabinIterations)){
                primes[i] = posPrime;
                i++;
            }

            posPrime = posPrime.add(mod);
        }

        return primes;
    }

    /**
     * Return true if it passes the miller rabin test
     * @param posPrime
     * @param tests
     * @return
     */
    public boolean millerRabinTest(BigInteger posPrime, int tests) {
        if (posPrime.equals(BigInteger.TWO)){
            return true;
        }
        if (posPrime.mod(BigInteger.TWO).intValue() == 2){
            return false;
        }

        int s = 0;
        BigInteger d = posPrime.subtract(BigInteger.ONE);
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s++;
            d = d.divide(BigInteger.TWO);
        }
        for (int i = 0; i < tests; i++) {
            //BigInteger a = randomBigIntegerMoreThanTwo(posPrime.subtract(oneBigInt));
            BigInteger a = randomBigIntegerMoreThanTwoLessThanPosPrime(posPrime);
            BigInteger x = a.modPow(d, posPrime);
            if (x.equals(BigInteger.ONE) || x.equals(posPrime.subtract(BigInteger.ONE)))
                continue;
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(BigInteger.TWO, posPrime);
                if (x.equals(BigInteger.ONE))
                    return false;
                if (x.equals(posPrime.subtract(BigInteger.ONE)))
                    break;
            }
            if (r == s)
                return false;
        }
        return true;
    }

    public BigInteger randomBigIntegerMoreThanTwoLessThanPosPrime(BigInteger posPrime){
        BigInteger minLimit = new BigInteger("2");
        BigInteger bigInteger = posPrime.subtract(minLimit);
        int len = posPrime.bitLength();
        BigInteger res = new BigInteger(len, ran);
        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);
        return res;
    }

    public BigInteger recover(ArrayList<BigInteger> vals){
        BigInteger repVal = BigInteger.ZERO;
        //TODO this should have a better name
        BigInteger tmpVal;
        for (int i = 0; i < vals.size(); i++) {
            tmpVal = vals.get(i).multiply(crtInverseValues[i]).mod(primes[i]);
            tmpVal = (tmpVal.multiply(crtValues[i])).mod(modulo);
            repVal = repVal.add(tmpVal).mod(modulo);
        }
        return repVal;
    }

    public BigInteger getModulo() {
        return modulo;
    }

    public BigInteger[] getPrimes() {
        return primes;
    }

    public ArrayList<NumberTheoreticalTransform> getNtts() {
        return ntts;
    }

}
