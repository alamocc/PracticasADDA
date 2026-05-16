package ejemplo01hg;

import us.lsi.graphs.alg.PD;
import us.lsi.graphs.alg.PD.PDType;
import us.lsi.hypergraphs.GraphTree;
import us.lsi.hypergraphs.SimpleVirtualHyperGraph;


public class Main {
	public static void main(String[] args) {
		DatosMulticonjunto.iniDatos("src/ejemplo01hg/ejemplo1_1.txt");
		
		// Construir el hipergrafo virtual:
		var vI = new Ejemplo01HV(0, DatosMulticonjunto.SUMA);
		var hG = SimpleVirtualHyperGraph.simpleVirtualHyperGraph(vI);
		
		// Crear y ejecutar la PD:
		var pD = PD.dynamicProgrammingSearch(hG, PDType.Min);
		pD.search();
		var arbol = pD.getSolutionsTree();
		
		var gT = GraphTree.graphTree(vI, arbol);
		System.out.println(gT.solution());
	}
}
