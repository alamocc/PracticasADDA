package tests;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.fitting.WeightedObservedPoint;

import ejercicios.Ejercicio5;
import us.lsi.common.Pair;
import us.lsi.common.String2;
import us.lsi.curvefitting.DataFile;
import us.lsi.curvefitting.Fit;
import us.lsi.curvefitting.GenData;
import us.lsi.curvefitting.PowerLog;
import us.lsi.graphics.MatPlotLib;


public class TestEjercicio5 {
	
	private static Integer nMin = 100; // n mínimo para el cálculo de potencia
	private static Integer nMax = 2000; // n máximo para el cálculo de potencia; si son demasiado lentas, reducir
	private static Integer razon = 200; // incremento en los valores de n del cálculo de potencia
	private static Integer nIter = 100; // número de iteraciones para cada medición de tiempo; si salen con mucha dispersion, aumenta
	private static Integer nIterWarmup = 1000; // número de iteraciones para warmup
	
	public static void genData (Consumer<Integer> algorithm, String file) {
		Function<Integer,Long> f1 = GenData.time(algorithm);
		GenData.tiemposEjecucionAritmetica(f1,file,nMin,nMax,razon,nIter,nIterWarmup);

	}
	
	public static void show(Fit pl, String file, String label) {
		List<WeightedObservedPoint> data = DataFile.points(file);
		pl.fit(data);
		MatPlotLib.show(file, pl.getFunction(), String.format("%s = %s",label,pl.getExpression()));
	}
	
	
	         
	public static void showCombined() {
		MatPlotLib.showCombined("Tiempos",
				List.of("resources/ejercicio5RecDouble.txt","resources/ejercicio5RecBigInteger.txt","resources/ejercicio5ItDouble.txt",
						"resources/ejercicio5ItBigInteger.txt"), 
				List.of("Recursiva-Double","Recursiva-BigInteger","Iterativa-Double", "Iterativa-BigInteger"));
	}
		
	public static void main(String[] args) {
		genData(t -> Ejercicio5.ejercicio5RecDouble(t),"resources/ejercicio5RecDouble.txt");
		genData(t -> Ejercicio5.ejercicio5RecBigInteger(t),"resources/ejercicio5RecBigInteger.txt");
		genData(t -> Ejercicio5.ejercicio5ItDouble(t),"resources/ejercicio5ItDouble.txt");
		genData(t -> Ejercicio5.ejercicio5ItBigInteger(t),"resources/ejercicio5ItBigInteger.txt");
		
		
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/ejercicio5RecDouble.txt","Recursiva-Double");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/ejercicio5RecBigInteger.txt","Recursiva-BigInteger");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/ejercicio5ItDouble.txt","Iterativa-Double");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/ejercicio5ItBigInteger.txt","Iterativa-BigInteger");
		
		showCombined();

	}
}