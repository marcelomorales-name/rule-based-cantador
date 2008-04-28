grammar cantador;
@header {
package name.marcelomorales.cantador.antlr;
}
@lexer::header {
package name.marcelomorales.cantador.antlr;
}
reglas 	:	regla+;
regla	:	NUMERO ':' ('[' poslit ']')? LITERAL ('[' poslit ']')?';';
poslit	:	(LITERAL|('<''x')|('>''x'))+;
NUMERO	:	'0'..'9'+;
LITERAL	:	'\''(' '|'('..'\uFFFE')*'\'';
WS	: 	('\t'|'\n'|'\r'|' ')+ {$channel=HIDDEN;} ;
