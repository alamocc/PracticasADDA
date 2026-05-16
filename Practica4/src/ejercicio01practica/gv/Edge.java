package ejercicio01practica.gv;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record Edge(Vertex source, Vertex target, Integer action, Double weight) 
	implements SimpleEdgeAction<Vertex, Integer> {



}
