package ej01_hg;

import us.lsi.graphs.alg.PD;
import us.lsi.graphs.alg.PD.PDType;
import us.lsi.hypergraphs.GraphTree;
import us.lsi.hypergraphs.SimpleVirtualHyperGraph;

public class Main {
	public static void main(String[] args) {
		// 1. Cargar datos programa
		DatosMulticonjunto.iniDatos("src/ej01/ejemplo1_1.txt");
		// 1.5 Crear clase solucion
		
		// 2. Record hipervertice
		
		// 3. Hiperarista
		
		// 4. Construir el Hipergrafo
		var vI = new Ej01HV(0, DatosMulticonjunto.SUMA);
		var hG = SimpleVirtualHyperGraph.simpleVirtualHyperGraph(vI);
		// 5. Crear y ejecutar PD
		var pD = PD.dynamicProgrammingSearch(hG, PDType.Min);
		pD.search();
		var arbol = pD.getSolutionsTree();
		// 6. Procesar la solucion
		var gT = GraphTree.graphTree(vI, arbol);
		System.out.println(gT.solution());
		
		
	}
}
