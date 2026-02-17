package ejercicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Ejercicio1 {

	// Del enunciado:
	public static Map<Integer,List<String>> solucionFuncional(Integer varA, String varB, Integer varC, String varD, Integer varE) {
		UnaryOperator<EnteroCadena> nx = elem -> {
			return EnteroCadena.of(elem.a()+2,
				elem.a()%3==0?
					elem.s()+elem.a().toString():
					elem.s().substring(elem.a()%elem.s().length()));
		};					
		
		//EnteroCadena.of(varA,varB) - Seed -> primer elemento potencial
		//elem -> elem.a() < varC - HasNext -> se evalua antes de emitir cada elemento
		// nx - Funcion ejecutada al anterior valor para producir el proximo valor
		
		return Stream.iterate(EnteroCadena.of(varA,varB), elem -> elem.a() < varC, nx)
					.map(elem -> elem.s() + varD)
					.filter(nom -> nom.length() < varE)
					.collect(Collectors.groupingBy(String::length));
	}
	
	public static Map<Integer,List<String>> solucionIterativa(Integer varA, String varB, Integer varC, String varD, Integer varE) {
		Map<Integer, List<String>> res = new HashMap<>();
		
		EnteroCadena x = EnteroCadena.of(varA, varB); // Seed -> primer elemento potencial
		
		while(x.a() < varC) {
			String s = x.s() + varD;
			int len = s.length();
			
			if (len < varE) {
				if (res.containsKey(len)) {
					res.get(len).add(s);
				} else {
					List<String> acum = new ArrayList<>();
					acum.add(s);
					res.put(len, acum);
				}
				
			}
			
			x = EnteroCadena.of(x.a()+2,
					x.a()%3==0?
							x.s()+x.a().toString():
							x.s().substring(x.a()%x.s().length()));
 			
		}
		
		
		
		return res;
	}
	
	public static Map<Integer,List<String>> solucionRecursivaFinal(Integer varA, String varB, Integer varC, String varD, Integer varE) {
		Map<Integer, List<String>> res = new HashMap<>();
		EnteroCadena cadena = EnteroCadena.of(varA, varB);
		return solucionRecursivaFinalAux(cadena, varC, varD, varE, res);		
	}
	
	
	private static Map<Integer,List<String>> solucionRecursivaFinalAux(EnteroCadena cadena, Integer varC, String varD, Integer varE, Map<Integer, List<String>> mapa) {
		if(cadena.a() < varC) {
			String s = cadena.s() + varD;
			int len = s.length();
			
			if (len < varE) {
				if (mapa.containsKey(len)) {
					mapa.get(len).add(s);
				} else {
					List<String> acum = new ArrayList<>();
					acum.add(s);
					mapa.put(len, acum);
				}
				
			}
			cadena = EnteroCadena.of(cadena.a()+2,
					cadena.a()%3==0?
							cadena.s()+cadena.a().toString():
							cadena.s().substring(cadena.a()%cadena.s().length()));
			
			return solucionRecursivaFinalAux(cadena, varC, varD, varE, mapa);
		}
		
		return mapa;
		
	}
}
