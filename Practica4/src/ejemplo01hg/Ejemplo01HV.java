package ejemplo01hg;

import java.util.ArrayList;
import java.util.List;

import us.lsi.common.Pair;
import us.lsi.hypergraphs.VirtualHyperVertex;

/*	Action: Integer: Definimos x_i como el número de veces que escogemos el elemento w_i. Sabemos que x_i debe ser un número entero >= 0. */

/* 	Estado del Sistema: Para ir comprobando la restricción necesitamos saber qué variable estamos despejando ahora mismo (el índice i) y cuánto nos queda para satisfacer la ecuación (N - lo acumulado).
 * 	Por eso el record Ejemplo01HV guarda dos enteros: indice y sumaRestante.
 * 	El método neighbors(Integer a) actualiza el estado matemático*/

/*	La Función Objetivo (Minimizar sum x_i): El Peso (Double): El coste o valor de esta función objetivo en un punto dado se guarda en el tipo Double dentro de tu Pair.*/

public record Ejemplo01HV(Integer indice, Integer sumaRestante) 
	implements VirtualHyperVertex<Ejemplo01HV, Ejemplo01HE, Integer, Pair<Double, List<Integer>>>{

	@Override
	// Que decisiones puedo tomar estando en el estado?
	// Nuestra decision es cuantas veces cogemos el numero que está en la posicion indice.
	// Podemos cogerlo 0 veces? Si, simplemente lo descartamos y pasamos al siguiente
	
	/*	El código que te han dado utiliza el método DatosMulticonjunto.getMultiplicidad(indice).
	 *  Si revisas el archivo DatosMulticonjunto.java, verás que ese método hace SUMA / numeros.get(i). 
	 *  Es decir, calcula cuántas veces cabe ese número en la suma total objetivo.*/
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		// Iteramos desde 0 hasta el maximo de veces que cabe el numero:
		for(Integer i = 0; i <= DatosMulticonjunto.getMultiplicidad(indice); i++) {
			actions.add(i);
		}
		return actions;
	}

	@Override
	// En un árbol de decisión, el caso base es el final del camino (las hojas del árbol).
	
	/*	Sabemos cuántos elementos totales hay gracias a DatosMulticonjunto.getNumElementos().
		Por tanto, si nuestro indice actual ha alcanzado ese número, significa que ya hemos decidido la multiplicidad 
		de todos los elementos y debemos detener la exploración.*/
	
	public Boolean isBaseCase() {
		return this.indice == DatosMulticonjunto.getNumElementos();
	}

	@Override
	//	Al llegar al final de una rama del árbol, tenemos que comprobar si la combinación de decisiones que hemos tomado cumple con
	// 	la restricción matemática que planteamos al principio: ¿La suma es exactamente n?
	public Double baseCaseWeight() {
		// Si la sumaRestante es 0, significa que las multiplicidades elegidas suman exactamente el objetivo. Es una solución válida.
		// Si la sumaRestante es mayor que 0 (o menor, si permitiéramos pasarnos), significa que nos hemos quedado cortos y no sumamos n. No es una solución válida. 
		if(this.sumaRestante > 0) {
			return null;
		}
			
		return 0.0;
	}

	@Override
	/*	Este método pertenece a la interfaz para ciertos tipos de filtrados intermedios de vértices, pero en este modelo de hipergrafo virtual no se requiere 
		una validación extra en este punto, por lo que simplemente devolvemos null.*/
	
	
	public Boolean isValid() {
		return null;
	}

	@Override
	//	Este método se encarga de inicializar el objeto solución cuando el caso base es válido. Como estamos construyendo la solución de abajo hacia arriba 
	// 	(desde el final hacia el principio), en el último nivel el acumulado de peso es `0.0` y la lista de acciones tomadas está vacía, lista para empezar 
	// 	a recibir los datos en el camino de vuelta.
	
	public Pair<Double, List<Integer>> baseCaseSolution() {
		// Usamos un objeto `Pair` donde guardamos el peso inicial (`0.0`) y una lista vacía de enteros donde se irán guardando las multiplicidades.
		return new Pair<Double, List<Integer>>(0.0, new ArrayList<>());
	}

	@Override
	/*	Este es el núcleo de la programación dinámica en el camino de vuelta (bottom-up).
	 *  Cuando el algoritmo ya ha explorado el árbol y empieza a regresar resolviendo los subproblemas, este método toma la solución del vecino (solutions.get(0)) 
	 *  y la combina con la acción a que tomamos en este nivel.*/
	public Pair<Double, List<Integer>> solution(Integer a, List<Pair<Double, List<Integer>>> solutions) {
		
		// la primera instruccion es el motor de la función objetivo. Está ejecutando literalmente el sumatorio. 
		Double nuevoPeso = solutions.get(0).first() + a;
		List<Integer> nuevasActions = new ArrayList<>(solutions.get(0).second());
		nuevasActions.add(0, a);
		
		return new Pair<>(nuevoPeso, nuevasActions);
	}

	@Override
	/*: Este método define la transición de estados. Si estamos en un vértice que representa el elemento actual con una sumaRestante,
	 *  y tomamos la decisión de elegir ese elemento a veces (la acción),
	 *   ¿a qué nuevo estado llegamos?*/
	
	public List<Ejemplo01HV> neighbors(Integer a) {
		Integer nuevoIndice = this.indice + 1;
		// Traducción directa en código de ir restando w_i * x_i a nuestra suma objetivo para pasársela a la siguiente variable x_i+1.
		Integer nuevaSumaRestante = this.sumaRestante - DatosMulticonjunto.getElemento(this.indice) * a;
		return List.of(new Ejemplo01HV(nuevoIndice, nuevaSumaRestante));
	}

	@Override
	public Ejemplo01HE edge(Integer a) {
		return new Ejemplo01HE(this, this.neighbors(a), a);
	}

}