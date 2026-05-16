package ejercicio01practica.gv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import ejercicio1.Datos1;
import us.lsi.graphs.virtual.VirtualVertex;

// 1er paso: Creando la estructura del archivo Vertex.java

/*	indice (Integer): Nos indica qué candidato estamos evaluando en este momento (por ejemplo, si vale 0, estamos decidiendo sobre el primer candidato C01).
 *  Funciona como el nivel del árbol de decisión.*/

/*	sueldoAcumulado (Double): Almacena el gasto total de los candidatos contratados hasta el momento. Lo necesitamos para vigilar la restricción de Presupuesto Máximo.*/

/*	valoracionAcumulada (Integer): La suma de las valoraciones de los contratados. Nos sirve para calcular la función objetivo a maximizar y guiar a la heurística.*/

/*	cualidadesPendientes (Set de Strings): El conjunto de cualidades exigidas que aún no han sido cubiertas por ningún candidato elegido. 
 * 	Cuando este conjunto esté vacío, sabremos que hemos cumplido la restricción de Cobertura.*/

/*	contratados (Lista de Integers): Guarda los índices de los candidatos que ya hemos decidido contratar en este camino. Es fundamental para verificar la restricción de 
 * 	Incompatibilidades cuando evaluemos a los siguientes candidatos.*/

public record Vertex(Integer indice, Double sueldoAcumulado, Integer valoracionAcumulada, 
		Set<String> cualidadesPendientes, List<Integer> contratados)
	implements VirtualVertex<Vertex, Edge, Integer>{
	
	// 2er paso: Metodo actions() (Las Restricciones)
	
	/*	Dado que nuestro modelo usa variables binarias (x_i en {0, 1}), las únicas respuestas posibles para un vértice en curso son:
	 * 	[0]: Solo puedo No contratarlo (porque contratarlo violaría alguna regla).
	 * 	[0, 1]: Puedo decidir No contratarlo o Sí contratarlo (ambas son legales).
	 * 	[]: Lista vacía (hemos llegado al final y no hay más decisiones que tomar).
	 * 
	 * 	Aquí es exactamente donde programamos dos de nuestras tres restricciones matemáticas: el presupuesto máximo y las incompatibilidades.
	 * 	La restricción de cubrir todas las cualidades la evaluaremos al final del camino, no en cada paso
	 * */
	@Override
	public List<Integer> actions() {
		List<Integer> actions = new ArrayList<>();
		
		// 1. Caso base: Si hemos evaluado a todos los candidatos, no hay acciones posibles.
		// El índice va de 0 a N-1, por lo que si índice == N, hemos terminado.
		if(this.indice == Datos1.getNumCandidatos()) {
			return actions;
		}
		// 2. Acción 0: NO lo contrato. 
		// Siempre es una opción matemáticamente válida saltarse a alguien.
		actions.add(0);
		
		// Comprobación de la Restricción de Presupuesto
		Double nuevoSueldo = this.sueldoAcumulado + Datos1.getSueldoMin(this.indice);
		boolean cumplePresupuesto = nuevoSueldo <= Datos1.getPresupuestoMax();
		
		// Comprobación de la Restricción de Incompatibilidades
		boolean esCompatible = true; 
		for(Integer contratadoPrevio: contratados) {
			if(Datos1.getSonIncompatibles(this.indice, contratadoPrevio)) {
				esCompatible = false; 
				break;
			}
		}
		
		// Si cumple restricciones
		if(cumplePresupuesto && esCompatible) {
			actions.add(1);
		}
		
		return actions;
	}

	@Override
	
	// Paso 3: El método neighbor(Integer a) (La Transición de Estado)
	
	/*	En la exploración de espacios de estados, una vez que tomamos una decisión (una acción válida devuelta por actions()), nuestro estado cambia.
	 *  El método neighbor (vecino) es la función de transición: calcula y devuelve el nuevo vértice resultante tras aplicar esa acción.*/
	
	public Vertex neighbor(Integer a) {
		// 1. El tiempo siempre avanza: pasamos al siguiente candidato
		Integer nuevoIndice = this.indice + 1;
		
		// 2. Preparamos las variables del nuevo estado (por defecto, asumiendo acción 0)
		Double nuevoSueldo = this.sueldoAcumulado;
		Integer nuevaValoracion = this.valoracionAcumulada;
		
		// ¡MUY IMPORTANTE! Creamos copias de las colecciones para mantener la inmutabilidad
		Set<String> nuevasCualidades = new HashSet<>(this.cualidadesPendientes);
		List<Integer> nuevosContratados = new ArrayList<>(this.contratados);
		
		// 3. Si la acción es 1 (SÍ lo contrato), actualizamos nuestras "mochilas"
		if(a == 1) {
			nuevoSueldo += Datos1.getSueldoMin(this.indice);
			nuevaValoracion += Datos1.getValoracion(this.indice);
			
			// Quitamos de la lista de pendientes las cualidades que este candidato aporta
			nuevasCualidades.removeAll(Datos1.getCualidades(this.indice));
			
			// Añadimos a este candidato a nuestra plantilla
			nuevosContratados.add(this.indice);
		}
		// 4. Devolvemos el nuevo estado encapsulado en un nuevo Vertex
		return new Vertex(nuevoIndice, nuevoSueldo, nuevaValoracion, nuevasCualidades, nuevosContratados);
	}

	@Override
	// Paso 4: El método edge(Integer a) (La Función Objetivo)
	
	/*	Una arista (Edge) conecta el vértice actual con su vértice vecino tras aplicar una acción. 
	 * 	En los problemas de optimización, las aristas tienen un peso (weight). Al sumar los pesos de todas las aristas de un camino,
	 * 	obtenemos el valor total de esa solución.*/
	
	/*	Como definimos en el modelo matemático, nuestra función objetivo es Maximizar la Valoración. Por lo tanto:
	 * 	Si la acción es 0 (No contrato), no gano ninguna valoración extra. El peso es 0.0.
	 * 	Si la acción es 1 (Sí contrato), gano la valoración de ese candidato. El peso es esa valoración.*/
	
	public Edge edge(Integer a) {
		Double peso = 0.0;
		
		// Si a = 1 (lo contrato), el peso de este paso es la valoración del candidato.
		if(a == 1) {
			peso = (double) Datos1.getValoracion(this.indice);
		}
		
		// Retornamos la arista conectando 'this' (vértice actual) con su vecino
		return new Edge(this, this.neighbor(a), a, peso);
	}
	
	// Paso 5: Los métodos goal() y goalHasSolution() (El Final del Camino)

	/*	El algoritmo recorrerá el árbol de decisiones profundizando nivel a nivel.
	 *  Necesita saber cuándo ha llegado a una "hoja" del árbol (el final) y si esa hoja representa una solución válida.*/
	
	/*	goal(): Responde a la pregunta "¿He terminado de tomar decisiones?". La respuesta es afirmativa cuando hemos evaluado 
	 * 	a todos los candidatos (es decir, el índice ha llegado al total de candidatos).*/
	public Boolean goal() {
		return this.indice == Datos1.getNumCandidatos();
	}
	
	/*	goalHasSolution(): Responde a la pregunta "Ahora que he terminado, ¿es esta solución válida?". Como las incompatibilidades
	 *  y el presupuesto ya los filtramos en actions(), la única regla que nos queda por comprobar es si hemos cubierto todas las cualidades.
	 *  Es decir, si nuestra "mochila" de cualidadesPendientes se ha quedado completamente vacía.*/
	
	public Boolean goalHasSolution() {
		// Nos exige que cubramos todas las cualidades. Es válido si no queda ninguna pendiente.
		return this.cualidadesPendientes.isEmpty();
	}
	
	// Paso 6: El método heuristica(...) (La Cota Superior)
	
	/*	La Teoría: Cuando algoritmos como bt exploran el grafo,
	 *  utilizan una función de evaluación que suma el coste real acumulado g y una estimación del coste restante (h, la heurística).*/
	
	// Heurística para MAXIMIZAR (Cota Superior / Optimista)
		public static Double heuristica(Vertex v, Predicate<Vertex> goal, Vertex end) {
			// Si ya hemos cubierto todas las cualidades, no necesitamos estimar nada más, el beneficio restante estimado es 0
			if (v.cualidadesPendientes().isEmpty()) {
				return 0.0;
			}
			
			Double h = 0.0;
			// Recorremos los candidatos que quedan desde el índice actual hasta el final
			for (int i = v.indice(); i < Datos1.getNumCandidatos(); i++) {
				final int idx = i;
				// Comprobamos si este candidato futuro nos aporta alguna cualidad que aún nos falta
				boolean ayudaACubrirCualidades = Datos1.getCualidades(idx).stream()
						.anyMatch(v.cualidadesPendientes()::contains);
				
				// Si nos ayuda, sumamos su valoración al "mejor escenario posible"
				if (ayudaACubrirCualidades) {								
					h += Datos1.getValoracion(idx);
			}
				
		
		}
			return h;
	}
}
