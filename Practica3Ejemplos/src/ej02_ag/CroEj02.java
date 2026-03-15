package ej02_ag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ej02_pl.DatosSubconjunto;
import us.lsi.ag.BinaryData;
import us.lsi.ag.agchromosomes.Chromosomes.ChromosomeType;

public class CroEj02 
implements BinaryData<List<Integer>> {

	@Override
	public Integer size() {
		// TODO Auto-generated method stub
		return DatosSubconjunto.getNumSubconjuntos();
	}

	@Override
	public Double fitnessFunction(List<Integer> value) {
		// TODO Auto-generated method stub
		Double goal = pesosSeleccionados(value);
		Integer elementosFaltan = elementosNoUniverso(value);
	
		return - goal -(elementosFaltan * 10000);
	}
	
	private Double pesosSeleccionados(List<Integer> value) {
		Double pesos = .0;
		for (Integer i = 0; i < size() ; i++) {
			pesos += (value.get(i) * DatosSubconjunto.getPeso(i));
		}
		return pesos;
	}
	
	private Integer elementosNoUniverso(List<Integer> value) {
		Set<Integer> seleccionados = new HashSet<>();	
		for(Integer i = 0; i < size(); i++) {
			if (value.get(i) == 1) {
				seleccionados.addAll(DatosSubconjunto.getSubConjunto(i).elementos());
			}
		}
		return DatosSubconjunto.getNumElementos() - seleccionados.size();
	}

	@Override
	public List<Integer> solution(List<Integer> value) {
		// TODO Auto-generated method stub
		return new ArrayList<>(value);
	}

	@Override
	public ChromosomeType type() {
		// TODO Auto-generated method stub
		return ChromosomeType.Binary;
	}
}
