package ejercicio1.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio1.Datos1;
import ejercicio1.gv.Vertex;

public class Estado {
	
	Vertex verticeActual;
	List<Integer> solucion;       // Guarda las acciones tomadas [1, 0, 0, 1...]
	Double valorSolucion;         // Guarda la valoración acumulada actual
	List<Vertex> verticesAnteriores; // Para poder hacer la "vuelta atrás"
	
	public Estado() {																			// Esto y lo de arriba siempre IGUAL
		solucion = new ArrayList<Integer>();
		valorSolucion = 0.0;
		verticesAnteriores = new ArrayList<>();
		
		// Usamos el método limpio que hicimos en Ej01Vertex
		verticeActual = new Vertex(0, 0.0, 0, Datos1.getCualidades(), new ArrayList<>());
	}
	
	public Vertex vertice() {						// Siempre Igual
		return verticeActual;
	}
	
	public Double valorSolucion() {					// Siempre Igual
		return valorSolucion;
	}
	
	public List<Integer> solucion() {				// Siempre Igual
		return new ArrayList<>(solucion);
	}
	
	// Método auxiliar: Si la acción es 1, devolvemos la valoración. Si es 0, devolvemos 0.
	private Double getPeso(Integer indice, Integer action) {
		return action == 1 ? (double) Datos1.getValoracion(indice) : 0.0;
	}
	
	public Double cota(Integer action) {							// EN VD SIEMPRE IGUAL (solo cambia el peso que es la valoracion)
		// ¡TRUCO PRO! Para calcular la cota real (f = g + h) del siguiente paso:
		// 1. Calculamos el valor actual + lo que ganamos con esta acción
		// 2. Le sumamos la estimación optimista (heurística) del vértice RESULTANTE (el vecino)
		Vertex vecino = verticeActual.neighbor(action);
		
		// Calculamos la estimación total:
	    return valorSolucion                                      // PASADO: Los puntos que ya tengo asegurados en mi marcador.
	         + getPeso(verticeActual.indice(), action)            // PRESENTE: Los puntos que gano cruzando esta puerta en concreto.
	         + Vertex.heuristica(vecino, null, null);             // FUTURO: Lo que la "bola de cristal" (heurística) predice desde la siguiente habitación.
	}
	
	public void forward(Integer action) {							// EN VD SIEMPRE IGUAL (solo cambia el peso que es la valoracion)
		// 1. Guardamos la decisión y sumamos su valor
		solucion.add(action);
		valorSolucion += getPeso(verticeActual.indice(), action);		// El peso realmente es la valoracion, que lo calculamos mas arriba
		// 2. Guardamos la habitación actual en la historia
		verticesAnteriores.add(verticeActual);
		// 3. Avanzamos a la siguiente habitación
		verticeActual = verticeActual.neighbor(action);
	}
	
	public void backward() {										// EN VD SIEMPRE IGUAL (solo cambia el peso que es la valoracion)
		// 1. Retrocedemos a la habitación anterior
		verticeActual = verticesAnteriores.remove(verticesAnteriores.size() - 1);
		// 2. Borramos la última decisión que tomamos y restamos su valor
		Integer ultimaAccion = solucion.remove(solucion.size() - 1);
		valorSolucion -= getPeso(verticeActual.indice(), ultimaAccion);
	}

}
