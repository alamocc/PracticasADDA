package ejercicios;

import java.math.BigInteger;

public class Ejercicio5 {

	public static Double ejercicio5ItDouble(Integer n) {
		Double res = 1.0;

		if (n <= 6) {
			return res;
		}

		for (int i = 7; i <= n; i++) {
			res = 1 + res * log2(i - 1);
		}
		return res;
	}

	public static Double ejercicio5RecDouble(Integer n) {

		Double res = 1.0;

		if (n <= 6) {
			return res;
		} else {
			res = 1 + ejercicio5RecDouble(n - 1) * log2(n - 1);
		}
		return res;
	}

	public static BigInteger ejercicio5RecBigInteger(Integer n) {

		BigInteger res = BigInteger.ONE;

		if (n <= 6) {
			return res;
		} else {
			BigInteger prev = ejercicio5RecBigInteger(n - 1);
			BigInteger logaritmo = BigInteger.valueOf(log2(n - 1));
			res = BigInteger.ONE.add(prev.multiply(logaritmo));
		}

		return res;
	}

	public static BigInteger ejercicio5ItBigInteger(Integer n) {
		BigInteger res = BigInteger.ONE;

		if (n <= 6) {
			return res;
		}

		for (int i = 7; i <= n; i++) {
			BigInteger logaritmo = BigInteger.valueOf(log2(i - 1));
			res = BigInteger.ONE.add(res.multiply(logaritmo));
		}

		return res;
	}

	public static int log2(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		return 31 - Integer.numberOfLeadingZeros(n);
	}

	public static void main(String[] args) { // COMPROBACION DE QUE ESTA TODO OK (todo da lo mismo)
		System.out.println(ejercicio5ItDouble(100));
		System.out.println(ejercicio5RecDouble(100));
		System.out.println(ejercicio5RecBigInteger(100));
		System.out.println(ejercicio5ItBigInteger(100));
	}
}