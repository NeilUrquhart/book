package book.ch11;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class Fact {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		BigDecimal bd = new BigDecimal(fatFactorial(442).divide(new BigInteger("2")));
		System.out.println(bd);
		NumberFormat formatter = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.ROOT));
	    String str = formatter.format(bd);
	    System.out.println(str);
	    
		
	}
	static BigInteger fatFactorial(int b) {
	    if (BigInteger.ONE.equals(BigInteger.valueOf(b))
	        || BigInteger.ZERO.equals(BigInteger.valueOf(b))) {
	            return BigInteger.ONE;
	    } else {
	        return BigInteger.valueOf(b).multiply(fatFactorial(b - 1));
	        }
	    }
}
