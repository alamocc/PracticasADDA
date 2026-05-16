package ejercicio1.gv;

import java.util.ArrayList;

import ejercicio1.Datos1;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {

	public static void main(String[] args) {
		// 1. Ver, entender, cargar datos del problema
		Datos1.iniDatos("src/ejercicio1/gv/DatosEntrada1.txt");
		
		// 2. Implementar el vértice virtual (LO TOCHO DE ESTO)
		// Es un Record no clase, default, atributos, ...
		
		// 3. Arista Virtual
		
		// 4. Heurística	(es obligatoria, pero puede que los resultados den bien sin ponerla)
		
		// 5. Usar el constructor de grafos para crear el Grafo Virtual (GV)
		Vertex vI = new Vertex(0, 0.0, 0, Datos1.getCualidades(), new ArrayList<>());
		var gV = EGraph.virtual(vI)
				.type(Type.Max)
				.pathType(PathType.Sum)
				.heuristic(Vertex::heuristica)
				.build();
		
		// 6. Crear el Algoritmo (tenemos 3 tipos aBT (BackTracking), aS (A estrella) y voraz
		var aBT = BT.of(gV);
		var aS = AStar.of(gV);
		
		// 7. Ejecutar y procesar solución
		var solBT = aBT.search();
		System.out.println(solBT.get().getEdgeList());
		
		var solAS = aS.search();
		System.out.println(solAS.get().getEdgeList());
	}

}
