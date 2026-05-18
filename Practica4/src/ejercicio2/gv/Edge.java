package ejercicio2.gv;
/*	Para describir una arista completamente, la librería necesita saber 4 cosas:

    source (Origen): El vértice (la foto) donde estábamos antes de decidir.

    target (Destino): El nuevo vértice (la foto) al que llegamos tras la decisión.

    action (Acción): Qué decisión tomamos (en nuestro caso, el ID del contenedor o de la papelera).

    weight (Peso): Qué ganancia o coste inmediato nos aporta esa decisión. Aquí es donde conectamos 
    directamente con nuestra Función Objetivo matemática (maximizar los contenedores llenos).*/

import us.lsi.graphs.virtual.SimpleEdgeAction;

public record Edge(Vertex source, Vertex target, Integer action, Double weight) implements SimpleEdgeAction<Vertex, Integer> {

}

