package ejemplo01hg;

import java.util.List;

import us.lsi.hypergraphs.SimpleHyperEdge;

/*	La interfaz SimpleHyperEdge nos obliga a definir tres cosas fundamentales que definen cualquier paso en nuestro grafo:

    Ej01HV source: El vértice de origen. Es decir, el estado en el que estábamos antes de tomar la decisión.

    List<Ej01HV> targets: El vértice (o vértices) de destino. Es la lista de vecinos a los que llegamos tras tomar la decisión. (Recuerda que tu método neighbors en la clase anterior devolvía exactamente esto).

    Integer action: La decisión que hemos tomado para ir del source a los targets. En nuestro caso, la multiplicidad (la variable a).*/

public record Ejemplo01HE(Ejemplo01HV source, List<Ejemplo01HV> targets, Integer action) 
	implements SimpleHyperEdge<Ejemplo01HV, Ejemplo01HE, Integer> {

	@Override
	public Double weight(List<Double> targetsWeight) {
		return targetsWeight.get(0) + action;
	}

}
