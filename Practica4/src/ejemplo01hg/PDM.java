package ejemplo01hg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.lsi.common.Pair;

/*	La regla de oro es: Si ya he resuelto este subproblema antes, no lo calculo de nuevo; simplemente lo busco en mi memoria.*/

// Paso 1: La estructura base y la Memoria (Ej01PDM.java)

// La Clave (Ej01HV): Es el subproblema actual (el estado).

// El Valor (Pair<Integer, Double>): Guarda la respuesta a ese subproblema.

// Integer: La mejor acción que debo tomar estando en este estado.

// Double: El peso óptimo (el coste o beneficio) que conseguiré desde este estado hasta el final si tomo esa mejor acción.
public class PDM {

	// Asocia un problema (Ej01HV) con su solución óptima (Acción, Peso)
	private Map<Ejemplo01HV, Pair<Integer, Double>> memoria;

	public static void main(String[] args) {
		// Cargamos los datos
		DatosMulticonjunto.iniDatos("src/ejemplo01hg/ejemplo1_1.txt");

		PDM pd = new PDM();

		// Creamos el problema inicial: índice 0 y la suma objetivo completa
		Ejemplo01HV inicial = new Ejemplo01HV(0, DatosMulticonjunto.getSuma());

		System.out.println(pd.search(inicial));
	}

	// Método principal que inicializa el proceso
	public List<Pair<Integer, Double>> search(Ejemplo01HV inicial) {
		// 1. Inicializamos la memoria en blanco para cada nueva búsqueda
		this.memoria = new HashMap<>();

		// 2. Llamamos a la función recursiva central que rellenará toda la memoria
		pd(inicial);

		// 3. Una vez la memoria está llena, reconstruimos el camino de la solución

		return solucion(inicial);
	}

	// Paso 2: La Función Recursiva pd(Ej01HV problema)

	/*
	 * Este método es el que viaja por el árbol de decisiones, pero con un
	 * "superpoder". En Backtracking, explorábamos todo. Aquí, la primera regla es
	 * ser vagos pero eficientes:
	 * "Si ya lo he pensado antes, no lo vuelvo a pensar".
	 */
	private Pair<Integer, Double> pd(Ejemplo01HV problema) {
		// 1. CONSULTA DE MEMORIA: ¿Ya he calculado esto antes?
		if (memoria.containsKey(problema)) {
			return memoria.get(problema);
		}

		// 2. CASO BASE: ¿He llegado al final de mi lista de elementos?
		if (problema.isBaseCase()) {
			// Le preguntamos al vértice si la solución es válida (sumaRestante == 0)
			if (problema.baseCaseWeight() == null) {
				return null; // No cuadra, camino inválido
			}
			// Si cuadra, devolvemos acción 'null' (no hay más que elegir) y el peso base
			// (0.0)
			Pair<Integer, Double> sp = new Pair<>(null, 0.0);
			memoria.put(problema, sp);
			return sp;
		}

		// 3. CASO RECURSIVO: Buscar la mejor acción

		Pair<Integer, Double> mejorSp = null;

		// Empezamos con el valor más alto posible porque queremos MINIMIZAR el peso
		// final
		Double mejorPeso = Double.MAX_VALUE;
		Double peso;

		for (Integer a : problema.actions()) {
			// En los hipergrafos, una acción puede generar varios subproblemas.
			// Iteramos sobre todos los "targets" o vecinos resultantes.

			for (Ejemplo01HV nuevoProblema : problema.neighbors(a)) {

				// Llamada recursiva: Calcula el futuro de esta rama
				Pair<Integer, Double> sp = pd(nuevoProblema);

				// Si el futuro nos devuelve null, es que esta rama lleva a un fallo. La
				// ignoramos.
				if (sp == null) {
					continue;
				}

				// Calculamos el peso: El peso óptimo del futuro + el coste de mi acción actual
				// (a)
				peso = sp.second() + a;

				// ¿Es esta suma menor (mejor) que el récord que tenía guardado?
				if (peso < mejorPeso) {
					mejorSp = new Pair<>(a, peso);
					mejorPeso = peso;
				}
			}
		}
		// 4. ALMACENAMIENTO: Guardamos la decisión si hemos encontrado alguna salida
		if (mejorSp == null) {
			return null; // Callejón sin salida absoluto
		}

		// Guardamos el problema y su solución óptima en la libreta para no repetir este
		// esfuerzo
		memoria.put(problema, mejorSp);
		return mejorSp;
	}
	
	// Paso 3: El método solucion(Ej01HV inicial) (La Reconstrucción)
	
	/*	Reconstruir la solución en programación dinámica es como seguir un camino de migas de pan. 
	 * 	En lugar de volver a calcular o buscar a ciegas, nos situamos en el problema inicial y miramos en nuestra "libreta de notas" (memoria) 
	 * 	qué acción fue la ganadora para ese estado.*/
	
	/*	Tomamos esa acción, la guardamos en nuestra lista de soluciones, calculamos cuál es el estado vecino que resulta de aplicar esa acción
	 * 	nos movemos a él, y repetimos el proceso en un bucle hasta que lleguemos a un caso base.*/
	private List<Pair<Integer, Double>> solucion(Ejemplo01HV inicial) {
		List<Pair<Integer, Double>> sol = new ArrayList<>();
		
		// 1. Caso INICIAL: Recuperamos la mejor decisión para el problema de partida
		Pair<Integer, Double> sp = memoria.get(inicial);
		sol.add(sp);
		
		// Avanzamos al primer subproblema hijo usando la acción elegida (sp.first())
		// En hipergrafos neighbors() devuelve una lista; tomamos el primer destino .get(0)
		Ejemplo01HV problema = inicial.neighbors(sp.first()).get(0);
		
		// 2. Demás Casos: Seguimos el rastro de migas de pan mientras no estemos en el caso base
		while (problema.isBaseCase() == false) {
			// Consultamos la memoria para el subproblema actual
			sp = memoria.get(problema);
			sol.add(sp);
			
			// Saltamos al siguiente subproblema usando la acción guardada
			problema = problema.neighbors(sp.first()).get(0);
		}
		
		return sol;
	}

}
