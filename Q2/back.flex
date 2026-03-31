
%%

%class Scanner
%line
%column
%standalone
%unicode



%{
StringBuffer stringBuffer = new StringBuffer();
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Identifier     = [a-zA-Z_][a-zA-Z0-9_]*
String         = \"[^\"]*\"
If             = if
else           = else
prefix         = prefix
suffix         = suffix

%state STRING
%%

<YYINITIAL> {
"("               { System.out.println("("); }
")"               { System.out.println(")"); }
"+"               { System.out.println("+"); }
","               { System.out.println(","); }
"{"               { System.out.println("{"); }
"}"               { System.out.println("}"); }
{If}              { System.out.println("if"); }
{else}            { System.out.println("else"); }
{prefix}          { System.out.println("prefix"); }
{suffix}          { System.out.println("suffix"); }
{Identifier}      { System.out.println(yytext()); }
{String}          { System.out.println(yytext()); }
{WhiteSpace}      { /* do nothing */ }

}

<STRING> {
      \"                             { yybegin(YYINITIAL);
                                       return symbol(sym.STRING_LITERAL, stringBuffer.toString()); }
      [^\n\r\"\\]+                   { stringBuffer.append( yytext() ); }
      \\t                            { stringBuffer.append('\t'); }
      \\n                            { stringBuffer.append('\n'); }

      \\r                            { stringBuffer.append('\r'); }
      \\\"                           { stringBuffer.append('\"'); }
      \\                             { stringBuffer.append('\\'); }
}

/* No token was found for the input so through an error.  Print out an
   Illegal character message with the illegal character that was found. */
[^] { throw new Error("Illegal character <"+yytext()+">"); }   
