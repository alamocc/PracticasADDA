package ejercicio3.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio3.Datos3;

public class BTEj03 {
	
	List<Integer> mejorSolucion;
	Double valorMejorSolucion;
	EstadoEj03 estado;
	
	public BTEj03() {
		mejorSolucion = null;
		valorMejorSolucion = Double.MAX_VALUE;		// Pq queremos Minimizar
		estado = new EstadoEj03();
	}
	
	public List<Integer> mejorSolucion() {
		return mejorSolucion == null ? null : new ArrayList<>(mejorSolucion);
	}
	
	public void bt() {
		// 1. CASO BASE
		if (estado.verticeActual().goal() ) {
			if (estado.verticeActual().goalHasSolution()) {
				if (estado.valorSolucion() < valorMejorSolucion) {	// Pq queremos Minimizar
					valorMejorSolucion = estado.valorSolucion();
					mejorSolucion = estado.solucion();
				}
			}
			return;
		}
		
		// 2. Caso Recursivo
		for (Integer action: estado.verticeActual().actions()) {
			// PODA
			if (estado.cota(action) >= valorMejorSolucion) {	// Pq queremos Minimizar		
				continue;		// No nos sirve
			}
			
			// RECORRIDO
			estado.forward(action);
			bt();
			estado.backward();
		}
	}

	
	public static void main(String[] args) {
		Datos3.iniDatos("src/ejercicio3/gv/DatosEntrada1.txt");
		
		BTEj03 bt = new BTEj03();
		bt.bt();
		
		System.out.println(bt.mejorSolucion());
		System.out.println(bt.valorMejorSolucion);
	}
}
