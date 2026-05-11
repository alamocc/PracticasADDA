package ej03manual;

import java.util.ArrayList;
import java.util.List;

import ej03.DatosAlumnos;

public class BTEje03 {
	
	List<Integer> mejorSolucion;
	Double valorMejorSolucion;
	EstadoEje03 estado;
	
	public BTEje03() {
		mejorSolucion = null;
		valorMejorSolucion = Double.MIN_VALUE;
		estado = EstadoEje03();
	}
	
	public List<Integer> mejorSolucion() {
		return new ArrayList<Integer>(mejorSolucion);
	}
	
	public void bt() {
		if(estado.vertice().goal()) {
			if(estado.vertice().goalHasSolution()) {
				if(estado.valorSolucion() > valorMejorSolucion) {
					valorMejorSolucion = estado.valorSolucion();
					mejorSolucion = estado.solucion();
				}
			}
			return;
		}
		for(Integer action: estado.vertice().action()) {
			if(estado.cota(action) <= valorMejorSolucion) {
				continue;
			}
			if(estado.cota(action) > valorMejorSolucion) {
				estado.forward(action);
				bt();
				estado.backward();
			}
		}
	}
	
	public static void main(String[] args) {
		DatosAlumnos.iniDatos("./src/ej03/alumnos_1.txt");
	}
}
