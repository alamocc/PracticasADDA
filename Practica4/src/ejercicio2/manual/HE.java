package ejercicio2.manual;

import java.util.List;

import ejercicio2.Datos2;
import us.lsi.hypergraphs.SimpleHyperEdge;

//Implementamos SimpleHyperEdge indicando nuestros tipos: Vértice, Arista y Acción (Integer)
public record HE(HV source, List<HV> targets, Integer action) implements SimpleHyperEdge<HV, HE, Integer>{

	@Override
	public Double weight(List<Double> targetsWeight) {
		// 1. Empezamos con el peso que ya han conseguido nuestros hijos (el vecino)
		// De nuevo, usamos .get(0) porque nuestro problema es lineal y solo hay un vecino.
		Double totalWeight = targetsWeight.get(0);
		
		// 2. Comprobamos si la acción que hemos tomado merece premio.
		// ¿La acción fue meter el objeto en un contenedor real (y no descartarlo)?
		if (this.action < Datos2.getNumContenedores()) {
			
			// Miramos la fotografía del estado SIGUIENTE (el vecino resultante)
			HV vecino = this.targets.get(0);
			
			// Si al mirar a ese vecino, el contenedor que acabamos de usar
			// se ha quedado exactamente a 0 de capacidad libre...
			if (vecino.capacidadesRestantes().get(this.action) == 0) {
				// ¡Premio! Sumamos un punto a la puntuación total que traíamos del vecino
				totalWeight += 1.0; 
			}
			
		}
		return totalWeight;
	}

}
