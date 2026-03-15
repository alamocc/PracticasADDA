package ej02_ag;

import ej02_pl.DatosSubconjunto;
import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;

public class Main {
	
	public static void main(String[] args) {
		// 1. Ver y entender los datos del problema. Cargarlos
		DatosSubconjunto.iniDatos("./src/ej02_ag/ejemplo2_1.txt");
		
		// 2. P. Hacer la clase solucion
		// 2. Elegir interfaz cromosoma e implementar
		// 2.1 Función Fitness
		// 2.2 Cuidado con los defaults
		
		// 3. Copiar y pegar las constantes del algoritmo.
		AlgoritmoAG.ELITISM_RATE  = 0.30;
		AlgoritmoAG.CROSSOVER_RATE = 0.8;
		AlgoritmoAG.MUTATION_RATE = 0.7;
		AlgoritmoAG.POPULATION_SIZE = 50;
		
		StoppingConditionFactory.NUM_GENERATIONS = 5000;
		StoppingConditionFactory.stoppingConditionType = StoppingConditionFactory.StoppingConditionType.GenerationCount;
		
		// 4. Crear el algoritmo y ejecutar
		var algG = AlgoritmoAG.of(new CroEj02());
		algG.ejecuta();
		var sol = algG.bestSolution();
		
		// 5. Procesar solucion
		System.out.println(sol);
	}
}
