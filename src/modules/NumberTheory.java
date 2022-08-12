package modules;

import java.math.BigInteger;
import java.util.Random;

public class NumberTheory {

    private Random ran;

    public NumberTheory(){
        ran = new Random();
    }

    // https://en.wikipedia.org/wiki/Modular_multiplicative_inverse
    // ax == 1 (mod m),
    public BigInteger moduloInverse(BigInteger val, BigInteger primeMod){
        // TODO explain what this is
        return val.modPow(primeMod.subtract(BigInteger.TWO), primeMod);
    }

    // TODO explain what a primitive element is
    // Explain what theorem I am using to find the primitive element
    public BigInteger findPrimitiveElementPrime(BigInteger prime){
        BigInteger[] primeFactors = new BigInteger[]{prime.divide(BigInteger.TWO), BigInteger.TWO};
        BigInteger possiblePrimitiveElement = BigInteger.ONE;

        while (possiblePrimitiveElement.compareTo(prime) < 0){
            possiblePrimitiveElement = possiblePrimitiveElement.add(BigInteger.ONE);
            if (!(possiblePrimitiveElement.modPow(primeFactors[0], prime).equals(BigInteger.ONE))){
                break;
            }
        }
        return possiblePrimitiveElement;
    }

    public BigInteger rootOfUnity(BigInteger order, BigInteger mod){
        BigInteger primitiveElement = findPrimitiveElementPrime(mod);

        BigInteger rootOfUnity = primitiveElement.modPow(mod.subtract(BigInteger.ONE).divide(order), mod);

        if (rootOfUnity.equals(BigInteger.ONE)){
            rootOfUnity(order, mod);
        }

        return rootOfUnity;
    }

    //TODO I can probably explain what hamming weight is
    public BigInteger[] hammingWeight(int totVals, int hammingWeight){
        BigInteger[] hammingVals = new BigInteger[totVals];
        int currentWeight = 0;

        int index;
        while (currentWeight < hammingWeight){
            index = ran.nextInt(totVals);
            if (hammingVals[index] == null){
                if (ran.nextInt(2) == 1){
                    hammingVals[index] = BigInteger.ONE;
                }
                else {
                    hammingVals[index] = new BigInteger("-1");
                }
                currentWeight++;
            }
        }
        for (int i = 0; i < totVals; i++) {
            if (hammingVals[i] == null) {
                hammingVals[i] = BigInteger.ZERO;
            }
        }

        return hammingVals;
    }

    // TODO explain why I use random triangle instead of uniform distribution
    public BigInteger[] randomTriangle(int numVals){
        BigInteger[] ranVals = new BigInteger[numVals];
        int ranVal;
        for (int i = 0; i < numVals; i++) {
            ranVal = ran.nextInt(4);
            if (ranVal == 3){
                ranVals[i] = BigInteger.ONE;
            } else if (ranVal == 2) {
                ranVals[i] = new BigInteger("-1");
            }
            else {
                ranVals[i] = BigInteger.ZERO;
            }
        }
        return ranVals;
    }

    // TODO test
    // Returns list of random values exclusive maxVal
    public BigInteger[] getRandomPosVals(BigInteger maxVal, int numVals){

        BigInteger[] ranVals = new BigInteger[numVals];

        int len = maxVal.bitLength();
        BigInteger ranVal = new BigInteger(len, ran);
        for (int i = 0; i < numVals; i++) {
            ranVal = new BigInteger(len, ran);
            if (ranVal.compareTo(maxVal) >= 0){
                ranVal = ranVal.mod(maxVal);
            }
            ranVals[i] = ranVal;
        }
        return ranVals;
    }

}
