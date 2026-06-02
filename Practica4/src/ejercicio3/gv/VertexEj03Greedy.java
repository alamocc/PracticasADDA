package ejercicio3.gv;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ejercicio3.Datos3;
import us.lsi.graphs.virtual.VirtualVertex;

public record VertexEj03Greedy(Integer indice, List<Integer> intersseccionesPendientes, Double duracionAcumulada, Boolean monumentosConsecutivosYaLogrados) implements VirtualVertex<VertexEj03Greedy, EdgeEj03Greedy, Integer>{

	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		// PASO 1: ¿Quedan intersecciones por visitar?
		if (this.intersseccionesPendientes.isEmpty()) {					// Si estan vacias, comprobamos que haya camino del ultimo al 0
			if (Datos3.tiempo(this.indice, 0) < 1000) {				// Si existe un camino desde el final al vertice inicial (se hace con lo del 1000, definidao en Datos3)
				actions.add(0);								// Añadimos el vertice final (el inicio) a nuestras action
			}
		}
		
		// PASO 2: Evaluar a qué intersecciones pendientes puedo ir
		for (Integer vecino: intersseccionesPendientes) {
			if (Datos3.tiempo(this.indice, vecino) < 1000) {			// SI hay camino de mi vertice al vecino
				Double nuevaDuracion = duracionAcumulada + Datos3.tiempo(this.indice, vecino);
				if (nuevaDuracion <= Datos3.maxTime) {											// RESTRICCIÓN (R1): Que la duración acumulada no supere maxTime
					actions.add(vecino);
				}
			}
		}
		return actions;
	}
	
	public Integer greedyAction() {									// BASICAMENTE LO QUE HACEMOS ES IR COGIENDO LA QUE MEJOR NOS CONVEZCA AHORA (menor esfuerzo actual), AUNQUE EN EL FUTURO NO SEAL EL MEJOR CAMINO
		List<Integer> accionesValidas = actions();
		
		// Si no hay acciones posibles (camino sin salida), devolvemos null
		if (accionesValidas.isEmpty()) {
			return null;
		}
		
		// Empezamos asumiendo que la primera acción es la mejor
		Integer mejorAccion = accionesValidas.get(0);
		Double menorEsfuerzo = Datos3.esfuerzo(this.indice, mejorAccion);
		
		// Comparamos con el resto de acciones disponibles
		for (int i = 1; i < accionesValidas.size(); i++) {
			Integer accionActual = accionesValidas.get(i);
			Double esfuerzoActual = Datos3.esfuerzo(this.indice, accionActual);
			
			// Como queremos MINIMIZAR el esfuerzo, buscamos el valor más pequeño
			if (esfuerzoActual < menorEsfuerzo) {
				menorEsfuerzo = esfuerzoActual;
				mejorAccion = accionActual;
			}
		}
		
		return mejorAccion;
	}
	@Override
	public VertexEj03Greedy neighbor(Integer a) {
		Integer nuevoIndice = a;	// Ahora no es +1, pq no estamos siguiendo un indice, si no la interseccion a la que voy
		List<Integer> nuevasIntersseccionesPendientes = new ArrayList<>(intersseccionesPendientes);
		Double nuevaDuracionAcumulada = duracionAcumulada;
		Boolean nuevoMonumentosConsecutivosYaLogrados = monumentosConsecutivosYaLogrados;
		
		nuevasIntersseccionesPendientes.remove(a);
		
		nuevaDuracionAcumulada = duracionAcumulada + Datos3.tiempo(this.indice, a);
		
		if (Datos3.sonMonumentos(this.indice, a)) {				// Para que se cumpla RESTRICCION (R2): Hay 2 monumentos consecutivos
			nuevoMonumentosConsecutivosYaLogrados = true;
		}
		
		return new VertexEj03Greedy(nuevoIndice, nuevasIntersseccionesPendientes, nuevaDuracionAcumulada, nuevoMonumentosConsecutivosYaLogrados);
	}

	@Override
	public EdgeEj03Greedy edge(Integer a) {
		return new EdgeEj03Greedy(this, this.neighbor(a), a, Datos3.esfuerzo(this.indice, a));
	}
	
	public Boolean goal() {
		return this.intersseccionesPendientes.isEmpty() && this.indice == 0;		// RESTRICCION (R3): Hemos recorrido todas las interssecciones y estamos en el inicio
	}
	
	public Boolean goalHasSolution() {						// RESTRICCION (R2): La solucion es válida solo si exiten 2 intersecciones consecutivas que alberguen un monumento de interés
		return this.monumentosConsecutivosYaLogrados;
	}
	
	public static Double heuristica(VertexEj03Greedy v, Predicate<VertexEj03Greedy> goal, VertexEj03Greedy fin) {
		// Para Dijkstra/A* básico devolvemos 0.0,	lo que estás haciendo en la práctica es convertir el algoritmo A* en el algoritmo de Dijkstra.				PARA ESTE EJERCICIO ES ASI SIEMPRE
		return 0.0;
	}

}
