package ej03manual;

import java.util.ArrayList;
import java.util.List;

import ej03_hg.DatosAlumnos;
import ej03_hg.Ej03Vertex;

public class EstadoEje03 {
	
	Ej03Vertex verticeActual;
	List<Integer> solucion;
	Double valorSolucion;
	List<Ej03Vertex> verticesAnteriores;
	
	public EstadoEje03() {
		solucion = new ArrayList<Integer>();
		valorSolucion = 0.0;
		verticesAnteriores = new ArrayList<>();
		
		List<Integer> plazas = new ArrayList<>();
		
		for (int i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			plazas.add(DatosAlumnos.getTamGrupo());
		}
		verticeActual = new Ej03Vertex(0, plazas);
	}
	
	public Ej03Vertex vertice() {
		return verticeActual;
	}
	
	public Double valorSolucion() {
		return valorSolucion;
	}
	
	public List<Integer> solucion() {
		return new ArrayList<>(solucion);
	}
	
	public Double cota(Integer action) {
		return valorSolucion + DatosAlumnos.getAfinidad(verticeActual.indice(), action) + Ej03Vertex.heuristica(verticeActual, null, null);		// Son null pq realmente en la heurística no se usan
	}
	
	public void forward(Integer action) {
		solucion.add(action);
		valorSolucion += DatosAlumnos.getAfinidad(verticeActual.indice(), action);
		verticesAnteriores.add(verticeActual);
		verticeActual = verticeActual.neighbor(action);
	}
	
	public void backward() {
		verticeActual = verticesAnteriores.remove(verticesAnteriores.size()-1);
		Integer ultimaAccion = solucion.remove(solucion.size()-1);
		valorSolucion -= DatosAlumnos.getAfinidad(verticeActual.indice(), ultimaAccion);
	}
}
