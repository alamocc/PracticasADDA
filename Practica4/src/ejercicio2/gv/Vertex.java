package ejercicio2.gv;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ejercicio2.Datos2;
import us.lsi.graphs.virtual.VirtualVertex;

//	 El tipo de vértice que estamos usando (Vertex).

// 	El tipo de arista que conectará los vértices (Edge).

// 	El tipo de la Acción (Integer). Una acción es la decisión que tomamos para ir de un estado a otro. En nuestro caso, 
//	la acción será un número entero: el índice del contenedor j donde decidimos meter el elemento.

// 	El indice es un número entero (Integer) que responde a la pregunta: "¿De qué elemento es el turno ahora mismo?".

// 	Es una lista de números enteros (List<Integer>) que representa el espacio libre que le queda a cada caja en este momento exacto de la partida.
public record Vertex(
		Integer indice, 
		List<Integer> capacidadesRestantes) 
	implements VirtualVertex<Vertex, Edge, Integer>{

	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		// PASO 1: Condición de parada
		if (this.indice == Datos2.getNumElementos()) {
			return actions; // Si ya hemos mirado todos los elementos, devolvemos una lista vacía.
		}
		
		// PASO 2: La opción de "Descarte" (Siempre disponible)
		// Como la restricción R1 dice que un elemento se asigna a "como mucho" 1 contenedor,
		// existe la posibilidad de no asignarlo a ninguno.
		// Usamos el número total de contenedores como un ID falso que representa la "papelera".
		Integer accionNoAsignar = Datos2.getNumContenedores();
		actions.add(accionNoAsignar);
		
		// Paso 3: Evaluar los contenedores reales uno a uno
		Integer tamElemento = Datos2.getTamElemento(this.indice);
		
		for(Integer i = 0; i < Datos2.getNumContenedores(); i++) {
			Integer capacidadContenedor = this.capacidadesRestantes.get(i);
			
			// Comprobamos la Restricción Compatibilidad de tipos y Capacidad máxima
			if(Datos2.getPuedeUbicarse(this.indice, i) && tamElemento <= capacidadContenedor) {
				actions.add(i);
			}
		}
		return actions;
	}

	@Override
	// el método neighbor (vecino) responde a la pregunta: "Si estoy en esta 'foto' y ejecuto la acción a, ¿cómo será la nueva foto?".
	public Vertex neighbor(Integer a) {
		// 1. Pase lo que pase, avanzamos al siguiente elemento
		Integer nuevoIndice = this.indice + 1;
		
		// 2. Hacemos una copia de las capacidades actuales
		List<Integer> nuevaCapacidadRestante = new ArrayList<>(this.capacidadesRestantes);
		
		// 3. LA MAGIA: ¿Es 'a' una caja de verdad o es una papelera?
		if (a < Datos2.getNumContenedores()) {
			Integer capacidadActualDelContenedor = nuevaCapacidadRestante.get(a);
			Integer tamElemento = Datos2.getTamElemento(this.indice);
			
			// Le restamos el espacio a la caja
			nuevaCapacidadRestante.set(a, capacidadActualDelContenedor - tamElemento);
		}
		return new Vertex(nuevoIndice, nuevaCapacidadRestante);
	}

	@Override
	public Edge edge(Integer a) {
		Double weight = 0.0; // Por defecto, una decisión no nos da puntos
		
		// Si la acción es meterlo en un contenedor real (no en la papelera)
		if (a < Datos2.getNumContenedores()) {
			// 1. Calculamos cómo será el vecino del futuro para inspeccionarlo
			Vertex vecino = this.neighbor(a);
			
			// 2. Comprobamos si el contenedor 'a' se ha quedado con 0 espacio libre en ese futuro
			if (vecino.capacidadesRestantes().get(a) == 0) {												
				weight = 1.0; // ¡Objetivo conseguido! Contenedor completamente lleno = 1 punto
			}
		}
		// Construimos y devolvemos la arista con toda la información
				return new Edge(this, this.neighbor(a), a, weight);
	}
	
	// El método goal(): ¿Se ha terminado la partida?
	public Boolean goal() {
		// ¿El índice actual es igual al número total de elementos?
		return this.indice == Datos2.getNumElementos();
	}
	
	public Boolean goalHasSolution() {
		// Cualquier reparto al que lleguemos cumpliendo el método actions() 
		// es una solución válida. El algoritmo ya se encargará de quedarse 
		// con la que maximice la suma de los pesos.
		return true;
	}
	
	public static Double heuristica(Vertex v, Predicate<Vertex> goal, Vertex end) {
		
		// Paso 1: Sumamos el tamaño de todos los elementos que nos quedan por evaluar
		Integer sumaTamanyosRestantes = 0;
		for(int i = 0; i < Datos2.getNumElementos(); i++) {
			sumaTamanyosRestantes += Datos2.getTamElemento(i);
		}
		
		
		// PASO 2: Miramos qué cajas no están llenas todavía
		List<Integer> huecosDisponibles = new ArrayList<Integer>();
		for(Integer cap : v.capacidadesRestantes()) {
			if(cap > 0) {
				huecosDisponibles.add(cap);
			}
		}
		
		// Paso 3: Ordenamos los huecos de menor a mayor para maximizar el numero de cajas llenas.
		// Lo mas barato y rapido es empezar llenando las cajas que necesitan menor espacio.
		huecosDisponibles.sort(null);
		
		// PASO 4: "Vertemos" el agua restante en las cajas
		Double contenedoresExtraLlenados = 0.0;
		for (Integer hueco : huecosDisponibles) {
		    if (sumaTamanyosRestantes >= hueco) {
		        contenedoresExtraLlenados += 1.0; 
		        sumaTamanyosRestantes -= hueco;    
		    } else {
		        break; 
		    }
		}
		return contenedoresExtraLlenados;
	}
}
