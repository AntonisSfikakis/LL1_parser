import java_cup.runtime.*;
%%

%class Scanner
%line
%column
%cup
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
If             = if
else           = else
prefix         = prefix
suffix         = suffix

%state STRING
%%

<YYINITIAL> {

  \"                { stringBuffer.setLength(0); yybegin(STRING);  } 
  "("               { return symbol(sym.LPAR);                     }
  ")"               { return symbol(sym.RPAR);                     }
  "+"               { return symbol(sym.CONCAT);                   }
  ","               { return symbol(sym.COMMA);                    }
  "{"               { return symbol(sym.LBRACK);                   }
  "}"               { return symbol(sym.RBRACK);                   }
  {If}              { return symbol(sym.IF);                       }
  {else}            { return symbol(sym.ELSE);                     }
  {prefix}          { return symbol(sym.PREFIX);                   }
  {suffix}          { return symbol(sym.SUFFIX);                   }
  {Identifier}      { return symbol(sym.ID, yytext());             }
  {WhiteSpace}      { /* do nothing */                             }

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
