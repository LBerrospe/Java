/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

import javax.swing.JTextArea;

public class Errores extends Consola {
	Consola c;
	JTextArea txtrConsola;
	static boolean errorControl=false;
	//ERRORES LEXICOS
	public final String ERR_LEX="Lexico.", ERR_LEX_INF="Secuencia de caracteres invalidos.";
	//ERRORES SINTACTICOS
	public final String ERR_SIN="Sintactico", 
	  		 ERR_PGR= "Sintaxis incorrecta.\n"
	  		 		+ "<programa>:=\n\t"
	  		 		+ "programa <identi> \n\t"
	  		 		+ "(<constantes>|<variables>|<procesos>|<funciones>)*\n\t"
	  		 		+ "inicio\n\t\t"
	  		 		+ "(<instruccion>)*\n\t"
	  		 		+ "fin de programa.",
	  		 		
	  		 ERR_CTE= "Sintaxis incorrecta.\n"
	  		 		+ "<constante>:=\n\t"
	  		 		+ "constante <entero>|<decimal>|<alfabetico>|<logico> (<identi> [\"[\"<identi>|<cteEnt>\"]\" = \"[\"<identi>|<cteEnt>\"]\"] = <cteLog>|<cteAlf>|<cteEnt>|<cteDec>)+ ;",
	  		 		
	  		 ERR_VAR= "Sintaxis incorrecta.\n"
	  		 		+ "<variable>:=\n\t"
	  		 		+ "<entero>|<decimal>|<alfabetico>|<logico> (<identi> [\"[\"<identi>|<cteEnt>\"]\" [= \"[\"<identi>|<cteEnt>\"]\"]] [= <cteLog>|<cteAlf>|<cteEnt>|<cteDec>][,]{n-1}){n} ;",
	  		 		
	  		 ERR_PCS= "Sintaxis incorrecta.\n"
	  		 		+ "<procedimiento>:=\n\t"
	  		 		+ "<procedimiento>:=\n\t"
	  		 		+ "procedimiento <identi> <parametros>\n\t"
	  		 		+ "(<variable>)*\n\t"
	  		 		+ "inicio\n\t\t"
	  		 		+ "(<instruccion>)*\n\t"
	  		 		+ "fin de <identi> ;", 	  		 		
	  		 
	  		 ERR_FNC="Sintaxis incorrecta.\n"
	  		 		+ "<funcion>:=\n\t"
		  		 		+ "funcion <entero>|<decimal>|<alfabetico>|<logico> <identi> <parametros>\n\t"
		  		 		+ "(<variable>)*\n\t"
		  		 		+ "inicio\n\t\t"
		  		 		+ "(<instruccion>)*\n\t"
		  		 		+ "fin de <identi> ;",  
		  		 		
		  	ERR_PAR= "Sintaxis incorrecta.\n"
		  			+ "<parametro>:=\n\t"
		  			+ "\"(\" (<entero>|<decimal>|<alfabetico>|<logico> <identi> \"[\"\"]\"[,]{n-1}){n} \")\"",
		  		 		
	  		ERR_INS=  "Sintaxis incorrecta.\n"
	  				+ "<instruccion>:=\n\t"
	  				+ "[<identi>[<llamarFuncion>]|<leerVector>| = <asignacion>] |<instruccionSi>|<instruccionPara>|<instruccionMientras>|<instruccionHaz>|<instruccionLee>|<instruccionImprime>|<instruccionImprimenl>|<instruccionInterrumpe>|<instruccionContinua>|<instruccionRegresa>",
	  		
	  		ERR_LLF= "Sintaxis incorrecta.\n"
	  				+ "<llamarFuncion>:=\n\t"
	  				+ "\"(\"<asignacion>\")\"",
	  		
	  		ERR_SI=  "Sintaxis incorrecta.\n"
	  				+ "<instruccionSi>:=\n\t"
	  				+ "si \"(\"<asignacion>\")\"\n\t"
	  				+ "[inicio]\n\t\t"
	  				+ "<instruccion>\n\t"
	  				+ "[fin;]",
	  		
	  		ERR_PDH= "Sintaxis incorrecta.\n"
	  				+ "<instruccionPara>:=\n\t"
	  				+ "para <identi> desde <cteEnt>|<cteDec>|<identi> hasta <cteEnt>|<cteDec>|<identi> [incr|decr <cteEnt>|<cteDec>|<identi>]\n\t"
	  				+ "[incio]\n\t\t"
	  				+ "<instruccion>\n\t"
	  				+ "[fin;]",
	  		
	  		ERR_MIE= "Sintaxis incorrecta.\n"
	  				+ "<instruccionMientras>:=\n\t"
	  				+ "mientras \"(\"<asignacion>\")\"\n\t"
	  				+ "[inicio]\n\t\t"
	  				+ "<instruccion>\n\t"
	  				+ "[fin;]",
	  		
	  		ERR_HAZ= "Sintaxis incorrecta.\n"
	  				+ "<instruccionHaz>:=\n\t"
	  				+ "haz opcion \"(\"<identi>\")\"\n\t"
	  				+ "inicio\n\t\t"
	  				+ "[caso <identi>|<entero>|<decimal>|<alfabetico>|<logico>: <instruccion>*]*\n\t"
	  				+ "fin;",
	  		
	  		ERR_CPO= "Sintaxis incorrecta.\n"
	  				+ "<cuerpoInstruccion>:=\n\t"
	  				+ "[inicio]\n\t\t"
	  				+ "<instruccion>\n\t"
	  				+ "[fin;]",
	  		
	  		ERR_ASI= "Sintaxis incorrecta.\n"
	  				+ "<asignacion>:=\n\t"
	  				+ "<identi>[<llamarFuncion>|<leerVecto>]|(<entero>|<decimal>|<alfabetico>|<logico>)|\"(\"<asignacion>\")\" [<opeLog>|<opeAri>|<opeRel> <asignacion>]",
	  		
	  		ERR_IMP= "Sintaxis incorrecta.\n"
	  				+ "<imprime>:=\n\t"
	  				+ "imprime|imprimenl\"(\"[<cteAlf>|<asignacion>[,]{n-1}){n}",
	  		
	  		ERR_LEE= "Sintaxis incorrecta.\n"
	  				+ "<lee>:=\n\t"
	  				+ "lee \"(\"<identi>[\"[\"<identi>|<asignacion>\"]\"]\")\"",
	  		
	  		ERR_INT= "Sintaxis incorrecta.\n"
	  				+ "<interrumpe>:=\n\t"
	  				+ "interrumpe;",
	  		
	  		ERR_CON= "Sintaxis incorrecta.\n"
	  				+ "<continua>:=\n\t"
	  				+ "continua;",
	  		
	  		ERR_REG= "Sintaxis incorrecta.\n"
	  				+ "<regresa>:=\n\t"
	  				+ "regresa [<asignacion>];";
	  		
	//ERRORES SEMANTICOS
	public final String ERR_SEM="Semantico",
			
						//Unicidad
						ERR_UNI_USO="Unicidad: Identificador en uso, cambie el nombre.",
						ERR_UNI_ND="Unicidad: El identificador no fue declarado",
						//Tipo
						ERR_TIP_OPE="Conflicto en tipo de operacion",
						ERR_TIP_ASI="Conflicto en tipo de asignacion",
						ERR_TIP_SI="Tipo resultante en instruccion SI debe de ser logico",
						ERR_TIPO_PARA="Variable de control en la instruccion PARA debe de ser variable entero",
						ERR_TIP_DHI="Tipo resultante en expresion DESDE, HASTA e INCR debe de ser entero",
						ERR_TIP_MIE="Tipo resultante en instruccion MIENTRAS debe de ser logico",
						ERR_TIP_DIM="tipo resultante en dimension debe de ser entero o identificador-constante-entero",
						ERR_TIP_RET="Tipo resultante de retorno no es compatible con el tipo de funcion",
						ERR_TIP_DIM_ENT="Tipo de identificador usado como dimension en declaracion de arreglos debe de ser entero",
						//Control de flujo
						ERR_CNF_RETF="Estatuto regresa en funcion debe de tener expresion",
						ERR_CNF_RETF1="Estatuto regresa debe existir almenos uno en funcion.",
						ERR_CNF_RETP="Estatuto regresa en procedimientos no debe de retornar nada.",
						ERR_CNF_IC="Estatuto INTERRUMPE,CONTINUA solo debe de aparecer en ciclos",
						//Relacion de nombres
						ERR_REL_DIM="Dimensionar variable",
						ERR_REL_TDIM="Dimension fuera de rango",
						ERR_REL_NDIM="No es posible dimensionar variable",
						ERR_REL_FINF="Nombre de funcion debe de corresponder con el fin de funcion",
						ERR_REL_CONS="Variables constantes no pueden ser asignadas";
	
	public Errores (Consola c) {
		this.txtrConsola=c.txtrConsola;
	}//Errores
		
	public void error( String tipo, String informacion, int numeroLinea, String lexema) {
		if (!errorControl){
			txtrConsola.append("Tipo: "+tipo+"\n");
			txtrConsola.append("Numero linea: "+numeroLinea+"\n");
			txtrConsola.append("antes de O en: "+lexema+"\n");
			txtrConsola.append("Informacion: "+informacion);
			txtrConsola.append("\n");
			errorControl=true;
		} //if
	}//error
	
	public void imprimirCadena(String cadena) {
		txtrConsola.append(cadena);
	}//imprimirCadena

}//Errores