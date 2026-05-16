package ej03manual;

import java.util.ArrayList;
import java.util.List;

import ej03.DatosAlumnos;
import ej03.VertexAlumnos;

public class EstadoEje03 {
	
	VertexAlumnos verticeActual;
	List<Integer> solucion;
	Double valorSolucion;
	List<VertexAlumnos> verticesAnteriores;
	
	public EstadoEje03() {
		solucion = new ArrayList<Integer>();
		valorSolucion = .0;
		verticesAnteriores = new ArrayList<>();
		
		List<Integer> plazas = new ArrayList<>();
		
		for(int i = 0; i < DatosAlumnos.getNumGrupos(); i++) {
			plazas.add(DatosAlumnos.getTamGrupo());
		}
		
		verticeActual = new VertexAlumnos(0, plazas);
	}
	public Object vertice() {
		// TODO Auto-generated method stub
		return verticeActual;
	}
	
	public Double valorSolucion() {
		// TODO
		return valorSolucion;
	}
	
	public List<Integer> solucion()	{
		// TODO
		return null;
	}
	

	public Double cota(Integer action) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void forward(Integer action) {
		solucion.add(action);
		valorSolucion += DatosAlumnos.getAfinidad(verticeActual.index(), action);
		verticesAnteriores.add(verticeActual);
		verticeActual = verticeActual.neighbor(action);
	}
	
	public void backward() {
		verticeActual = verticesAnteriores.remove(verticesAnteriores.size()-1);
		Integer ultimaAccion = solucion.remove(solucion.size()-1);
		valorSolucion -= DatosAlumnos.getAfinidad(verticeActual.index(), ultimaAccion);
	}
	public Boolean goal() {
		//TODO
	}
	
	public Boolean goalHasSolution() {
		return true;
	}


}
