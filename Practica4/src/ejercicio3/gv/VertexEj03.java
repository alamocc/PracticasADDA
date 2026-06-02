package ejercicio3.gv;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ejercicio3.Datos3;
import us.lsi.graphs.virtual.VirtualVertex;

public record VertexEj03(Integer indice, List<Integer> intersseccionesPendientes, Double duracionAcumulada, Boolean monumentosConsecutivosYaLogrados) implements VirtualVertex<VertexEj03, EdgeEj03, Integer> {

	@Override
	public List<Integer> actions() {					// Son las intersecciones por las que pasamos
		List<Integer> actions = new ArrayList<>();
		
		// PASO 1: ¿Quedan intersecciones por visitar?
		if (this.intersseccionesPendientes.isEmpty()) {
			if (Datos3.tiempo(this.indice, 0) < 1000) {		// Si hay camino desde el vertice en el que estamos al inicio (se hace lo de 1000, pq en datos 3 el metodo de tiempo nos dice que si es menor que 1000 existe camino desde un vertice al otro)
				actions.add(0);		// Añadimos el vertice final (el inicio) a nuestras action
			}
			return actions;
		}
		
		// PASO 2: Evaluar a qué intersecciones pendientes puedo ir
		for (Integer vecino: intersseccionesPendientes) {
			if (Datos3.tiempo(this.indice, vecino) < 1000) {		// Si hay camino desde mi vertice al vecino
				Double nuevaDuracion = this.duracionAcumulada + Datos3.tiempo(this.indice, vecino);
				if (nuevaDuracion <= Datos3.maxTime) {					// RESTRICCIÓN (R1): Que la duración acumulada no supere maxTime
					actions.add(vecino);
				}
			}
		}
		return actions;
	}

	@Override
	public VertexEj03 neighbor(Integer a) {
		Integer nuevoIndice = a;	// Ahora no es +1, pq no estamos siguiendo un indice, si no la interseccion a la que voy
		List<Integer> nuevasIntersseccionesPendientes = new ArrayList<>(intersseccionesPendientes);
		Double nuevaDuracionAcumulada = this.duracionAcumulada;
		Boolean nuevosMonumentosConsecutivosYaLogrados = this.monumentosConsecutivosYaLogrados;
		
		
		nuevasIntersseccionesPendientes.remove(a);
		
		nuevaDuracionAcumulada = this.duracionAcumulada + Datos3.tiempo(this.indice, a);
		
		if (Datos3.sonMonumentos(this.indice, a)) {				// Para que se cumpla RESTRICCION (R2): Hay 2 monumentos consecutivos
			nuevosMonumentosConsecutivosYaLogrados = true;
		}
		
		
		return new VertexEj03(nuevoIndice, nuevasIntersseccionesPendientes, nuevaDuracionAcumulada, nuevosMonumentosConsecutivosYaLogrados);
	}

	@Override
	public EdgeEj03 edge(Integer a) {
		return new EdgeEj03(this, this.neighbor(a), a, Datos3.esfuerzo(this.indice, a));	// El peso de las aristas en el esfuerzo de ir de la actual a la siguiente
	}
	
	public Boolean goal() {
		return this.indice.equals(0) && this.intersseccionesPendientes.isEmpty();		// RESTRICCION (R3): Hemos recorrido todas las interssecciones y estamos en el inicio
	}
	
	public Boolean goalHasSolution() {						// RESTRICCION (R2): La solucion es válida solo si exiten 2 intersecciones consecutivas que alberguen un monumento de interés
		return this.monumentosConsecutivosYaLogrados;
	}
	
	public static Double heuristica(VertexEj03 v, Predicate<VertexEj03> goal, VertexEj03 fin) {
		// Para Dijkstra/A* básico devolvemos 0.0,	lo que estás haciendo en la práctica es convertir el algoritmo A* en el algoritmo de Dijkstra.				PARA ESTE EJERCICIO ES ASI SIEMPRE
		return 0.0;
	}
}
