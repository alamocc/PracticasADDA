package ej01;

import us.lsi.hypergraphs.VirtualHyperVertex;

import java.util.ArrayList;
import java.util.List;

import us.lsi.common.Pair;

public record Ej01HV(Integer indice, Integer sumaRestante) implements VirtualHyperVertex<Ej01HV, Ej01HE, Integer, Pair<Double, List<Integer>>> {		// Hyper Grafo Virtual Vertex			Siempre es VirtualHyperVertex
																									// Tenemos uno mas que en GV, el ultimo (S, solucion)
	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		//*
		for (Integer i = 0; i < DatosMulticonjunto.getMultiplicidad(indice); i++) {
			actions.add(i);
		}
		
		return actions;
	}

	@Override
	public Boolean isBaseCase() {
		return this.indice == DatosMulticonjunto.getNumElementos();
	}

	@Override
	public Double baseCaseWeight() {			// Devuelve el peso si el caso base es solucion valida o null si no es solucion valida
		if (this.sumaRestante > 0) {
			return null;
		}
		return 0.0;
	}

	@Override
	public Boolean isValid() {						// Dice que no se usa
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Double, List<Integer>> baseCaseSolution() {
		return new Pair<Double, List<Integer>>(0.0, new ArrayList<>());
	}

	@Override														// Double = peso, List<Integer> = solucion
	public Pair<Double, List<Integer>> solution(Integer a, List<Pair<Double, List<Integer>>> solutions) {		// Este metodo es complicado
		Double nuevoPeso = solutions.get(0).first() + a;		// Es la suma del double del pair mas la action, que es la suma de los subproblemas en los que dividimos el problema mas el action de unir los subproblemas
		List<Integer> nuevasActions = new ArrayList<>(solutions.get(0).second());
		nuevasActions.add(a);
		return new Pair<Double, List<Integer>> (nuevoPeso, nuevasActions);
	}

	@Override
	public List<Ej01HV> neighbors(Integer a) {
		Integer nuevoIndice = this.indice + 1;
		Integer nuevaSumaRestante = this.sumaRestante - DatosMulticonjunto.getElemento(this.indice) * a;
		return List.of(new Ej01HV(nuevoIndice, nuevaSumaRestante));
	}

	@Override
	public Ej01HE edge(Integer a) {								// DICE QUE SIEMPRE ES IGUAL
		return new Ej01HE(this, this.neighbors(a), a);
	}	
}
