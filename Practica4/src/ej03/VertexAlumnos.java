package ej03;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import us.lsi.common.List2;
import us.lsi.graphs.virtual.VirtualVertex;

public record VertexAlumnos(Integer index, List<Integer> restantes) 
	implements VirtualVertex<VertexAlumnos, EdgeAlumnos, Integer>{
	
	public static Double heuristic(VertexAlumnos vertAct, Predicate<VertexAlumnos> pred, VertexAlumnos vert2) {
		Double h = .0;
		
		for(Integer alumnoSinAsignar = vertAct.index(); alumnoSinAsignar < DatosAlumnos.getNumAlumnos(); alumnoSinAsignar++) {
			Integer afinidadMax = 0;
			for(Integer grupo = 0; grupo < DatosAlumnos.getNumAlumnos(); grupo++) {
				if(vertAct.restantes.get(grupo) == 0) {
					continue;
				}
				if(DatosAlumnos.getAfinidad(alumnoSinAsignar, grupo) > afinidadMax) {
					afinidadMax = DatosAlumnos.getAfinidad(alumnoSinAsignar, grupo);
				}
			}
			h += afinidadMax;
		}
		return h;
	}
	@Override
	public List<Integer> actions() {
		List<Integer> res = List2.empty();
		for(int i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			if(DatosAlumnos.getAfinidad(index, i) == 0) {
				continue;
			}
			
			if(restantes.get(i) == 0) {
				continue;
			}
			res.add(i);
		}
		
		return res;
	}
	
	public Integer greedyAction() {
		List<Integer> variableGA = actions();
		Double maxAf = Double.MIN_VALUE;
		Integer grupoSeleccionado = -1;
		Integer grupo;
		
		for(int i = 0; i < variableGA.size(); i++) {
			grupo = variableGA.get(i);
			
			if(DatosAlumnos.getAfinidad(index, grupo) > maxAf) {
				maxAf = (double) DatosAlumnos.getAfinidad(index, grupo);
				grupoSeleccionado = grupo;
			}	
		}
		
		return grupoSeleccionado;
	}

	@Override
	public VertexAlumnos neighbor(Integer a) {
		Integer nuevoIndice = index+1;
		
		List<Integer> copiaLista = new ArrayList<>(restantes);
		
		Integer nuevoEspacio = restantes.get(a);
		
		copiaLista.set(a, nuevoEspacio - 1);
		
		return new VertexAlumnos(nuevoIndice, copiaLista);
	}

	@Override
	public EdgeAlumnos edge(Integer a) {
		EdgeAlumnos arista = new EdgeAlumnos(this, this.neighbor(a), a, (double) DatosAlumnos.getAfinidad(index, a));
		
		return arista;
	}
	
	
	
	public Boolean goal() {
		return index == DatosAlumnos.getNumAlumnos();
	}
	
	public Boolean goalHasSolution() {
		return true;
	}

}
