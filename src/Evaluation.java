

import data.Ciphertext;
import keys.PublicKey;
import modules.ChineseRemainderTheorem;
import modules.Polynomial;

import java.math.BigInteger;

public class Evaluation {

    private final BigInteger polyDeg;
    private final  BigInteger bigMod;
    private final BigInteger scalingFactor;
    private final ChineseRemainderTheorem crt;

    public Evaluation(Parameters params){
        polyDeg = params.getPolynomialDegree();
        bigMod = params.getBigModulo();
        scalingFactor = params.getScalingFactor();
        crt = params.getCrt();
    }

    public Ciphertext addition(Ciphertext cipher0, Ciphertext cipher1){
        BigInteger modulo = cipher1.getModulo();

        Polynomial c0p0 = cipher0.getPoly0();
        Polynomial c0p1 = cipher0.getPoly1();
        Polynomial c1p0 = cipher1.getPoly0();
        Polynomial c1p1 = cipher1.getPoly1();

        Polynomial p0New = c0p0.additionMod(c1p0, modulo).moduloSmall(modulo);
        Polynomial p1New = c0p1.additionMod(c1p1, modulo).moduloSmall(modulo);

        return new Ciphertext(p0New, p1New, cipher1.getScaling(), modulo);
    }

    public Ciphertext multiply(Ciphertext cipher0, Ciphertext cipher1, PublicKey relinearizationKey){
        BigInteger smallMod = cipher0.getModulo();

        Polynomial c0p0 = cipher0.getPoly0();
        Polynomial c0p1 = cipher0.getPoly1();
        Polynomial c1p0 = cipher1.getPoly0();
        Polynomial c1p1 = cipher1.getPoly1();

        Polynomial p0New = c0p0.multiplicationCRT(cipher1.getPoly0(), crt);
        p0New = p0New.moduloSmall(smallMod);

        Polynomial p1New = c0p0.multiplicationCRT(c1p1, crt);
        Polynomial tmp = c0p1.multiplicationCRT(c1p0, crt);
        p1New = p1New.additionMod(tmp, smallMod);
        p1New = p1New.moduloSmall(smallMod);

        Polynomial p2New = c0p1.multiplicationCRT(c1p1, crt);
        p2New = p2New.moduloSmall(smallMod);

        return relinearize(relinearizationKey, p0New, p1New, p2New, cipher0.getScaling().multiply(cipher1.getScaling()),
                smallMod);
    }

    //relinearize reduces 3d cipher text to 2d
    public Ciphertext relinearize(PublicKey relinearizationKey, Polynomial p0, Polynomial p1, Polynomial p2,
                                  BigInteger newScaling, BigInteger cipherModNew){
        Polynomial p0New = relinearizationKey.getP0().multiplicationCRT(p2, crt);
        p0New = p0New.moduloSmall(cipherModNew.multiply(bigMod));
        p0New = p0New.scalarDivide(bigMod);
        p0New = p0New.additionMod(p0, cipherModNew).moduloSmall(cipherModNew);

        Polynomial p1New = relinearizationKey.getP1().multiplicationCRT(p2, crt);
        p1New = p1New.moduloSmall(cipherModNew.multiply(bigMod));
        p1New = p1New.scalarDivide(bigMod);
        p1New = p1New.additionMod(p1,cipherModNew).moduloSmall(cipherModNew);

        return new Ciphertext(p0New, p1New, newScaling, cipherModNew);
    }

}
