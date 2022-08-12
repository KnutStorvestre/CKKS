import keys.PublicKey;
import keys.SecretKey;
import modules.NumberTheory;
import modules.Polynomial;

import java.math.BigInteger;

public class KeyGenerator {

    private Parameters parameters;
    private SecretKey secretKey;
    private PublicKey publicKey;
    private PublicKey relinearizationKey;
    private NumberTheory numTheory;


    public KeyGenerator(Parameters params){
        parameters = params;
        numTheory = new NumberTheory();

        // TODO implement this!!
        // TODO implement secret key generator
        secretKey = secretKeyGenerator();
        publicKey = publicKeyGenerator();
        relinearizationKey = relinearizationKeyGenerator();
    }

    private SecretKey secretKeyGenerator(){
        // TODO explain what hamming-weight is and why we divide by 4
        int hammingWeight = parameters.getPolynomialDegree().divide(new BigInteger("4")).intValue();
        BigInteger[] secretKeyCoefficients = numTheory.hammingWeight(parameters.getPolynomialDegree().intValue(), hammingWeight);
        return new SecretKey(parameters.getPolynomialDegree(), new Polynomial(parameters.getPolynomialDegree(), secretKeyCoefficients));
    }

    private PublicKey publicKeyGenerator(){
        BigInteger bigModulo = parameters.getBigModulo();
        BigInteger[] randomVals = numTheory.getRandomPosVals(bigModulo, parameters.getPolynomialDegree().intValue());
        BigInteger[] randomError = numTheory.randomTriangle(parameters.getPolynomialDegree().intValue());

        Polynomial publicKeyPolynomial1 = new Polynomial(parameters.getPolynomialDegree(), randomVals);
        Polynomial errorPolynomial = new Polynomial(parameters.getPolynomialDegree(), randomError);

        Polynomial publicKeyPolynomial0 = publicKeyPolynomial1.multiplicationGaloidField(secretKey.getPoly(), bigModulo);
        publicKeyPolynomial0 = publicKeyPolynomial0.scalarModMultiplication(new BigInteger("-1"), bigModulo);
        publicKeyPolynomial0 = publicKeyPolynomial0.additionMod(errorPolynomial, bigModulo);

        return new PublicKey(parameters.getPolynomialDegree(), publicKeyPolynomial0, publicKeyPolynomial1);
    }

    private PublicKey relinearizationKeyGenerator(){
        Polynomial squaredSecretKeyPolynomial = secretKey.getPoly().multiplicationGaloidField(secretKey.getPoly(),
                parameters.getBigModulo());
        PublicKey relinearizationKey = swithchingKeyGenerator(squaredSecretKeyPolynomial);
        return relinearizationKey;
    }

    private PublicKey swithchingKeyGenerator(Polynomial squaredSecretKey){
        BigInteger bigModSquared = parameters.getBigModulo().multiply(parameters.getBigModulo());

        BigInteger[] switchingKeyCoefficients = numTheory.getRandomPosVals(bigModSquared, parameters.getPolynomialDegree().intValue());
        BigInteger[] switchingKeyErrorCoefficients = numTheory.randomTriangle(parameters.getPolynomialDegree().intValue());


        Polynomial switchingKeyPolynomial = new Polynomial(parameters.getPolynomialDegree(), switchingKeyCoefficients);

        Polynomial switchingKeyErrorPolynomial = new Polynomial(parameters.getPolynomialDegree(),
                switchingKeyErrorCoefficients);

        Polynomial switchingKeyPolynomial0;
        switchingKeyPolynomial0 = switchingKeyPolynomial.multiplicationGaloidField(secretKey.getPoly(),
                bigModSquared);

        switchingKeyPolynomial0 = switchingKeyPolynomial0.scalarModMultiplication(new BigInteger("-1"),
                bigModSquared);
        switchingKeyPolynomial0 = switchingKeyPolynomial0.additionMod(switchingKeyErrorPolynomial, bigModSquared);
        Polynomial tmp = squaredSecretKey.scalarModMultiplication(parameters.getBigModulo(), bigModSquared);
        switchingKeyPolynomial0 = switchingKeyPolynomial0.additionMod(tmp, bigModSquared);

        return new PublicKey(parameters.getPolynomialDegree(), switchingKeyPolynomial0, switchingKeyPolynomial);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PublicKey getRelinearizationKey() {
        return relinearizationKey;
    }
}
