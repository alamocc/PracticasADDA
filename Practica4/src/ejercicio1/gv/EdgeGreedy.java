package ejercicio1.gv;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EdgeGreedy(VertexGreedy source, VertexGreedy target, Integer action, Double weight) implements SimpleEdgeAction<VertexGreedy, Integer>{

}
