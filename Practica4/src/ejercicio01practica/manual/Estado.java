package ejercicio01practica.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio01practica.gv.Vertex;
import ejercicio1.Datos1;

/*	En lugar de crear miles de copias, creamos un único objeto mutable llamado Estado que actúa como nuestro "tablero de juego".
 * 	Este objeto viajará hacia abajo en el árbol de decisión modificando sus datos internos "en vivo" (forward), 
 * 	y cuando termine de explorar una rama, deshará los cambios de forma exacta para volver atrás (backward). Así, la huella en memoria es mínima.*/

/*	verticeActual (Vertex): Reutilizaremos el récord Vertex que creamos en la práctica anterior para saber en qué candidato estamos y consultar sus reglas.*/

/*	solucion (List<Integer>): La lista de decisiones que estamos tomando en el camino actual (ej. [1, 0, 1]).*/

/*	valorSolucion (Double): La valoración acumulada de los candidatos elegidos hasta el momento (nuestra variable Vi).*/

/*	verticesAnteriores (List<Vertex>): Una lista donde guardamos el histórico de los vértices por los que hemos pasado para poder restaurar el estado al volver atrás.*/

public class Estado {

	private Vertex verticeActual;
	private List<Integer> solucion;
	private Double valorSolucion;
	private List<Vertex> verticesAnteriores;

	// Constructor: Inicializa el estado en el "Punto Cero"
	public Estado() {
		this.solucion = new ArrayList<>();
		this.valorSolucion = 0.0;
		this.verticesAnteriores = new ArrayList<>();

		this.verticeActual = new Vertex(0, 0.0, 0, Datos1.getCualidades(), new ArrayList<>());
	}

	// Métodos Getters simples para que el algoritmo BT pueda consultar - Siempre es
	// igual
	public Vertex vertice() {
		return this.verticeActual;
	}

	public Double valorSolucion() {
		return this.valorSolucion;
	}

	public List<Integer> solucion() {
		// Devolvemos una copia para evitar que se modifique la solución en curso
		// externamente
		return new ArrayList<>(this.solucion);
	}

	// Paso 2: El cálculo de la Cota en Estado.java

	/*
	 * Para que el Backtracking no sea una búsqueda a ciegas (fuerza bruta),
	 * necesita conocer la Cota de una acción antes de tomarla. La cota responde a:
	 * "Si tomo esta decisión ahora, ¿cuál es el máximo teórico de valoración al que aspiro en total?"
	 * .
	 */

	/*
	 * Cota = Valor Acumulado Actual + Peso de la Acción Actual + Heurística del
	 * Estado Siguiente
	 */

	/*
	 * Peso de la Acción Actual: Si decidimos contratar al candidato (action == 1),
	 * sumamos su valoración. Si pasamos de él (action == 0), sumamos 0.0.
	 */

	/*
	 * Heurística del Estado Siguiente: Le preguntamos a nuestro método
	 * Vertex.heuristica cuánto podríamos ganar de manera optimista desde el vecino
	 * resultante hacia adelante.
	 */

	// Método auxiliar: Convierte la acción binaria en el peso real (valoración)

	private Double getPeso(Integer indice, Integer action) {
		return action == 1 ? (double) Datos1.getValoracion(indice) : 0.0;
	}

	public Double cota(Integer action) {
		// 1. Averiguamos como seria el vertice vecino si tomaramos esa accion
		Vertex vecino = this.verticeActual.neighbor(action);

		// 2. Aplicamos la fórmula: lo que ya tengo + lo que gano ahora + lo que estimo
		// del futuro
		return this.valorSolucion + getPeso(this.verticeActual.indice(), action)
				+ Vertex.heuristica(vecino, null, null);
	}

	// Paso 4: Movimiento e Inmutabilidad Simulada (forward y backward)

	/*
	 * Como solo tenemos un único objeto Estado mutando en memoria, avanzar y
	 * retroceder deben ser operaciones perfectamente simétricas. Todo lo que se
	 * añade o suma en forward debe ser eliminado o restado en backward exactamente
	 * en el orden inverso (comportamiento de pila LIFO).
	 */

	public void forward(Integer action) {

		// 1. Registramos la decisión tomada en nuestra lista de soluciones
		this.solucion.add(action);

		// 2. Sumamos la valoración obtenida al acumulado total
		this.valorSolucion += getPeso(this.verticeActual.indice(), action);

		// 3. Guardamos el vértice actual en el historial antes de cambiarlo (para poder
		// volver)
		this.verticesAnteriores.add(this.verticeActual);

		// 4. Transicionamos al nuevo vértice vecino
		this.verticeActual = this.verticeActual.neighbor(action);
	}
	
	public void backward() {
		// 1. Recuperamos el último vértice en el que estuvimos y regresamos a él
		this.verticeActual = this.verticesAnteriores.remove(this.verticesAnteriores.size() - 1);
		
		// 2. Quitamos la última decisión que habíamos tomado de la lista
		Integer ultimaAccion = this.solucion.remove(this.solucion.size() - 1);
		
		// 3. Restamos la valoración que esa acción nos había aportado
		this.valorSolucion -= getPeso(this.verticeActual.indice(), ultimaAccion);
	}
}
