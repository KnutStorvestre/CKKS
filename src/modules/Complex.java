package modules;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class Complex {

    private BigDecimal x,y;
    private MathContext mc = new MathContext(100);


    /**
     * Stores Complex numbers in BigDecimal form.
     * Each calculation has a precision of 100 decimals
     * @param real
     * @param imag
     */
    public Complex(BigDecimal real, BigDecimal imag){
        x = real;
        y = imag;
    }

    public BigDecimal real(){
        return x;
    }

    public BigDecimal imag(){
        return y;
    }

    // TODO not tested
    public Complex times(Complex o){
        return new Complex(x.multiply(o.real()).subtract(y.multiply(o.imag())),x.multiply(o.imag()).add(y.multiply(o.real())));
    }

    public BigDecimal mod(){
        if ((!x.equals(BigDecimal.ZERO)) || (!y.equals(BigDecimal.ZERO))){
            return BigDecimalMath.sqrt((x.multiply(x)).add(y.multiply(y)), mc);
        }
        return BigDecimal.ZERO;
    }


    /**
     Modulus of this Complex number
     (the distance from the origin in polar coordinates).
     @return |z| where z is this Complex number.
     */
    /*
    public double mod() {
        if (x!=0 || y!=0) {
            return Math.sqrt(x*x+y*y);
        } else {
            return 0d;
        }
    }
    */

    public Complex plus(Complex o){
        return new Complex(x.add(o.real()), y.add(o.imag()));
    }

    public Complex minus(Complex o){
        return new Complex(x.subtract(o.real()), y.subtract(o.imag()));
    }

    public String toString() {
        if ((!x.equals(BigDecimal.ZERO)) && (y.compareTo(BigDecimal.ZERO)>0)) {
            return x+" + "+y+"i";
        }
        if ((!x.equals(BigDecimal.ZERO)) && (y.compareTo(BigDecimal.ZERO)<0)) {
            return x+" - "+(y.multiply(new BigDecimal("-1")))+"i";
        }
        if (y.equals(BigDecimal.ZERO)) {
            return String.valueOf(x);
        }
        if (x.equals(BigDecimal.ZERO)) {
            return y+"i";
        }
        // shouldn't get here (unless Inf or NaN)
        return x+" + i*"+y;
    }

}
