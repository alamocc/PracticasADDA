package ej03_hg;

import java.util.ArrayList;
import java.util.List;

import us.lsi.graphs.alg.PD;
import us.lsi.graphs.alg.PD.PDType;
import us.lsi.hypergraphs.GraphTree;
import us.lsi.hypergraphs.SimpleVirtualHyperGraph;

public class MainHG {

	public static void main(String[] args) {
		// 1. Cargar datos del problema
			// 1.2. Crear clase solucion		(todavia no lo hemos hecho)
		DatosAlumnos.iniDatos("src/ej03/ejemplo3_1.txt");
		
		// 2. Record hipervertices
		
		// 3. Hyperarista
		
		// 4. Construir el hypergrafo
		List<Integer> plazas = new ArrayList<>();
		for (Integer i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			plazas.add(DatosAlumnos.getTamGrupo());
		}
		
		var vI = new Ej03HV(0, plazas);
		var hG = SimpleVirtualHyperGraph.simpleVirtualHyperGraph(vI);
		
		// 5. Crear y ejecutar PD (Programacion Dinamica)									// SIEMPRE ES TODO IGUAL
		var pD = PD.dynamicProgrammingSearch(hG, PDType.Max);		// Minimizar, pues Min
		pD.search();
		var arbol = pD.getSolutionsTree();
		
		// 6. Procesar la SOL
		var gT = GraphTree.graphTree(vI, arbol);
		System.out.println(gT.solution());

	}

}
