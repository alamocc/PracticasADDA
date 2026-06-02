package ejercicio2.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio2.Datos2;
import us.lsi.graphs.alg.PD;
import us.lsi.graphs.alg.PD.PDType;
import us.lsi.hypergraphs.GraphTree;
import us.lsi.hypergraphs.SimpleVirtualHyperGraph;

public class Main {

	public static void main(String[] args) {
		// 1. Cargar Datos
		Datos2.iniDatos("src/ejercicio2/manual/DatosEntrada1.txt");
		
		// 2. Vertice Inicial
		List<Integer> capacidadInicial =  new ArrayList<>();
		for (Integer i = 0; i < Datos2.getNumContenedores(); i++) {
			capacidadInicial.add(Datos2.getTamContenedor(i));
		}
		HV vI = new HV(0, capacidadInicial);
		
		// 3. Grafo Virtual
		var hG = SimpleVirtualHyperGraph.simpleVirtualHyperGraph(vI);
		
		// 4. Usamos el Algoritmo
		var pD = PD.dynamicProgrammingSearch(hG, PDType.Max);		// PDType.Max pq queremos maximizar
		pD.search();
		var arbol = pD.getSolutionsTree();
		
		var gT = GraphTree.graphTree(vI, arbol);
		System.out.println(gT.solution());

	}

}