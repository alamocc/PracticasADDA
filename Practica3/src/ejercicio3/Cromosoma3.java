package ejercicio3;

import java.util.List;

import us.lsi.ag.RangeIntegerData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;

public class Cromosoma3 implements RangeIntegerData<Solucion3> {

	public Cromosoma3(String file) {
		Datos3.iniDatos(file);
	}

	@Override
	public Integer max(Integer i) {
		return Datos3.getNumContenedores() + 1;
	}

	@Override
	public Integer min(Integer i) {
		return 0;
	}

	@Override
	public Integer size() {
		return Datos3.getNumElementos();
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		Double goal = ocupados(value);
		Double errorCapacidad = errorCapacidad(value);
		Double errorTipo = errorTipo(value);
		
		return goal - (1000 * errorCapacidad) - (1000 * errorTipo);
	}

	private Double errorTipo(List<Integer> value) {
		Double errores = 0.0;
		
		for(int i = 0; i < size(); i++) {
			Integer j = value.get(i);		// j es el contenedor donde ha caido el elemento i
			
			// Si j es menor que el número de contenedores (no se ha quedado fuera) y es incompatible
			if (j < Datos3.getNumContenedores() && Datos3.getNoPuedeUbicarse(i, j)) {
				errores++;
			}
		}
		return errores;
	}

	private Double errorCapacidad(List<Integer> value) {
		Double excesoTotal = 0.0;
		
		for(int j = 0; j < Datos3.getNumContenedores(); j++) {
			Double pesoMetido = 0.0;
			
			// value es un cromosoma que al hacer value.get(i) me devuelve su propio valor, el cual sera el numero del contenedor en el que se ha introducido.
			for(int i = 0; i < size(); i++) {
				if(value.get(i).equals(j)) {
					pesoMetido += Datos3.getTamElemento(i);
				}
			}
			
			if(pesoMetido > Datos3.getTamContenedor(j)) {
				excesoTotal += (pesoMetido - Datos3.getTamContenedor(j));
			}
		}
		return excesoTotal;
	}

	private Double ocupados(List<Integer> value) {
		Double contenedoresLlenos = 0.0;
		
		// Miro contenedor a contenedor
		for(int j = 0; j < Datos3.getNumContenedores(); j++) {
			Double pesoMetido = 0.0;
			
			// Recorro la lista a ver que elementos (i) han caido en ese contenedor
			for(int i = 0; i < size(); i++) {
				if(value.get(i).equals(j)) {
					pesoMetido += Datos3.getTamElemento(i);
				}
			}
			
			if (pesoMetido.equals(Datos3.getTamContenedor(j).doubleValue())) {
				contenedoresLlenos++;
			}
		}
		return contenedoresLlenos;
	}

	@Override
	public Solucion3 solution(List<Integer> value) {
		return Solucion3.create(value);
	}

	@Override
	public ChromosomeType type() {
		return ChromosomeType.RangeInteger;
	}

}
