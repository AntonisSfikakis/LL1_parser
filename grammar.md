# Grammar after changes
```
exp -> temp / exp |
       temp

temp -> str temp2

temp2 -> ** str temp2 |
        e

str -> char str2

str -> char str2 |
       e

char -> A-Z |
        a-z |
        (exp)
```

