package ejercicio01practica.manual;

import java.util.ArrayList;
import java.util.List;

import ejercicio1.Datos1;

//	¿Cómo funciona el bucle recursivo?

/*	1 - El Caso Base (La Meta): Cada vez que la función se llama a sí misma, lo primero que hace es preguntar: ¿He llegado al final del árbol? (goal()).
 * 
 * 	Si la respuesta es sí, comprueba si la solución es válida (goalHasSolution()).
 * 
 * 	Si es válida, mira si la puntuación de este camino es mayor que nuestra valorMejorSolucion. Si es así, ¡tenemos un nuevo récord! 
 * 	Guardamos la valoración y hacemos una copia de la lista de decisiones.*/

/*	2 - El Caso Recursivo (La Exploración y Poda): Si no estamos en el final, pedimos las acciones permitidas y las evaluamos una a una.
 * 
 * 	Calculamos la cota de la acción. Si lo máximo a lo que aspira esa decisión (estado.cota(action)) es menor o igual a nuestro récord 
 * 	actual (valorMejorSolucion), el algoritmo hace un continue y pasa de largo. Hemos podado una rama entera del árbol.
 * 
 * 	Si la cota promete superar nuestro récord, entonces ejecutamos el baile del Backtracking: forward(action) (dar un paso adelante),
 *  bt() (llamar a la recursividad para que explore el futuro), y backward() (recoger cable al volver para dejar el escenario limpio).
 * */

// TODO ESTO ES SIEMPRE IGUAL LOL
public class BT {
	
	private List<Integer> mejorSolucion;
	private Double valorMejorSolucion;
	private Estado estado;
	
	public BT() {
		this.mejorSolucion = null;
		// Como queremos MAXIMIZAR, empezamos con el valor más bajo posible (el suelo absoluto).
		// Si quisiéramos minimizar, empezaríamos en Double.MAX_VALUE.
		this.valorMejorSolucion = Double.MIN_VALUE; 
		this.estado = new Estado();
	}
	
	public List<Integer> mejorSolucion() {
		return this.mejorSolucion == null ? null : new ArrayList<>(this.mejorSolucion);
	}
	
	public Double valorMejorSolucion() {
		return this.valorMejorSolucion;
	}
	
	public void bt() {
		// 1. Caso base: Hemos evaluado ya a todos los candidatos?
		if(estado.vertice().goal()) {
			// 1.1: La solución del camino actual es válida (cubre todas las cualidades)?
			if (estado.vertice().goalHasSolution()) {
				// 1.2: ¿Es MEJOR (mayor valoración) que el récord que teníamos guardado?
				if(estado.valorSolucion() > valorMejorSolucion) {
					this.valorMejorSolucion = estado.valorSolucion();
					this.mejorSolucion = estado.solucion();
				}
			}
			return; // Cortamos la recursividad de esta rama porque ya no hay más candidatos por delante
		}
		
		// 2. CASO RECURSIVO: Evaluamos las acciones posibles (0 o 1) devueltas por el vértice
		
		for (Integer action : estado.vertice().actions()) {
			// 3. LA PODA (La magia para ahorrar millones de cálculos)
						// Si la estimación más optimista del mañana no supera nuestro récord actual en el bolsillo...
						if (estado.cota(action) <= valorMejorSolucion) {
							continue; // ...nos saltamos esta acción y no perdemos el tiempo explorándola
						}
						// 4. EL DESPLAZAMIENTO (Exploración en profundidad)
						// Si la cota es buena, avanzamos en el árbol, llamamos a la recursividad y luego deshacemos el paso
						estado.forward(action);
						bt(); // Llamada recursiva (mismo método, pero con el estado habiendo avanzado un paso)
						estado.backward(); // Vuelta atrás (limpieza del estado para la siguiente iteración del bucle)
					
		}
	}
	
	// Método Main para probar tu Backtracking Manual
		public static void main(String[] args) {
			// Asegúrate de que la ruta al archivo de datos de entrada es correcta en tu proyecto
			Datos1.iniDatos("datos_entrada/ejercicio1/DatosEntrada1.txt");
			
			BT algoritmo = new BT();
			algoritmo.bt(); // ¡Encendemos el motor manual!
			
			System.out.println("=========================================");
			System.out.println(" SOLUCIÓN BACKTRACKING MANUAL            ");
			System.out.println("=========================================");
			System.out.println("Secuencia de decisiones (0=No, 1=Sí): " + algoritmo.mejorSolucion());
			System.out.println("Valoración total conseguida: " + algoritmo.valorMejorSolucion());
			System.out.println("=========================================");
		}
}
