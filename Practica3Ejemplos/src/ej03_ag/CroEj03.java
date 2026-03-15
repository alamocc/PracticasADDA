package ej03_ag;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ej03_pl.DatosAlumnos;
import us.lsi.ag.PermutationData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;
import us.lsi.common.List2;

public class CroEj03 implements PermutationData<List<Integer>>{
	
	// TENER CUIDADO CON LOS DEFAULTS
	
	@Override	
	public Integer maxMultiplicity(int index) {
		return DatosAlumnos.getTamGrupo();
	}
	
    
	@Override
	public Integer size() {
		// TODO Auto-generated method stub
		return DatosAlumnos.getNumGrupos();
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		Double goal = afinidadConGrupos(value);
		Integer alumnosEnCero = cuentaAfinidadCero(value);
		return goal - (1000 * alumnosEnCero);
	}

	private Integer cuentaAfinidadCero(List<Integer> value) {
		// TODO Auto-generated method stub
		Integer cero = 0;
		for(Integer i = 0; i < value.size(); i++) {
			if (DatosAlumnos.getAfinidad(i, value.get(i)) == 0) {
				cero += 1;
			}
		}
		return cero;
	}

	private Double afinidadConGrupos(List<Integer> value) {
		// TODO Auto-generated method stub
		Double goal = .0;
		for(Integer i = 0; i < value.size(); i++) {
			goal += DatosAlumnos.getAfinidad(i,value.get(i));
		}
		return goal;
	}

	@Override
	public List<Integer> solution(List<Integer> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChromosomeType type() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
