# GroovyLab: Groovy classes for math  #

GroovyLab is a collection of Groovy classes to provide matlab-like syntax and basic features (linear algebra, 2D/3D plots). It is based on jmathplot and jmatharray libs:

    Matrix class
    Plot class 

## Example Groovy code ##

Basically, it allows to create scripts with matlab-like syntax for Matrices and plots:
```groovy
import static org.math.array.Matrix.*
import static org.math.plot.Plot.*
 
def A = rand(10,3)              // random Matrix of 10 rows and 3 columns
def B = fill(10,3,1.0)  // one Matrix of 10 rows and 3 columns
def C = A + B                   // support for matrix addition with "+" or "-"
def D = A - 2.0                 // support for number addition with "+" or "-"
def E = A * B                   // support for matrix multiplication or division
def F = rand(3,3)       
def G = F**(-1)                 // support for matrix power (with integers only)
 
println A                               // display Matrix content
 
plot("A",A,"SCATTER")   // plot Matrix values as ScatterPlot
 
def M = rand(5,5) + id(5) //Eigenvalues decomposition
println "M=\n" + M
println "V=\n" + V(M)
println "D=\n" + D(M)
println "M~\n" + (V(M) * D(M) * V(M)**(-1))
 
...
def A = rand(10,3)
println A
plot("A",A,"SCATTER")
 
def B = rand(10,3)
println B
plot("B",B,"LINE")
...
```
## Use it ##

To use it, just put Matrix.groovy or Plot.groovy groovy classes in your Groovy classpath, as well as jmathplot.jar and jmatharray.jar.
Matrix

Following access (i.e. set/get) are possible:
```groovy
                    x = M[1][2]
                    x = M[-1][2]            // where -1 stands for last row index
                    x = M[1][-2]            // where -2 stands for last column index
                    x = M[-1][2]            // where -1 stands for last row index
                    x = M[-1][-2]           // where -1 stands for last row index, -2 stands for last column index
                    X = M[1..4][2..3]
                    X = M[4..1][2..3]       // thus reverting rows order
                    X = M[1..-1][-2..3]
                    X = diagonal(Matrix)                    // get diagonal of Matrix, alias to diag(Matrix)
                    X = diagonal(Matrix, int order)         // get order diagonal of Matrix, alias to diag(Matrix, int)

                    M[1][2] = x                     // set x value at first row, second column
                    M[-1][2] = x            // set x value at last row, second column
                    M[1..2][2..5] = [[1.1,1.2,1.3,1.4],[2.1,2.2,2.3,2.4]]
                    M[2..1][2..5] = [[1.1,1.2,1.3,1.4],[2.1,2.2,2.3,2.4]]   // thus reverting rows order
```
Following operators are available:
```groovy
                    Matrix + Matrix
                    Matrix + Number
                    Matrix - Matrix
                    Matrix - Number
                    Matrix * Matrix
                    Matrix * Number
                    Matrix / Matrix
                    Matrix / Number
                    Matrix ** int"
```
Following static operations are available:
```groovy
                    sum(Matrix)
                    prod(Matrix)
                    cumsum(Matrix)
                    cumprod(Matrix)
                    inverse(Matrix)
                    solve(Matrix A, Matrix b)       //returns X Matrix verifying A*X = b. if 
                    rank(Matrix)
                    trace(Matrix)
                    det(Matrix)
                    cond(Matrix)
                    norm1(Matrix)
                    norm2(Matrix)
                    normF(Matrix)
                    normInf(Matrix)
```
Following static Linear Algebra (from JAMA) are available:
```groovy
                    //Cholesky decomposition:
                            Cholesky_L(Matrix)
                            Cholesky_SPD(Matrix)
                    //QR decomposition:
                            QR_Q(Matrix)
                            QR_H(Matrix)
                            QR_R(Matrix)
                    //LU decomposition:
                            LU_L(Matrix)
                            LU_U(Matrix)
                            LU_P(Matrix)
                    //Singular values decomposition:
                            Singular_S(Matrix)
                            Singular_U(Matrix)
                            Singular_V(Matrix)
                            Singular_values(Matrix)
                    //Eigenvalues decomposition:
                            Eigen_D(Matrix)
                            Eigen_V(Matrix)
```
Following static constructors are available:
```groovy
                    matrix(double[][])
                    matrix(double[])        // one row Matrix constructor
                    matrix(ArrayList)       // compatible with ArrayList of Numbers or ArrayList of ArrayList of Numbers
            
                    identity(int n)                 // identity Matrix of size n*n alias to id(int n)
                    diagonal(int, double)   // diagonal Matrix of constant values, alias to diag(int, double)
                    diagonal(double[])              // diagonal Matrix with given diagonal values, alias to diag(double[])
                    one(int, int)                   // constant Matrix of given size, filled with 1.0 values 
                    fill(int, int, double)  // constant Matrix of given size, filled with given values 
                    increment(int, int, double begin, double pitch) // Matrix of given size with row incrementing values from given beginning value wsith given pitch increment
                    increment(int, int, double[] begin, double[] pitch) // Matrix of given size with row incrementing values from given beginning values wsith given pitchs increment
```
Following statistic sample constructors are available (random generator from RngPack?):
```groovy
                    random(int, int)                                                                // independant random values (between 0.0 and 1.0) Matrix of given size, alias to rand(int, int)
                    random(int, int, double min, double max)                // independant random values (between min and max) Matrix of given size, alias to rand(int, int, double min, double max)
                    randomUniform(int m, int n, double min, double max)  
                    randomDirac(int m, int n, double[] values, double[] prob)  
                    randomNormal(int m, int n, double mu, double sigma)  
                    randomChi2(int m, int n, int d)  
                    randomLogNormal(int m, int n, double mu, double sigma)  
                    randomExponential(int m, int n, double lambda)  
                    randomTriangular(int m, int n, double min, double max)  
                    randomTriangular(int m, int n, double min, double med, double max)  
                    randomBeta(int m, int n, double a, double b)  
                    randomCauchy(int m, int n, double mu, double sigma)  
                    randomWeibull(int m, int n, double lambda, double c)  
```
Following static sort/find methods are available:
```groovy
                    sort(Matrix)
                    sort(Matrix, int columnIndex)
                    min(Matrix)
                    max(Matrix)
```
Following static transformation methods are available:
```groovy
                    transpose(Matrix)       // alias to t(Matrix)
                    resize(Matrix, int, int)
                    rowsMatrix >> Matrix    // appends rowsMatrix to Matrix at last position (i.e. add last row)
                    columnsMatrix >>> Matrix        // appends columnsMatrix to Matrix at last position (i.e. add last column)
                    Matrix << rowsMatrix    // appends rowsMatrix to Matrix at first position (i.e. add first row)
```
Following static statistic sample methods are available:
```groovy
                    mean(Matrix)
                    variance(Matrix)
                    covariance(Matrix,Matrix)
                    correlation(Matrix,Matrix)
```

![Analytics](https://ga-beacon.appspot.com/UA-109580-20/groovylab)
