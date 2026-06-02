package ejercicio3.gv;

import java.util.ArrayList;
import java.util.List;

import ejercicio3.Datos3;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class MainGreedy {

	public static void main(String[] args) {
		// 1. Cargamos Datos
		Datos3.iniDatos("src/ejercicio3/gv/DatosEntrada1.txt");
		
		// 2. Creamos el Vertice Inicial
		List<Integer> intersseccionesPendientesIniciales = new ArrayList<>();
		for (int i = 1; i < Datos3.N; i++) {
			intersseccionesPendientesIniciales.add(i);
		}
		VertexEj03Greedy vI = new VertexEj03Greedy(0, intersseccionesPendientesIniciales, 0.0, false);
		
		// 3. Creamos el Grafo Virtual
		var gV = EGraph.virtual(vI).type(Type.Min).pathType(PathType.Sum).heuristic(VertexEj03Greedy::heuristica).build();
		
		// 4. Ejercutamos los ALgortimos
		var aBT = BT.ofGreedy(gV);
		var aS = AStar.ofGreedy(gV);
		
		var solBT = aBT.search();
		var solAS = aS.search();
		
		System.out.println(solBT.get().getEdgeList());
		System.out.println(solAS.get().getEdgeList());

	}

}
