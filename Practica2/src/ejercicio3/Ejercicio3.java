package ejercicio3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.color.GreedyColoring;
import org.jgrapht.alg.interfaces.VertexColoringAlgorithm.Coloring;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.colors.GraphColors.Style;
import us.lsi.common.Pair;
import us.lsi.graphs.views.SubGraphView;


public class Ejercicio3 {
	
	// APARTADO A
	public static Graph<Investigador,Colaboracion> getSubgraph_EJ3A(Graph <Investigador, Colaboracion> g ) {
		/*1) CREAR UN PREDICADO PARA EL TIPO INVESTIGADOR
		 * 2) CREAR UN PREDICADO PARA EL TIPO COLABORADOR
		 * 3)EXPORTAR EL GRAFO USANDO AMBOS PREDICADOS
		 * 4) DEVOLVER LA LSTA USANDO AMBOS PREDICADOS
		 */
		
		Set<Colaboracion> aristasQueCumplenLaCondicion = new HashSet<>();			// Aristas que cumplen la condicion (número de artículos (peso) mayor que 5)
		
		for (Colaboracion aristas: g.edgeSet()) {
			if (aristas.getNColaboraciones() > 5) {
				aristasQueCumplenLaCondicion.add(aristas);
			}
		}
		
		
		Set<Investigador> verticesQueCumplenLaCondicion = new HashSet<>();			// Vertices que cumplen la condición (Nacidos antes de 1982 o que tengan mas de 5 articulos)
		
		for (Investigador vertices : g.vertexSet()) {
		    boolean tieneColaboracionRelevante = false;
		    for (Colaboracion c : g.edgesOf(vertices)) {
		        if (aristasQueCumplenLaCondicion.contains(c)) {
		            tieneColaboracionRelevante = true;
		        }
		    }
		    
		    if (vertices.getFNacimiento() < 1982 || tieneColaboracionRelevante) {
		        verticesQueCumplenLaCondicion.add(vertices);
		    }
		}
		
		// Predicate<Colaboracion> pe = e -> e.getNColaboraciones() > 5;										OTRA FORMA DE HACER EL BUCLE ANTERIOR CON PREDICADO	  // "Las colaboraciones más relevantes (número de artículos mayor que 5)"
		// Predicate<Investigador> pv = v -> v.getFNacimiento() < 1982 || g.edgesOf(v).stream().anyMatch(pe);	OTRA FORMA DE HACER EL BUCLE ANTERIOR CON PREDICADO	  // "Los investigadores nacidos antes de 1982..." y "...o que tengan más de 5 artículos con alguno de sus coautores."
		
		GraphColors.toDot(g,"ficheros/grafos/EJ3A.gv",					// Muestre el grafo configurando su apariencia de forma que se resalten los vértices y las aristas de la vista. 
				 v-> v.toString() + " " + v.getFNacimiento(),			// LO QUE APARECE DENTRO DE CADA VERTICE EN EL DIBUJO DEL GRAFO
				 e-> e.getNColaboraciones().intValue()+"",				// LO QUE APARECE EN LO ALTO DE LA ARISTA EN EL DIBUJO DEL GRAFO
				 v -> GraphColors.colorIf(Color.blue, verticesQueCumplenLaCondicion.contains(v)), 		// PINTAMOS LOS VERTICES SI CUMPLEN LA CONDICION DEL PREDICADO pv
				 e -> GraphColors.colorIf(Color.blue, aristasQueCumplenLaCondicion.contains(e)));		// PINTAMOS LAS ARISTAS SI CUMPLEN LA CONDICION DEL PREDICADO pa
		     //  v -> GraphColors.colorIf(Color.blue, pv.test(v)),				 PARA LA FORMA DEL PREDICADO
			 //  e -> GraphColors.colorIf(Color.blue, pe.test(e);				 PARA LA FORMA DEL PREDICADO
		
		return SubGraphView.of(g,
				v -> verticesQueCumplenLaCondicion.contains(v),
				e -> aristasQueCumplenLaCondicion.contains(e));			// Devolvemos la vista
	}
	
	// APARTADO B
	public static Set<Investigador> getMayoresColaboradores_E3B (Graph<Investigador,Colaboracion> g) {
		/*1) CREAR UN COMPARATOR CMP de Investigador según el grado (nº aristas)
		 * 2) ORDENAR TODOS LOS vertices descendentemente según CMP
		 * 3) SELECCIONAR LOS CINCO PRIMEROS
		 */
		
		Set<Investigador> investigadoresConMayoresColaboradores = new HashSet<>();  // Creamos un conjunto para almacenar los vertices
		
		for (Investigador vertices: g.vertexSet()) {								// Metemos los vértices
			investigadoresConMayoresColaboradores.add(vertices);
		}
		
		Comparator<Investigador> cmp = Comparator.comparing(v -> g.degreeOf(v));	// Creamos el comparador que ordene por el grado (numero de colaboraciones)
		
		List<Investigador> listaVertices = new ArrayList<>(investigadoresConMayoresColaboradores); // Pasammos a lista el Set Porque el .sort() solo funciona con listas. Lo que meto entre parentesis es una copia del set investigadoresConMayoresColaboradores
		Collections.sort(listaVertices, cmp.reversed());  // Ordenamos la lista de mayor a menor numero de colaboraciones (cmp.reversed())
		
		Set<Investigador> top5 = new HashSet<>();  									// Nos quedamos solo con los 5 primeros
		for (int i = 0; i < 5; i++) {
		    top5.add(listaVertices.get(i));
		}
		
		
		// Esto de abajo es otra manera con streams, mas chunga de entender
		// Comparator<Investigador> cmp = Comparator.comparing(v -> g.degreeOf(v));	// Ya que queremos el los 5 investigadores con el mayor numero de colaboradores, es decir, el mayor numero de aristas incidentes
		// var ls = g.vertexSet().stream().sorted(cmp.reversed()).limit(5).toList();	// Los ordenamos de mayor a menor y nos quedamos con los 5 mejores que los pasamos a una lista
		
		GraphColors.toDot(g,"ficheros/grafos/EJ3B.gv",
				 v-> v.toString(),											// Etiqueta de vertices
				 e-> e.getNColaboraciones().intValue()+"",					// Etiqueta de aristas
				 v -> GraphColors.colorIf(Color.blue, top5.contains(v)), 	// Coloreado de vertices que cummplen la condición
				 e -> GraphColors.color(Color.green));						// Coloreado de aristas
		return top5;					// Devolvemos el conjunto de 5 investigadores (el resultado)
	}

	// APARTADO C
	public static Map<Investigador,List<Investigador>> getMapListaColabroradores_E3C (Graph<Investigador,Colaboracion> g) {
		/*
		 * 1) CREAR UN COMPARATOR CMP DE LAS ARISTAS (colaboraciones) SEGUN EL NUMERO DE COLABORACIONES
		 * 2) OBTENER LOS VERTICES DEL GRAFO (investigadres) Y PARA CADA VERTICE OBTENER SUS VECINOS (colaboraciones) ORDENADOS SEGUN CMP
		 * 3) CREAR EL MAPA DONDE LA CLAVE ES EL VERTICE Y EL VALOR LA LISTA DE VECINOS ORDENADOS
		 * */
		
		Map<Investigador,List<Investigador>> res = new HashMap<>();        							// Mapa del resultado
		// Comparator<Colaboracion> cmp = Comparator.comparing(v -> v.getNColaboraciones());		// Comparador para el numero de colaboraciones en caso de usar el strean
		
		for (Investigador vertices : g.vertexSet()) {														// Obetenemos los vertices y las aristas de esos vertices para luego pasarselos al Graphs.getOppositeVertex(g, aristas, vertices)
			List<Investigador> colaboradores = new ArrayList<>();
			for (Colaboracion aristas: g.edgesOf(vertices)) {
				Investigador listaVerticesAdyacentes = Graphs.getOppositeVertex(g, aristas, vertices);		// Con este metodo obtenemos los vertices adyacentes a v por la arista a
				colaboradores.add(listaVerticesAdyacentes);			// Guardamos los vertices adyacentes en una lista, para luego crear el mapa:  {vertice1 = (vertice1, vertice2,...), vertice 2 = .....}
				
			}
			Collections.sort(colaboradores, (a, b) -> {						// Ordenamos la lista de vertices adyacentes (colaboradores), segun el numero de colaboraciones, atributo de las aristas
		        Double colaboracionesA = g.getEdge(vertices, a).getNColaboraciones();		// Obtiene el numero de colaboraciones que hay entre el vertice vertices y el vertice a
		        Double colaboracionesB = g.getEdge(vertices, b).getNColaboraciones();		// Obtiene el numero de colaboraciones que hay entre el vertice vertices y el vertice b
		        return Double.compare(colaboracionesB, colaboracionesA); 	// Esto compara el numero de colaboraciones y se queda con la mayor (porque es B,A, si fuese A,B seria de menor a mayor)
		    });
			res.put(vertices, colaboradores);
		}
		
		
		// Otra forma de hacerlo con streams, mas lioso
	    // for (Investigador i : g.vertexSet()) {
	    //     List<Investigador> colaboradoresOrdenados = g.edgesOf(i).stream()
	    //             .sorted(cmp.reversed()) 													// Ordenamos las colaboraciones por peso (artículos) descendente
	    //             .map(edge -> Graphs.getOppositeVertex(g, edge, i)) 							// Usamos Graphs.getOppositeVertex(grafo, arista, vertice) para encontrar quién está al otro lado de la arista desde la perspectiva del investigador actual.
	    //             .collect(Collectors.toList());
	        
	    //     res.put(i, colaboradoresOrdenados);		// Para cada investigador obtenemos sus colaboradores ordenados segun cmp (esto devuelve: <Investigador,List<Investigador>>)
	    // }
		
		List<Colaboracion> top = new ArrayList<>();								// Creamos una lista con las colaboracones top, que son las aristas que posteriormente se van a colorear
		for (Entry<Investigador, List<Investigador>> l : res.entrySet()) {	// Recorrido de mapas
			top.add(g.getEdge(l.getKey(), l.getValue().get(0)));				// Obtenemos la arista entre el investigador (l.getKey()) y el investigador con su mejor colaboracion (l.getValue().get(0))). Ejemplo: arista entre inv 4 y inv 1 para el primer caso
			// l.getKey() es un vertice y l.getValue().get(0) es otro vertice y g.getEdge() obtiene la arista entre ellos, que posteriormente se colorea
		}
	    
	    GraphColors.toDot(g,"ficheros/grafos/EJ3C.gv",
				 v-> v.toString(),														// Etiqueta de vertices
				 e-> e.getNColaboraciones().intValue()+"",								// Etiqueta de aristas
				 v -> GraphColors.color(Color.black), 									// Coloreado de vertices
				 e -> GraphColors.colorIf(Color.blue, top.contains(e)));				// Coloreado de aristas segun la condicion
	    return res;									// Devolvemos el resultado
	}
	
	// APARTADO D
	
	public static Pair<Investigador,Investigador> getParMasLejano_E3D (Graph<Investigador,Colaboracion> g) {
		/*
		 * 1) CREAMOS LA VARIABLE alg Y CREAMOS EL PAR DEL RESULTADO
		 * 2) PROCEDEMOS A VER EL CAMINO ENTRE TODOS LOS VERTICES, PARA ELLO ITERAMOS SOBRE ELLOS CREANDO UN FOR QUE RECORRA UNO Y LUEGO EL SIGUIENTE
		 * 3) DENTRO DE ESTE BUCLE NOS QUEDAMOS CON LOS PARES DE VERTICES CON MAYOR DISTANCIA
		 * */
		var alg = new DijkstraShortestPath<>(g);											// Utilizamos DIJKSTRA para obtener el camino mas corto entre 2 pares de vertices
		Pair<Investigador, Investigador> res = null;										// Creamos la variable de resultado
		
		List<Investigador> investigadores = new ArrayList<>(g.vertexSet());					// Pasamos todos los vertices a una lista para poder iterar en ellos para posteriormente poder coger un investigador y el siguiente
	    
		Double maxDistancia = 0.0;									// Creamos una variable maxDistancia para luego buscar el camino mas largo
	    for (int i = 0; i < investigadores.size(); i++) {									// Recorremos la lista de vertices creada anteriormente, y cogemos el primer invstigador
	        for (int j = i + 1; j < investigadores.size(); j++) {							// Recorremos la lista de vertices creada anteriormente, y cogemos el segundo invstigador, asi podemos crear todas las posobles combinaciones de investigadores para poder buscar el camino mas largo
	            
	            Investigador inv1 = investigadores.get(i);			// Investigador 1 con el que vamos a coger su camino con el investigador 2
	            Investigador inv2 = investigadores.get(j);			// Investigador 2 con el que vamos a coger su camino con el investigador 1
	            
	            GraphPath<Investigador, Colaboracion> path = alg.getPath(inv1, inv2);			// Obtenemos el camino entre el investigador 1 y el 2 (en este caso no hemos definido peso para las aristas como si hemos hecho en el ejercicio 4, A, por lo tanto el algoritmo de Dijkstra obtiene el camino mas corto segun el numero de aristas que haya entre los 2 vertices

	            // getLength() devuelve el número de aristas en grafos no ponderados
	            Double distanciaActual = (double) path.getEdgeList().size(); 					// Obtenemos la distancia del camino obtenido anteriormente (el numero de aristas que hay entre ellos
	                
	            if (distanciaActual > maxDistancia) {						// Esto lo hacemos para quedarnos con el camino mas largo
	                maxDistancia = distanciaActual;
	                    
	                // Actualizamos el par ganador (el par con el camino mas largo)
	                res = new Pair<>(inv1, inv2);
	            }
	        }
	    }
	    
	    // Para el Coloreado (esto lo hacemos para meterle la condicion al colorIf)
	    Investigador origen = res.first();												// Obtenemos el investigador donde empieza el camino mas largo							
	    Investigador destino = res.second();											// Obtenemos el investigador donde termina el camino mas largo
	    
	    GraphPath<Investigador, Colaboracion> camino = alg.getPath(origen, destino);	// Obtenemos el caminos entre los 2 investigadores definidos anteriormente
	    System.out.println("Longitud del camino: " + camino.getEdgeList().size());		// Esto lo ponemos porque en la solucion pone que mostremos la longitud del camino
	    
	    Set<Investigador> verticesEnCamino = new HashSet<>(camino.getVertexList());			// Obteremos los vertices
	    Set<Colaboracion> aristasEnCamino = new HashSet<>(camino.getEdgeList());			// Obtenemos las aristas
	    
	    GraphColors.toDot(g,"ficheros/grafos/EJ3D.gv",
				 v-> v.toString(),														// Etiqueta de vertices
				 e-> e.getNColaboraciones().intValue()+"",								// Etiqueta de aristas
				 v -> GraphColors.colorIf(Color.blue, verticesEnCamino.contains(v)), 	// Coloreado de vertices
				 e -> GraphColors.colorIf(Color.blue, aristasEnCamino.contains(e)));	// Coloreado de aristas
	    return res;									// Devolvemos el resultado	    
	}
	
	// APARTADO E
	public static List<Set<Investigador>> getReuniones_E3E (Graph<Investigador,Colaboracion> g) {
		/*
		 * 1) CREAMOS UN GRAFO AUXILIAR DONDE METEMOS TODOS LOS CONFLICTOS, es decir, LE METEMOS LOS VERTICES Y LAS ARISTAS SEGUN LAS CONDICIONES QUE NOS DIGAN
		 * 2) APLICAMOS EL ALGORITMO DE COLOREADO PARA QUE LOS VERTICES ADTACENTES TENGAN COLORES DISTINTOS
		 * */
		
		// 1. Crear un grafo auxiliar SOLAMENTE para gestionar los conflictos
		Graph<Investigador, DefaultEdge> gConflictos = new SimpleGraph<>(DefaultEdge.class);          // LO DE DefaultEdge SE USA SIEMPRE EN COLOREADO, lo que hace es aristas simples sin informacion, solo relacionan los vertices
	    
	    						// Añadimos todos los investigadores al grafo de conflictos (los vértices)
	    for (Investigador inv : g.vertexSet()) {
	        gConflictos.addVertex(inv);
	    }
	    
	    // 2. AÑADIR CONFLICTO 1: Si ya son colaboradores (Añade a gConflictos las aristas del grafo original)
	    for (Colaboracion edge : g.edgeSet()) {				// Esto lo hacemos porque .addEdge(v1, v2), necesita 2 vertices, el inicial y el final, para poder añadir esa arista al grafo
	        Investigador v1 = g.getEdgeSource(edge);
	        Investigador v2 = g.getEdgeTarget(edge);
	        
	        					// Si no existe ya la arista en el grafo de conflictos, la añadimos
	        if (!gConflictos.containsEdge(v1, v2)) {
	            gConflictos.addEdge(v1, v2);
	        }
	    }
	    
	    // 3. AÑADIR CONFLICTO 2: Si son de la misma universidad                     (EL ITERADO SE HACE IGUAL QUE EN EL APARTADO D)
	    List<Investigador> listaInv = new ArrayList<>(g.vertexSet());
	    for (int i = 0; i < listaInv.size(); i++) {
	        for (int j = i + 1; j < listaInv.size(); j++) {
	            Investigador inv1 = listaInv.get(i);
	            Investigador inv2 = listaInv.get(j);
	            
	            // Si son de la misma universidad, NO pueden estar en la misma reunión -> Conflicto
	            if (inv1.getUniversidad().equals(inv2.getUniversidad())) {
	                 if (!gConflictos.containsEdge(inv1, inv2)) {
	                    gConflictos.addEdge(inv1, inv2);
	                }
	            }
	        }
	    }
	    
	    // 4. El resto es igual (Algoritmo de coloreado)
	    var alg = new GreedyColoring<>(gConflictos);						// Asignamos a alg el algoritmo
	    Coloring<Investigador> coloreado = alg.getColoring();				// Obtenemos el coloreado
	    
	    Map<Investigador, Integer> asignacion = coloreado.getColors();		// Esto asigna a cada vertice un numero que indica que todos los que tienen 1 por ejemplo son del mismo color
	    List<Set<Investigador>> res = coloreado.getColorClasses();								// Esto nos devuelve los vertices agrupados con los que tienen el mismo color
	    
	    GraphColors.toDot(gConflictos, "ficheros/grafos/EJ3E.gv", 
				v->v.toString(), 								// Etiqueta de vertices
				e->"",											// Etiqueta de aristas (no me pintes las aristas)
				v -> GraphColors.color(asignacion.get(v)),		// Coloreado de vertices
				e -> GraphColors.style(Style.solid));    		// Coloreado de vertices (Style.solid es la raya por defecto, se puede poner bold y sale en negrita, es lo mismo que .color(Color.black))
	    return res;
	}

}
