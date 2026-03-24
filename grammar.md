# Grammar after changes
```
exp -> temp / exp |
       temp

temp -> str temp2

temp2 -> ** str temp2 |
        e

str -> char str2

str2 -> char str2 |
       e

char -> A-Z |
        a-z |
        (exp)
```

# First compuation
```
FIRST(char) = A-Z , a-z , (

FIRST(str2) = A-Z , a-z , (

FIRST(str) = A-Z , a-z , (

FIRST(temp2) = **

FIRST(temp) = A-Z , a-z , (

FIRST(exp) = A-Z , a-z , (

```



