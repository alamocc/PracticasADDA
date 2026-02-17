package tests;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.Graph;

import ejercicio4.Calle;
import ejercicio4.Ejercicio4;
import ejercicio4.Interseccion;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

public class TestEjercicio4 {

	public static void main(String[] args) {
		Graph<Interseccion, Calle> g = GraphsReader
				.newGraph("ficheros/PI2E4_DatosEntrada.txt", //fichero de datos
						Interseccion::ofFormat, //factoria para construir los vertices
						Calle::ofFormat, //factoria para crear las aristas
						Graphs2::simpleWeightedGraph); //creador del grafo

		
	// Apartado A (comentamos el segundo para que no me sobreescriba el archivo del primero)
	var res11 = Ejercicio4.getSubgraph_EJ4A("m2", "m7", g);
	System.out.println("---------- APARTADO A ----------");
	System.out.println("El camino más corto segun duracion entre m7 y m3 es:" + res11 + ", con un duracion de: " + res11.getWeight() + " minutos");
	// var res12 = Ejercicio4.getSubgraph_EJ4A("m4", "m9", g);
	// System.out.println("El camino más corto segun duracion entre m4 y m9 es:" + res12 + ", con un duracion de: " + res12.getWeight() + " minutos");
	
	// Apartado B
	var res2 = Ejercicio4.getRecorrido_E4B(g);
	System.out.println("\n---------- APARTADO B ----------");
	System.out.println("El camino más corto segun esfuerzo es:" + res2 + ", con un esfuerzo de: " + res2.getWeight() + " minutos");
	
	// Apartado C
	Set<Calle> callesCortadas = new HashSet<>();
	for (Calle c : g.edgeSet()) {
	    String txt = g.getEdgeSource(c).toString() + g.getEdgeTarget(c).toString();

	    // Si la calle tiene el 1 y el 6, pa dentro
	    if (txt.contains("1") && txt.contains("6")) callesCortadas.add(c);
	    
	    // Si tiene el 4 y el 7...
	    if (txt.contains("4") && txt.contains("7")) callesCortadas.add(c);
	    
	    // Si tiene el 5 y el 8...
	    if (txt.contains("5") && txt.contains("8")) callesCortadas.add(c);
	    
	    // Si tiene el 4 y el 6...
	    if (txt.contains("4") && txt.contains("6")) callesCortadas.add(c);
	}

	var res3 = Ejercicio4.getRecorridoMaxRelevante_E4C(callesCortadas, g);
	System.out.println("La componente conexa con más relevancia es: " + res3.vertexSet());
	}
}

