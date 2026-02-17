package ejercicio1;

import java.util.ArrayList;

import us.lsi.common.List2;
import us.lsi.tiposrecursivos.BEmpty;
import us.lsi.tiposrecursivos.BTree;
import us.lsi.tiposrecursivos.BLeaf;
import java.util.List;


import us.lsi.tiposrecursivos.BinaryTree;
import us.lsi.tiposrecursivos.TEmpty;
import us.lsi.tiposrecursivos.TLeaf;
import us.lsi.tiposrecursivos.TNary;
import us.lsi.tiposrecursivos.Tree;

public class Ejercicio1 {	
	
	record Tupla(List<Integer> camino, Integer prod) {
		// Creamos una Tupla con record que contiene el camino actual desde la raíz hasta la hoja y el producto de sus nodos
	}
	
	// Hacemos el producto de una rama del arbol hasta llegar al final, y ese es su producto, queremos el maximo, nustro algoritmo debe de devolver la lista de nodos con ese prodcuto maximo
	public static List<Integer> caminoMaximo (BinaryTree<Integer> tree) {      // Producto minimo, suma de etquitas, etc, (Variantes de examen)
		// Hacemos llamada a otra funcion como en la Recursiva Final
		return caminoMaximoAux(tree, new ArrayList<>(), 1, new Tupla(new ArrayList<>(), 1)).camino();   // Lista con el camino que llevamos y tupla con la lista de caminos y el producto de los nodos de ese camino (.camino() porque queremos que devuelva el camino maximo solo, no el producto), los otros valores porque es multiplicar, por 1 es lo mismo, si fuese sumando seria 0
	}

	private static Tupla caminoMaximoAux(BinaryTree<Integer> tree, List<Integer> currentPath, Integer currentProd, Tupla max) {
		// currentPath: camino que llevamos hasta ahora (vacío al inicio)
		// currentProd: producto acumulado (1 al inicio, porque multiplicar por 1 no cambia nada)
		// max: tupla que guarda el camino y producto máximo encontrados hasta ahora
		
		
		return switch (tree) {    // TODOS IGUALES CON ESTOS 3 CASOS
		
			// Caso Base (B de Binary):
			case BEmpty() -> max;  // Nodo vacío: no hay camino, devolvemos el máximo acumulado hasta ahora
		
			// lb = label = etiqueta
			// Caso Base (B de Binary):
			case BLeaf(var lb) -> lb*currentProd > max.prod()? new Tupla(List2.addLast(currentPath, lb), lb*currentProd): max; // Esto lo que hace es multiplicar el nodo actual con el producto que vayamos arrastrando, y vemos si es mayor que el producto que tenemos guardado en la tupla, que si, pues actualizamos la tupla con el nuevo producto y el nuevo nodo, que no, pues devolvemos la tupla que llevemos como maxima
			// Hoja: multiplicamos el valor actual por el producto acumulado
			// Si es mayor que el máximo, actualizamos la tupla con el nuevo camino y producto
			// Si no, devolvemos la tupla máxima hasta ahora
			
			
			// lt = left, rt = right
			// Caso Recursivo (B de Binary):
			case BTree(var lb, var lt, var rt) -> {     // Esta parte, a diferencia de Bleaf, es un subarbol que se divide en 2 nodos (Bleaf es cuando llega a una hoja, es decir, nodo final). Analizamos estos 2 nodos.
				// Nodo interno: recorremos recursivamente ambos hijos
				
				// Primero exploramos el subárbol izquierdo
				max = caminoMaximoAux(lt, List2.addLast(currentPath, lb), currentProd*lb, max); // Nodo izquierdo (Aplica el algoritmo pasando como parametro el arbol izquierdo)
				
				// Luego exploramos el subárbol derecho usando el máximo actualizado (ya que hemos mirado la parte izquierda antes)
				yield caminoMaximoAux(rt, List2.addLast(currentPath, lb), currentProd*lb, max); // Nodo derecho (Aplica el algoritmo pasando como parametro el arbol derecho)
				// yield es el return de los case
				
				/*   													OTRA FORMA UN POCO MAS CLARA
				 * // Primero exploramos el subárbol izquierdo
    				Tupla maxIzq = caminoMaximoAux(lt, List2.addLast(currentPath, lb), currentProd * lb, max);

    				// Luego exploramos el subárbol derecho usando el máximo actualizado
   					Tupla maxDer = caminoMaximoAux(rt, List2.addLast(currentPath, lb), currentProd * lb, maxIzq);

    				// Devolvemos el máximo entre el izquierdo y el derecho
    				Tupla maxFinal = (maxIzq.prod() > maxDer.prod()) ? maxIzq : maxDer;

    				yield maxFinal; // yield devuelve el resultado de este case
				 */
			}
		
		};
	}
	
	// Ttree, de N-ario
	public static List<Integer> caminoMaximo(Tree<Integer> tree) {
	    // Llamada inicial a la función recursiva auxiliar
	    return caminoMaximoAux2(tree, new ArrayList<>(), 1, new Tupla(new ArrayList<>(), 1)).camino();
	}
	
	private static Tupla caminoMaximoAux2(Tree<Integer> tree, List<Integer> currentPath, Integer currentProd, Tupla max) {
        return switch (tree) {

            // Caso 1: Nodo vacío
            case TEmpty() -> max;

            // Caso 2: Hoja (Nodo sin hijos)
            case TLeaf(var lb) -> lb * currentProd > max.prod()
                    ? new Tupla(List2.addLast(currentPath, lb), lb * currentProd)
                    : max;

            // Caso 3: Nodo n-ario
            case TNary(var lb, var children) -> {
                // Camino y producto actualizados                         (esto en la funcion de arbol binario lo metiamos dentro de la llamada recursiva, aqui lo podemos hacer, es lo mismo)
                List<Integer> newPath = List2.addLast(currentPath, lb);
                int newProd = currentProd * lb;

                Tupla currentMax = max;

                // Recorremos todos los hijos               LA UNICA DIFERENCIA CON EL BINARIO ES QUE EN EL BINARIO SOLO TENEMOS 2 HIJOS, Y AQUI TENEMOS N HIJOS, POR LO TANTO HAY QUE RECORRERLOS TODOS, Y ESTO LO HACEMOS CON EL for
                for (Tree<Integer> child : children) {
                    currentMax = caminoMaximoAux2(child, newPath, newProd, currentMax);
                }

                yield currentMax; // Devolvemos el máximo después de recorrer los hijos
            }
        };
    }
	

}
