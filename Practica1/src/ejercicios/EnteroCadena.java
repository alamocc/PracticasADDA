package ejercicios;

public record EnteroCadena(String s, Integer a) {
	public static EnteroCadena of(Integer a, String s) {
		return new EnteroCadena(s, a);
	}
}
