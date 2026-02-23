package ej02_pl;

import java.io.IOException;

import us.lsi.gurobi.GurobiLp;
import us.lsi.gurobi.GurobiSolution;
import us.lsi.solve.AuxGrammar;

public class Main {
	
	public static void main(String[] args) throws IOException{
		DatosSubconjunto.iniDatos("./src/ej02_pl/ejemplo2_1.txt");
		// lsi
		AuxGrammar.generate(DatosSubconjunto.class, 
				"./src/ej02_pl/ej02.lsi", 
				"./src/ej02_pl/ej02.lp");
		
		var sol = GurobiLp.gurobi( "./src/ej02_pl/ej02.lp");
		
		if (sol.isEmpty()) {
			System.out.println("Sin solucion");
			return;
		}
		
		System.out.println(sol.get().toString((s, d) -> d>.0));
	}
	
	private static void MuestreSeleccionados(GurobiSolution gS) {
		for(Integer i = 0; i < DatosSubconjunto.getNumSubconjuntos(); i++) {
			String var = "x_"+i;
			if(gS.values.get(var) > 0) {
				System.out.println("Elijo" + i);
			}
		}
	}
}

