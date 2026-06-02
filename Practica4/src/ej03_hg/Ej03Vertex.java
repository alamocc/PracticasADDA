package ej03_hg;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import us.lsi.common.List2;
import us.lsi.graphs.virtual.VirtualVertex;

public record Ej03Vertex(Integer indice, List<Integer> restantes) implements VirtualVertex<Ej03Vertex, Ej03Edge, Integer> { //A (action) siempre Integer
				// siempre se pone Integer indice, para movernos entre los datos

	@Override
	public List<Integer> actions() {
		List<Integer> actions = List2.empty();
		
		for(Integer i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			if(DatosAlumnos.getAfinidad(indice, i) == 0) {
				continue;
			}
			
			if(restantes.get(i) == 0) {
				continue;
			}
			
			actions.add(i);
		}
		
		return actions;
	}
	
	public Integer greedyAction() {							// Metodo de VirtualVertex			PARA EL ALGORITMO VORAZ
		List<Integer> variableGA = actions();
		Double maxAf = Double.MIN_VALUE;															// PORQUE ESTAMOS MAXIMIZANDO
		Integer grupoSeleccionado = -1;		// Se pone como valor DESCARTE
		Integer grupo;
		
		for(Integer i = 0; i < variableGA.size(); i++) {					// LO QUE HACEMOS ES COGER EL GRUPO CON MAYOT AFINIDAD AHORA MISMO, AUNQUE EN EL FUTURO NO SEA BUENA IDEA, PQ OTROS ALUMNOS TENGAN MAS AFINIDAD EN ESTE GRUPO O LO QUE SEA
			grupo = variableGA.get(i);
			if (DatosAlumnos.getAfinidad(indice, grupo) > maxAf) {
				maxAf = (double) DatosAlumnos.getAfinidad(indice, grupo);
				grupoSeleccionado = grupo;
			}
		}
		
		return grupoSeleccionado;
	}

	@Override
	public Ej03Vertex neighbor(Integer a) {
		Integer nuevoIndice = indice+1;
		List<Integer> nuevosRestantes = new ArrayList<>(restantes);
		
		Integer nuevoEspacio = restantes.get(a);		// Para espacios libres en el grupo
		nuevosRestantes.set(a, nuevoEspacio - 1);
		
		return new Ej03Vertex(nuevoIndice, nuevosRestantes);
	}

	@Override
	public Ej03Edge edge(Integer a) {
		return new Ej03Edge(this, this.neighbor(a), a, (double) DatosAlumnos.getAfinidad(indice, a));
	}
	
	public Boolean goal() {						// Siempre hay que poner este metodo del VirtualVertex (sin el default)
		return indice == DatosAlumnos.getNumAlumnos();
	}
	
	public Boolean goalHasSolution() {			// Siempre hay que poner este metodo del VirtualVertex (sin el default)
		return true;	// pq todas las soluciones son validas, no es lo de .isEmpty() pq esta lista siempre va a estar llena, pq en el neighbor no le eliminamos cosas como si lo hacemos en el ejemplo2
	}

	
	// HEURISTICA
	public static Double heuristica(Ej03Vertex vertice, Predicate<Ej03Vertex> goal, Ej03Vertex end) {
		Double h = 0.0;
		
		for (Integer alumnossinAsignar = vertice.indice(); alumnossinAsignar < DatosAlumnos.getNumAlumnos(); alumnossinAsignar++) {
			Integer afinidadMax = Integer.MIN_VALUE;
			for (Integer grupo = 0; grupo < DatosAlumnos.getNumGrupos();  grupo++) {
				if (vertice.restantes.get(grupo) == 0) {
					continue;
				}
				if (DatosAlumnos.getAfinidad(alumnossinAsignar, grupo) > afinidadMax) {
					afinidadMax = DatosAlumnos.getAfinidad(alumnossinAsignar, grupo);
				}
			}
			h += afinidadMax;
		}
		
		return h;
	}
}
