/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AnalizadorLexico extends Errores{
	private String codigoFuente="";
	private StringBuilder lexema = new StringBuilder();
	private int index=0;
	private int numeroLinea=1;
	private String token="";
	private short estado=0, estadoAnterior=-1, columna=0;
	private final short NUM_PAL_RES=26, NUM_CTE_LOG=2, NUM_OPE_LOG=3, COLUMNA_INICIAL=0, COLUMNA_FINAL=13, 
						ERR=-1, ACP=99;
	private final int TAM_CODIGO_FUENTE;	
	private final String[] PALABRA_RESERVADA= {
						   "caso", "constante", "continua", "de", "decimal", "desde",
						   "entero", "fin", "funcion", "hasta", "haz", "imprime", "imprimenl",
						   "inicio", "interrumpe", "lee", "logico", "mientras", "opcion",
						   "otro", "para", "procedimiento","programa", "regresa", "si", "sino"
						   };
	private final String[] CONSTANTE_LOGICA= {
						   "falso", "verdadero"
						   };
	private final String[] OPERADOR_LOGICO= {
						   "no", "o", "y"
						   };
	private final short[][] MATRIZ_TRAN={
	/*  _																, :	
	   a-z										+ -						( )						tab
	   A-Z 		dgt		 = 		 / 		 * 		^ %		< >		 ! 		[ ] ;	 . 		 " 		' '		nl		CC*/
/* 0*/ { 1,		 2,		 5,		 7,		13,		13,		14,		16,		18,		18,		19,		21,		21,		ERR},
/* 1*/ { 1,		 1,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/* 2*/ {ERR,	 2,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	 3,		ACP,	ACP,	ACP,	ACP},
/* 3*/ {ERR,	 4,		ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR},
/* 4*/ {ERR,	 4,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/* 5*/ {ACP,	ACP,	 6,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/* 6*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/* 7*/ {ACP,	ACP,	ACP,	 8,		10,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/* 8*/ { 8,		 8,		 8,		 8,		 8,		 8,		 8,		 8,		 8,		 8,		 8,		 8,		 9,		 8 },
/* 9*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*10*/ {10,		10,		10,		10,		11,		10,		10,		10,		10,		10,		10,		10,		10,		10 },
/*11*/ {10,		10,		10,		12,		11,		10,		10,		10,		10,		10,		10,		10,		10,		10 },
/*12*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*13*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*14*/ {ACP,	ACP,	15,		ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*15*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*16*/ {ERR,	ERR,	17,		ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR,	ERR},
/*17*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*18*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*19*/ {19, 	19, 	19, 	19, 	19, 	19, 	 19,	19, 	19, 	19, 	20,		 19,	19 ,	19 },
/*20*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP},
/*21*/ {ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP,	ACP} };

	
	public AnalizadorLexico (String rutaArchivo, Consola c) {
		super(c);
		codigoFuente=leerArchivo(rutaArchivo);
		TAM_CODIGO_FUENTE=codigoFuente.length();
	}//Constructor
	
	public int obtenerNumeroLinea() {
		return numeroLinea;
	}//obtenerNumeroLinea

	public String obtenerToken() {
		return token;
	}//obtenerToken
	
	public String obtenerLexema() {
		return lexema.toString();
	}//obtenerLexema
	
	public int obtenerIndex() {
		return index;
	}//obtenerIndex
	
	public int obtenerTAM_CODIGO_FUENTE() {
		return TAM_CODIGO_FUENTE;
	}//obtenerTAM_CODIGO_FUENTE
	
	private String leerArchivo(String rutaArchivo){
			StringBuilder sb = new StringBuilder();
			String ln="";
			
			try (FileReader fr = new FileReader(rutaArchivo);
				 BufferedReader br = new BufferedReader(fr);){
				sb.append(br.readLine());
				while ((ln = br.readLine()) != null){
					sb.append('\n');
					sb.append(ln);	
				}//while
			} catch (IOException e) {
				e.printStackTrace();
			}//catch
			return sb.toString();
		}//leerArchivo
	
	private boolean esPalabraReservada( String lexema ) {
		for (int i = 0; i < NUM_PAL_RES ; i++) {
			if(lexema.equals(PALABRA_RESERVADA[i])){				
				return true;
			}//if
		}//for
		return false;
	}//esPalabraReservada
	
	private boolean esConstanteLogica( String lexema ) {
		for (int i = 0; i < NUM_CTE_LOG; i++) {
			if(lexema.equals(CONSTANTE_LOGICA[i])){
				return true;
			}//if
		}//for
		return false;
	}//esConstanteLogica
	
	private boolean esOperadorLogico( String lexema ) {
		for (int i = 0; i < NUM_OPE_LOG; i++) {
			if(lexema.equals(OPERADOR_LOGICO[i])){
				return true;
			}//if
		}//for
		return false;
	}//esOperadorLogico
	
	private short obtenerColumna( char c ) {
		if ( Character.isAlphabetic(c) ) {
			return 0;
		} else if (Character.isDigit(c) ) {
			return 1;
		} else {
			switch (c) {
			case '=':	return 2;
			case '/':	return 3;
			case '*':	return 4;
			case '^': //return 5;
			case '%': //return 5;
			case '+': //return 5;
			case '-':	return 5;
			case '<': //return 6;
			case '>':	return 6;
			case '!':	return 7;
			case '[': //return 8;
			case ']': //return 8;
			case '(': //return 8;
			case ')': //return 8;
			case ',': //return 8;
			case ';': //return 8;
			case ':':	return 8;
			case '.':	return 9;
			case '\"':	return 10;
			case ' ': //return 11;
			case '\t':	return 11;
			case '\n':	return 12;
			default :	return 13;
			}//swith
		}//if{}elseif{}else{}	
	}//obtenerColumna
	
	public String escanear() {
		columna=0;//Variable que almacenara la columna
		char c='\0';//Variable que almacenara el caracter
		estado=0;//Inicializar el estado
		lexema.delete(0, lexema.length());//Inicializar el StringBuilder
		token="";//Variable que almacena el tipo de token
		while (estado != ACP && estado != ERR && 
			   index < TAM_CODIGO_FUENTE) {
			c=codigoFuente.charAt(index++);
			while ( (estado == 0 || estado == 8 || estado == 10 || estado == 12) && //El estado 8 y 10 son de comentarios 
				    (c == ' ' || c == '\t' || c == '\n') ) {
				if (c == '\n') {
					numeroLinea++;
				}//if
				switch ( estado ) {
				case 8:
					if (c == '\n') {
						estado=0;
						token="";
					}//if
					break;
					
				case 10:
					if ( c == '*' ) {
						estado=11;
					}//if
					break;
					
				case 12:
					estado=0;
					break;
				}//switch ( estado )
				c=codigoFuente.charAt(index++);
			}//while omitir espacios en blanco y comentarios
			columna=obtenerColumna(c);
			if ( columna >= COLUMNA_INICIAL && columna <= COLUMNA_FINAL ) {
				estadoAnterior=estado;
				estado=MATRIZ_TRAN[estado][columna];
				if ( estado != ERR && estado != ACP) {
					lexema.append(c);
					estadoAnterior=estado;
				} else {
					index--;
				}//if{}else{}
				switch (estadoAnterior) {
				case 1:
					if ( esPalabraReservada(lexema.toString()) ) {
						token="palRes";
					} else if ( esOperadorLogico(lexema.toString()) ) {
						token="opeLog";
					} else if ( esConstanteLogica(lexema.toString()) ) {
						token="cteLog";
					} else {
						token="identi";
					}//if{}elseif{}elseif{}else{}
					break;
					
				case 2:		token="cteEnt";	break;
				case 4:		token="cteDec";	break;
				case 5:		token="opeAsi";	break;
				case 7:	  //token="opeAri";	
				case 13:	token="opeAri";	break;
				case 6:	  //token="opeRel";
				case 14:  //token="opeRel";
				case 15:  //token="opeRel";
				case 17:	token="opeRel";	break;
				case 18:	token="delimi";	break;
				case 20:	token="cteAlf";	break;
				case 8:		//COMENTARIOS case 8 y 12
				case 12: 	token=""; lexema.delete(0, lexema.length());	break;
				}//switch`
			} //if{}
		}//while (indice < tamanoCodigoFuente)
		if (estado==ERR){
			error(ERR_LEX, ERR_LEX_INF, numeroLinea, lexema.toString());
			index = TAM_CODIGO_FUENTE;
		}//if{}
		return lexema.toString();
	}//escanear
}//class