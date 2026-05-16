package ejercicio1.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio1.Datos1;

public class BT {						// SIEMPRE ES IGUAL (cambiando lo de maximizar y minimizar)
	
	// 1. El Récord Actual: Guarda el mejor equipo que hemos encontrado hasta ahora
		List<Integer> mejorSolucion;
		
		// 2. La Puntuación del Récord: Guarda cuántos puntos tiene ese mejor equipo
		Double valorMejorSolucion;
		
		// 3. El Explorador o acumulador
		Estado estado;
		
	public BT() {
		mejorSolucion = null; // Al principio no tenemos ninguna solucion
		
		// ¡CLAVE! Como queremos MAXIMIZAR, empezamos con el récord más bajo posible.
		// Así, el primer equipo válido que encontremos superará este récord automáticamente.
		// (Si fuera MINIMIZAR, empezaríamos con Double.MAX_VALUE)
		valorMejorSolucion = Double.MIN_VALUE; 
		estado = new Estado();
	}
	
	public List<Integer> mejorSolucion() {
		return mejorSolucion == null ? null : new ArrayList<Integer>(mejorSolucion);
	}
	
	public void bt() {
		// ==========================================
		// 1. CASO BASE: ¿Hemos llegado al final?
		// ==========================================
		if (estado.vertice().goal()) { // ¿Hemos evaluado al último candidato?
			
			if (estado.vertice().goalHasSolution()) { // ¿El equipo cumple todas las cualidades?
				
				// Comparamos los puntos de este equipo con nuestro Récord Histórico
				if (estado.valorSolucion() > valorMejorSolucion) {
					// ¡NUEVO RÉCORD! Actualizamos la pizarra con los nuevos datos
					valorMejorSolucion = estado.valorSolucion();
					mejorSolucion = estado.solucion();
				}
			}
			// IMPORTANTE: Como ya no hay más candidatos, cortamos esta rama y retrocedemos.
			return; 
		}
		
		// ==========================================
		// 2. CASO RECURSIVO: Aún quedan candidatos
		// ==========================================
		// Sacamos la lista de puertas que podemos cruzar (ej: [0, 1])
		for (Integer action: estado.vertice().actions()) {
			
			// --- LA PODA (PRUNING) ---
			// El radar (cota) nos dice los puntos MÁXIMOS que podríamos conseguir por esta puerta.
			// Si esa promesa máxima es PEOR o IGUAL al récord que ya tenemos guardado...
			if (estado.cota(action) <= valorMejorSolucion) {
				// ¡Ignoramos esta puerta y no perdemos el tiempo! Pasamos a la siguiente.
				continue; 
			}
			
			// --- EL MOVIMIENTO ---
			// Si el radar dice que este camino promete superar el récord, vamos a explorarlo:
			estado.forward(action); // 1. Damos un paso adelante
			
			bt();                   // 2. MAGIA: Nos llamamos a nosotros mismos para explorar todo lo que hay tras esa puerta
			
			estado.backward();      // 3. Cuando terminamos de explorar esa rama, hacemos "Ctrl+Z" (paso atrás) para poder probar la otra puerta.
		}
	}

	public static void main(String[] args) {
		Datos1.iniDatos("src/ejercicio1/gv/DatosEntrada1.txt");
		
		BT bt = new BT();
		bt.bt(); // ¡Arrancamos el motor!
		
		System.out.println("=========================================");
		System.out.println(" SOLUCIÓN BACKTRACKING MANUAL (EJ1)      ");
		System.out.println("=========================================");
		System.out.println("Secuencia de acciones (0=No, 1=Sí): " + bt.mejorSolucion());
		System.out.println("Valoración total conseguida: " + bt.valorMejorSolucion);
		System.out.println("=========================================");
	}
	
}
