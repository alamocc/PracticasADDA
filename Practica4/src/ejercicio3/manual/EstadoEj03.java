package ejercicio3.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio3.Datos3;
import ejercicio3.gv.VertexEj03;

public class EstadoEj03 {
	
	VertexEj03 verticeActual;
	List<Integer> solucion;
	Double valorSolucion;
	List<VertexEj03> verticesAnteriores;
	
	public EstadoEj03() {
		List<Integer> intersseccionesPendientesIniciales = new ArrayList<>();
		for (int i = 1; i < Datos3.N; i++) {
			intersseccionesPendientesIniciales.add(i);
		}
		
		verticeActual = new VertexEj03(0, intersseccionesPendientesIniciales, 0.0, false);
		solucion = new ArrayList<>();
		valorSolucion = 0.0;
		verticesAnteriores = new ArrayList<>();
	}
	
	// METODOS
	public VertexEj03 verticeActual() {
		return verticeActual;
	}
	
	public List<Integer> solucion() {
		return new ArrayList<>(solucion);
	}
	
	public Double valorSolucion() {
		return valorSolucion;
	}
	
	
	public Double cota(Integer action) {
		return this.valorSolucion + Datos3.esfuerzo(verticeActual.indice(), action) + VertexEj03.heuristica(verticeActual.neighbor(action), null, null);
	}
	
	public void forward(Integer action) {
		solucion.add(action);
		valorSolucion += Datos3.esfuerzo(verticeActual.indice(), action);
		verticesAnteriores.add(verticeActual);
		verticeActual = verticeActual.neighbor(action);
	}
	
	public void backward() {
		verticeActual = verticesAnteriores.remove(verticesAnteriores.size() - 1);
		Integer ultimaAccion = solucion.remove(solucion.size() - 1);
		valorSolucion -= Datos3.esfuerzo(verticeActual.indice(), ultimaAccion);
	}

}
