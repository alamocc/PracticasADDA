package ejercicio01practica.gv;

import java.util.ArrayList;

// Importamos Datos1 del paquete donde te lo hayan otorgado
import ejercicio1.Datos1; 
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {

	public static void main(String[] args) {
		// 1. Cargar los datos del problema desde el archivo de entrada
		// ¡OJO! Asegúrate de que esta ruta apunte correctamente a tu archivo DatosEntrada1.txt
		Datos1.iniDatos("datos_entrada/ejercicio1/DatosEntrada1.txt");
		
		// 2. Definir el Estado Inicial (Vértice Raíz)
		// Empezamos en el candidato 0, con 0.0 gasto, 0 valoración, TODAS las cualidades pendientes y nadie contratado
		Vertex vI = new Vertex(0, 0.0, 0, Datos1.getCualidades(), new ArrayList<>());
		
		// 3. Construir el Grafo Virtual con la configuración del modelo matemático
		var gV = EGraph.virtual(vI)
				.type(Type.Max)
				.pathType(PathType.Sum)
				.heuristic(Vertex::heuristica)
				.build();
		
		// 4. Inicializar los dos algoritmos de espacio de estados
		var aBT = BT.of(gV);   // Algoritmo de Backtracking (Ramificación y Acotación)
		var aS = AStar.of(gV); // Algoritmo A* (Búsqueda preferente por el mejor camino estimado)
		
		// 5. Ejecutar Backtracking y mostrar la secuencia de aristas (decisiones) encontradas
		var solBT = aBT.search();
		System.out.println("=========================================");
		System.out.println("SOLUCIÓN ENCONTRADA POR BACKTRACKING (BT):");
		System.out.println("=========================================");
		if (solBT.isPresent()) {
			System.out.println(solBT.get().getEdgeList());
		} else {
			System.out.println("No se encontró ninguna solución válida.");
		}
		
		System.out.println("\n");
		
		// 6. Ejecutar A* y mostrar su secuencia de decisiones
		var solAS = aS.search();
		System.out.println("=========================================");
		System.out.println("SOLUCIÓN ENCONTRADA POR A ESTRELLA (A*):");
		System.out.println("=========================================");
		if (solAS.isPresent()) {
			System.out.println(solAS.get().getEdgeList());
		} else {
			System.out.println("No se encontró ninguna solución válida.");
		}
	}
}