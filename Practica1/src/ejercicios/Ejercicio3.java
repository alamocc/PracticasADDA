package ejercicios;

import java.util.HashMap;
import java.util.Map;

import us.lsi.common.Trio;

public class Ejercicio3 {

	public static Long iterativo(Integer a, Integer b, Integer c) {

		// Uso memoria
		Map<Trio<Integer, Integer, Integer>, Long> mem = new HashMap<>();
		Long res = null;

		// Itero por cada uno de los inputs
		for (int i = 0; i <= a; i++) {
			for (int j = 0; j <= b; j++) {
				for (int k = 0; k <= c; k++) {
					if (i < 3 || j < 3 || k < 3) {
						res = i + j * j + 2L * k;
					} else if (i % j == 0 && i >= 0) {
						res = mem.get(Trio.of(i - 1, j / 2, k / 2)) + mem.get(Trio.of(i - 3, j / 3, k / 3));
					} else {
						res = mem.get(Trio.of(i / 3, j - 3, k - 3)) + mem.get(Trio.of(i / 2, j - 2, k - 2));
					}
					mem.put(Trio.of(i, j, k), res);
				}
			}
		}
		return mem.get(Trio.of(a, b, c));
	}

	public static Long recursivo_sin_memoria(Integer a, Integer b, Integer c) {
		Long res = null;

		if (a < 3 || b < 3 || c < 3) {
			res = a + b * b + 2L * c;
		} else if (a % b == 0 && a >= 0) {
			res = recursivo_sin_memoria(a - 1, b / 2, c / 2) + recursivo_sin_memoria(a - 3, b / 3, c / 3);
		} else {
			res = recursivo_sin_memoria(a / 3, b - 3, c - 3) + recursivo_sin_memoria(a / 2, b - 2, c - 2);
		}
		return res;
	}

	public static Long recursivo_con_memoria(Integer a, Integer b, Integer c) {
		Map<Trio<Integer, Integer, Integer>, Long> res = new HashMap<>();
		return recursivo_con_memoria_aux(a, b, c, res);
	}

	public static Long recursivo_con_memoria_aux(Integer a, Integer b, Integer c,
			Map<Trio<Integer, Integer, Integer>, Long> mem) {

		// Consulto memoria
		Long res = mem.get(Trio.of(a, b, c));

		if (!(res == null)) {
			return res;
		}

		if (a < 3 || b < 3 || c < 3) {
			res = a + b * b + 2L * c;
		} else if (a % b == 0 && a >= 0) {
			res = recursivo_con_memoria_aux(a - 1, b / 2, c / 2, mem)
					+ recursivo_con_memoria_aux(a - 3, b / 3, c / 3, mem);
			mem.put(Trio.of(a, b, c), res);
		} else {
			res = recursivo_con_memoria_aux(a / 3, b - 3, c - 3, mem)
					+ recursivo_con_memoria_aux(a / 2, b - 2, c - 2, mem);
			mem.put(Trio.of(a, b, c), res);
		}
		return res;

	}
}
