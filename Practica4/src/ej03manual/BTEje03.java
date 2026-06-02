package ej03manual;

import java.util.ArrayList;
import java.util.List;

import ej03_hg.DatosAlumnos;

public class BTEje03 {
	
	
	List<Integer> mejorSolucion;
	Double valorMejorSolucion;
	EstadoEje03 estado;
	
	public BTEje03() {			// nombre de la clase (constructor)
		mejorSolucion = null;
		valorMejorSolucion = Double.MIN_VALUE;
		estado = new EstadoEje03();
	}
	
	public List<Integer> mejorSolucion() {
		return new ArrayList<Integer>(mejorSolucion);
	}
	
	public void bt() {								// ALGORITMO DE BACKTRACKING MANUAL
		if (estado.vertice().goal()) {
			if (estado.vertice().goalHasSolution()) {
				if (estado.valorSolucion() > valorMejorSolucion) {
					valorMejorSolucion = estado.valorSolucion();
					mejorSolucion = estado.solucion();
					
				}
			}
			return;
		}
		for (Integer action: estado.vertice().actions()) {
			if (estado.cota(action) <= valorMejorSolucion) {		// la cota es peor, la descartamos
				continue;
			}
			if (estado.cota(action) > valorMejorSolucion) {		// SI ES MAX O MIN, LO QUE VARIA ES LO DE <=, >=, < Y >
				estado.forward(action);
				bt();
				estado.backward();
			}
		}
	}

	public static void main(String[] args) {
		DatosAlumnos.iniDatos("src/ej03/ejemplo3_1.txt");
		BTEje03 bt = new BTEje03();
		bt.bt();
		var sol = bt.mejorSolucion;
		System.out.println(sol);

	}

}
