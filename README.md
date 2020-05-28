# LinearAlgebraCalculator

## Usage
The calculator can handle many different functions, and does not care about whitespace

### Assignment
Can store scalars, vectors, and matrices

+ `x = 5`
+ `y = [1, x, 9]`
+ `A = [[1, 1, 1], y, [1, 4, 9]]`
+ `A = [[1, 1, 1], [1, 2, 3], [1, 4, 9]]`
+ `b = x`
+ `v = [x, 10]`

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

### Order of operations
Parenthesis can be used besides functions to prioritize expressions
+ `2 * (3 + 5)` is treated differently than `2 * 3 + 5`

### Keywords
+ `vars`: see all stored variables and their values
+ `ans`: see most recent output
+ `clear`: delete all stored variables, including `ans`
