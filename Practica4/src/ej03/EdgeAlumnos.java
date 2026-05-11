package ej03;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EdgeAlumnos(VertexAlumnos source, VertexAlumnos target, Integer action, Double weight) 
	implements SimpleEdgeAction<VertexAlumnos, Integer>{
	
	
}
