import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorCodigo {
	String ruta="";
	private StringBuilder codigoTabSim = new StringBuilder();
	private StringBuilder codigo = new StringBuilder();
	private int codigoNum=1;
	
	public GeneradorCodigo( String ruta ){
		this.ruta=ruta;
	}//GenerarCodigo
	
	public void generarCodigoTabSim(String nombre, String clase, String tipo, 
									String dim01, String dim02) {
		codigoTabSim.append(nombre+","+clase+","+tipo+","+dim01+","+dim02+",#,\n");
	}//generarCodigoTabSim
	
	public void generarCodigo( String mnemo, String dirUno, String dirDos ){
		codigo.append("\n"+String.valueOf(codigoNum++)+" "+mnemo+" "+dirUno+", "+dirDos);
	}//generarCodigo

	public void generarArchivoEjecutable() {
		File archivoEjecutable = new File(ruta);
		try (FileWriter fw = new FileWriter(archivoEjecutable);
				BufferedWriter bw = new BufferedWriter(fw);) {
			bw.write(codigoTabSim.toString()+"@"+codigo.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}//try{}catch{}
	}//generarArchivoEjecutable
	
	void generarCodigoFuncion(String funcion,AnalizadorSintactico as, AnalizadorSemantico asm){
		String params="";
		int i=0;
		String eX="_E"+String.valueOf(as.etiquetaNum++);
		generarCodigo("LOD", eX, "0");
		String s[]=funcion.split("\\|");
		i++; //(
		i++; 
		while (!s[i].equals(")")){
			params+="¤"+s[i];
			i++;
			if (s[i].equals(",")){
				generarCodigoExpresion(params.replace('|', '¤'),as,asm);
				i++;
				params="";
			} else if (s[i].equals(")")) {
				generarCodigoExpresion(params.replace('|', '¤'),as,asm);
				params="";
			}//if{}elseif{}
		}//while
		generarCodigo("CAL", s[0], "0");
		asm.insertarSimbolo(eX, "I", "I", String.valueOf(codigoNum), "0", this);
		generarCodigo("LOD", s[0], "0");
	}//generarCodigoFuncion
	
	void generarCodigoVector(String vector,AnalizadorSintactico as, AnalizadorSemantico asm){
		String expresion="";
		String s[]=vector.split("\\|");
		int i=0;
		i++; //[
		i++; 
		while (!s[i].equals("]")){
			expresion+="¤"+s[i];
			i++;
			if (s[i].equals("]")){
				generarCodigoExpresion(expresion,as,asm);
				expresion="";
				if ( i+1 != s.length){
					i++;
					i++;
				}//if{}
			}//if{}
		}//while
		generarCodigo("LOD", s[0], "0");
	}//generarCodigoVector
	
	void generarCodigoExpresion(String expresionInfija, AnalizadorSintactico as, AnalizadorSemantico asm) {
		String expresionPostfija = asm.infijaAPostfija(expresionInfija);
		for (String s: expresionPostfija.split("¤")) {
			if (asm.prioridadOperador(s) == -1 && !(s.equals(""))) {
				if (s.startsWith("_")) {
					generarCodigo("LIT", s.substring(1), "0");
				} else if(s.startsWith("ƒ")){
					generarCodigoFuncion(s.substring(1),as,asm);
				} else if (s.startsWith("£")){
					generarCodigoVector(s.substring(1),as,asm);
				} else {
					generarCodigo("LOD", s, "0");
				}//if{}else{}
			} else if (asm.prioridadOperador(s) == 2){
				generarCodigo("OPR", "0", "17");
			} else {
				switch (s) {
				case "+":	generarCodigo("OPR", "0", "2");	break;
				case "-":	generarCodigo("OPR", "0", "3");	break;
				case "*":	generarCodigo("OPR", "0", "4");	break;
				case "/":	generarCodigo("OPR", "0", "5");	break;
				case "%":	generarCodigo("OPR", "0", "6");	break;
				case "^":	generarCodigo("OPR", "0", "7");	break;
				case "<":	generarCodigo("OPR", "0", "9");	break;
				case ">":	generarCodigo("OPR", "0", "10");	break;
				case "<=":	generarCodigo("OPR", "0", "11");	break;
				case ">=":	generarCodigo("OPR", "0", "12");	break;
				case "!=":	generarCodigo("OPR", "0", "13");	break;
				case "==":	generarCodigo("OPR", "0", "14");	break;
				case "y":	generarCodigo("OPR", "0", "15");	break;
				case "o":	generarCodigo("OPR", "0", "16");	break;
				}//switch
			}//if{}else{}elseif{}
		}//for
	}//resultadoExpresion
	
	public int obtenerCodigoNum() {
		return codigoNum;
	}//obtenerCodigoNum
}//GeneradorCodigo
