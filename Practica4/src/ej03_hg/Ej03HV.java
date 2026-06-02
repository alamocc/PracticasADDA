package ej03_hg;

import us.lsi.hypergraphs.VirtualHyperVertex;

import java.util.ArrayList;
import java.util.List;

import us.lsi.common.Pair;

public record Ej03HV(Integer indice, List<Integer> plazasLibres) implements VirtualHyperVertex<Ej03HV, Ej03HE, Integer, Pair<Double, List<Integer>>> {

	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		for (Integer grupos = 0; grupos < DatosAlumnos.getNumGrupos(); grupos++) {
			if (this.plazasLibres.get(grupos) == 0) {
				continue;
			}
			if (DatosAlumnos.getAfinidad(indice, grupos) == 0) {
				continue;
			}
			actions.add(grupos);
		}
		return actions;
	}

	@Override
	public Boolean isBaseCase() {
		return this.indice == DatosAlumnos.getNumAlumnos();
	}

	@Override
	public Double baseCaseWeight() {
		return 0.0;
	}

	@Override
	public Boolean isValid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair<Double, List<Integer>> baseCaseSolution() {
		return new Pair<>(0.0, new ArrayList<>());
	}

	@Override
	public Pair<Double, List<Integer>> solution(Integer a, List<Pair<Double, List<Integer>>> solutions) {
		Double nuevoPeso = solutions.get(0).first() + DatosAlumnos.getAfinidad(this.indice, a);
		List<Integer> nuevosActions = new ArrayList<>(solutions.get(0).second());
		nuevosActions.add(0, a); // IMPORTANTE: Al principio (0) PARA QUE LA LISTA NO SALGA INVERTIDA
		return new Pair<Double, List<Integer>>(nuevoPeso, nuevosActions);
	}

	@Override
	public List<Ej03HV> neighbors(Integer a) {
		Integer nuevoIndice = this.indice + 1;
		List<Integer> nuevasPlazasLibres = new ArrayList<>(this.plazasLibres);
		nuevasPlazasLibres.set(a, this.plazasLibres.get(a) - 1);
		return List.of(new Ej03HV(nuevoIndice, nuevasPlazasLibres));
	}

	@Override
	public Ej03HE edge(Integer a) {
		return new Ej03HE(this, this.neighbors(a), a);
	}

}
