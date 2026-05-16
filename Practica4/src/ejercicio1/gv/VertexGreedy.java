package ejercicio1.gv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import ejercicio1.Datos1;
import us.lsi.graphs.virtual.VirtualVertex;

public record VertexGreedy(Integer indice, Double sueldoAcumulado, Integer valoracionAcumulada, Set<String> cualidadesPendientes, List<Integer> contratados) implements VirtualVertex<VertexGreedy, EdgeGreedy, Integer> {

	@Override
	public List<Integer> actions() {						// AQUI ESTAN LAS RESTRICCIONES DE PRESUPUESTO MAXIMO E INCOMPATIBILIDADES
		List<Integer> actions = new ArrayList<>();
		
		// Si hemos evaluado a todos los candidatos, no hay acciones posibles
		if (this.indice == Datos1.getNumCandidatos()) {
			return actions;
		}

		// Acción 0: NO lo contrato (siempre es una opción válida)
		actions.add(0);

		// Acción 1: SÍ lo contrato (¡OJO! Solo si cumple las normas)
		Double nuevoSueldo = this.sueldoAcumulado + Datos1.getSueldoMin(this.indice);
		boolean cumplePresupuesto = nuevoSueldo <= Datos1.getPresupuestoMax();
		
		// Comprobamos que no se lleve mal con nadie de los que ya hemos contratado
		boolean esCompatible = true;
		for (Integer contratadoPrevio : contratados) {
			if (Datos1.getSonIncompatibles(this.indice, contratadoPrevio)) {
				esCompatible = false;
				break;
			}
		}

		// Solo le doy al algoritmo la opción de contratarlo si hay dinero y no hay peleas
		if (cumplePresupuesto && esCompatible) {
			actions.add(1);
		}

		return actions;
	}
	
	/*													SERIA ASI, PERO COMO MIS ACTIONS SOLO CONTIENEN 0 Y 1, en el Ejemplo 3, queremos saber los grupos, 1,2,3,4, pero aqui solo si es 0 o 1, entonces si hacemos maxValoracion = Datos1.getValoracion(i);, vamos a coger la de 0 o 1, y no la del candidato que le toca
	public Integer greedyAction() {	
		List<Integer> variableGA = actions();
		Integer maxValoracion = Integer.MIN_VALUE;										// PORQUE ESTAMOS MAXIMIZANDO
		Integer personaSeleccionada = -1;				// Se pone como valor DESCARTE
		Integer vertice;
		
		for (Integer i = 0; i < variableGA.size(); i++) {
			vertice = variableGA.get(i);
			if (Datos1.getValoracion(i) > maxValoracion) {
				maxValoracion = Datos1.getValoracion(i);
				personaSeleccionada = vertice;
			}
		}
		return personaSeleccionada;
	}
	*/
	
	public Integer greedyAction() {							// Pq queremos saber si es 0 o 1, no contratado o si
		List<Integer> accionesDisponibles = actions();
		
		// Si en mi lista de acciones permitidas está el 1 (puedo contratarlo), lo hago.
		if (accionesDisponibles.contains(1)) {
			return 1;
		}
		
		// Si no, no lo contrato.
		return 0;
	}
	
	@Override
	public VertexGreedy neighbor(Integer a) {									// BASICAMENTE COGEMOS LO SIGUIENTE, ES DECIR, LO QUE TENEMOS AHORA Y EL INDICE +1
		Integer nuevoIndice = this.indice + 1;
		Double nuevoSueldo = this.sueldoAcumulado;
		Integer nuevaValoracion = this.valoracionAcumulada;
		Set<String> nuevasCualidades = new HashSet<>(this.cualidadesPendientes);
		List<Integer> nuevosContratados = new ArrayList<>(this.contratados);

		if (a == 1) { // Si decidimos contratarlo, actualizamos nuestras "mochilas"
			nuevoSueldo += Datos1.getSueldoMin(this.indice);
			nuevaValoracion += Datos1.getValoracion(this.indice);
			nuevasCualidades.removeAll(Datos1.getCualidades(this.indice));		// De las cualidades que nos faltan quitamos las que nos da el nuevo candidato
			nuevosContratados.add(this.indice);
		}

		return new VertexGreedy(nuevoIndice, nuevoSueldo, nuevaValoracion, nuevasCualidades, nuevosContratados);
	}

	@Override
	public EdgeGreedy edge(Integer a) {
		// VERSIÓN 2: Queremos MAXIMIZAR la VALORACIÓN.
		// Si a = 1 (lo contrato), el peso de este paso es la valoración del candidato.
		// Si a = 0 (paso de él), el peso es 0.0.
		Double weight = 0.0;
		if (a == 1) {
			weight = (double) Datos1.getValoracion(this.indice);
		}
		
		return new EdgeGreedy(this, this.neighbor(a), a, weight);
	}
	
	public Boolean goal() {					// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex
		// Hemos llegado al final cuando hemos tomado una decisión para todos los candidatos		LE DECIMOS QUE HEMOS LLEGADO AL FINAL
		return this.indice == Datos1.getNumCandidatos();
	}
	
	public Boolean goalHasSolution() {		// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex				ULTIMA RESTRICCION DE QUE SE CUBREN TODAS LAS CUALIDADES
		// Nos exige que cubramos todas las cualidades												LLEGAMOS AL FINAL CUANDO CUBRIMOS TODAS LAS CUALIDADES
		return this.cualidadesPendientes.isEmpty();
	}

	// Heurística para MAXIMIZAR (Cota Superior / Optimista)
	public static Double heuristica(VertexGreedy v, Predicate<VertexGreedy> goal, VertexGreedy end) {
		if (v.cualidadesPendientes().isEmpty()) return 0.0;
		
		Double h = 0.0;
		for (int i = v.indice(); i < Datos1.getNumCandidatos(); i++) {
			// Si el candidato tiene alguna cualidad que aún nos falta, 
			// sumamos su valoración al "mejor caso posible"
			final int idx = i;
			boolean ayuda = Datos1.getCualidades(idx).stream().anyMatch(v.cualidadesPendientes()::contains);
			if (ayuda) {								
				h += Datos1.getValoracion(idx);				// Si es minimizar solo cogemos una (es un poco raro, no lo entiendo)
			}
		}
		return h;
	}
}
