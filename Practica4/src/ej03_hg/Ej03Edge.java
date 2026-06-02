package ej03_hg;

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record Ej03Edge(Ej03Vertex source, Ej03Vertex target, Integer action, Double weight) implements SimpleEdgeAction<Ej03Vertex, Integer> {

}
