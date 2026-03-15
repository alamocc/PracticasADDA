package ejercicio2;

import java.util.List;

import us.lsi.ag.RangeIntegerData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;

public class Cromosoma2 implements RangeIntegerData<Solucion2>{
	
	public Cromosoma2(String file) {
		Datos2.iniDatos(file);
	}
	
	// Max y min indican el intervalo
	@Override
	public Integer max(Integer i) {
		// El +1 es porque el intervalo en la libreria es abierto y queremos que sea cerrado para que coja todos los valores
		// Ademas, esto cobre la restriccion 1
		return Datos2.getUnidsSemanaProd(i)+1;
	}

	@Override
	public Integer min(Integer i) {
		return 0;
	}

	@Override
	public Integer size() {
		return Datos2.getNumProductos();
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		Double goal = ingresos(value);
		Double totalTiempoProd = funcionTotalTiempoProd(value);
		Double totalTiempoElab = funcionTotalTiempoElab(value);
		
		return goal - (1000 * totalTiempoProd) - (1000 * totalTiempoElab);
	}

	private Double funcionTotalTiempoElab(List<Integer> value) {
		Double tiempoElab = 0.0;
		for(int i = 0; i < size(); i++) {
			tiempoElab += (value.get(i) * Datos2.getTiempoElabProd(i));
		}
		if(tiempoElab <= Datos2.getTiempoElabTotal()) {		// Como es una funcion de penalizacion, si se cumple la condicion, no devolvemos nada. Solo interesa cuando no se cumple la condiccion
			return 0.0;
		}
		return tiempoElab - Datos2.getTiempoElabTotal();
	}

	private Double funcionTotalTiempoProd(List<Integer> value) {
		Double tiempoProd = 0.0;
		for(int i = 0; i < size(); i++) {
			tiempoProd += (value.get(i) * Datos2.getTiempoProdProd(i));
		}
		if(tiempoProd <= Datos2.getTiempoProdTotal()) {
			return 0.0;
		}
		return tiempoProd - Datos2.getTiempoProdTotal();
	}

	private Double ingresos(List<Integer> value) {
		Double ingresos = 0.0;
		for(int i = 0; i < size(); i++) {
			ingresos += (value.get(i) * Datos2.getPrecioProd(i));
		}
		return ingresos;
	}

	@Override
	public Solucion2 solution(List<Integer> value) {
		return Solucion2.create(value);
	}

	@Override
	public ChromosomeType type() {
		return ChromosomeType.RangeInteger;
	}

}
