package ejercicio3.gv;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record EdgeEj03(VertexEj03 source, VertexEj03 target, Integer action, Double weight) implements SimpleEdgeAction<VertexEj03, Integer> {

}
