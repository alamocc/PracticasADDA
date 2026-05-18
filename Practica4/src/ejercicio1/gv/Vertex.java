package ejercicio1.gv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import ejercicio1.Datos1;
import us.lsi.graphs.virtual.VirtualVertex;

// 1er paso: Definir la informacion que tiene los vertices: el índice, y las acumulaciones de las restricciones
public record Vertex(Integer indice, Double sueldoAcumulado, Integer valoracionAcumulada, Set<String> cualidadesPendientes, List<Integer> contratados) 
	implements VirtualVertex<Vertex, Edge, Integer> {
						// indice, sueldoAcumulado y cualidadesPendientes obligatorias, contratados solo para ver las incompatiblilidades y valoracionAcumulada para la heuristica
	
	// 2do paso: Definir las opciones disponibles para cada vertice (metodo actions())
	@Override
	public List<Integer> actions() {						// AQUI ESTAN LAS RESTRICCIONES DE PRESUPUESTO MAXIMO E INCOMPATIBILIDADES
		List<Integer> actions = new ArrayList<>();
		
		// Si hemos evaluado a todos los candidatos, no hay acciones posibles
		if (this.indice == Datos1.getNumCandidatos()) {
			return actions;
		}

		// Acción 0: NO lo contrato (siempre es una opción válida)
		actions.add(0);

		
		// Acción 1: SÍ lo contrato (¡OJO! Solo si cumple las restricciones siguientes)
		// Antes de añadir el '1' a la lista de opciones, tenemos que comprobar dos cosas:
	    
	    // Restriccion: El Presupuesto
	    // Calculamos cuánto gastaríamos si contratamos a este candidato.
		Double nuevoSueldo = this.sueldoAcumulado + Datos1.getSueldoMin(this.indice);
		boolean cumplePresupuesto = nuevoSueldo <= Datos1.getPresupuestoMax();
		
		// Restriccion: La Compatibilidad (El buen ambiente laboral)
	    // Asumimos que es compatible hasta que se demuestre lo contrario.
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
	
	// 3er Paso: Avanzar al siguiente Estado (El método neighbor())
	/*
	 * El concepto más importante aquí es la inmutabilidad. Como estamos explorando muchas ramas diferentes a la vez, 
	 * no podemos modificar la mochila actual, porque arruinaríamos otras posibles realidades.
	 *  Tenemos que hacer una copia de nuestra mochila, meter (o no) los cambios, 
	 *  y pasarle esa mochila nueva al siguiente vértice.
	 * 
	 * */
	@Override
	public Vertex neighbor(Integer a) {									
		// 1. PREPARAMOS LAS VARIABLES PARA EL NUEVO VERTICE
	    // El índice SIEMPRE avanza, pase lo que pase, porque pasamos al siguiente candidato.
		Integer nuevoIndice = this.indice + 1;
		// Por defecto, copiamos los valores tal y como están en este momento.
		Double nuevoSueldo = this.sueldoAcumulado;
		Integer nuevaValoracion = this.valoracionAcumulada;
		// Hacemos copias nuevas de las colecciones.
	    // Si no pusiéramos el "new", estaríamos modificando la lista original compartida por todos.
		Set<String> nuevasCualidades = new HashSet<>(this.cualidadesPendientes);
		List<Integer> nuevosContratados = new ArrayList<>(this.contratados);

		// 2. Aplicamos los cambios si se decide continuar al siguiente vertice.
		if (a == 1) { 
			nuevoSueldo += Datos1.getSueldoMin(this.indice);
			nuevaValoracion += Datos1.getValoracion(this.indice);
			nuevasCualidades.removeAll(Datos1.getCualidades(this.indice));		// De las cualidades que nos faltan quitamos las que nos da el nuevo candidato
			nuevosContratados.add(this.indice);
		}

		return new Vertex(nuevoIndice, nuevoSueldo, nuevaValoracion, nuevasCualidades, nuevosContratados);
	}

	
	// 4to paso: El método edge(Integer a) le dice al algoritmo cuántos puntos ganamos al tomar la decisión a.
	@Override
	public Edge edge(Integer a) {
		// VERSIÓN 2: Queremos MAXIMIZAR la VALORACIÓN. La funcion objetivo es el peso o sistema de puntuacion de las aristas.
		// Si a = 1 (lo contrato), el peso de este paso es la valoración del candidato.
		// Si a = 0 (paso de él), el peso es 0.0.
		Double weight = 0.0;
		if (a == 1) {
			weight = (double) Datos1.getValoracion(this.indice);
		}
		
		return new Edge(this, this.neighbor(a), a, weight);
	}
	
	// Este método simplemente responde a la pregunta: ¿He llegado al final de mi proceso?
	// No le importa si el equipo es bueno o malo, solo quiere saber si ya has entrevistado a todo el mundo.
	public Boolean goal() {					// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex
		// Hemos llegado al final cuando hemos tomado una decisión para todos los candidatos		LE DECIMOS QUE HEMOS LLEGADO AL FINAL
		return this.indice == Datos1.getNumCandidatos();
	}
	
	// El algoritmo llega al final de una rama (porque evaluó al último candidato) y entonces ejecuta este método para decir:
	// "Vale, he terminado, pero... ¿este equipo que he formado me sirve, o lo descarto directamente?"
	public Boolean goalHasSolution() {		// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex				ULTIMA RESTRICCION DE QUE SE CUBREN TODAS LAS CUALIDADES
		// Nos exige que cubramos todas las cualidades												LLEGAMOS AL FINAL CUANDO CUBRIMOS TODAS LAS CUALIDADES
		return this.cualidadesPendientes.isEmpty();
	}

	// 5to paso: Heurística para MAXIMIZAR (Cota Superior / Optimista / El mejor caso posible)
	public static Double heuristica(Vertex v, Predicate<Vertex> goal, Vertex end) {
		// 1. CASO BASE: YA HEMOS TERMINADO
	    // Si la lista de cualidades que nos pide la empresa ya está vacía, 
	    // significa que ya no "necesitamos" contratar a nadie más para cumplir el objetivo.
	    // Por tanto, la previsión de puntos extra es 0.
		if (v.cualidadesPendientes().isEmpty()) return 0.0;
		
		// 2. MIRAMOS AL FUTURO (A los candidatos que quedan)
	    // El bucle empieza en 'v.indice()', es decir, ignora a los que ya hemos evaluado 
	    // y solo mira a los que están esperando en la cola.
		Double h = 0.0;
		for (int i = v.indice(); i < Datos1.getNumCandidatos(); i++) {
			// Si el candidato tiene alguna cualidad que aún nos falta, 
			// sumamos su valoración al "mejor caso posible"
			final int idx = i;
			boolean ayuda = false;
			
			for(String cualidad : Datos1.getCualidades(idx)) {
				if (v.cualidadesPendientes().contains(cualidad)) {
			        ayuda = true; // ¡Bingo! Este candidato nos sirve
			        break;        // IMPORTANTE: Rompemos el bucle. Ya no hace falta mirar el resto de su CV.
			    }
			}
			
			if (ayuda) {								
				h += Datos1.getValoracion(idx);				
				// Si el candidato tiene algo que nos falta, sumamos TODA su valoración a nuestra predicción.
	            // Fíjate que aquí la heurística no mira si hay presupuesto, ni si se lleva mal con los demás. 
	            // Asume que lo vamos a poder contratar mágicamente. ¡Eso es ser optimista!
			}
		}
		return h;
	}
	
	
}
