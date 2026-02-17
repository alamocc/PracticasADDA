package tests;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.fitting.WeightedObservedPoint;

import ejercicios.Ejercicio4;
import us.lsi.common.Pair;
import us.lsi.common.String2;
import us.lsi.curvefitting.DataFile;
import us.lsi.curvefitting.Fit;
import us.lsi.curvefitting.GenData;
import us.lsi.curvefitting.PowerLog;
import us.lsi.graphics.MatPlotLib;

public class TestEjercicio4 {
	private static Integer nMin = 10; // n mínimo para el cálculo de potencia
	private static Integer nMax = 2000; // n máximo para el cálculo de potencia
	private static Integer razon = 200; // incremento en los valores de n del cálculo de potencia
	private static Integer nIter = 50; // número de iteraciones para cada medición de tiempo
	private static Integer nIterWarmup = 1000; // número de iteraciones para warmup
	
	private static Double a = 3.;
	
	public static void genData (Consumer<Integer> algorithm, String file) {
		Function<Integer,Long> f1 = GenData.time(algorithm);
		GenData.tiemposEjecucionAritmetica(f1,file,nMin,nMax,razon,nIter,nIterWarmup); 
		// SIEMPRE tiemposEjecucionAritmetica, geometrica va fatal

	}
	
	public static void show(Fit pl, String file, String label) {
		List<WeightedObservedPoint> data = DataFile.points(file);
		pl.fit(data);
		MatPlotLib.show(file, pl.getFunction(), String.format("%s = %s",label,pl.getExpression()));
	}
	
    
	public static void showCombined() {
		MatPlotLib.showCombined("Tiempos",
				List.of("resources/RecDouble.txt","resources/ItDouble.txt","resources/RecBigInteger.txt", "resources/ItBigInteger.txt"), 
				List.of("Recursiva-Double","Iterativa-Double","Recursiva-BigInteger", "Iterativa-BigInteger"));
	}

	
	public static void main(String[] args) {
		// Para cada metodo que quiero medir la complejidad
		/*
		 * ESTO SOLO HAY QUE GENERARLO UNA VEZ, UNA VEZ QUE ESTE SE COMENTA PARA QUE NO TARDE TANTO EN EJECUTARLO, 
		 * se guarda en la ruta que le pasamos como segundo parametro.
		 * 
		 * genData(t -> Ejercicio4.funcRecDouble(t),"resources/RecDouble.txt");
		 * genData(t -> Ejercicio4.funcItDouble(t),"resources/ItDouble.txt");
		 * genData(t -> Ejercicio4.funcRecBig(t),"resources/RecBigInteger.txt");
		 * genData(t -> Ejercicio4.funcItBig(t),"resources/ItBigInteger.txt"); 
		 */
		
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/RecDouble.txt","Recursiva-Double");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))),"resources/ItDouble.txt","Iterativa-Double");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))), "resources/RecDouble.txt","Recursiva-Double");
		show(PowerLog.of(List.of(Pair.of(2, 0.),Pair.of(3, 0.))),"resources/ItDouble.txt","Iterativa-Double");
		
		showCombined();
		
		}

	
}