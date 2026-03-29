# Gramma i created 

```
entry -> declaration call

declaration -> ID (params) {expr} declaration | e
params ->  params_list | e 
params_list -> ID , params_list | ID 


call -> ID (args) call  | e 
args ->  args_list | e
args_list -> expr , args_list | expr 

expr -> IF cond expr ELSE expr  | atom concat_expr
concat_expr -> + atom concat_expr | e 
atom -> ID | STR | call 

condition -> atom prefix atom | atom suffix atom 

```
