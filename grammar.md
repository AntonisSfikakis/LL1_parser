# Grammar after changes
```
[1] exp -> temp exp2 

[2] exp2 -> / exp |
[3]         e 


[4] temp -> str temp2

[5] temp2 -> ** str temp2 |
[6]        e

[7] str -> char str2

[8] str2 -> char str2 |
[9]       e

[10] char -> A-Z |
[11]        a-z |
[12]        (exp)

```
# Table 
`
+-------+---+-------+--------------+-----------+-----------+-----------+---+
|       | $ |   /   |      **      |    A-Z    |    a-z    |     (     | ) |
+-------+---+-------+--------------+-----------+-----------+-----------+---+
|  exp  | - |   -   |      -       | temp exp2 | temp exp2 | temp exp2 | - |
|  exp2 | e | / exp |      -       |     -     |     -     |     -     | e |
|  temp | - |   -   |      -       | str temp2 | str temp2 | str temp2 | - |
| temp2 | e |   e   | ** str temp2 |     -     |     -     |     -     | e |
|  temp | - |   -   |      -       | char str2 | char str2 | char str2 | - |
| temp2 | e |   e   |      e       | char str2 | char str2 | char str2 | e |
|  char | - |   -   |      -       |    A-Z    |    a-z    |     (     | - |
+-------+---+-------+--------------+-----------+-----------+-----------+---+``

```

# FIRST compuation
```
FIRST(char)  = A-Z , a-z , (

FIRST(str2)  = A-Z , a-z , (

FIRST(str)   = A-Z , a-z , (

FIRST(temp2) = **

FIRST(temp)  = A-Z , a-z , (

FIRST(exp2)  = /

FIRST(exp)   = A-Z , a-z , (

```

# FOLLOW computation
```
FOLLOW(exp)   = $, )

FOLLOW(exp2)  = $, )

FOLLOW(temp)  = /, $, )

FOLLOW(temp2) = /, $, )

FOLLOW(str)   = **, /, $, )

FOLLOW(str2)  = **, /, $, )

FOLLOW(char)  = A-Z, a-z, (, **, /, $, )

```
# FIRST+ computation
```
FIRST+(1) = A-Z, a-z, ( 

FIRST+(2) = /

FIRST+(3) = $, )


FIRST+(4) = A-Z, a-z, ( 

FIRST+(5) = **

FIRST+(6) = /, $, )

FIRST+(7) = A-Z, a-z , (

FIRST+(8) = A-Z, a-z , (

FIRST+(9) =  /, $, **, )

FIRST+(10) = A-Z 

FIRST+(11) = a-z

FIRST+(12) = (

```


