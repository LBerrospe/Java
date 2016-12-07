import java.util.Stack;

/**
 * @author Hector Eduardo Berrospe Barajas
 * @version 1.0
 */

public class AnalizadorSintactico extends AnalizadorLexico {
	StringBuilder asignacion = new StringBuilder();
	StringBuilder auxAsignacion = new StringBuilder();
	StringBuilder asignacionGC = new StringBuilder();
	StringBuilder auxAsignacionGC = new StringBuilder();
	Stack<String> parametro = new Stack<String>();
	Consola userInterface=null;
	AnalizadorLexico al=null;
	AnalizadorSemantico as=null;
	GeneradorCodigo gc=null;
	String nombre="", clase="V", tipo="I",dimension01="0",dimension02="0", nombreProcOFunc="",nombreVariableL="", tipoVariableL="",
			retornoAsignacion="";
	String tipoFunc="";
	short contDimension=0, etiquetaNum=1;
	boolean local=false, contieneRegreso = false, funcion = false, procedimiento = false, principal = false;
	
	public AnalizadorSintactico( String rutaArchivo, Consola c ) {
		super(rutaArchivo, c);
		al = new AnalizadorLexico(rutaArchivo, c);
		as = new AnalizadorSemantico();
		gc = new GeneradorCodigo(rutaArchivo.replace(".txt", ".eje"));
	}//Constructor
	
	void programa() {
		errorControl = false;
		al.escanear();
		if ( !al.obtenerLexema().equals("programa")) {	//programa
			error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si el lexema no es programa
		al.escanear();
		if ( !al.obtenerToken().equals("identi")) {	//programa identificador
			error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		} else {
			as.insertarSimbolo(al.obtenerLexema(), "I", "I", "0", "0", gc);
		}//if{}else{}
		al.escanear();
		//programa identificador [cte|var|func|proc]
		String etiqueta=String.valueOf(etiquetaNum++);
		while (!(al.obtenerLexema().equals("procedimiento") || al.obtenerLexema().equals("funcion") || al.obtenerLexema().equals("inicio"))){
			switch(al.obtenerLexema()){
			case "constante":
				clase="C";
				constante();
				break;
				
			case "entero":		  //variable();		break;
			case "decimal":		  //variable();		break;
			case "alfabetico":	  //variable();		break;
			case "logico":
				clase="V";
				tipo=as.obtenerTipo(al.obtenerLexema());
				variable();	
				break;
				
			case ";":
				al.escanear();
				break;
				
			default: 
				error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//switch
		}//while
		gc.generarCodigo("JMP", "0", "_E"+etiqueta);
		
		while (!al.obtenerLexema().equals("inicio")) {
			switch (al.obtenerLexema()) {
			case "procedimiento":	
				clase = "V";
				tipo = "P";
				local = true;
				procedimiento = true;
				procedimiento();
				local = false;
				procedimiento = false;
				break;
				
			case "funcion":			
				clase = "F";
				local = true;
				funcion = true;
				funcion();
				local = false;
				funcion = false;
				contieneRegreso=false;
				break;
				
			case ";":
				al.escanear();
				break;
				
			default: 
				error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//switch
		}//while proc y func
		
		as.insertarSimbolo("_P", "I", "I", String.valueOf(gc.obtenerCodigoNum()), dimension02, gc);
		gc.generarCodigo("JMP", "0", "1");
		as.insertarSimbolo("_E"+etiqueta, "I", "I", String.valueOf(gc.obtenerCodigoNum()), dimension02, gc);
		al.escanear();
		nombreProcOFunc="";
		principal = true;
		while ( !al.obtenerLexema().equals("fin")) {	//programa identificador [cte|var|func|proc] inicio
			instruccion("","",false);	//programa identificador [cte|var|func|proc] inicio [instruccion]
			if (errorControl){
				return;
			}//if
		}//if fin
		al.escanear();	//programa identificador [cte|var|func|proc] inicio [instruccion] fin
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if de
		al.escanear();
		if ( !al.obtenerLexema().equals("programa")) {
			error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if programa
		al.escanear();
		if ( !al.obtenerLexema().equals(".")){
			error(ERR_SIN, ERR_PGR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		} else {
			gc.generarCodigo("OPR", "0", "0");
			gc.generarArchivoEjecutable();
			imprimirCadena("Compilado con exito");
		}//if{}else{}
	}//programa
	
	void constante(){
		al.escanear();
		if (al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
			al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ){
				tipo=as.obtenerTipo(al.obtenerLexema());				
			} else {
				error(ERR_SIN, ERR_CTE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}else{}
		do {
			al.escanear();
			if ( al.obtenerToken().equals("identi")) {
				if (!as.simboloUsado(al.obtenerLexema())) {
					nombre = al.obtenerLexema();
				} else {
					error(ERR_SEM, ERR_UNI_USO, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}//if{}else{}
			} else {
				error(ERR_SIN, ERR_CTE, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}else{}
			al.escanear();
			if ( al.obtenerLexema().equals("=")) {
				al.escanear();
				if ( !(al.obtenerToken().equals("cteAlf") || al.obtenerToken().equals("cteDec") ||
					   al.obtenerToken().equals("cteEnt") || al.obtenerToken().equals("cteLog"))) {
				error(ERR_SIN, ERR_CTE, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				} else {
					if (as.asignacionValida(tipo, al.obtenerToken())) {
						dimension01 = al.obtenerLexema();
						if (al.obtenerToken().equals("cteLog")) {
							if (al.obtenerLexema().equals("verdadero")) {
								gc.generarCodigo("LIT", "V", "0");
							} else {
								gc.generarCodigo("LIT", "F", "0");
							}//if{}else{}
						} else {
							gc.generarCodigo("LIT", al.obtenerLexema(), "0");
						}//if{}else{}
					} else {
						error(ERR_SEM, ERR_TIP_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
					}//if{}else{}
				}//if{}else{}
				gc.generarCodigo("STO", "0", nombre);
			} else {
				error(ERR_SIN, ERR_CTE, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			} //si no es  = entonces es error
			al.escanear();
			if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
				error(ERR_SIN, ERR_CTE, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			} else {
				as.insertarSimbolo(nombre, clase, tipo, dimension01, dimension02,gc);
				dimension01=dimension02="0";
			}//if{}else{}
		} while (!al.obtenerLexema().equals(";"));
		al.escanear();
	}//constante

	void variable() {
		do {
			al.escanear();
			if ( al.obtenerToken().equals("identi")) {
				nombre = al.obtenerLexema();
				if (local) {
					nombre+="$"+nombreProcOFunc;
				}//if{} 
				if (as.simboloUsado(nombre)) {
					error(ERR_SEM, ERR_UNI_USO, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}//if{}
			} else {
				error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}else{}
			al.escanear();
			if ( al.obtenerLexema().equals("[")) {	//variable[],	variable[cteEnt|identi];
				declararVector();
				contDimension=0;
				if ( al.obtenerLexema().equals("=")) {
					al.escanear();
					if (al.obtenerLexema().equals("[")) {
						inicializarVector();
						al.escanear();
						if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
							error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
						}// if no es punto y coma o coma
					} else {
						error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
					}// si no asigna el vector como vector entonces es un error
				} else if ( al.obtenerLexema().equals(";") || al.obtenerLexema().equals(",")) {
					as.insertarSimbolo(nombre, clase, tipo, dimension01, dimension02,gc);
					dimension01=dimension02="0";
				} else {
					error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}//if{}elseif{}else{}
			} else if ( al.obtenerLexema().equals("=")) {		//variable=[asignacion], variable=[asignacion];
				al.escanear();
				if ( !(al.obtenerToken().equals("cteAlf") || al.obtenerToken().equals("cteDec") ||
					   al.obtenerToken().equals("cteEnt") || al.obtenerToken().equals("cteLog"))) {
				error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				} else {
					if (as.asignacionValida(tipo, al.obtenerToken())) {
						if (al.obtenerToken().equals("cteLog")) {
							if (al.obtenerLexema().equals("verdadero")) {
								gc.generarCodigo("LIT", "V", "0");
							} else {
								gc.generarCodigo("LIT", "F", "0");
							}//if{}else{}
						} else {
							gc.generarCodigo("LIT", al.obtenerLexema(), "0");
						}//if{}else{}
					} else {
						error(ERR_SEM, ERR_TIP_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
					}//if{}else{}
				}//if{}else{}
				al.escanear();
				if ( !(al.obtenerLexema().equals(";") || al.obtenerLexema().equals(","))) {
					error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				} else {
					as.insertarSimbolo(nombre, clase, tipo, dimension01, dimension02,gc); 
					dimension01=dimension02="0";
					gc.generarCodigo("STO", "0", nombre);
				}//if{}else{}
			} else if ( al.obtenerLexema().equals(",") || al.obtenerLexema().equals(";")) { //variable,		variable;
				as.insertarSimbolo(nombre, clase, tipo, dimension01, dimension02,gc);
				dimension01=dimension02="0";
			}  else {
				error(ERR_SIN, ERR_VAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}elseif{}elseif{}else{}
		} while (!al.obtenerLexema().equals(";"));
		al.escanear();
	}//variable
	
	void declararVector() {
		do {
			contDimension++;
			al.escanear();
			if ( al.obtenerToken().equals("identi") || al.obtenerToken().equals("cteEnt")) {
				if (al.obtenerToken().equals("cteEnt")) {
					if (contDimension==1){
						dimension01=al.obtenerLexema();
					} else if(contDimension==2){
						dimension02=al.obtenerLexema();
					}//if{}else{}
				} else {
					if (as.simboloUsado(al.obtenerLexema())) {
						if ( as.obtenerInfoSim(al.obtenerLexema(), as.CLASE).equals("C") && 
							 as.obtenerInfoSim(al.obtenerLexema(), as.TIPO).equals("E") ) {
							if (contDimension==1){
								dimension01=as.obtenerInfoSim(al.obtenerLexema(), as.DIM01); 
							} else if(contDimension==2){
								dimension02=as.obtenerInfoSim(al.obtenerLexema(), as.DIM01);
							}//if{}else{}
						} else {
							error(ERR_SEM, ERR_TIP_DIM, al.obtenerNumeroLinea(), nombre); break;
						}//if{}else{}
					} else {
						error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), nombre); break;
					}//if{}else{}
				}//if{}else{}
			} else {
				error(ERR_SIN, "Sintaxis incorrecta.\n"
						+ "<declararVector>:=\n\t"
						+ "\"[\"<identi>|<cteEnt>\"]\"+", al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}else{}
			al.escanear();
			if ( !al.obtenerLexema().equals("]")) {
				error(ERR_SIN, "Sintaxis incorrecta.\n"
						+ "<declararVector>:=\n\t"
						+ "\"[\"<identi>|<cteEnt>\"]\"+", al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//si no es ]
			al.escanear();
		} while ( al.obtenerLexema().equals("["));
	}//declararVector
	
	void leerVector() {
		do {
			al.escanear();
			asignacion(auxAsignacion,auxAsignacionGC,nombreVariableL); 
			retornoAsignacion=as.resultadoExpresionSem(auxAsignacion.toString()); 
			if (retornoAsignacion.equals("E")) {
				gc.generarCodigoExpresion(auxAsignacionGC.toString(), this, as);
				auxAsignacionGC.delete(0, auxAsignacionGC.length());
				auxAsignacion.delete(0, auxAsignacion.length());	
			} else {
				error(ERR_SEM, ERR_TIP_DIM, al.obtenerNumeroLinea(), auxAsignacion.toString()); break;
			}//if{}else{}
			if ( !al.obtenerLexema().equals("]")) {
				error(ERR_SIN, "Sintaxis incorrecta.\n"
						+ "<leerVector>:=\n\t"
						+ "\"[\"<asignacion>\"]\"+", al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//si no es ]
			al.escanear();
		} while ( al.obtenerLexema().equals("["));
	}//declararVector
	
	String leerVectorAsi() {
		String vec="";
		do {
			vec+="¤"+al.obtenerLexema();
			al.escanear();
			asignacion(auxAsignacion,auxAsignacionGC,nombreVariableL); 
			retornoAsignacion=as.resultadoExpresionSem(auxAsignacion.toString()); 
			if (retornoAsignacion.equals("E")) {
				vec+=auxAsignacionGC.toString();
				auxAsignacionGC.delete(0, auxAsignacionGC.length());
				auxAsignacion.delete(0, auxAsignacion.length());	
			} else {
				error(ERR_SEM, ERR_TIP_DIM, al.obtenerNumeroLinea(), auxAsignacion.toString()); break;
			}//if{}else{}
			if ( !al.obtenerLexema().equals("]")) {
				error(ERR_SIN, "Sintaxis incorrecta.\n"
						+ "<leerVector>:=\n\t"
						+ "\"[\"<asignacion>\"]\"+", al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			} else {
				vec+="¤"+al.obtenerLexema();
			}//si no es ]
			al.escanear();
		} while ( al.obtenerLexema().equals("["));
		return vec;
	}//declararVector
		
	void inicializarVector() {
		//System.out.println("Aqui se asigna el vector :D");
	}//inicializarVector
	
	void procedimiento(){
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			nombre=al.obtenerLexema();
		} else {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}else{}
		al.escanear();
		parametros();
		nombreProcOFunc=nombre;
		if (!as.simboloUsado(nombre)) {
			as.insertarSimbolo(nombre, clase, tipo, String.valueOf(gc.obtenerCodigoNum()), dimension02,gc);
			while (!parametro.isEmpty()) {
				tipoVariableL=parametro.pop();
				nombre = parametro.pop()+"$"+nombreProcOFunc;
				as.insertarSimbolo(nombre, "L", tipoVariableL, "0", "0", gc);
				gc.generarCodigo("STO", "0", nombre);
			}//while
		} else {
			error(ERR_SEM, ERR_UNI_USO, al.obtenerNumeroLinea()-1, nombreProcOFunc); return;
		}//if{}else{}
		while ( !al.obtenerLexema().equals("inicio")) {
			if ( al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
				 al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ) {
				clase="L";
				tipo=as.obtenerTipo(al.obtenerLexema());
				variable(); 
			} else if (al.obtenerLexema().equals(";")){
				al.escanear();
			} else {
				error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}elseif{}else{}
		}//Mientras no sea inicio
		al.escanear();	
		while ( !al.obtenerLexema().equals("fin")) {
			instruccion("","",false);
			if (errorControl){
				break;
			}//if
		}// mientras no es FIN
		al.escanear();
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}// if no es DE
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			String s[]=nombreProcOFunc.split("\\$",0);
			if (!al.obtenerLexema().equals(s[0])) {
				error(ERR_SEM, ERR_REL_FINF, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}
		} else {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}else{}
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}// if no es punto y coma
		gc.generarCodigo("OPR", "0", "1");
		al.escanear();
	}//procesos
	
	void funcion(){
		al.escanear();
		if ( !(al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
			   al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico")) ){
				error(ERR_SIN, ERR_FNC, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		} else {
			tipoFunc=as.obtenerTipo(al.obtenerLexema());
		}//si no es un tipo de dato es error
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			nombre=al.obtenerLexema();
		} else {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}else{}
		al.escanear();
		parametros();
		nombreProcOFunc=nombre;
		if (!as.simboloUsado(nombre)) {
			as.insertarSimbolo(nombre, clase, tipoFunc, String.valueOf(gc.obtenerCodigoNum()), dimension02,gc);
			while (!parametro.isEmpty()) {
				tipoVariableL=parametro.pop();
				nombre = parametro.pop()+"$"+nombreProcOFunc;
				as.insertarSimbolo(nombre, "L", tipoVariableL, "0", "0", gc);
				gc.generarCodigo("STO", "0", nombre);
			}//while
		} else {
			error(ERR_SEM, ERR_UNI_USO, al.obtenerNumeroLinea()-1, nombreProcOFunc); return;
		}//if{}else{}
		while ( !al.obtenerLexema().equals("inicio")) {
			if ( al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
				 al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ) {
				clase="L";
				tipo=as.obtenerTipo(al.obtenerLexema());
				variable();
			} else if (al.obtenerLexema().equals(";")){
				al.escanear();
			} else {
				error(ERR_SIN, ERR_FNC, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}elseif{}else{}
		}//Mientras no sea inicio
		al.escanear();
		while ( !al.obtenerLexema().equals("fin")) {
			instruccion("","",false);
			if (errorControl){
				return;
			}
		}// mientras no es FIN
		if ( !contieneRegreso ) {
			error(ERR_SIN, ERR_CNF_RETF1, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}
		al.escanear();
		if ( !al.obtenerLexema().equals("de")) {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}// if no es DE
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			String s[]=nombreProcOFunc.split("\\$",0);
			if (!al.obtenerLexema().equals(s[0])) {
				error(ERR_SEM, ERR_REL_FINF, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}
		} else {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}else{}
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_PCS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}// if no es punto y coma
		al.escanear();
	}//funciones

	void parametros() {
		if ( !al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_PAR, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if es (
		al.escanear();	
		while ( !al.obtenerLexema().equals(")")) {
			if ( al.obtenerLexema().equals("entero") || al.obtenerLexema().equals("decimal") ||
				 al.obtenerLexema().equals("alfabetico") || al.obtenerLexema().equals("logico") ){
					tipoVariableL = as.obtenerTipo(al.obtenerLexema());
					nombre+="$"+tipoVariableL;
				} else {
					error(ERR_SIN, ERR_PAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}//if{}else{}
				al.escanear();
				if ( al.obtenerToken().equals("identi")) {
					parametro.push(al.obtenerLexema());
					parametro.push(tipoVariableL);
				} else {
					error(ERR_SIN, ERR_PAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}//if{}else{}
				al.escanear();
				if ( al.obtenerLexema().equals(",")) {
					al.escanear();
				} else if (!al.obtenerLexema().equals(")")) {
					error(ERR_SIN, ERR_PAR, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}// si no es , y tampoco es ) entonces es un error
		}//while parentesis_d
		al.escanear();		
	}//parametros
	
	void instruccion(String jmpInterrumpe, String jmpContinua, boolean ciclo) {
		if ( al.obtenerToken().equals("identi")) {	//identificador
			nombre = al.obtenerLexema();
			al.escanear();
			if ( al.obtenerLexema().equals("(")) { //identificador([cte]+)
				String eX="_E"+String.valueOf(etiquetaNum++);
				gc.generarCodigo("LOD", eX, "0");
				nombre=llamarFuncion(nombre);
				if (!as.simboloUsado(nombre)) {
					error(ERR_SEM, ERR_UNI_ND+".\nCrear la funcion: "+nombre, al.obtenerNumeroLinea(), nombre); return;
				}//if{}
				gc.generarCodigo("CAL", nombre, "0");
				as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
				al.escanear();
				if ( !al.obtenerLexema().equals(";")){
					error(ERR_SIN, ERR_INS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//si no es ; entonces es un error
				al.escanear();
			} else if ( al.obtenerLexema().equals("[")) { //identificador[][]
				if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
					nombre=nombre+"$"+nombreProcOFunc;
				} else if (as.simboloUsado(nombre)) {} 
				else {
					error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//if{}elseif{}else{}
				if (as.obtenerInfoSim(nombre, as.DIM01).equals("0") &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V")) {
					error(ERR_SEM, ERR_REL_DIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//ERROR NO ES UN VECTOR
				leerVector();
				 if ( al.obtenerLexema().equals("=")){  //identificador[][]=
					 tipo=as.obtenerInfoSim(nombre, as.TIPO);
					 al.escanear();
					 asignacion(asignacion,asignacionGC,nombre); 			//identificador[][]= <asignacion>
					 retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
					 if (!as.asignacionValida(tipo, retornoAsignacion)) {
							error(ERR_SEM, ERR_TIP_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
						} else {
							gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
							asignacionGC.delete(0, asignacionGC.length());
							asignacion.delete(0, asignacion.length());
							gc.generarCodigo("STO","0",nombre);
						}//if{}
					 if ( !al.obtenerLexema().equals(";")){  //identificador[][]= <asignacion>;
						error(ERR_SIN, ERR_INS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
					}//si no es ; entonces es un error
					al.escanear();
					}//si se va a igualar
			} else if ( al.obtenerLexema().equals("=")){	//identificador=
				if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
					nombre=nombre+"$"+nombreProcOFunc;
					al.escanear();
					tipo = as.obtenerInfoSim(nombre, as.TIPO);
				} else if (as.simboloUsado(nombre)) {
					if (as.obtenerInfoSim(nombre, as.CLASE).equals("C")) {
						 error(ERR_SEM, ERR_REL_CONS, al.obtenerNumeroLinea(), nombre); return;
					 }//if{}
					al.escanear();
					tipo = as.obtenerInfoSim(nombre, as.TIPO);
				} else {
					error(ERR_SEM, ERR_UNI_ND,al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//if{}else{}
				if ((!as.obtenerInfoSim(nombre, as.DIM01).equals("0")) &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V")) {
					error(ERR_SEM, ERR_REL_NDIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//ERROR NO ES UN VECTOR
				asignacion(asignacion,asignacionGC,nombre); 							//identificador=<asignacion>
				retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
				if (as.asignacionValida(tipo, retornoAsignacion)) {
					gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
					asignacion.delete(0, asignacion.length());
					asignacionGC.delete(0, asignacionGC.length());
					gc.generarCodigo("STO", "0", nombre);
				} else {
					error(ERR_SEM, ERR_TIP_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//if{}
				if ( !al.obtenerLexema().equals(";")){	//identificador=<asignacion>;
					error(ERR_SIN, ERR_INS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//si no es ; entonces es un error
				al.escanear();
			} else {
				error(ERR_SIN, ERR_INS, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}else if{}else if{}elseif{} else {}
		} else {
			switch(al.obtenerLexema()){
			case "si": instruccionSi(jmpInterrumpe , jmpContinua, ciclo); break;	
			case "para": instruccionPara(); break;
			case "mientras": instruccionMientras(); break;
			case "haz": instruccionHaz(jmpInterrumpe, jmpContinua, ciclo); break;
			case "lee": instruccionLee(); break;
			case "imprime": instruccionImprime(); break;
			case "imprimenl": instruccionImprimenl(); break;
			case "interrumpe": instruccionInterrumpe(jmpInterrumpe, ciclo); break;
			case "continua": instruccionContinua(jmpContinua, ciclo); break;
			case "regresa": instruccionRegresa(); break;
			case ";": al.escanear(); break;
			default: error(ERR_SIN, ERR_INS,  al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//switch	
		}//if{}else{}
	}//
	
	String llamarFuncion(String nombre) {
		String n=nombre;
		while ( !al.obtenerLexema().equals(")")) {
			al.escanear();
			if (!al.obtenerLexema().equals(")")) {
				asignacion(auxAsignacion,auxAsignacionGC, nombreVariableL);
				retornoAsignacion=as.resultadoExpresionSem(auxAsignacion.toString());
				gc.generarCodigoExpresion(auxAsignacionGC.toString(), this, as);
				auxAsignacion.delete(0, auxAsignacion.length());
				auxAsignacionGC.delete(0, auxAsignacionGC.length());
				n+="$"+retornoAsignacion;
				if (!(al.obtenerLexema().equals(")") || al.obtenerLexema().equals(","))) {
					error(ERR_SIN, ERR_LLF, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				}// si no es , y tampoco es ) entonces es un error	
			}//if{}	
		}//while parentesis_d
		return n;
	}//llamarFuncion
	
	String[] llamarFuncion(String nombre, String params) {
		String n[]={nombre,params};
		while ( !al.obtenerLexema().equals(")")) {
			al.escanear();
			if (!al.obtenerLexema().equals(")")) {
				asignacion(auxAsignacion,auxAsignacionGC, nombreVariableL);
				retornoAsignacion=as.resultadoExpresionSem(auxAsignacion.toString());
				n[1]+=auxAsignacionGC.toString();
				auxAsignacion.delete(0, auxAsignacion.length());
				auxAsignacionGC.delete(0, auxAsignacionGC.length());
				n[0]+="$"+retornoAsignacion;
				if (!(al.obtenerLexema().equals(")") || al.obtenerLexema().equals(","))) {
					error(ERR_SIN, ERR_LLF, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
				} else {
					if (al.obtenerLexema().equals(",")){
						n[1]+="¤,";
					}//if{}
				}// si no es , y tampoco es ) entonces es un error
			}//if{}	
		}//while parentesis_d
		return n;
	}//llamarFuncion
	
	void instruccionSi(String jmpInterrumpe, String jmpContinua, boolean ciclo) {
		al.escanear();
		if (!al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_SI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es (
		al.escanear();
		asignacion(asignacion,asignacionGC,nombre); 
		retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
		if (retornoAsignacion.equals("L")) {
			gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
			asignacion.delete(0, asignacion.length());
			asignacionGC.delete(0, asignacionGC.length());
		} else {
			error(ERR_SEM, ERR_TIP_SI, al.obtenerNumeroLinea(), "(<asignacion>)"); return;
		}//if{}
		if (!al.obtenerLexema().equals(")")) {
			error(ERR_SIN, ERR_SI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es )
		al.escanear();
		if (!al.obtenerLexema().equals("hacer")) {
			error(ERR_SIN, ERR_SI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es hacer
		String eX,eY,etiqueta=String.valueOf(etiquetaNum++);
		eX="_E"+etiqueta;
		gc.generarCodigo("JMC", "F", eX);
		al.escanear();
		cuerpoInstruccion(jmpInterrumpe, jmpContinua, ciclo);
		if (al.obtenerLexema().equals("sino")) {
			etiqueta=String.valueOf(etiquetaNum++);
			eY="_E"+etiqueta;
			gc.generarCodigo("JMP", "0", eY);
			as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			al.escanear();
			if (!al.obtenerLexema().equals("hacer")) {
				error(ERR_SIN, ERR_SI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//si no es hacer entonces eserror
			al.escanear();
			cuerpoInstruccion(jmpInterrumpe, jmpContinua, ciclo);
			as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		} else {
			as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		}//if{]else{}
	}//si
	
	void instruccionPara() {
		al.escanear();
		String nombre=this.nombre;
		if (al.obtenerToken().equals("identi")) {
			nombre = al.obtenerLexema();
			if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
				nombre =nombre+"$"+nombreProcOFunc;
				if (!as.obtenerInfoSim(nombre, as.TIPO).equals("E")) {
					error(ERR_SEM, ERR_TIPO_PARA, al.obtenerNumeroLinea(), al.obtenerLexema());	return;
				}//if{}
			} else if (as.simboloUsado(nombre)) {
				if (as.obtenerInfoSim(nombre, as.TIPO).equals("E")) {
					if (as.obtenerInfoSim(nombre, as.CLASE).equals("C")) {
						error(ERR_SEM, ERR_TIPO_PARA, al.obtenerNumeroLinea(), al.obtenerLexema());	return;
					}//if{}
				} else {
					error(ERR_SEM, ERR_TIPO_PARA, al.obtenerNumeroLinea(), al.obtenerLexema());	return;
				}//if{}else{}
			} else {
				error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), nombre);	return;
			}//if{]else{}
		} else {
			error(ERR_SIN, ERR_PDH, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//else{}
		al.escanear();
		if (!al.obtenerLexema().equals("desde")) {
			error(ERR_SIN, ERR_PDH, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es desde
		al.escanear();
		asignacion(asignacion,asignacionGC,nombre); 
		retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
		if (retornoAsignacion.equals("E")) {
			gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
			asignacion.delete(0, asignacion.length());
			asignacionGC.delete(0, asignacionGC.length());
			gc.generarCodigo("STO", "0", nombre);
		} else {
			error(ERR_SEM, ERR_TIP_DHI, al.obtenerNumeroLinea(), asignacion.toString()); return;
		}//if{}else{}
		if (!al.obtenerLexema().equals("hasta")) {
			error(ERR_SIN, ERR_PDH, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es hasta
		al.escanear();
		asignacion(asignacion,asignacionGC,nombre); 
		retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
		String eX,eY,eZ, etiqueta=String.valueOf(etiquetaNum++);
		if (retornoAsignacion.equals("E")) {
			eX = "_E"+etiqueta;
			etiqueta=String.valueOf(etiquetaNum++);
			eY="_E"+etiqueta;
			etiqueta=String.valueOf(etiquetaNum++);
			eZ="_E"+etiqueta;
			as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			gc.generarCodigo("LOD", nombre, "0");
			gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
			asignacion.delete(0, asignacion.length());
			asignacionGC.delete(0, asignacionGC.length());
		} else {
			error(ERR_SEM, ERR_TIP_DHI, al.obtenerNumeroLinea(), asignacion.toString()); return;
		}//if{}else{}
		String asignaGC="";
		if ( al.obtenerLexema().equals("incr")) {
			al.escanear();
			asignacion(asignacion,asignacionGC,nombre); 
			retornoAsignacion=as.resultadoExpresionSem(asignacion.toString());
			asignaGC=asignacionGC.toString();
			if (retornoAsignacion.equals("E")) {
				asignacion.delete(0, asignacion.length());
				asignacionGC.delete(0, asignacionGC.length());
			} else {
				error(ERR_SEM, ERR_TIP_DHI, al.obtenerNumeroLinea(), asignacion.toString()); return;
			}//if{}else{}
			gc.generarCodigo("OPR", "0", "11");
			gc.generarCodigo("JMC", "F", eY);
			cuerpoInstruccion(eY,eZ,true); 
			as.insertarSimbolo(eZ, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			gc.generarCodigoExpresion(asignaGC.toString(),this ,as);
			gc.generarCodigo("LOD", nombre, "0");
			gc.generarCodigo("OPR", "0", "2");
			gc.generarCodigo("STO", "0", nombre);
			gc.generarCodigo("JMP", "0", eX);
			as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		} else if (al.obtenerLexema().equals("decr")) {
			al.escanear();
			asignacion(asignacion,asignacionGC,nombre); 
			retornoAsignacion=as.resultadoExpresionSem(asignacion.toString());
			asignaGC=asignacionGC.toString();
			if (retornoAsignacion.equals("E")) {
				asignacion.delete(0, asignacion.length());
				asignacionGC.delete(0, asignacionGC.length());
			} else {
				error(ERR_SEM, ERR_TIP_DHI, al.obtenerNumeroLinea(), asignacion.toString()); return;
			}//if{}else{}
			gc.generarCodigo("OPR", "0", "12");
			gc.generarCodigo("JMC", "F", eY);
			cuerpoInstruccion(eY,eZ,true); 
			as.insertarSimbolo(eZ, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			gc.generarCodigoExpresion(asignaGC.toString(),this ,as);
			gc.generarCodigo("LOD", nombre, "0");
			gc.generarCodigo("OPR", "0", "2");
			gc.generarCodigo("STO", "0", nombre);
			gc.generarCodigo("JMP", "0", eX);
			as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		} else {
			gc.generarCodigo("OPR", "0", "11");
			gc.generarCodigo("JMC", "F", eY);
			cuerpoInstruccion(eY,eZ,true); 
			as.insertarSimbolo(eZ, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			gc.generarCodigo("LIT", "1", "0");
			gc.generarCodigo("LOD", nombre, "0");
			gc.generarCodigo("OPR", "0", "2");
			gc.generarCodigo("STO", "0", nombre);
			gc.generarCodigo("JMP", "0", eX);
			as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		}// si el siguiente lexema no es incr entonces
	}//para
	
	void instruccionMientras() {
		al.escanear();
		if ( !al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_MIE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es parentesis_i
		al.escanear();
		asignacion(asignacion,asignacionGC,nombre); 
		retornoAsignacion=as.resultadoExpresionSem(asignacion.toString());
		String eX,eY, etiqueta=String.valueOf(etiquetaNum++);
		if (retornoAsignacion.equals("L")) {
			//AQUI DEBE DE HABER UNA ETIQUETA X
			eX = "_E"+etiqueta;
			etiqueta=String.valueOf(etiquetaNum++);
			eY="_E"+etiqueta;
			as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
			gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
			asignacion.delete(0, asignacion.length());
			asignacionGC.delete(0, asignacionGC.length());
			gc.generarCodigo("JMC", "F", eY);
		} else {
			error(ERR_SEM, ERR_TIP_MIE, al.obtenerNumeroLinea(), asignacion.toString()); return;
		}//if{}else{}
		if ( !al.obtenerLexema().equals(")")) {
			error(ERR_SIN, ERR_MIE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es parentesis der
		al.escanear();
		cuerpoInstruccion(eY,eX,true);
		gc.generarCodigo("JMP", "0", eX);
		as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		//ETIQUETA Y
	}//mientras

	void instruccionHaz(String jmpInterrumpe, String jmpContinua, boolean ciclo) { // interrumpe y continua son el mismo
		al.escanear();
		if ( !al.obtenerLexema().equals("opcion")) {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es opcion
		al.escanear();
		if ( !al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ( es un error
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			nombre = al.obtenerLexema();
			if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
				nombre = nombre+"$"+nombreProcOFunc;
				tipo=as.obtenerInfoSim(nombre, as.TIPO);
			} else if (as.simboloUsado(nombre)) {
				tipo=as.obtenerInfoSim(nombre, as.TIPO);
			} else {
				error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), nombre);
			}//if{]elseif{}else{}
		} else {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es identificador es error
		al.escanear();
		if ( !al.obtenerLexema().equals(")")) {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ) es un error
		al.escanear();
		if ( !al.obtenerLexema().equals("inicio")) {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es inicio entonces es un error
		al.escanear();
		String eX,eY,etiqueta=String.valueOf(etiquetaNum++);
		eY="_E"+etiqueta;
		while ( !(al.obtenerLexema().equals("fin") || al.obtenerLexema().equals("otro"))) {
			if ( !al.obtenerLexema().equals("caso")) {
				error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//si no es caso entonces es error
			al.escanear();
			if (!(al.obtenerToken().equals("identi") || al.obtenerToken().equals("cteEnt") ||
				  al.obtenerToken().equals("cteDec") || al.obtenerToken().equals("cteAlf") ||
				  al.obtenerToken().equals("cteLog"))) {
				error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			} else {
				etiqueta=String.valueOf(etiquetaNum++);
				eX="_E"+etiqueta;
				gc.generarCodigo("LOD", nombre, "0");
				switch(al.obtenerToken()) {
				case "identi":
					nombre = al.obtenerLexema();
					if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
						nombre=nombre+"$"+nombreProcOFunc;
						if (as.asignacionValida(tipo, as.obtenerInfoSim(nombre, as.TIPO))) {
							gc.generarCodigo("LOD", nombre, "0");
							gc.generarCodigo("OPR", "0", "14");
							gc.generarCodigo("JMC", "F", eX);
						} else {
							error(ERR_SEM,"Los casos deben de ser del mismo tipo que el de la opcion", al.obtenerNumeroLinea(), nombre);
						}//if{}else{}
					} else if (as.simboloUsado(nombre)) {
						if (as.asignacionValida(tipo, as.obtenerInfoSim(nombre, as.TIPO))) {
							gc.generarCodigo("LOD", nombre, "0");
							gc.generarCodigo("OPR", "0", "14");
							gc.generarCodigo("JMC", "F", eX);
						} else {
							error(ERR_SEM,"Los casos deben de ser del mismo tipo que el de la opcion", al.obtenerNumeroLinea(), nombre);
						}//if{}else{}
					} else {
						error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), nombre); return;
					}//if{]elseif{}else{}
					break;
				case "cteEnt":case "cteDec":case "cteAlf":
					if (as.asignacionValida(tipo, al.obtenerToken())) {
						gc.generarCodigo("LIT", al.obtenerLexema(), "0");
						gc.generarCodigo("OPR", "0", "14");
						gc.generarCodigo("JMC", "F", eX);
					} else {
						error(ERR_SEM,"Los casos deben de ser del mismo tipo que el de la opcion", al.obtenerNumeroLinea(), nombre); return;
					}//if{}else{}
					break;
				case "cteLog":
					if (as.asignacionValida(tipo, al.obtenerToken())) {
						if (al.obtenerLexema().charAt(0) == 'v') {
							gc.generarCodigo("LIT", "V", "0");
						} else {
							gc.generarCodigo("LIT", "F", "0");
						}//if{}else{}
						gc.generarCodigo("OPR", "0", "14");
						gc.generarCodigo("JMC", "F", eX);
					} else {
						error(ERR_SEM,"Los casos deben de ser del mismo tipo que el de la opcion", al.obtenerNumeroLinea(), nombre); return;
					}//if{}else{}
					break;
				}//switch
			}//if{}else{}
			al.escanear();
			if ( !al.obtenerLexema().equals(":")) {
				error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//si no es : es un error
			al.escanear();
			while ( !(al.obtenerLexema().equals("caso") || al.obtenerLexema().equals("fin") ||
					  al.obtenerLexema().equals("otro"))) {
				instruccion(eY, eY, true);
				if (errorControl){
					break;
				}//if
			}//mientras sea diferente de caso
			as.insertarSimbolo(eX, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
		}//si no es fin | otro
		if ( al.obtenerLexema().equals("otro")) {
			al.escanear();
			if ( !al.obtenerLexema().equals("caso")) {
				error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//si no es 
			al.escanear();
			if ( !al.obtenerLexema().equals(":")) {
				error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//si no es : es un error
			al.escanear();
			while ( !al.obtenerLexema().equals("fin")) {
				instruccion(eY, eY, true);
				if (errorControl){
					break;
				}//if
			}//mientras sea diferente de fin
		}//si es otro entonces haz el bloque
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_HAZ, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ; es un error
		al.escanear();
		as.insertarSimbolo(eY, "I", "I", String.valueOf(gc.obtenerCodigoNum()), "0", gc);
	}//instruccionHaz

	void cuerpoInstruccion(String jmpInterrumpe, String jmpContinua, boolean ciclo) {
		if ( !al.obtenerLexema().equals("inicio")) {
			instruccion(jmpInterrumpe, jmpContinua, ciclo);
			if (errorControl){
				return;
			}//if
		} else {
			al.escanear();
			while ( !al.obtenerLexema().equals("fin")) {
				instruccion(jmpInterrumpe, jmpContinua, ciclo);
				if (errorControl){
					break;
				}//if
			}//mientras esl lexema no sea fin
			al.escanear();
			if ( !al.obtenerLexema().equals(";")) {
				error(ERR_SIN, ERR_CPO, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//si no es ; es un error
			al.escanear();
		}// si no es inicio entonces es un bloque, si es inicio pueden ser muchos bloques
	}//cuerpoInstruccion

	void asignacion(StringBuilder sb, StringBuilder sb2, String nombre) {
		if ( al.obtenerToken().equals("identi")) {
			nombre=al.obtenerLexema();
			al.escanear();
			if ( al.obtenerLexema().equals("(")) {
				String s[] = llamarFuncion(nombre,"");
				s[1]=s[1].replace('¤', '|');
				if (as.simboloUsado(s[0])) {
					sb.append("¤"+as.obtenerInfoSim(s[0], as.TIPO));
					sb2.append("¤ƒ"+s[0]+"|("+s[1]+"|)");
				} else {
					error(ERR_SEM, "Funcion no declarada / parametros incorrectos",al.obtenerNumeroLinea(), nombre); return;
				}//if{}else{}
				al.escanear();
			} else if (al.obtenerLexema().equals("[")) {
				String vec="";
				vec=leerVectorAsi();
				vec=vec.replace('¤', '|');
				if (as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
					nombre=nombre+"$"+nombreProcOFunc;
					sb.append("¤"+as.obtenerInfoSim(nombre, as.TIPO));
					sb2.append("¤£"+nombre+vec);
				} else if (as.simboloUsado(nombre)) {
					sb.append("¤"+as.obtenerInfoSim(nombre, as.TIPO));
					sb2.append("¤£"+nombre+vec);
				} else {
					error(ERR_SEM, ERR_UNI_ND,al.obtenerNumeroLinea(), nombre); return;
				}//if{}elseif{}else{}
				if (as.obtenerInfoSim(nombre, as.DIM01).equals("0") &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V")) {
					error(ERR_SEM, ERR_REL_DIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//ERROR NO ES UN VECTOR
			} else {
				if(as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
					nombre=nombre+"$"+nombreProcOFunc;
					sb.append("¤"+as.obtenerInfoSim(nombre, as.TIPO));
					sb2.append("¤"+nombre);
				} else if(as.simboloUsado(nombre)) {
					sb.append("¤"+as.obtenerInfoSim(nombre, as.TIPO));
					sb2.append("¤"+nombre);
				} else {
					error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//if{}elseif{}else{}
				if ((!as.obtenerInfoSim(nombre, as.DIM01).equals("0")) &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V")) {
					error(ERR_SEM, ERR_REL_NDIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
				}//ERROR NO ES UN VECTOR
			}//if{}else if{}else{}
		} else if (al.obtenerToken().equals("cteAlf") || al.obtenerToken().equals("cteDec") ||
				   al.obtenerToken().equals("cteEnt") || al.obtenerToken().equals("cteLog")) {
			switch (al.obtenerToken()){
			case "cteAlf":	sb.append("¤A"); sb2.append("¤_"+al.obtenerLexema());	break;
			case "cteDec":	sb.append("¤D"); sb2.append("¤_"+al.obtenerLexema());	break;
			case "cteEnt":	sb.append("¤E"); sb2.append("¤_"+al.obtenerLexema());	break;
			case "cteLog":	sb.append("¤L"); 
				if (al.obtenerLexema().equals("verdadero")) {
					sb2.append("¤_V");	
				} else {
					sb2.append("¤_F");	
				}//if{}else{}
				break;
			}//switch
			al.escanear();
		} else if (al.obtenerLexema().equals("(")) {
			sb.append("¤"+al.obtenerLexema());
			sb2.append("¤"+al.obtenerLexema());	
			al.escanear();
			asignacion(sb,sb2,nombre);
			if (al.obtenerLexema().equals(")")) {
				sb.append("¤"+al.obtenerLexema());
				sb2.append("¤"+al.obtenerLexema());	
			} else {
				error(ERR_SIN, ERR_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}else{}
			al.escanear();
		} else if(al.obtenerLexema().equals(")")){} //ESTE ES PARA TEST//////////////////
		else {
			error(ERR_SIN, ERR_ASI, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}else if{}else if{}else{}
		if (al.obtenerToken().equals("opeLog") || al.obtenerToken().equals("opeAri") ||
			al.obtenerToken().equals("opeRel")) {
			sb.append("¤"+al.obtenerLexema());
			sb2.append("¤"+al.obtenerLexema());	
			al.escanear();
			asignacion(sb,sb2,nombre); 
		}//si es un operador logico, aritmetico o relacional
	}//asignacion	
	
	void instruccionImprime() {
		al.escanear();
		if (!al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_IMP, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ( es un error
		al.escanear();
		while ( !al.obtenerLexema().equals(")")) {
			asignacion(asignacion,asignacionGC,nombre); 
			retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
			gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
			asignacion.delete(0, asignacion.length());
			asignacionGC.delete(0, asignacionGC.length());
			if (al.obtenerLexema().equals(",")) {
				al.escanear();
			} else if (!al.obtenerLexema().equals(")")){
				error(ERR_SIN, ERR_IMP, al.obtenerNumeroLinea(), al.obtenerLexema()); break;
			}//if{}elseif{}
			gc.generarCodigo("OPR", "0", "20");
		}//mientras no sea )
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_IMP, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ; es un error
		al.escanear();
	}//instruccionImprime()
	
	void instruccionImprimenl() {
		instruccionImprime();
		gc.generarCodigo("LIT", " ", "0");
		gc.generarCodigo("OPR", "0", "21");
	}//instruccionImprime()
		
	void instruccionLee() {
		al.escanear();
		if (!al.obtenerLexema().equals("(")) {
			error(ERR_SIN, ERR_LEE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ( es un error
		al.escanear();
		if ( al.obtenerToken().equals("identi")) {
			nombre=al.obtenerLexema();
			if(as.simboloUsado(nombre+"$"+nombreProcOFunc)) {
				nombre=nombre+"$"+nombreProcOFunc;
			} else if (as.simboloUsado(nombre)) {
				//
			} else {
				error(ERR_SEM, ERR_UNI_ND, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}
			//Si si existe has lo siguiente
		} else {
			error(ERR_SIN, ERR_LEE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}// si no es una constante alfabetica es un error
		al.escanear();
		if (al.obtenerLexema().equals("[")) {
			if (as.obtenerInfoSim(nombre, as.DIM01).equals("0") &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V")) {
				error(ERR_SEM, ERR_REL_DIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//ERROR NO ES UN VECTOR
			leerVector();
		} else {
			if (!(as.obtenerInfoSim(nombre, as.DIM01).equals("0")) &&  as.obtenerInfoSim(nombre, as.CLASE).equals("V") ){
				error(ERR_SEM, ERR_REL_NDIM, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//ERROR NO ES UN VECTOR
		}//if{]else{}
		gc.generarCodigo("OPR", nombre, "19");
		if (!al.obtenerLexema().equals(")")) {
			error(ERR_SIN, ERR_LEE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ) es un error
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_LEE, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ; es un error
		al.escanear();
	}//instruccionLee
	
	void instruccionInterrumpe(String jmpInterrumpe, boolean ciclo) {
		if (!ciclo && principal) {
			if (principal) System.out.println(al.obtenerNumeroLinea()+"Estamos en principal");
			error(ERR_SEM, ERR_CNF_IC, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_INT, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ; es un error
		gc.generarCodigo("JMP", "0", jmpInterrumpe);
		al.escanear();
	}//instruccionInterrumpe()
	//Continua
	void instruccionContinua(String jmpContinua, boolean ciclo) {
		if (!ciclo && principal) {
			error(ERR_SEM, ERR_CNF_IC, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}
		al.escanear();
		if ( !al.obtenerLexema().equals(";")) {
			error(ERR_SIN, ERR_CON, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//si no es ; es un error
		al.escanear();
		gc.generarCodigo("JMP", "0", jmpContinua);
	}//instruccionContinua
	
	void instruccionRegresa() {
		if (principal) {
			error(ERR_SEM, "Estatuto regresa no debe de aparecer en la funcion principal", al.obtenerNumeroLinea(), al.obtenerLexema()); return;
		}//if{}
		al.escanear();
		if ( al.obtenerLexema().equals(";")) {
			if (funcion) {
				error(ERR_SEM, ERR_CNF_RETF, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}
			al.escanear();
		} else {
			if (procedimiento) {
				error(ERR_SEM, ERR_CNF_RETP, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//if{}
			asignacion(asignacion,asignacionGC,nombre); 
			retornoAsignacion=as.resultadoExpresionSem(asignacion.toString()); 
			if (retornoAsignacion.equals(tipoFunc)) {
				gc.generarCodigoExpresion(asignacionGC.toString(), this, as);
				asignacionGC.delete(0, asignacionGC.length());
				asignacion.delete(0, asignacion.length());
				gc.generarCodigo("STO", "0", nombreProcOFunc);
			} else {
				error(ERR_SEM, ERR_TIP_RET, al.obtenerNumeroLinea(), asignacion.toString()); return;
			}//if{}else{}
			if (!al.obtenerLexema().equals(";")) {
				error(ERR_SIN, ERR_REG, al.obtenerNumeroLinea(), al.obtenerLexema()); return;
			}//si el siguiente token no es ; es error
			al.escanear();
		}//if{}else{}
		contieneRegreso=true;
		gc.generarCodigo("OPR", "0", "1");
	}//instruccionRegresa
}//class