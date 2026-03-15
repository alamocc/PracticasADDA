package ejercicio1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import us.lsi.ag.BinaryData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;

public class Cromosoma1 implements BinaryData<Solucion1>{

	public Cromosoma1(String file) {
		Datos1.iniDatos(file);
	}
	@Override
	public Integer size() {
		return Datos1.getNumCandidatos();
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		double goal = valoraciones(value);
		Integer exceso = excedePresupuesto(value);
		Integer cualidadesNo = cuentaCualidadesNo(value);
		Integer incompatibilidades = cuentaIncompatibilidades(value);
		
		return goal - (1000*exceso) - (1000 * cualidadesNo) - (1000 * incompatibilidades); // Para que los cromosomas defectuosos desaparezcan
	}

	private Integer cuentaIncompatibilidades(List<Integer> value) {	// Mira si has metido a dos personas que no se soportan. Devuelve el numero de parejas incompatibles.
		Integer incompatibles = 0;
		for(int i = 0; i < size(); i++) {
			if (value.get(i) == 0) {
				continue;
			}
			for (int k = 0; k < size(); k++) {
				if ((value.get(i) == 1) && (value.get(k) == 1) && (Datos1.getSonIncompatibles(i, k))) {
					incompatibles += 1;
				}
			}
		}
		return incompatibles;
	}
	private Integer cuentaCualidadesNo(List<Integer> value) {
		Set<String> cualidades = new HashSet<>();
		for (int i = 0; i < size(); i++) {
			if(value.get(i) == 1) {
				cualidades.addAll(Datos1.getCualidades(i));
			}
		}
		return Datos1.getNumCualidades() - cualidades.size();
	}
	private Integer excedePresupuesto(List<Integer> value) {
		// TODO Auto-generated method stub
		Double presupuesto = 0.0;
		for (Integer i = 0; i < size(); i++) {
			presupuesto += (value.get(i) * Datos1.getSueldoMin(i));
		}
		if (presupuesto <= Datos1.getPresupuestoMax())
			return 0;
		return presupuesto.intValue() - Datos1.getPresupuestoMax();
	}
	private double valoraciones(List<Integer> value) {
		// TODO Auto-generated method stub
		Double valoraciones = 0.0;
		for (int i = 0;  i < size(); i++) {
			valoraciones += (value.get(i) * Datos1.getValoracion(i));
		}
		return valoraciones;
	}
	@Override
	public Solucion1 solution(List<Integer> value) {
		// TODO Auto-generated method stub
		return Solucion1.create(value);
	}

	@Override
	public ChromosomeType type() {
		// TODO Auto-generated method stub
		return ChromosomeType.Binary;
	}

}
