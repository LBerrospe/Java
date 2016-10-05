import javax.swing.JTextArea;


public class Errores extends UI {
	UI userInterface=null;
	JTextArea txtrConsola;
	//ERRORES LEXICOS
	public final String ERR_LEX="lexico.", ERR_LEX_INF="secuencia de caracteres invalidos.";
	//ERRORES SINTACTICOS
	public final String ERR_SIN="sintactico", 
	  		 ERR_PGR="sintaxis incorrecta.\nprograma <identi> [<constantes>][<variables>][<procesos>|<funciones>] inicio [<block>] fin de programa.",
	  		 ERR_CTE="sintaxis incorrecta.\nconstante <entero>|<decimal>|<alfabetico>|<logico> (<identi> = <cteLog>|<cteAlf>|<cteEnt>|<cteDec>)+ ;",
	  		 ERR_PCS="sintaxis incorrecta.\nprocedimiento <identi> \"(\" (<entero>|<decimal>|<alfabetico>|<logico> <identi> [,])* \")\" [<variables>] inicio [<bloque>] fin de <identi> ;", 
	  		 ERR_VAR="sintaxis incorrecta.\n<entero>|<decimal>|<alfabetico>|<logico> (<identi> [= <cteLog>|<cteAlf>|<cteEnt>|<cteDec>][,])+ ;", 
	  		 ERR_FNC="sintaxis incorrecta.\nfuncion <entero>|<decimal>|<alfabetico>|<logico><identi> \"(\" (<entero>|<decimal>|<alfabetico>|<logico> <identi> [,])* \")\" [<variables>] inicio [<bloque>] regresa <cteLog>|<cteAlf>|<cteEnt>|<cteDec>; fin de <identi>;", 
	  		 ERR_BLQ="sintaxis incorrecta.\n";
	//ERRORES SEMANTICOS
	public final String ERR_SEM="semantico", 
						ERR_SEM_VAR="variable usada, no definida o operando incompatible",
						ERR_SEM_VAL="conversion de tipo invalido";
	
	public Errores (UI userInterface) {
		this.txtrConsola=userInterface.txtrConsola;
	}//Errores
		

	public void error( String tipo, String informacion, int numeroLinea, String lexema) {
		txtrConsola.append("Tipo: "+tipo+"\n");
		txtrConsola.append("Numero linea: "+numeroLinea+"\n");
		txtrConsola.append("Informacion: "+informacion+"\n");
		txtrConsola.append("En: "+lexema+"\n");
		System.out.println("Tipo: "+tipo);
		System.out.println("Numero linea: "+numeroLinea);
		System.out.println("Informacion: "+informacion);
		System.out.println("Cerca de: "+lexema);
	}//error
	
	public void imprimirCadena(String cadena) {
		txtrConsola.append(cadena);
	}//imprimirCadena

}//Errores