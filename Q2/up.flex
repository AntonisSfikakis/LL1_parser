
%%

%class Scanner
%line
%column
%standalone
%unicode




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

