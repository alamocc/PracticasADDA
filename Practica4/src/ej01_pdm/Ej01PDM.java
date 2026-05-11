package ej01_pdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ej01_hg.DatosMulticonjunto;
import ej01_hg.Ej01HV;
import us.lsi.common.Pair;

public class Ej01PDM {
	
	private Map<Ej01HV, Pair<Integer, Double>> memoria;
	
	public List<Pair<Integer, Double>> search(Ej01HV inicial) {
		memoria = new HashMap<>();
		pd(inicial);
		return solucion(inicial);
	}
	
	private List<Pair<Integer, Double>> solucion(Ej01HV inicial) {
		// TODO Auto-generated method stub
		List<Pair<Integer, Double>> sol = new ArrayList<>();
		
		Pair<Integer, Double> sp = memoria.get(inicial); 
		sol.add(sp);
		Ej01HV problema = inicial.neighbors(sp.first()).get(0);
		
		while(problema.isBaseCase() == false) {
			sp = memoria.get(problema);
			sol.add(sp);
			problema = problema.neighbors(sp.first()).get(0);
		}
		
		
		return sol;
	}

	private Pair<Integer, Double> pd(Ej01HV problema) {
		Pair<Integer, Double> sp; 
		
		if(memoria.containsKey(problema)) {
			return memoria.get(problema);
		}
		
		if(problema.isBaseCase()) {
			if(problema.baseCaseWeight() == null) {
				return null;
			}
			sp = new Pair<>(null, .0); 
			memoria.put(problema, sp);
			return sp;
		}
		Pair<Integer, Double> mejorSp = null;
		Double mejorPeso = Double.MAX_VALUE;
		Double peso;
		
		for(Integer a: problema.actions()) {
			for(Ej01HV nuevoProblema: problema.neighbors(a)) {
				sp = pd(nuevoProblema);
				if(sp == null) {
					continue;
				}
				peso = sp.second() + a;
				if(peso < mejorPeso) {
					mejorSp = new Pair<>(a, peso);
					mejorPeso = peso;
				}
			}
		}
		if(mejorSp == null) {
			return null;
		}
		
		memoria.put(problema, mejorSp);
		return mejorSp;
		
	}

	public static void main(String[] args) {
		DatosMulticonjunto.iniDatos("./src/ej01_hg/ejemplo1_1.txt");
		Ej01PDM pd = new Ej01PDM();
		Ej01HV inicial = new Ej01HV(0, DatosMulticonjunto.SUMA);
		
		System.out.println(pd.search(inicial));
	}
}
