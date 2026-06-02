package ej03_hg;

import java.util.ArrayList;
import java.util.List;

import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {

	public static void main(String[] args) {
		// 1. Ver, entender, cargar datos del problema
		DatosAlumnos.iniDatos("src/ej03_hg/ejemplo3_1.txt");
		
		// 2. Implementar el vértice virtual (LO TOCHO DE ESTO)
		// Es un Record no clase, default, atributos, ...
		
		// 3. Arista Virtual
		
		// 4. Heurística	(es obligatoria, pero puede que los resultados den bien sin ponerla)
		
		// 5. Usar el constructor de grafos para crear el Grafo Virtual (GV)
		List<Integer> plazas = new ArrayList<>();
		for (Integer i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			plazas.add(DatosAlumnos.getTamGrupo());
		}
		
		Ej03Vertex vI = new Ej03Vertex(0, plazas);
		var gV = EGraph.virtual(vI).type(Type.Max).pathType(PathType.Sum).heuristic(Ej03Vertex::heuristica).build();
		
		// 6. Crear el Algoritmo (tenemos 3 tipos aBT (BackTracking), aS (A estrella) y voraz (que es el que vemos con el greedy))
		var algAStar = AStar.ofGreedy(gV);
		var algBactracking = BT.ofGreedy(gV);					// EL GREEDY ES LO VORAZ JUNTO CON LO QUE HAY DE GREEDY EN LO DE LA CLASE VERTICE
		
		// 7. Ejecutar y procesar solución
		var solBT = algBactracking.search();
		System.out.println(solBT.get().getEdgeList());
		
		var solAS = algAStar.search();
		System.out.println(solAS.get().getEdgeList());

	}

}
