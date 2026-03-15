package ejercicio4;

import java.util.List;

import us.lsi.ag.PermutationData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;

public class Cromosoma4 implements PermutationData<Solucion4>{
	
	public Cromosoma4(String file) {
		Datos4.iniDatos(file);
	}
	
	@Override
	public Integer size() {
		return Datos4.N;
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		Double goal = esfuerzo(value);
		Double totalDuracion = totalDuracion(value);
		Double totalConsecutivos = totalConsecutivos(value);
		
		return -goal - (1000 * totalDuracion) - (1000 * totalConsecutivos);
	}

	private Double esfuerzo(List<Integer> value) {
		Double esfuerzo = 0.0;
		
		// Sumo los tramos intermedios (bucle hasta n - 1)
		for(int i = 0; i < size() - 1; i++) {
			int origen = value.get(i);
			int destino = value.get(i+1);
			
			esfuerzo += Datos4.esfuerzo(origen, destino);
		}
		
		// Cerramos el ciclo (ultima interseccion a la primera)
		Integer ultimo = value.get(size() - 1);
		Integer primero = value.get(0);
		
		esfuerzo += Datos4.esfuerzo(ultimo, primero);
		return esfuerzo;
	}

	private Double totalDuracion(List<Integer> value) {
		Double duracion = 0.0;
		
		// Sumamos la duracion de los tramos intermedios
		for(int i = 0; i < size() - 1; i++) {
			int origen = value.get(i);
			int destino = value.get(i+1);
			
			duracion += Datos4.tiempo(origen, destino);
		}
		
		// Sumamos la duracion de volver a casa
		Integer ultimo = value.get(size() - 1);
		Integer primero = value.get(0);
		
		duracion += Datos4.tiempo(ultimo, primero);
		
		// Calculamos la penalizacion
		if (duracion <= Datos4.maxTime) {
			return 0.0;
		}
		
		// Si nos pasamos, devolvemos cuanto tiempo extra hemos gastado
		return duracion - Datos4.maxTime;
	}

	private Double totalConsecutivos(List<Integer> value) {
		boolean hayDosConsecutivos = false;
		
		// Comprobamos tramos intermedios
		for(int i = 0; i < size() - 1; i++) {
			int origen = value.get(i);
			int destino = value.get(i+1);
			
			// Si encontramos dos intersecciones seguidas con monumentos, marcamos true y rompemos ciclo
			if (Datos4.sonMonumentos(origen, destino)) {
				hayDosConsecutivos = true;
				break;
			}
		}
		
		// Si no lo hemos encontrado en el camino de ida, miramos en el de vuelta
		if(!hayDosConsecutivos) {
			Integer ultimo = value.get(size() - 1);
			Integer primero = value.get(0);
			
			if(Datos4.sonMonumentos(ultimo, primero) ) {
				hayDosConsecutivos = true;
			}
		}
		
		// Devolvemos la multa: Si hay dos consecutivos, devolvemos 0. En caso contrario, devolvemos 1
		if(hayDosConsecutivos) {
			return 0.0;
		}
		return 1.0;
	}

	@Override
	public Solucion4 solution(List<Integer> value) {
		return Solucion4.create(value);
	}

	@Override
	public ChromosomeType type() {
		return ChromosomeType.Permutation;
	}

}
