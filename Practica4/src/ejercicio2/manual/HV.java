package ejercicio2.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio2.Datos2;
import us.lsi.common.Pair;
import us.lsi.hypergraphs.VirtualHyperVertex;

//Definimos el estado: en qué elemento estamos (indice) y el espacio de los contenedores
public record HV(Integer indice, List<Integer> capacidadesRestantes) implements VirtualHyperVertex<HV, HE, Integer, Pair<Double, List<Integer>>>{

	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		// 1. CONDICIÓN DE PARADA: ¿Hemos terminado con todos los elementos?
		// Si el índice ha llegado al total de elementos, devolvemos una lista vacía.
		// El algoritmo entenderá que aquí ya no hay más decisiones que tomar.
		if(this.indice == Datos2.getNumElementos()) {
			return actions;
		}
		
		// 2. ACCIÓN DE DESCARTE: (Siempre está disponible)
		// Imaginamos un "contenedor extra" (cuyo número es el total de contenedores).
		// Si hay 3 contenedores (0, 1, 2), el descarte será el número 3.
		Integer contenedorDescarte = Datos2.getNumContenedores();
		actions.add(contenedorDescarte);
		
		// 3. ACCIONES DE UBICACIÓN: Evaluar los contenedores reales uno a uno
		Integer tamElemento = Datos2.getTamElemento(this.indice);
		
		for(int i = 0; i < Datos2.getNumContenedores(); i++) {
			// Miramos cuánto espacio le queda a ESTE contenedor en concreto
			Integer capacidadContenedor = this.capacidadesRestantes.get(i);
			
			// Si el elemento es compatible con el tipo de contenedor Y además cabe...
			if(Datos2.getPuedeUbicarse(this.indice, i)) {
				actions.add(i);
			}
		}
		
		return actions;
	}
	
	// 1. ¿Cuándo paramos la recursión? Cuando hayamos mirado todos los elementos
	@Override
	public Boolean isBaseCase() {
		return this.indice == Datos2.getNumElementos();
	}
	
	// 3. ¿Qué peso/puntuación base tiene? 0.0 (ya que los puntos se suman en el hiperarista, no al final)
	@Override
	public Double baseCaseWeight() {
		return this.isBaseCase() ? 0.0 : null;
	}

	// 2. ¿Es válido este estado final? Sí, porque cualquier combinación de descartes/ubicaciones es legal
	@Override
	public Boolean isValid() {
		return true;
	}

	// 4. Inicializamos la solución vacía para que luego se vaya rellenando hacia atrás
	@Override
	public Pair<Double, List<Integer>> baseCaseSolution() {
		return new Pair<>(0.0, new ArrayList<>());
	}
	
	// Construcción de la solución (Bottom-Up)
	@Override
	public Pair<Double, List<Integer>> solution(Integer a, List<Pair<Double, List<Integer>>> solutions) {
		Double weight = 0.0;
		
		// 1. Calcular los puntos de la acción que acabamos de tomar
		if (a < Datos2.getNumContenedores()) {
			// Cogemos el vecino al que fuimos (el estado siguiente)
			HV vecino = this.neighbors(a).get(0);
			
			// Si en ese estado siguiente el contenedor 'a' se ha quedado a 0 de capacidad...
			if (vecino.capacidadesRestantes().get(a) == 0) {				
				weight = 1.0; // ¡Ganamos un punto!
			}
		}
		// 2. Sumar nuestro punto al total que traemos del subproblema (el vecino)
		Double nuevoPeso = solutions.get(0).first() + weight;
		
		// 3. Añadir nuestra acción a la lista de decisiones
		List<Integer> nuevasActions = new ArrayList<>(solutions.get(0).second());
		
		// IMPORTANTE: Se añade en la posición 0 (al principio).
	    // Como la solución se construye desde el final hacia el principio, 
	    // si no lo hacemos así, la lista de acciones saldría del revés.
		nuevasActions.add(0, a);
		
		return new Pair<>(nuevoPeso, nuevasActions);
	}

	@Override
	public List<HV> neighbors(Integer a) {
		// 1. Avanzamos el índice (pasamos al siguiente elemento sí o sí)
		Integer nuevoIndice = this.indice + 1;
		
		// 2. Clonamos la lista de capacidades. 
		// ¡MUY IMPORTANTE! En Programación Dinámica nunca modificamos el estado actual, 
		// siempre creamos uno nuevo.
		List<Integer> nuevaCapacidadRestante = new ArrayList<>(this.capacidadesRestantes);
		
		// 3. Si la acción elegida fue meter el elemento en un contenedor (y no descartarlo)
		if(a < Datos2.getNumContenedores()) {
			// Calculamos cuánto ocupa el elemento que acabamos de meter
			Integer tamElemento = Datos2.getTamElemento(this.indice);
			
			// Miramos cuánto espacio le quedaba al contenedor elegido
			Integer capacidadRestante = nuevaCapacidadRestante.get(a);
			
			// Actualizamos el espacio libre de ese contenedor restándole el tamaño del elemento
			nuevaCapacidadRestante.set(a, capacidadRestante - tamElemento);
		}
		
		// 4. Devolvemos la nueva "fotografía" empaquetada en una lista.
		// (Es una lista porque la interfaz lo pide, pero en este problema siempre hay un solo vecino)
		return List.of(new HV(nuevoIndice, nuevaCapacidadRestante));
	}

	@Override
	public HE edge(Integer a) {
		return new HE(this, this.neighbors(a), a);
	}

}
