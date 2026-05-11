package ej03;

import java.util.ArrayList;
import java.util.List;

import us.lsi.graphs.alg.AStar;
import us.lsi.graphs.alg.BT;
import us.lsi.graphs.virtual.EGraph;
import us.lsi.graphs.virtual.EGraph.Type;
import us.lsi.path.EGraphPath.PathType;

public class Main {
	 public static void main(String[] args) {
		 DatosAlumnos.iniDatos("src/ej03/alumnos_1.txt");
		 List<Integer> plazas = new ArrayList<>();
		 
		 for (int i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			 plazas.add(DatosAlumnos.getTamGrupo());
		 }
		 
		 VertexAlumnos start = new VertexAlumnos(0, plazas);
		 
		 var gV = EGraph.virtual(start)
				 .type(Type.Max)
				 .pathType(PathType.Sum)
				 .heuristic(VertexAlumnos::heuristic)
				 .build();
		 
		 // Algoritmo a*
		 var algAStar = AStar.ofGreedy(gV);
		 var solutionAStar = algAStar.search();
		 
		 System.out.println("Solucion Algoritmo AStar: " + solutionAStar.get().getEdgeList());
		 
		 // Algoritmo de Backtracking -> Falta heurística, por lo que no obtiene el mejor resultado
		 var algBacktracking = BT.ofGreedy(gV);
		 var solutionBacktracking = algBacktracking.search();
		 
		 System.out.println("Solucion Algoritmo Backtracking: " + solutionBacktracking.get().getEdgeList());
	 }
}
