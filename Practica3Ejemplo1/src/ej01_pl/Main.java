package ej01_pl;

import java.io.IOException;

import us.lsi.gurobi.GurobiLp;
import us.lsi.solve.AuxGrammar;

public class Main {
	
	public static void main(String[] args) throws IOException {
		// 1. Ver y entender los datos de entrada.
		
		// 2. Crear (o copiar) la clase de datos del problema y cargar los datos del problema.
		
		DatosMulticonjunto.iniDatos("./src/ej01_pl/ejemplo1_1.txt");
		// 3. Escribir el modelo en ".lsi".
		
		// 4. Traducir el lsi a lo que entiende Gurobi y ejecutar Gurobi.
		AuxGrammar.generate(DatosMulticonjunto.class, 
				"./src/ej01_pl/ej01.lsi", 
				"./src/ej01_pl/ej01.lp");
		var sol = GurobiLp.gurobi("./src/ej01_pl/ej01.lp");
		// 5. Procesar la solución.
		
		if (sol.isEmpty()) {
			System.out.println("Sin solucion");
			return;
		}
		
		System.out.println(sol.get());
	}
}
