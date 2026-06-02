package ejercicio3.gv;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EdgeEj03Greedy(VertexEj03Greedy source, VertexEj03Greedy target, Integer action, Double weight) implements SimpleEdgeAction<VertexEj03Greedy, Integer> {

}
