package ej02;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import us.lsi.graphs.virtual.VirtualVertex;

public record Ej02Vertex(Integer indice, Set<Integer> elementosPendientes) implements VirtualVertex<Ej02Vertex, Ej02Edge, Integer>{	// V (vertice) es Ej02Vertex siempre, la E (Arista) seria el record Ej02Edge, A (action) siempre Integer

		// ADD UNIMPLEMENTEDS METHODS Y SE ME PONE LO DE ABAJO
	
	@Override
	public List<Integer> actions() {			// El más importante	(ahora esta hecho lo minimo para funcionar)
		List<Integer> actions = Arrays.asList(0,1);		// Porque queremos una lista de binarios		SE PUEDE CREAR UNA LISTA Y AÑADIR 0 Y 1 COMO SE QUIERA
		
		// Optimizaciones	(SIEMPRE IGUAL)
		if (this.elementosPendientes.isEmpty()) {
			actions = Arrays.asList(0);
			
		} else {		// O evitarnos este else poniendo return actions en cada if
			if (this.indice == DatosSubconjunto.getNumSubconjuntos()-1) {
				actions = Arrays.asList(1);
			}
		}
		
		return actions;
	}

	@Override
	public Ej02Vertex neighbor(Integer a) {		// Integer a es l acción
		Integer nuevoIndice = this.indice + 1;
		Set<Integer> nuevosElementosPendientes = new HashSet<>(this.elementosPendientes);
		if (a == 1) {
			nuevosElementosPendientes.removeAll(DatosSubconjunto.getElementos(this.indice));
		}
		
		return new Ej02Vertex(nuevoIndice, nuevosElementosPendientes);
	}

	@Override
	public Ej02Edge edge(Integer a) {
		Double weight = 0.0;
		if (a == 1) {
			weight = DatosSubconjunto.getPeso(this.indice);
		}
		return new Ej02Edge(this, this.neighbor(a), a, weight);	// Parametros del record Ej02Edge
	}
	
	public Boolean goal() {						// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex
		return this.indice == DatosSubconjunto.getNumSubconjuntos();
	}
	
	public Boolean goalHasSolution() {			// ES UN METODO POR DEFECTO DE LA CLASE VirtualVertex
		return this.elementosPendientes.isEmpty();		// tiene solucion cuando elementosPendientes esta vacio
	}

	
	// Heurística
	public static Double miHeu(Ej02Vertex vertice, Predicate<Ej02Vertex> noLoUso, Ej02Vertex tampocoLoUso) {	// El predicado y el ultimo vertice no se usan en este ejemplo
		Double h = 0.0;		// En la proxima clase dice que la acaba
		return h;
	}
}
