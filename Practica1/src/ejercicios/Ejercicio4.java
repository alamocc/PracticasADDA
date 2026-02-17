package ejercicios;

import java.math.BigInteger;

public class Ejercicio4 {
	
	public static Double funcRecDouble(Integer a) {
		Double res = 0.0;
		
		if (a < 10) {
			res = 5.0;
		} else {
			res = Math.sqrt(3*a) + funcRecDouble(a - 2);
		}
		return res;
	}
	
	public static BigInteger funcRecBig(Integer a) {
		BigInteger res = BigInteger.ZERO;
		
		if (a < 10) {
			res = BigInteger.valueOf(5L);
		} else {
			res = BigInteger.valueOf((long) Math.sqrt(3 * a)).multiply(funcRecBig(a - 2));
		}
		return res;
	}
	
	public static Double funcItDouble(Integer a) {
		
		Double res = 5.0;
		
		if (a < 10) {
			return res;
		}
		
		for (int i = a; i >= 10; i-=2) {
			res = Math.sqrt(3 * a);
			a -= 2;
		}
		return res;
	}
	
	public static BigInteger funcItBig(Integer a) {
		
		BigInteger res = BigInteger.valueOf(5);
		
		if (a < 10) {
			return res;
		}
		
		for (int i = a; i >= 10; i-=2) {
			res = BigInteger.valueOf((long) Math.sqrt(3 * a));
			a -= 2;
		}
		
		return res;
	}

}