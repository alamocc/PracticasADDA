package ejercicio2.gv;

import java.util.ArrayList;
import java.util.List;

import ejercicio2.Datos2;
import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {
	public static void main(String[] args) {
		Datos2.iniDatos("src/ejercicio2/gv/DatosEntrada1.txt");
		
		List<Integer> capacidadInicial = new ArrayList<>();
		for(int i = 0; i < Datos2.getNumContenedores(); i++) {
			capacidadInicial.add(Datos2.getTamContenedor(i));
		}
		
		Vertex vI = new Vertex(0, capacidadInicial);
		
		EGraph<Vertex, Edge> gV =
				EGraph.virtual(vI)
				.pathType(PathType.Sum)
				.type(Type.Max)
				.heuristic(Vertex::heuristica)
				.build();
		
		// 4. Usamos Algoritmos
		var aBT = BT.of(gV);
		var aS = AStar.of(gV);

		var solBT = aBT.search();
		var solAs = aS.search();

		System.out.println(solBT.get().getEdgeList());
		System.out.println(solAs.get().getEdgeList());
	}
}
