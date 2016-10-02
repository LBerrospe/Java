/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

public class AnalizadorSintactico extends AnalizadorLexico{
	UI userInterface=null;
	private AnalizadorLexico al=null;
	//private int contParentIzq=0, contParentDer=0, contCorchIzq=0, contCorchDer=0;
	//private boolean conRetorno = false;
	
	public AnalizadorSintactico( String rutaArchivo, UI userInterface ) {
		super(rutaArchivo, userInterface );
		al = new AnalizadorLexico(rutaArchivo, userInterface);
	}//Constructor

	void programa() {
		al.escanear();
		if ( !al.obtenerLexema().equals("programa")) {
			error(ERR_SIN, ERR_PGR, al.obtenerLexema()); return;
		}//si el lexema no es programa
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {
			error(ERR_SIN, ERR_PGR, al.obtenerLexema()); return;
		}//si el token no es identificador
		al.escanear();
		while ( !al.obtenerLexema().equals("inicio")) {
			switch (al.obtenerLexema()) {
			case "constante":		constante();	break;
			case "procedimiento":	procedimiento();break;
			case "funcion":			funcion();		break;
			case "entero":		  //variable();		break;
			case "decimal":		  //variable();		break;
			case "alfabetico":	  //variable();		break;
			case "logico":			variable();		break;
			default: 
				error(ERR_SIN, ERR_PGR, al.obtenerLexema()); 
				return;
			}//switch	
		}//mientras el lexema no sea inicio
		al.escanear();
		while ( !al.obtenerLexema().equals("fin")) {
			instruccion();
		}//if fin
		al.escanear();
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PGR, al.obtenerLexema()); return;
		}//if de
		al.escanear();
		if ( !al.obtenerLexema().equals("programa")) {
			error(ERR_SIN, ERR_PGR, al.obtenerLexema()); return;
		}//if programa
		al.escanear();
		if ( !al.obtenerLexema().equals(".")){
			error(ERR_SIN, ERR_PGR, al.obtenerLexema()); return;
		}//if .
		System.out.println("Sintactico sin errores");
	}//programa
	
	void constante(){
		al.escanear();
		if ( !(al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
			   al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico")) ){
			error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
		}//if
		do {
			al.escanear();
			if ( !al.obtenerToken().equals("identi")) {
				error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
			}//if no es identi
			al.escanear();
			if ( al.obtenerLexema().equals("[")) {
				declararVector();
				if ( !al.obtenerLexema().equals("=")) {
					error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
				}//if no es igual
				al.escanear();
				if (al.obtenerLexema().equals("[")) {
					inicializarVector();
				} else {
					error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
				}// si no asigna el vector entonces es un error
			} else if ( al.obtenerLexema().equals("=")) {
				al.escanear();
				if ( !(al.obtenerToken().equals("cteAlf") || al.obtenerToken().equals("cteDec") ||
					   al.obtenerToken().equals("cteEnt") || al.obtenerToken().equals("cteLog"))) {
				error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
				}//si no es constante es un error
			} else {
				error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
			} //si no es [ ni = entonces es error
			al.escanear();
			if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
				error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
			}// if no es punto y coma o coma
		} while (!al.obtenerLexema().equals(";"));
		al.escanear();
	}//constante

	void variable() {
		do {
			al.escanear();
			if ( !al.obtenerToken().equals("identi")) {
				error(ERR_SIN, ERR_VAR, al.obtenerLexema()); return;
			}//if no es identi
			al.escanear();
			if ( al.obtenerLexema().equals("[")) {
				declararVector();
				if ( al.obtenerLexema().equals("=")) {
					al.escanear();
					if (al.obtenerLexema().equals("[")) {
						inicializarVector();
						al.escanear();
						if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
							error(ERR_SIN, ERR_VAR, al.obtenerLexema()); return;
						}// if no es punto y coma o coma
					} else {
						error(ERR_SIN, ERR_VAR, al.obtenerLexema()); return;
					}// si no asigna el vector entonces es un error
				} else if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
					error(ERR_SIN, ERR_VAR, al.obtenerLexema()); return;
				}//si no es = y no es , o ; entonces es un error
			} else if ( al.obtenerLexema().equals("=")) {
				al.escanear();
				asignacion();
				if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
					error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
				}// if no es punto y coma o coma
			} else if ( !(al.obtenerLexema().equals(",") || al.obtenerLexema().equals(";"))) {
				error(ERR_SIN, ERR_CTE, al.obtenerLexema()); return;
			} //si no es [ ni = ni , entonces es error
		} while (!al.obtenerLexema().equals(";"));
		al.escanear();
	}//variable
	
	void declararVector() {
		do {
			al.escanear();
			asignacion();
			if ( !al.obtenerLexema().equals("]")) {
				error(ERR_SIN, "ERR_VEC", al.obtenerLexema()); return;
			}//si no es ]
			al.escanear();
		} while ( al.obtenerLexema().equals("["));
	}//declararVector
		
	void inicializarVector() {
		//System.out.println("Aqui se asigna el vector :D");
	}//inicializarVector
	
	void procedimiento(){
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}//if es identi
		al.escanear();
		parametros();
		while ( !al.obtenerLexema().equals("inicio")) {
			if ( al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
				 al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ){
				variable();
			} else {
				error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;	
			}//if{}else{}
		}//Mientras no sea inicio
		al.escanear();	
		while ( !al.obtenerLexema().equals("fin")) {
			instruccion();
		}// mientras no es FIN
		al.escanear();
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es DE
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es el identi
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es punto y coma
		al.escanear();
	}//procesos
	
	void funcion(){
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {
			error(ERR_SIN, ERR_FNC, al.obtenerLexema()); return;
		}//si no es identi
		al.escanear();
		if ( !(al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
			   al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico")) ){
			error(ERR_SIN, ERR_FNC, al.obtenerLexema()); return;
		}//si no es un tipo de dato es error
		al.escanear();
		parametros();
		while ( !al.obtenerLexema().equals("inicio")) {
			if ( al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
				 al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ) {
				variable();
			} else {
				error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;	
			}//if{}else{}
		}//Mientras no sea inicio
		al.escanear();
		while ( !al.obtenerLexema().equals("fin")) {
			instruccion();
		}// mientras no es FIN
//		if ( !contieneRegreso ) error(ERR_SEM, ERR_FNC, al.obtenerLexema()); return;
		al.escanear();
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es DE
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es el identi
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_PCS, al.obtenerLexema()); return;
		}// if no es punto y coma
		al.escanear();
	}//funciones

	void parametros() {
		if ( !al.obtenerLexema().equals("(")) {
			error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
		}//if es (
		al.escanear();	
		while ( !al.obtenerLexema().equals(")")) {
			if ( !(al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
					   al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico")) ){
					error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
				}//if
				al.escanear();
				if ( !al.obtenerToken().equals("identi")) {
					error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
				}//if no es identi
				al.escanear();
				if ( al.obtenerLexema().equals(",")) {
					al.escanear();
				} else if (!al.obtenerLexema().equals(")")) {
					error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
				}// si no es , y tampoco es ) entonces es un error
		}//while parentesis_d
		al.escanear();		
	}//parametros
	
	void instruccion() {
		if ( al.obtenerToken().equals("identi")) {
			al.escanear();
			if ( al.obtenerLexema().equals("(")) {
				llamarFuncion();
				al.escanear();
				if ( !al.obtenerLexema().equals(";")){
					error(ERR_SIN, "ERR_INSTRUCCION", al.obtenerLexema()); return;
				}//si no es ; entonces es un error
				al.escanear();
			} else if ( al.obtenerLexema().equals("=")){
				al.escanear();
				asignacion();
				if ( !al.obtenerLexema().equals(";")){
					error(ERR_SIN, "ERR_INSTRUCCION", al.obtenerLexema()); return;
				}//si no es ; entonces es un error
				al.escanear();
			} else if ( al.obtenerLexema().equals("[")) {
				declararVector();
				 if ( al.obtenerLexema().equals("=")){ 
					 al.escanear();
					 System.out.println("entro de asignacion con: "+al.obtenerLexema());
					asignacion();
					System.out.println("salio de asignacion con: "+al.obtenerLexema());
					if ( !al.obtenerLexema().equals(";")){
						error(ERR_SIN, "ERR_INSTRUCCION", al.obtenerLexema()); return;
					}//si no es ; entonces es un error
					al.escanear();
					}//si se va a igualar
			} else {
				error(ERR_SIN, "ERR_INSTRUCCION", al.obtenerLexema()); return;
			}//if{}else if{}else if{} else {}
		} else {
			switch(al.obtenerLexema()){
			case "si": instruccionSi(); break;	//BUENA :D
			case "para": instruccionPara(); break;
			case "mientras": instruccionMientras(); break;
			case "regresa": instruccionRegresa(); break;
			default: error(ERR_SIN, "ERR_INSTRUCCION EN SWITCH"+al.obtenerToken(), al.obtenerLexema()); return;
			}//switch	
		}//if{}else{}
	}//
	
	void llamarFuncion() {
		while ( !al.obtenerLexema().equals(")")) {
			al.escanear();
			asignacion();
			if (!(al.obtenerLexema().equals(")") || al.obtenerLexema().equals(","))) {
				error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
			}// si no es , y tampoco es ) entonces es un error		
		}//while parentesis_d
	}//llamarFuncion
	
	void instruccionSi() {
		al.escanear();
		if (!al.obtenerLexema().equals("(")) {
			error(ERR_SIN, "ERR_SI", al.obtenerLexema()); return;
		}//si no es (
		al.escanear();
		asignacion();
		if (!al.obtenerLexema().equals(")")) {
			error(ERR_SIN, "ERR_SI", al.obtenerLexema()); return;
		}//si no es )
		al.escanear();
		if (!al.obtenerLexema().equals("hacer")) {
			error(ERR_SIN, "ERR_SI", al.obtenerLexema()); return;
		}//si no es hacer
		al.escanear();
		cuerpoInstruccion(); 
		if (al.obtenerLexema().equals("sino")) {
			al.escanear();
			if (!al.obtenerLexema().equals("hacer")) {
				error(ERR_SIN, "ERR_SI", al.obtenerLexema()); return;
			}//si no es hacer entonces eserror
			al.escanear();
			cuerpoInstruccion();
		} // si el siguiente lexema es sino
	}//si

	void instruccionPara() {
		al.escanear();
		if (!al.obtenerToken().equals("identi")) {
			error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
		}//si no es identi
		al.escanear();
		if (!al.obtenerLexema().equals("desde")) {
			error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
		}//si no es desde
		al.escanear();
		if (!(al.obtenerToken().equals("identi") || al.obtenerToken().equals("cteEnt") ||
			  al.obtenerToken().equals("cteDec"))) {
			error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
		}//si no es identi o constante
		al.escanear();
		if (!al.obtenerLexema().equals("hasta")) {
			error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
		}//si no es hasta
		al.escanear();
		if (!(al.obtenerToken().equals("identi") || al.obtenerToken().equals("cteEnt") ||
			  al.obtenerToken().equals("cteDec"))) {
				error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
			}//si no es identi o constante
		al.escanear();
		if ( al.obtenerLexema().equals("incr")) {
			al.escanear();
			if (!(al.obtenerToken().equals("identi") || al.obtenerToken().equals("cteEnt") ||
				  al.obtenerToken().equals("cteDec"))) {
				error(ERR_SIN, "ERR_PAR", al.obtenerLexema()); return;
			}//si no es identi o constante
			al.escanear();
		}// si el siguiente lexema no es incr entonces
		cuerpoInstruccion(); 
	}//para

	void instruccionMientras() {
		al.escanear();
		if ( !al.obtenerLexema().equals("(")) {
			error(ERR_SIN, "ERR_MIE", al.obtenerLexema()); return;
		}//si no es parentesis_i
		al.escanear();
		asignacion();
		if ( !al.obtenerLexema().equals(")")) {
			error(ERR_SIN, "ERR_MIE", al.obtenerLexema()); return;
		}//si no es parentesis der
		al.escanear();
		cuerpoInstruccion();
	}//mientras

	void cuerpoInstruccion() {
		if ( !al.obtenerLexema().equals("inicio")) {
			instruccion();
		} else {
			al.escanear();
			while ( !al.obtenerLexema().equals("fin")) {
				instruccion();
			}//mientras esl lexema no sea fin
			al.escanear();
			if ( !al.obtenerLexema().equals(";")) {
				error(ERR_SIN, "ERR_CPO", al.obtenerLexema()); return;
			}//si no es ; es un error
			al.escanear();
		}// si no es inicio entonces es un bloque, si es inicio pueden ser muchos bloques
	}//cuerpoInstruccion
	
	void asignacion() {
		if ( al.obtenerToken().equals("identi")) {
			al.escanear();
			if ( al.obtenerLexema().equals("(")) {
				llamarFuncion();
				al.escanear();
			} else if (al.obtenerLexema().equals("[")) {
				declararVector();
				if (!al.obtenerLexema().equals(";")) {
					error(ERR_SIN, "ERR_ASI", al.obtenerLexema()); return;
				}//si el siguiente token no es ; es error
				al.escanear();
			}//if{}else if{}
		} else if (al.obtenerToken().equals("cteAlf") || al.obtenerToken().equals("cteDec") ||
				   al.obtenerToken().equals("cteEnt") || al.obtenerToken().equals("cteLog")) {
			al.escanear();
		} else if (al.obtenerLexema().equals("(")) {
			al.escanear();
			asignacion();
			al.escanear();
			if (!al.obtenerLexema().equals(")")) {
				error(ERR_SIN, "ERR_ASI", al.obtenerLexema()); return;
			}//si no es )
			//al.escanear();
		} else if(al.obtenerLexema().equals(")")){} //ESTE ES PARA TEST//////////////////
		else {
			error(ERR_SIN, "ERR_ASI", al.obtenerLexema()); return;
		}//if{}else if{}else if{}else{}
		//al.escanear();
		if (al.obtenerToken().equals("opeLog") || al.obtenerToken().equals("opeAri") ||
			al.obtenerToken().equals("opeRel")) {
			al.escanear();
			asignacion();
		}//si es un operador logico, aritmetico o relacional
	}//asignacion	
	
	void instruccionRegresa() {
		al.escanear();
		asignacion();
		if (!al.obtenerLexema().equals(";")) {
			error(ERR_SIN, "ERR_REG", al.obtenerLexema()); return;
		}//si el siguiente token no es ; es error
		al.escanear();
	}//instruccionRegresa
	
}//class