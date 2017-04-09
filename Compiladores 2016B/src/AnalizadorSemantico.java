import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

public class AnalizadorSemantico {
	
	Hashtable<String, String[]> tablaSimbolos = new Hashtable<>();
	Hashtable<String, String> operacionResultado = new Hashtable<String, String>();
	public final short NOMBRE=0, CLASE=1, TIPO=2, DIM01=3, DIM02=4,ERR=-1;
	
	public AnalizadorSemantico() {
		operacionResultado.put("E=E", "");	operacionResultado.put("E>E", "L");
		operacionResultado.put("A=A", "");	operacionResultado.put("E>D", "L");
		operacionResultado.put("D=D", "");	operacionResultado.put("D>E", "L");
		operacionResultado.put("L=L", "");	operacionResultado.put("D>D", "L");
		operacionResultado.put("D=E", "");	operacionResultado.put("E<E", "L");
		operacionResultado.put("E+E", "E");	operacionResultado.put("E<D", "L");
		operacionResultado.put("E+D", "D");	operacionResultado.put("D<E", "L");
		operacionResultado.put("D+E", "D");	operacionResultado.put("D<D", "L");
		operacionResultado.put("D+D", "D");	operacionResultado.put("E<=E", "L");
		operacionResultado.put("A+A", "A");	operacionResultado.put("E<=D", "L");
		operacionResultado.put("E-E", "E");	operacionResultado.put("D<=E", "L");
		operacionResultado.put("E-D", "D");	operacionResultado.put("D<=D", "L");
		operacionResultado.put("D-E", "D");	operacionResultado.put("E>=E", "L");
		operacionResultado.put("D-D", "D");	operacionResultado.put("E>=D", "L");
		operacionResultado.put("E*E", "E");	operacionResultado.put("D>=E", "L");
		operacionResultado.put("E*D", "D");	operacionResultado.put("D>=D", "L");
		operacionResultado.put("D*E", "D");	operacionResultado.put("E!=E", "L");
		operacionResultado.put("D*D", "D");	operacionResultado.put("E!=D", "L");
		operacionResultado.put("E/E", "D");	operacionResultado.put("D!=E", "L");
		operacionResultado.put("E/D", "D");	operacionResultado.put("D!=D", "L");
		operacionResultado.put("D/E", "D");	operacionResultado.put("A!=A", "L");
		operacionResultado.put("D/D", "D");	operacionResultado.put("E==E", "L");
		operacionResultado.put("E^E", "E");	operacionResultado.put("E==D", "L");
		operacionResultado.put("E^D", "D");	operacionResultado.put("D==E", "L");
		operacionResultado.put("D^E", "D");	operacionResultado.put("D==D", "L");
		operacionResultado.put("D^D", "D");	operacionResultado.put("A==A", "L");
		operacionResultado.put("noL", "L"); 
		operacionResultado.put("LoL", "L");		
		operacionResultado.put("LyL", "L");		
		operacionResultado.put("E%E", "E");	
	}//constructor
	
	void insertarSimbolo(String nombre, String clase, String tipo, 
			 			 String dimension01, String dimension02, GeneradorCodigo gc) {
		//System.out.println(nombre+" "+clase+" "+tipo+" "+dimension01+" "+dimension02);
		String s[]={nombre, clase, tipo, dimension01, dimension02};
		gc.generarCodigoTabSim(nombre, clase, tipo, dimension01, dimension02);
		tablaSimbolos.put(nombre, s);	
	}//introducirSimbolo
	
	boolean simboloUsado(String nombre) {
		if (tablaSimbolos.containsKey(nombre)) {
			return true;
		} else {
			return false;
		}//if{}else{}
	}//simboloUsado
	
	String obtenerTipo(String tipo) {
		switch (tipo) {
		case "entero": return "E";
		case "decimal": return "D";
		case "alfabetico": return "A";
		case "logico": return "L";
		default: return "I";
		}//switch
	}//obtenerTipoDato
	
	String obtenerInfoSim(String key, short columna) {
		String infoSim[]=tablaSimbolos.get(key);
		switch (columna){
		case NOMBRE: return infoSim[NOMBRE];
		case CLASE: return infoSim[CLASE];
		case TIPO:	return infoSim[TIPO];
		case DIM01:	return infoSim[DIM01];
		case DIM02: return infoSim[DIM02];
		default: return "";
		}//switch
	}//obtenerInfoSim
	
	boolean asignacionValida (String tipoDeclarado, String constanteAsignada) {
		switch (tipoDeclarado) {
		case "E":
			if (constanteAsignada.equals("cteEnt") || constanteAsignada.equals("E")) {
				return true;
			} else {
				return false;
			}//if{}else{}
		case "D":
			if (constanteAsignada.equals("cteDec") || constanteAsignada.equals("cteEnt")
					|| constanteAsignada.equals("E") || constanteAsignada.equals("D")) {
				return true;
			} else {
				return false;
			}//if{}else{}
		case "A":
			if (constanteAsignada.equals("cteAlf") || constanteAsignada.equals("A")) {
				return true;
			} else {
				return false;
			}//if{}else{}
		case "L":
			if (constanteAsignada.equals("cteLog") || constanteAsignada.equals("L")) {
				return true;
			} else {
				return false;
			}//if{}else{}
			default : return false;
		}//switch
	}//asignacionValida
	
	void imprimirTablaSimbolos() {
		Enumeration<String[]> e = tablaSimbolos.elements();

		  while (e.hasMoreElements()) {
			  System.out.println(Arrays.toString((String[]) e.nextElement()));
		  }
	}//imprimirTablaSimbolos
	
	short prioridadOperador (String s) {
		switch (s) {
		case "o": case "y":
			return 0;
		case "<":case "<=":case ">":case ">=":case "==":case "!=":
			return 1;
		case "no":
			return 2;
		case "+":case "-":
			return 3;
		case "*":case "/": case "%":
			return 4;
		case "^":
			return 5;
			
			default: return ERR;
		}//switch
	}//prioridadOperador
	
	String infijaAPostfija(String expresionInfija) {
		if (expresionInfija.length() == 1) {
			return expresionInfija;
		} else {
			StringBuilder expresionPostfija=new StringBuilder();
			Stack<String> pila = new Stack<String>();
			
			for (String s: expresionInfija.split("¤")) {
				switch(s) {
				case "^":case "*":case "/":case "%":case "+":
				case "-":case "<":case "<=":case ">":case ">=":
				case "==":case "!=":case "no":case "o":case "y":
					while ( !pila.empty() && prioridadOperador(s) <= prioridadOperador(pila.peek()) ) {
						expresionPostfija.append(pila.pop()+"¤");					
					}//while
					pila.push(s);
					break;
					
				case "(":
					pila.push(s);
					break;
					
				case ")":
					while (!pila.peek().equals("(")) {
						expresionPostfija.append(pila.pop()+"¤");
					}//while
					pila.pop();//No se agrega a expresionPost
					break;
					
				default:
					expresionPostfija.append(s+"¤");
					break;
				}//switch
			}//for
			while (!pila.isEmpty()) {
				expresionPostfija.append(pila.pop()+"¤");
			}//while
			return expresionPostfija.toString();
		}//if{}else{}
	}//infijaAPostfija
	
	String resultadoExpresionSem(String expresionInfija) {
		String expresionPostfija = infijaAPostfija(expresionInfija);
		Stack <String> entrada = new Stack <String>();
		for (String s: expresionPostfija.split("¤")) {
			if (prioridadOperador(s) == ERR) {
				entrada.push(s);
			} else if (prioridadOperador(s) == 2){
				if (operacionResultado.containsKey(s+entrada.peek())) {
					entrada.push(operacionResultado.get(s+entrada.pop()));
				} else {
					return "I";
				}//if{}else{}
			} else {
				entrada.push(evaluarOperacionSem(s, entrada.pop(), entrada.pop()));
			}//if{}else{}
		}//for
		return entrada.peek();
	}//resultadoExpresion
	
	
	String evaluarOperacionSem(String operando, String cteDos, String cteUno) {
		if (operacionResultado.containsKey(cteUno+operando+cteDos)) {
			return operacionResultado.get(cteUno+operando+cteDos);
		} else {
			return "I";
		}//if{}else{}
	}//evaluarOperacion

	/*
	public static void main(String[] args) {
		GeneradorCodigo gc = new GeneradorCodigo("hola.eje");
		AnalizadorSemantico as= new AnalizadorSemantico();
		as.generarCodigoExpresion(" - ( _4 - _2 ) * _5", gc);
		gc.generarArchivoEjecutable();
	}//main
	*/
}//class
