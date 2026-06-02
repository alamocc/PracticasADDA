package ej03_hg;

import java.util.List;

import us.lsi.hypergraphs.SimpleHyperEdge;

public record Ej03HE(Ej03HV source, List<Ej03HV> targets, Integer action) implements SimpleHyperEdge<Ej03HV, Ej03HE, Integer> {

	@Override
	public Double weight(List<Double> targetsWeight) {
		return targetsWeight.get(0) + DatosAlumnos.getAfinidad(source.indice(), action);
	}

}
