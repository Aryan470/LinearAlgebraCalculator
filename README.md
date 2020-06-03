[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/Aryan470/LinearAlgebraCalculator) 

# LinearAlgebraCalculator

## Access
(https://linearalgebracalculator.herokuapp.com/)

## System
This repository stores a standalone API that can process expressions on the /query address, using a Javalin backend. Each session is served based on the client, so each user gets a personalized storage space for their custom variables and functions. This expires after one hour of inactivity. To make your own instance, build the project or simply grab the jar file and then run it, it will start a Javalin instance and allow POST requests at port 7000 and address /query, and the frontend can be accessed at the root.

## Usage
The calculator can handle many different functions, and does not care about whitespace. Click on any item in the history to replace the contents of the expression box.

### Assignment
Can store scalars, vectors, and matrices

+ `x = 5`
+ `y = [1, x, 9]`
+ `A = [[1, 1, 1], y, [1, 4, 9]]`
+ `A = [[1, 1, 1], [1, 2, 3], [1, 4, 9]]`
+ `b = x`
+ `v = [x, 10]`

### Retrieval
To see the value of a stored variable or use it in a new expression, simply type the name

+ `x`
+ `y = x + 2`

### Binary operators
+ Addition and subtraction are defined only for 2 of the same type
	- `5 + 5`
	- `x - 2`

+ Multiplication is defined for any two types, where vector-vector is a dot product, matrix-matrix is matrix multiplication, and matrix-vector is a matrix-vector product (dot product of all rows of matrix by vector)
	- `[[1, 1], [2, 3]] * 3`
	- `[[1, 2, 3], [2, 4, 6]] * [10, -2]`
	- `[10, -2] * [-1, 0]`
	- `[[1, 1], [2, 3]] * [[10, 1], [-2, 3]]`

### Functions
Functions are used on vectors and matrices:
+ `proj(u, v)`: project vector u onto vector v
+ `mag(v)`: magnitude of vector v
+ `inv(A)`: inverse of matrix A
+ `det(A)`: determinant of matrix A
+ `trans(A)`: transpose of matrix A

#### Custom Functions
User-defined functions are supported (multivariate, scalars, vectors, matrices):
+ `f(x) = 10 * x^2`
+ `f(2)`
+ `g(x, y) = proj(x, y) * 2`
* Please do not do nested definitions

### Order of operations
Parenthesis can be used besides functions to prioritize expressions
+ `2 * (3 + 5)` is treated differently than `2 * 3 + 5`

### Keywords
+ `vars`: see all stored variables and their values
+ `funcs`: see all user defined functions
+ `ans`: see most recent output
+ `clear`: delete all stored variables and custom functions, including `ans`

## Dependencies
* Java
  + [Javalin](https://javalin.io/)
