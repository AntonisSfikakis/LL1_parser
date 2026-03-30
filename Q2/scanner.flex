
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
if             = if
else           = else
prefix         = prefix
suffix         = suffix

%%

<YYINITIAL> {
{if}              { System.out.println("if"); }

{else}              { System.out.println("else"); }

{prefix}              { System.out.println("prefix"); }

{suffix}             { System.out.println("suffix"); }

{Identifier}      { System.out.println(yytext()); }

{String}          { System.out.println(yytext()); }
}
