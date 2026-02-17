package ejercicio4;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm.SpanningTree;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.grafos.datos.Carretera;
import us.lsi.grafos.datos.Ciudad;


public class Ejercicio4 {

	// Esto hace lo de sacar vertice 1 del nombre y eso, por si lo pone en el exame, esta en lo de ejemplo 4 y es solo copiar y pegar
	
	// public static Interseccion interseccion(Graph<Interseccion,Calle> graph, String nombre) {
	//     return graph.vertexSet().stream().filter(c->c.getNombre().equals(nombre)).findFirst().get();
	// }
	
	
	// APARTADO A
	public static GraphPath<Interseccion,Calle> getSubgraph_EJ4A(String mIn, String mOut,Graph <Interseccion, Calle> g) {  // String ftest es para pooner el fichero del test cuando llamamos a la funcion en el test, pero yo he puesto el nombre manualmente: "ficheros/grafos/EJ4A.gv"

		// Obtenemos el grafo con el peso para las aristas que queramos (le ponemos de peso el atributo que queramos en este caso, la duracion), para luego aplicar Dijkstra (camino mas corto entre 2 vertices)
		for (Calle aristas: g.edgeSet()) {
			g.setEdgeWeight(aristas, aristas.getDuracion());
		}
				
		var alg = new DijkstraShortestPath<>(g);	
		
		Interseccion vertice1 = null;
		for (Interseccion vertices: g.vertexSet()) {
			if (vertices.getNombre().equals(mIn)) {		
				vertice1 = vertices;
			}
		}
		Interseccion vertice2 = null;
		for (Interseccion vertices: g.vertexSet()) {
			if (vertices.getNombre().equals(mOut)) {		
				vertice2 = vertices;
			}
		}
		
		GraphPath<Interseccion,Calle> caminoMasCortoSegunDuracion = alg.getPath(vertice1, vertice2);

		// Coloreamos el camino mas corto segun duracion		
		Set<Interseccion> verticesDelCaminoMasCercano = new HashSet<>();
		Set<Calle> aristasDelCaminoMasCercano = new HashSet<>();
				
		for (Interseccion vertices: caminoMasCortoSegunDuracion.getVertexList()) {
			verticesDelCaminoMasCercano.add(vertices);
		}
		for (Calle aristas: caminoMasCortoSegunDuracion.getEdgeList()) {
			aristasDelCaminoMasCercano.add(aristas);
		}
				
		GraphColors.toDot(g,"ficheros/grafos/EJ4A.gv",
				v->v.toString() + ", " + v.getNombre(), 
				a->a.getDuracion()+"",															// Esto me ahorra tener que recorrer los verticces y aristas y meterlos en conjuntos nuevos
				v->GraphColors.colorIf(Color.blue, verticesDelCaminoMasCercano.contains(v)),		// caminoMasCortoSegunDuracion.getVertexList().contains(v)
				a->GraphColors.colorIf(Color.blue, aristasDelCaminoMasCercano.contains(a)));		// caminoMasCortoSegunDuracion.getEdgeList().contains(e)
		return caminoMasCortoSegunDuracion;		
	}
	
	// APARTADO B
	public static GraphPath<Interseccion,Calle> getRecorrido_E4B(Graph <Interseccion, Calle> g) {

		// Obtenemos el grafo con el peso para las aristas que queramos (le ponemos de peso el atributo que queramos en este caso, el esfuerzo), para luego aplicar Kruskal (camino mas corto que pase por todos los vertices), pero este nos devuelve solo las aristas, no el conjunto vertices aristas para eso usamos HeldKarp
		for (Calle aristas: g.edgeSet()) {
			g.setEdgeWeight(aristas, aristas.getEsfuerzo());
		}
				
		var alg = new HeldKarpTSP<Interseccion, Calle>();
		
		GraphPath<Interseccion,Calle> caminoMasCortoSegunEsfuerzo = alg.getTour(g);
		
		// Coloreado del camino mas corto segun esfuerzo			
		GraphColors.toDot(g,"ficheros/grafos/EJ4B.gv",
				v->v.toString() + ", " + v.getNombre(), 
				a->a.getDuracion()+"",
				v->GraphColors.colorIf(Color.blue, caminoMasCortoSegunEsfuerzo.getVertexList().contains(v)),
				a->GraphColors.colorIf(Color.blue, caminoMasCortoSegunEsfuerzo.getEdgeList().contains(a)));
		return caminoMasCortoSegunEsfuerzo;		
	}
	
	// APARTADO C
	public static Graph<Interseccion,Calle> getRecorridoMaxRelevante_E4C(Set<Calle> cs,Graph <Interseccion, Calle> g) {	// String ftest es para pooner el fichero del test cuando llamamos a la funcion en el test, pero yo he puesto el nombre manualmente: "ficheros/grafos/EJ4C.gv"
		
		// Obtenemos el grafo sin las calles cortadas (eliminamoos esas asristas)
		g.removeAllEdges(cs);
		
		// Obtenemos las componentes conexas
		var alg = new ConnectivityInspector<>(g);
		List<Set<Interseccion>> componentesConexas = alg.connectedSets();
		
		// BUSCAR LA MEJOR COMPONENTE (Maximizar relevancia)
		Set<Interseccion> mejorComponente = new HashSet<>();
		Integer maximaRelevancia = 0;
		for (Set<Interseccion> conjuntos: componentesConexas) {
			Integer relevanciaActual = 0;
			for (Interseccion vertices: conjuntos) {
				relevanciaActual += vertices.getRelevancia();
			}
			if (maximaRelevancia < relevanciaActual) {
				maximaRelevancia = relevanciaActual;
				mejorComponente = conjuntos;
			}
		}
		
		// Coloreado de Componentes Conexas
		Set<Interseccion> verticesGanadores = mejorComponente; // Esto hay que hacerlo porqu esi no da error al colorear
		
		// Hago la condicional del if porque me dicen que conteste a la pregunta de si se pueden visitar o no todos los monumentos
		if (componentesConexas.size() == 1) {
			System.out.println("\n---------- APARTADO C ----------");			// No pongo esto en el test porque se bugea y sale arriba
			System.out.println("SÍ se pueden recorrer todos los monumentos");
			GraphColors.toDot(g, "ficheros/grafos/EJ4C.gv",
					v -> v.toString() + ", " + v.getNombre() + ", " + v.getRelevancia(), 
					a -> a.getEsfuerzo() + "",
					v -> GraphColors.color(Color.black),		// No hace falta colorear porque solo hay una componente conexa
					a -> GraphColors.color(Color.black));		// No hace falta colorear porque solo hay una componente conexa
		} else {
			System.out.println("\n---------- APARTADO C ----------");
			System.out.println("NO se pueden recorrer todos los monumentos");	// No pongo esto en el test porque se bugea y sale arriba
			GraphColors.toDot(g, "ficheros/grafos/EJ4C.gv",
					v -> v.toString() + ", " + v.getNombre() + ", " + v.getRelevancia(), 
					a -> a.getEsfuerzo() + "",
					v -> GraphColors.colorIf(Color.blue, verticesGanadores.contains(v)),
					a -> GraphColors.colorIf(Color.blue, verticesGanadores.contains(g.getEdgeSource(a)) && verticesGanadores.contains(g.getEdgeTarget(a))));
		}
		
		return new AsSubgraph<>(g, mejorComponente);
	}
		
}
