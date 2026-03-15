package tests.ag;

import java.util.List;
import java.util.Locale;

import ejercicio3.Cromosoma3;
import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;

public class TestsE3 {
	public static void main(String[] args) {
		Locale.setDefault(Locale.of("en", "US"));
		
		AlgoritmoAG.ELITISM_RATE  = 0.10;
		AlgoritmoAG.CROSSOVER_RATE = 0.95;
		AlgoritmoAG.MUTATION_RATE = 0.8;
		AlgoritmoAG.POPULATION_SIZE = 2000;	// Aumentar si no se obtiene un cromosoma optimo
		
		StoppingConditionFactory.NUM_GENERATIONS = 2000;	// Aumentar si no se obtiene un cromosoma optimo
		StoppingConditionFactory.stoppingConditionType = StoppingConditionFactory.StoppingConditionType.GenerationCount;
		
		List.of(1,2,3).forEach(i -> {
			var cr = new Cromosoma3("datos_entrada/ejercicio3/DatosEntrada"+i+".txt");
			var ag = AlgoritmoAG.of(cr);
			ag.ejecuta();
			

			System.out.println("================================");
			System.out.println(ag.bestSolution());
			System.out.println("================================");
		});
	}
}
