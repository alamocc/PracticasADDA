package tests.ple;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ejercicio3.Datos3;
import us.lsi.gurobi.GurobiLp;
import us.lsi.solve.AuxGrammar;

public class TestsE3 {
	// Si da error en el parser: El resultado de invocar el metodo getNumCandidatos con {} es null, hay que poner un EXPORTS en el module info
	
	// 1. Ver y entender los datos de entrada.
	
	// 2. Crear (o copiar) la clase de datos del problema y cargar los datos del problema
	
	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.of("es", "ES"));
		
		for(Integer i: List.of(1, 2, 3)) {
			Datos3.iniDatos("datos_entrada/ejercicio3/DatosEntrada"+i+".txt");
			// 3. Escribo el modelo LSI en el archivo .lsi
			
			// 4. Traduzco el .lsi a lo que entiende Gurobi (.lp):
			AuxGrammar.generate(Datos3.class, "modelos/Ejercicio3.lsi", "lp/Ejercicio3-"+i+".lp");
			
			// 5. Proceso la solucion:
			var solucion = GurobiLp.gurobi("lp/Ejercicio3-"+i+".lp");
			if(solucion.isPresent()) {
				System.out.println(solucion.get().toString((vble, valor)-> valor > 0));
			} else {
				System.out.println("\n\nModelo sin solucion");
			}
		}
	}
}
