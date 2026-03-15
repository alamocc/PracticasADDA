package tests.ag;

import java.util.List;
import java.util.Locale;

import ejercicio4.Cromosoma4;
import us.lsi.ag.agchromosomes.AlgoritmoAG;
import us.lsi.ag.agstopping.StoppingConditionFactory;

public class TestsE4 {
	public static void main(String[] args) {
		Locale.setDefault(Locale.of("en", "US"));
		
		AlgoritmoAG.ELITISM_RATE  = 0.10;
		AlgoritmoAG.CROSSOVER_RATE = 0.95;
		AlgoritmoAG.MUTATION_RATE = 0.8;
		AlgoritmoAG.POPULATION_SIZE = 100;	// Aumentar si no se obtiene un cromosoma optimo
		
		StoppingConditionFactory.NUM_GENERATIONS = 5000;	// Aumentar si no se obtiene un cromosoma optimo
		StoppingConditionFactory.stoppingConditionType = StoppingConditionFactory.StoppingConditionType.GenerationCount;
		
		List.of(1,2,3).forEach(i -> {
			var cr = new Cromosoma4("datos_entrada/ejercicio4/DatosEntrada"+i+".txt");
			var ag = AlgoritmoAG.of(cr);
			ag.ejecuta();
			

			System.out.println("================================");
			System.out.println(ag.bestSolution());
			System.out.println("================================");
		});
	}
}
