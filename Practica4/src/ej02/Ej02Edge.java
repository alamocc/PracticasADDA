package ej02;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record Ej02Edge(Ej02Vertex source, Ej02Vertex target, Integer action, Double weight) implements SimpleEdgeAction<Ej02Vertex, Integer> {	// SIEMPRE VA  A SER SimpleEdgeAction, al crear el recod me deja implementar la interfaz dandole a add
						// LAS COSAS ESTAS DE ARRIBA SIEMPRE IGUAL, pq si no los default dan error
	
}
