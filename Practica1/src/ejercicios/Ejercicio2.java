package ejercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import us.lsi.common.List2;

public class Ejercicio2 {

	public static List<Integer> f_RNF(Integer a, Integer b) {

		if (a < 2 || b < 2) {
			List<Integer> res = new ArrayList<>();
			res.add(a * b);
			return res;
		} else if (a > b) {
			List<Integer> res = f_RNF(a % b, b - 1);
			res.add(a);
			return res;
		} else {
			List<Integer> res = f_RNF(a - 2, b / 2);
			res.add(b);
			return res;
		}
	}

	public static List<Integer> f_it(Integer a, Integer b) {

		List<Integer> res = new ArrayList<>();

		while (!(a < 2 || b < 2)) {
			if (a > b) {
				res.add(0, a);
				a = a % b;
				b--;
			} else {
				res.add(0, b);
				a = a - 2;
				b = b / 2;
			}
		}
		res.add(0, a * b);
		return res;
	}

	public static List<Integer> f_RF(Integer a, Integer b) {
		List<Integer> res = new ArrayList<>();
		return f_RF_Aux(a, b, res);
	}

	private static List<Integer> f_RF_Aux(Integer a, Integer b, List<Integer> lista) {

		if (a < 2 || b < 2) {
			lista.add(0, a * b);
			return lista;
		} else if (a > b) {
			lista.add(0, a);
			lista = f_RF_Aux(a % b, b - 1, lista);
		} else {
			lista.add(0, b);
			lista = f_RF_Aux(a - 2, b / 2, lista);
		}
		return lista;
	}
	
	private static record Tupla(List<Integer> ac, Integer a, Integer b) {
		
		public static Tupla of(List<Integer> ba, Integer a, Integer b) {
			return new Tupla(ba, a, b);
		}
		
		public static Tupla first(Integer a, Integer b) {
			return of(List2.empty(), a, b);
		}
		
		public Tupla next() {
			if(a>b) {
				return of(List2.addFirst(ac, a), a%b, b-1);
			}
			return of(List2.addFirst(ac, b), a-2, b/2);
		
		}

	}

	public static List<Integer> f_funcional(Integer a, Integer b) {
		 Tupla last = Stream.iterate(Tupla.first(a, b), e -> e.next())
				 .filter(e -> e.a() < 2 || e.b() < 2)
				 .findFirst().get();
		 
		 return List2.addFirst(last.ac(), last.a()*last.b());
	}
}
