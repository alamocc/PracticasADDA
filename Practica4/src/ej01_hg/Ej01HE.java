package ej01;

import java.util.List;

import us.lsi.hypergraphs.SimpleHyperEdge;

public record Ej01HE(Ej01HV source, List<Ej01HV> targets, Integer action) implements SimpleHyperEdge<Ej01HV, Ej01HE, Integer> {	// Siempre es SimpleHyperEdge

	@Override
	public Double weight(List<Double> targetsWeight) {		// Dice que casi siempre es igual
		return targetsWeight.get(0) + action;
	}
}
