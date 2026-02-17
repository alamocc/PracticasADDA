package tests;

import java.util.Set;

import org.jgrapht.Graph;
import ejercicio3.Colaboracion;
import ejercicio3.Ejercicio3;
import ejercicio3.Investigador;
import us.lsi.colors.GraphColors;
import us.lsi.colors.GraphColors.Color;
import us.lsi.graphs.Graphs2;
import us.lsi.graphs.GraphsReader;

public class TestEjercicio3 {

	public static void main(String[] args) {
		
				// ---------------------------------------------------------
				// APARTADO A
				// ---------------------------------------------------------
		
		// 1. CARGA
		Graph<Investigador,Colaboracion> g = GraphsReader
				.newGraph("ficheros/PI2E3_DatosEntrada.txt", 
						Investigador::ofFormat,
						Colaboracion::ofFormat,
						Graphs2::simpleGraph); 
	
		System.out.println("--- Grafo Original Cargado ---");
		System.out.println("Vértices: " + g.vertexSet().size());
		System.out.println("Aristas: "  + g.edgeSet().size());

		// 2. GUARDAR ORIGINAL (Para comparar luego)                        NO ES OBLIGATORIO, ESTO ES HACER EL GRAFO SIN LOS PREDICADOS
		// Lo llamamos 'original.gv' para no machacar el de la solución
		GraphColors.toDot(g, "ficheros/grafos/original.gv",
				v -> v.getId() + "",
				e -> e.getNColaboraciones() + "",
				v -> GraphColors.color(Color.black),
				e -> GraphColors.color(Color.black));
				
		// 3. EJECUTAR EL FILTRO
		System.out.println("\n--- Generando Vista (Apartado A) ---");
		
		// Capturamos el resultado en la variable 'vista'
		Graph<Investigador, Colaboracion> vista = Ejercicio3.getSubgraph_EJ3A(g);
		
		// 4. COMPROBACIONES (NO HACE FALTA)
		
		System.out.println("Vértices en la vista: " + vista.vertexSet().size());
		System.out.println("Aristas en la vista: "  + vista.edgeSet().size());
		
		System.out.println("\nComprueba 'ficheros/grafos/EJ3A.gv' para ver los colores.");      // Esto lo pongo para que vaya a esa carpeta y lo meta en el graphviz online XD
		
		
		
		

				// ---------------------------------------------------------
				// APARTADO B
				// ---------------------------------------------------------
		
		System.out.println("\n--- Ejecutando Apartado B (Top 5 Colaboradores) ---");
				
		// 1. Llamamos al método y capturamos el conjunto en la variable 'topInvestigadores'
		Set<Investigador> topInvestigadores = Ejercicio3.getMayoresColaboradores_E3B(g);
		
		// 2. Imprimimos el resultado por consola
		// Es útil imprimir también el grado (número de vecinos) para verificar que el orden es correcto
		System.out.println("Los 5 investigadores con más colaboradores son:");
		
		topInvestigadores.forEach(inv -> {																				// TOTALMENTE INNECESARIO PERO PARA QUE QUEDE BONITO EN LA CONSOLA EL PRINT
			int numColaboradores = g.degreeOf(inv);
			System.out.println("   " + inv + " (Colaboradores: " + numColaboradores + ")");
		});

		// 3. Aviso del fichero generado
		System.out.println("Fichero generado en: ficheros/grafos/EJ3B.gv");						// Esto lo pongo para que vaya a esa carpeta y lo meta en el graphviz online XD
		
		//String msg = "Los 5 investigadores que tienen un mayor número de investigadores colaboradores son: " + Ejercicio3.getMayoresColaboradores_E3B(g);			// MANERA MUCHO MAS SIMPLE Y COMODA y como lo devuelve el test de la solucion
		//System.out.println(msg);
		
		
		
		
		
				// ---------------------------------------------------------
				// APARTADO C
				// ---------------------------------------------------------
		
		System.out.println("\n--- Ejecutando Apartado C (Mapa de Investigadores con lista de colaboradores asociados ordenados de mayor a menor colaboraciones) ---");
		
		String res = "Las listas de colaboradores ordenados por artículos conjuntos para cada investigador son: " + Ejercicio3.getMapListaColabroradores_E3C(g);
		System.out.println(res);
		
		
		
		
		
				// ---------------------------------------------------------
				// APARTADO D
				// ---------------------------------------------------------

		System.out.println("\n--- Ejecutando Apartado D (Camino mas largo, el par mas alejado) ---");
		
		String res2 = "El par de investigadores mas lejanos es: " + Ejercicio3.getParMasLejano_E3D(g);
		System.out.println(res2);
		
		
		
		
		
				// ---------------------------------------------------------
				// APARTADO E
				// ---------------------------------------------------------
		
		System.out.println("\n--- Ejecutando Apartado E (Coloreado por conflicto) ---");
		
		String res3 = "Las reuniones serían:  " + Ejercicio3.getReuniones_E3E(g);
		System.out.println(res3);
	}
}