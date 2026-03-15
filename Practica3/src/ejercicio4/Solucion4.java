package ejercicio4;

import java.util.List;
import java.util.stream.Collectors;

import us.lsi.common.List2;

public class Solucion4 {

    public static Solucion4 create(List<Integer> ls) {
        return new Solucion4(ls);
    }

    private String camino, txt;
    private Double totalTime, totalEffort;
    private Double avgTime, avgEffort, monCons;

    private Solucion4(List<Integer> ls) {
		txt = "";
		// aux ya incluye la vuelta al origen, ¡nos facilita la vida!
		List<Integer> aux = List2.addLast(ls, ls.getFirst());
		
		camino = aux.stream().map(i -> Datos4.getVertex(i)+"")
			.collect(Collectors.joining(" ->\n\t", "Recorrido:\n\t", ""));
			
		// 1. Inicializamos a 0.0
		totalTime = 0.0; 
		totalEffort = 0.0;
		monCons = 0.0; 
		
		// 2. Calculamos recorriendo 'aux'
		for (int i = 0; i < aux.size() - 1; i++) {
			Integer origen = aux.get(i);
			Integer destino = aux.get(i + 1);
			
			totalTime += Datos4.tiempo(origen, destino);
			totalEffort += Datos4.esfuerzo(origen, destino);
			
			if (Datos4.sonMonumentos(origen, destino)) {
				monCons += 1.0;
			}
		}
		
		// 3. Ahora las medias funcionarán perfectamente porque totalTime ya no es null
		avgTime = totalTime / Datos4.N;
		avgEffort = totalEffort / Datos4.N;
	}

	@Override
    public String toString() {
    	String s1 = String.format("\nTiempos (total/medio/maximo): %.1f / %.1f / %.1f", totalTime, avgTime, Datos4.maxTime);
    	String s2 = String.format("\nEsfuerzos (total/medio): %.1f / %.1f", totalEffort, avgEffort);
    	String s3 = String.format("\nNº de lugares con monumento consecutivos a otro con monumento: %d", monCons.intValue());
        return txt+camino+s1+s2+s3;
    }

}