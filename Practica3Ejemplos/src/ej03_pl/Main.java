package ej03_pl;

import java.io.IOException;

import us.lsi.gurobi.GurobiLp;
import us.lsi.solve.AuxGrammar;

public class Main {
	public static void main(String[] args) throws IOException{
		DatosAlumnos.iniDatos("./src/ej03_pl/ejemplo3_1.txt");
		// lsi
		AuxGrammar.generate(DatosAlumnos.class, 
				"./src/ej03_pl/ej03.lsi", 
				"./src/ej03_pl/ej03.lp");
		
		var sol = GurobiLp.gurobi( "./src/ej03_pl/ej03.lp");
		
		if (sol.isEmpty()) {
			System.out.println("Sin solucion");
			return;
		}
		
		System.out.println(sol.get());
	}
}
