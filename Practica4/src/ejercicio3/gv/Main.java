package ejercicio3.gv;

import java.util.ArrayList;
import java.util.List;

import ejercicio3.Datos3;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {

	public static void main(String[] args) {
		// 1. Cargamos Datos
		Datos3.iniDatos("src/ejercicio3/gv/DatosEntrada1.txt");
		
		// 2. Vertice Inicia
		List<Integer> intersseccionesPendientesIniciales = new ArrayList<>();
		for (int i = 1; i < Datos3.N; i++) {
			intersseccionesPendientesIniciales.add(i);
		}
		VertexEj03 vI = new VertexEj03(0, intersseccionesPendientesIniciales, 0.0, false);
		
		// 3. Creamos el Grafo Virtual
		var gV = EGraph.virtual(vI).type(Type.Min).pathType(PathType.Sum).heuristic(VertexEj03::heuristica).build();
		
		// 4. Usamos Algoritmos
		var aBT = BT.of(gV);
		var aS = AStar.of(gV);
		
		var solBT = aBT.search();
		var solAs = aS.search();
		
		System.out.println(solBT.get().getEdgeList());
		System.out.println(solAs.get().getEdgeList());

	}

}
