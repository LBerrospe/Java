/*
struct simbolos {
    string nom;
    string clase;
    string tipo;
    string dimen1;
    string dimen2;
};

struct locales {
    string clase;
    string tipo;
    string dimen1;
    string dimen2;
    string procp;
};

struct codigo {
    string mnemo;
    string dir1;
    string dir2;
};

codigo progra[10000];
simbolos tabSim[10000];

fstream aFuente;
ofstream aObjeto;

string lexema= "", token = "", lex= "", nomf="", ambito="";
char fileArray[800000], linea[100];
const int ERR = -1, ACP = 999;
int conVar=0, conE=1, conR=0;
int indx = 0, reng = 1, colo = 1, codn=1, simn=0;
bool banP=false, banMain=false;
string dim1, dim2;

//Generacion de Código
void genCod(string m, string d1, string d2) {
    progra[codn].mnemo = m;
    progra[codn].dir1 = d1;
    progra[codn++].dir2 = d2;

}
void insTabSim(string nmb, string cla, string tip, string d1, string d2) {
    tabSim[simn].nom = nmb;
    tabSim[simn].clase = cla;
    tabSim[simn].tipo = tip;
    tabSim[simn].dimen1 = d1;
    tabSim[simn++].dimen2 = d2;
}
//Verificacion Semántica
stack<string> ptipos;
const string cTipo[] = { "E:=E", "A:=A", "R:=R", "L:=L", "R:=E",
                                         "E+E", "E+R", "R+E", "R+R", "A+A",
                                         "E-E", "E-R", "R-E", "R-R",
                                         "E*E", "E*R", "R*E", "R*R",
                                         "E/E", "E/R", "R/E", "R/R",
                                         "E%E", "-E", "-R",
                                         "L&&L", "L||L", "!L",
                                         "E>E", "R>E", "E>R", "R>R",
                                         "E<E", "R<E", "E<R", "R<R",
                                         "E>=E", "R>=E", "E>=R", "R>=R",
                                         "E<=E", "R<=E", "E<=R", "R<=R",
                                         "E!=E", "R!=E", "E!=R", "R!=R", "A!=A",
                                         "E==E", "R==E", "E==R", "R==R", "A==A"
};
const string tipoR[] = {                 "",  "",  "",  "",  "",
                                          "E", "R", "R", "R", "A",
                                          "E", "R", "R", "R",
                                          "E", "R", "R", "R",
                                          "R", "R", "R", "R",
                                          "E", "E", "R",
                                          "L", "L", "L",
                                          "L", "L", "L", "L",
                                          "L", "L", "L", "L",
                                          "L", "L", "L", "L",
                                          "L", "L", "L", "L",
                                          "L", "L", "L", "L", "L",
                                          "L", "L", "L", "L", "L"
};

//Analizador Sintáctico
void expr(); //Prototipo de Expr
string tvar;
string nvar;

void uparams() {
    do {
        if(lex == ",") {
            if (banP) genCod("OPR", "0", "20");
            lex = lexico();
        }
        expr();
    }while( lex == ",");
}

void callf() {
    lex = lexico();
    if( lex != ")")  uparams();
    if( lex != ")") {
          cout << "[" << reng << "]" << "[" << colo << "] "
                    << " Se esperaba [cerrar parentesis ')'] y encontro " + lex << endl;
    }
    lex = lexico();
}
void udimen() {
    lex = lexico();
    expr();
    if( lex != "]" ) {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [ cerrar corchetes ']' ] y encontro " + lex << endl;
    }
    lex = lexico();
}
void asigna() {
    if( lex == "[") udimen();
    if( lex != ":=") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [ := ] y encontro " + lex << endl;
    }
    lex = lexico();
    expr();
    string tp = ptipos.top(), tp1;
    ptipos.pop();
    tp1=ptipos.top();
    ptipos.pop();
    tp = tp1+":="+tp;
    int i;
    if( (i = buscaTipo(tp) ) >= 0)
        ptipos.push(tipoR[i]);
    else {
             ptipos.push("I");
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Conflicto en tipos en asignacion <"<< tp << ">" << endl;
    }
    genCod("STO", "0", nvar);
}
void term() {
     if( lex == "(") {
        lex = lexico();
        expr();
        if( lex != ")" ) {
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Se esperaba [cerrar parentesis ')' ] y encontro " + lex << endl;
        }
        lex = lexico();
     }
     else if ( token == "conlog" || token == "conent" ||
                    token == "conrea" || token == "conalf") {
            if( token == "conent" || token == "conrea") {
                 if(lex == "0 ") lex = "0";
                 genCod("LIT", lex, "0");
            }
            if(token=="conalf")
                 genCod("LIT", lex, "0");
            if(token == "conlog") ptipos.push("L");
            else if( token == "conent") ptipos.push("E");
            else if( token == "conrea") ptipos.push("R");
            else if( token == "conalf") ptipos.push("A");
            if(token == "conlog") {
                  if(lex == "false") genCod("LIT", "F", "0");
                  else if(lex == "true") genCod("LIT", "V", "0");
            }
            lex = lexico();
     }
     else if( token == "identi") {
            string ide = lex;
            int i;
            if( (i = buscaTabSim(ide) ) >=0) {
                 ptipos.push(tabSim[i].tipo);
            }
            else {
                  cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Identificador " + lex + " NO ha sido declarado" << endl;
                  ptipos.push("I");
            }
            lex = lexico();
            if (lex == "(" ) {
                 string Ex = "_E" + convertInt(conE++);
                 genCod("LOD", Ex, "0");
                 callf();
                 genCod("CAL", nomf, "0");
                 insTabSim(Ex, "I", "I", convertInt(codn), "0");
            }
            else if (lex == "[") udimen();

            genCod("LOD", ide, "0");
     } else {
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Se esperaba [se esperaba abrir '(', constante, lamada a Funcion, true o false ] y encontro " + lex << endl;
                lex = lexico();
     }
}

void opSig() {
         string op="";
         if (lex == "-") {
                op = "-";
                lex = lexico();
         }
         term();
        if( op == "-" ) {
            string tp = ptipos.top();
            ptipos.pop();
            tp = op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
        }
         if( op == "-") genCod("OPR", "0", "8");
}

void opMul() {
    string op;
    do {
        if( lex == "*" || lex == "/" || lex == "%" ) {
                op = lex;
                lex = lexico();
        }
         opSig();
        if( op == "*" || op == "/" || op == "%") {
            string tp = ptipos.top(), tp1;
            ptipos.pop();
            tp1=ptipos.top();
            ptipos.pop();
            tp = tp1+op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
        }
        if( op == "*" ) genCod("OPR", "0", "4");
        if( op == "/" ) genCod("OPR", "0", "5");
        if( op == "%" ) genCod("OPR", "0", "6");
    } while( lex == "*" || lex == "/" || lex == "%");
}
void opSum() {
    string op="";
    bool bin=false;
    do {
        if( (lex == "+" || lex == "-") and bin ) {
                op = lex;
                lex = lexico();
         }
         opMul();
        if( op == "+" || op == "-") {
            string tp = ptipos.top(), tp1;
            ptipos.pop();
            tp1=ptipos.top();
            ptipos.pop();
            tp = tp1+op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
        }
        if( op == "+" ) genCod("OPR", "0", "2");
        else if (op == "-" ) genCod("OPR", "0", "3");
        if (lex == "+"|| lex == "-") bin = true;
    } while( lex == "+" || lex == "-");
}
void opRel() {
         string op;
         opSum();
         if( lex == "<" || lex == ">" || lex == "<=" || lex == ">=" ||
              lex == "==" || lex == "!=") {
              op = lex;
              lex = lexico();
              opSum();
              if( op == "<" || op == ">=" || op == "<=" || op == ">=" ||
                   op == "!=" || op == "==") {
                    string tp = ptipos.top(), tp1;
                    ptipos.pop();
                    tp1=ptipos.top();
                    ptipos.pop();
                    tp = tp1+op+tp;
                    int i;
                    if( (i = buscaTipo(tp) ) >= 0)
                        ptipos.push(tipoR[i]);
                    else {
                        ptipos.push("I");
                        cout << "[" << reng << "]" << "[" << colo << "] "
                                 << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
                    }
              }
              if ( op == "<") genCod("OPR", "0", "9");
              else if ( op == ">")   genCod("OPR", "0", "10");
              else if ( op == "<=") genCod("OPR", "0", "11");
              else if ( op == ">=") genCod("OPR", "0", "12");
              else if ( op == "!=")  genCod("OPR", "0", "13");
              else if ( op == "==") genCod("OPR", "0", "14");
        }
}
void opN() {
         string op = "";
         if( lex == "!" ) {
                op = "!";
                lex = lexico();
         }
         opRel();
        if( op == "!" ) {
            string tp = ptipos.top();
            ptipos.pop();
            tp = op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
        }
         if (op == "!") {
            genCod("OPR", "0", "17");
         }
}
void opy() {
    string op="";
    do {
        if( lex == "&&") {
                op = lex;
                lex = lexico();
        }
        opN();
        if( op == "&&") {
            string tp = ptipos.top(), tp1;
            ptipos.pop();
            tp1=ptipos.top();
            ptipos.pop();
            tp = tp1+op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
        }
        if( op == "&&") genCod("OPR", "0", "16");
    } while( lex == "&&");
}
void expr() {
    string op;
    do {
         if( lex == "||") {
                op = lex;
                lex = lexico();
         }
         opy();
         if( op == "||" ) {
            string tp = ptipos.top(), tp1;
            ptipos.pop();
            tp1=ptipos.top();
            ptipos.pop();
            tp = tp1+op+tp;
            int i;
            if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
            else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Conflicto en tipos en la operacion " << op <<" <"<< tp << ">" << endl;
            }
         }
         if( op == "||") {
            genCod("OPR", "0", "15");
         }
    } while( lex == "||");
}
void regresa() {
    lex = lexico();
    if( token == "identi" || token == "conent" ||
         token == "conlog" || token == "conrea" ||
         token == "conalf" ||
         lex == "(" || lex == "false" || lex == "true") {
             expr();
             conR++;
             string tp = ptipos.top(), tp1, tip;
             ptipos.pop();
             int i;
             if( (i = buscaTabSim(ambito) ) >=0) {
                 tp1 = tabSim[i].tipo;
             }
             else {
                  cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Identificador " + ambito + " NO ha sido declarado" << endl;
                  tp1="I";
             }
             tip = tp;
             tp = tp1+":="+tp;
             if( (i = buscaTipo(tp) ) >= 0)
                ptipos.push(tipoR[i]);
             else {
                ptipos.push("I");
                cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Conflicto en tipos en regresa de funcion <"
                      << tp1 << " regresa tipo " << tip << ">" << endl;
             }

             genCod("STO", "0", nomf);
    }
    genCod("OPR", "0", "1");
}
void libF() {
    lex = lexico();
    if( token != "identi") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [Identificador de Funcion Librerira] y encontro " + lex << endl;
    }
    if( lex == "Println") banP = true;
    lex = lexico();
    if( lex != "(") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [abrir parentesis '('] y encontro " + lex << endl;
            lex = lexico();
    }
    else {
            lex = lexico();
            uparams();
    }
    if( lex != ")") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [cerrar parentesis ')'] y encontro " + lex << endl;
    }
    if ( banP )
        genCod("OPR", "0", "21");
    banP = false;
    lex = lexico();
}
void estatuto();
void block();


void estatuto() {
    if ( token == "identi")  {
         nvar = lex;
         lex = lexico();
         if(lex == ".")
             insTabSim(nvar, "L", "I", "0", "0");
        int i;
        if( (i = buscaTabSim(nvar) ) >=0) {
                if(lex != ".") ptipos.push(tabSim[i].tipo);
         } else {
                  cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Identificador " + nvar + " NO ha sido declarado" << endl;
                  ptipos.push("I");
         }
         if ( lex == "(" )  callf();
         else if ( lex == "[" || lex == ":=") {
            asigna();
         }
         else if ( lex == "." ) {
            libF();
         }
    }
    else if( lex == "return" ) regresa();
    else if( lex == "for" ) fore();
    else if( lex == "if") ife();
}
void comandos(){
    do {
         if( lex == ";") lex = lexico();
         if( lex != ";") estatuto();
    } while ( lex == ";");
}

void funcs() {
    conR = 0;
    do {
        lex = lexico();
        if( token != "identi") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [Identificador] y encontro " + lex << endl;
         }
         if(lex == "main") {
            banMain = true;
            char *s = new char(20);
            ambito = "main";
            stringstream ss;
            ss << codn;
            insTabSim("_P", "I", "I", ss.str(), "0");
         }
         else {
            nomf = lex;
            ambito = lex;
         }
         lex = lexico();
        if( lex != "(") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [parentesis (] y encontro " + lex << endl;
         }
         lex = lexico();
         int linf = codn;
         if( lex != ")") params();
         if( lex != ")") {
             cout << "[" << reng << "]" << "[" << colo << "] "
                      << " Se esperaba [parentesis )] y encontro " + lex << endl;
         }
         lex = lexico();
         if( lex == "{" ) {
            block();
         }
         else {
                char s[5];
                tipo();
                stringstream ss;
                ss << linf;
                insTabSim(nomf, "F", tvar, ss.str(), "0");
                block();
                int i;
                string tp ="";
                if( (i = buscaTabSim(ambito) ) >=0) {
                    tp = tabSim[i].tipo;
                }
                else {
                    cout << "[" << reng << "]" << "[" << colo << "] "
                         << " Identificador " + ambito + " NO ha sido declarado" << endl;
                }
                if(tp != "" && tp != "I" && conR == 0)
                    cout << "[" << reng << "]" << "[" << colo << "] "
                          << " La funcion " + ambito + " debe tener regresa" << endl;
                conR=0;
         }
    } while (lex == "func" );
    if (banMain)  genCod("OPR", "0", "0");
}

void prgm() {
    lex = lexico();
    if( lex != "package") {
        cout << "[" << reng << "]" << "[" << colo << "] "
                  << " Se esperaba [package] y encontro " + lex << endl;
    }
    lex = lexico();
    if( lex != "main") {
        cout << "[" << reng << "]" << "[" << colo << "] "
                  << " Se esperaba [main] y encontro " + lex << endl;
    }
    lex = lexico();
    while( lex == "import") {
       imports();
    }
    while( lex == "var") {
       vars();
    }
    while( lex == "func") {
       funcs();
    }
}


void Sintaxis() {
    prgm();
}
void grabaTabla() {
    for(int i=0; i < simn; i++)
        aObjeto << tabSim[i].nom << ","
                  << tabSim[i].clase << ","
                  << tabSim[i].tipo << ","
                  << tabSim[i].dimen1 << ","
                  << tabSim[i].dimen2 << ",#,"
                  << endl;
    aObjeto << "@" << endl;
}
void grabaCod() {
    for(int i=1; i < codn; i++)
        aObjeto << i << " " << progra[i].mnemo << " " << progra[i].dir1 << ", " << progra[i].dir2 << endl;
}
int main() {
    loadFile();
    reng = 1; colo = 1;
    Sintaxis();
    grabaTabla();
    grabaCod();
    aObjeto.flush();
    aObjeto.close();
    return EXIT_SUCCESS;
}

public class Profe {

}
*/