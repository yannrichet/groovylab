package org.math.array;

import org.math.array.LinearAlgebra as LinearAlgebra
import org.math.array.DoubleArray as DoubleArray
import org.math.array.StatisticSample as StatisticSample

import org.math.io.files.ASCIIFile as ASCIIFile

/** Matrix class to provide similar behaviour that high level math languages like Matlab, Scilab, R, ...
	Wrapper to JMathArray static classes: DoubleArray, LinearAlgebra (based on JAMA), StatisticSample (using RngPack)
	
	@author Yann Richet
	@License BSD
	
	Following access (i.e. set/get) are possible:
		x = M[1][2]
		x = M[-1][2]		// where -1 stands for last row index
		x = M[1][-2]		// where -2 stands for last column index
		x = M[-1][2]		// where -1 stands for last row index
		x = M[-1][-2]		// where -1 stands for last row index, -2 stands for last column index
		X = M[1..4][2..3]
		X = M[4..1][2..3]	// thus reverting rows order
		X = M[1..-1][-2..3]
		X = diagonal(Matrix)			// get diagonal of Matrix, alias to diag(Matrix)
		X = diagonal(Matrix, int order)		// get order diagonal of Matrix, alias to diag(Matrix, int)

		M[1][2] = x			// set x value at first row, second column
		M[-1][2] = x		// set x value at last row, second column
		M[1..2][2..5] = [[1.1,1.2,1.3,1.4],[2.1,2.2,2.3,2.4]]
		M[2..1][2..5] = [[1.1,1.2,1.3,1.4],[2.1,2.2,2.3,2.4]]	// thus reverting rows order
	
	Following operators are available:
		Matrix + Matrix
		Matrix + Number
		Matrix - Matrix
		Matrix - Number
		Matrix * Matrix
		Matrix * Number
		Matrix / Matrix
		Matrix / Number
		Matrix ** int"
		
	Following static operations are available:
		sum(Matrix)
		prod(Matrix)
		cumsum(Matrix)
		cumprod(Matrix)
		inverse(Matrix)
		solve(Matrix A, Matrix b)	//returns X Matrix verifying A*X = b. if 
		rank(Matrix)
		trace(Matrix)
		det(Matrix)
		cond(Matrix)
		norm1(Matrix)
		norm2(Matrix)
		normF(Matrix)
		normInf(Matrix)
		
	Following static Linear Algebra (from JAMA) are available:
		Cholesky decomposition:
			Cholesky_L(Matrix)
			Cholesky_SPD(Matrix)
		QR decomposition:
			QR_Q(Matrix)
			QR_H(Matrix)
			QR_R(Matrix)
		LU decomposition:
			LU_L(Matrix)
			LU_U(Matrix)
			LU_P(Matrix)
		Singular values decomposition:
			Singular_S(Matrix)
			Singular_U(Matrix)
			Singular_V(Matrix)
			Singular_values(Matrix)
		Eigenvalues decomposition:
			Eigen_D(Matrix)
			Eigen_V(Matrix)
	
	Following static constructors are available:
		matrix(double[][])
		matrix(double[])	// one row Matrix constructor
		matrix(ArrayList)	// compatible with ArrayList of Numbers or ArrayList of ArrayList of Numbers
	
		identity(int n)			// identity Matrix of size n*n alias to id(int n)
		diagonal(int, double)	// diagonal Matrix of constant values, alias to diag(int, double)
		diagonal(double[])		// diagonal Matrix with given diagonal values, alias to diag(double[])
		one(int, int)			// constant Matrix of given size, filled with 1.0 values 
		fill(int, int, double)	// constant Matrix of given size, filled with given values 
		increment(int, int, double begin, double pitch) // Matrix of given size with row incrementing values from given beginning value wsith given pitch increment
		increment(int, int, double[] begin, double[] pitch) // Matrix of given size with row incrementing values from given beginning values wsith given pitchs increment

	Following statistic sample constructors are available (random generator from RngPack):
		random(int, int)								// independant random values (between 0.0 and 1.0) Matrix of given size, alias to rand(int, int)
		random(int, int, double min, double max)		// independant random values (between min and max) Matrix of given size, alias to rand(int, int, double min, double max)
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
	
	Following static sort/find methods are available:
		sort(Matrix)
		sort(Matrix, int columnIndex)
		min(Matrix)
		max(Matrix)
	
	Following static transformation methods are available:
		transpose(Matrix)	// alias to t(Matrix)
		resize(Matrix, int, int)
		rowsMatrix >> Matrix	// appends rowsMatrix to Matrix at last position (i.e. add last row)
		columnsMatrix >>> Matrix	// appends columnsMatrix to Matrix at last position (i.e. add last column)
		Matrix << rowsMatrix	// appends rowsMatrix to Matrix at first position (i.e. add first row)
	
	Following static statistic sample methods are available:
		mean(Matrix)
		variance(Matrix)
		covariance(Matrix,Matrix)
		correlation(Matrix,Matrix)
*/

class Matrix {
	
	/**Internal storage of data. NO DIRECT ACCESS: use get* and set* methods instead*/
	def private double[][] array
		
	Matrix(double[][] _a,boolean isRef=false) {
		if (isRef)
			array=_a
		else 
			array=DoubleArray.copy(_a)
	}
		
	static Matrix matrix(double[][] _a) {
		return new Matrix(_a)
	}
	
	Matrix(double[] _a,boolean isRef=false) {
		if (isRef)
			array=[_a]
		else 
			array=[DoubleArray.copy(_a)]
	}
	
	static Matrix matrix(double[] _a) {
		return new Matrix(_a)
	}
	
	Matrix(double _a) {
		array=[[_a]]
	}
	
	static Matrix matrix(double _a) {
		return new Matrix(_a)
	}
	
	Matrix(ArrayList _al,boolean isRef=false) {
		if (isRef) throw new IllegalArgumentException("Impossible to build a reference Matrix from ArrayList")
		
		if (_al.get(0) instanceof ArrayList) {
			array =new double[_al.size][_al.get(0).size]
			for (i in 1.._al.size) 
				for (j in 1.._al.get(i-1).size) 
					set(i,j,_al.get(i-1).get(j-1))
		} else {
			array= new double[1][_al.size]
			for (i in 1.._al.size)
				set(1,i,_al.get(i-1))
		}	
	}
	
	static Matrix matrix(ArrayList _al) {
		return new Matrix(_al)
	}
	
	def static Matrix read(File f)  {
		return new Matrix(ASCIIFile.readDoubleArray(f) )
	}
	
	def static Matrix read(String filename)  {
		return read(new File(filename))
	}
	
	def static void write(File f,Matrix M)  {
		ASCIIFile.writeDoubleArray(M.array,f)
	}
	
	def static void write(String filename,Matrix M)  {
		write(new File(filename),M)
	}
	
	///////////////////////////////////////////////
	// Getter/Setter methods called by [] access //
	///////////////////////////////////////////////

	/**TODO: Auto resizing behaviour during access to data */
	//def static boolean AUTO_RESIZE=true
	
	def private double get(int i, int j) {
		return array[i-1][j-1]
	}
	
	def private double[][] getRef() {
		return array
	}
	
	def private double[][] getColumnCopy(int j) {
		return DoubleArray.getColumnsCopy(array,j-1)
	}
	
	def private double[] getColumnCopyasRow(int j) {
		return DoubleArray.getColumnCopy(array,j-1)
	}
	
	def private double[] getRowRef(int i) {
		return array[i-1]
	}
	
	def int getRowsNumber() {
		return array.length
	}
	
	def int getColumnsNumber() {
		return array[0].length
	}
	
	def private void set(int i, int j, Number v) {
		array[i-1][j-1] = v
	}
	
	def private void setRef(double[][] a) {
		array=a
	}
	
	def private void setRowRef(int i,double[] row) {
		array[i-1]=row
	}
	
	def resize( int m, int n) {
		array = DoubleArray.resize(array,m,n)
	}
	
	def String toString() {
		return DoubleArray.toString(array)
	}
	
	def plus(String s) {
		return DoubleArray.toString(array)+s
	}
	
	// NO ACCESS TO "array" OBJECT BEHIND //
	
	def private IntRange convert(IntRange I,int max) {
		int f=I.isReverse()?I.getToInt():I.getFromInt()
		int t=I.isReverse()?I.getFromInt():I.getToInt()

		if (f<0) f=f+max+1	
		if (t<0) t=t+max+1
		
		return new IntRange(f,t)
	}
	
	def private IntRange convertRowsRange(IntRange I) {	
		return convert(I,getRowsNumber())
	}
	
	def private IntRange convertColumnsRange(IntRange I) {	
		return convert(I,getColumnsNumber())
	}
	
	def private Integer convert(Integer i,int max) {	
		if (i<0) 
			return new Integer(i.value+max+1)
		//else if (i==0)
		//	return 1..max
		else
			return i
	}
	
	def private Integer convertRowIndex(Integer i) {	
		return convert(i,getRowsNumber())
	}
	
	def private Integer convertColumnIndex(Integer i) {	
		return convert(i,getColumnsNumber())
	}
	
	/** Tag to control if we are searching for column index in A[][] calls */
	def private boolean subMatrixTag=false 
	
	def getAt(Integer i) {
		if (subMatrixTag) {
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				return get(1,i)
			} else {
				i=convertColumnIndex(i)
				return new Matrix(getColumnCopy(i))
			}
		} else {
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				return get(1,i)
			} else {
				i=convertRowIndex(i)
				return new Matrix(getRowRef(i),true)
			}
		}
	}
	
	def Matrix getAt(IntRange I) {
		if (subMatrixTag){
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
				def subM = new double[I.size()]
				def j=0
				for (itI in I.iterator()) {
					subM[j] = get(1,itI)
					j++
				}
				def MsubM = new Matrix(subM)
				return MsubM
			} else {
				I=convertColumnsRange(I)
				def subM = new double[getRowsNumber()][I.size()]
				for (i in 1..getRowsNumber()) {
					def j=0
					for (itI in I.iterator()) {
						subM[i-1][j] = get(i,itI)
						j++
					}	
				}
				return new Matrix(subM)
			}
		} else {
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
				def subM = new double[I.size()]
				def j=0
				for (itI in I.iterator()) {
					subM[j] = get(1,itI)
					j++
				}
				def MsubM = new Matrix(subM,true)
				MsubM.subMatrixTag = true
				return MsubM
			} else {
				I=convertRowsRange(I)
				def subM = new double[I.size()][]
				def i=0
				for (itI in I.iterator()) {
					subM[i] = getRowRef(itI)
					i++
				}
				def MsubM = new Matrix(subM,true)
				MsubM.subMatrixTag = true
				return MsubM	
			}	
		}	
	}
	
	def void putAt(Integer i,Matrix v) {
		if (subMatrixTag){
			if (v.getColumnsNumber() != 1)
				throw new IllegalArgumentException("Input Matrix must have 1 column")
			
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				if (v.getRowsNumber()==1)
					set(1,i,v.get(1,1))
				else
					throw new IllegalArgumentException("Input Vector length needs to be 1 instead of "+v.getColumnsNumber())
			} else {
				i=convertColumnIndex(i)
				if (v.getRowsNumber()==getRowsNumber())
					for (j in 1..v.getRowsNumber())  
						set(j,i,v.get(j,1))
				else
					throw new IllegalArgumentException("Input Vector length needs to be "+getRowsNumber()+" instead of "+v.getRowsNumber())
			}
		} else {
			if (v.getRowsNumber() != 1)
				throw new IllegalArgumentException("Input Matrix must have 1 row")
			
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				if (v.getColumnsNumber()==1)
					set(1,i,v.get(1,1))
				else
					throw new IllegalArgumentException("Input Vector length needs to be 1 instead of "+v.getColumnsNumber())
			} else {
				i=convertRowIndex(i)
				if (v.getColumnsNumber()==getColumnsNumber())
					for (j in 1..v.getColumnsNumber())
						set(i,j,v.get(1,j))
				else
					throw new IllegalArgumentException("Input Vector length needs to be "+getRowsNumber()+" instead of "+v.getRowsNumber())
			}
		}
	}
	
	def void putAt(Integer i,double[] v) {
		putAt(i,new Matrix(v))
	}
	
	def void putAt(Integer i,ArrayList v) {
		putAt(i,new Matrix(v))
	}
	
	def void putAt(Integer i,Number v) {
		if (subMatrixTag){
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				set(1,i,v.doubleValue())
			} else {
				i=convertColumnIndex(i)
				for (j in 1..getRowsNumber())  
					set(j,i,v.doubleValue())
			}
		} else {
			if (getRowsNumber()==1) {
				i=convertColumnIndex(i)
				set(1,i,v.doubleValue())
			} else {
				i=convertRowIndex(i)
				for (j in 1..getColumnsNumber())
					set(i,j,v.doubleValue())
			}
		}
	}
	
	def void putAt(IntRange I,Matrix v) {
		if (subMatrixTag){
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
					
				if (getRowsNumber() != v.getRowsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+getRowsNumber()+" rows, instead of "+v.getRowsNumber())
				if (I.size() != v.getColumnsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+I.size+" columns, instead of "+v.getColumnsNumber())
		
				def j=1
				for (itI in I.iterator()) {
					set(1,itI,v.get(1,j))
					j++
				}
			} else {
				I=convertColumnsRange(I)
				
				if (getRowsNumber() != v.getRowsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+getRowsNumber()+" rows, instead of "+v.getRowsNumber())
				if (I.size() != v.getColumnsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+I.size+" columns, instead of "+v.getColumnsNumber())
						
				for (i in 1..getRowsNumber()) {
					def j=1
					for (itI in I.iterator()) {
						set(i,itI,v.get(i,j))
						j++
					}	
				}
			}
		} else {
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
				
				if (getRowsNumber() != v.getRowsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+getRowsNumber()+" rows, instead of "+v.getRowsNumber())
				if (I.size() != v.getColumnsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+I.size+" columns, instead of "+v.getColumnsNumber())
				
				def j=1
				for (itI in I.iterator()) {
					set(1,itI,v.get(1,j))
					j++
				}
			} else {
				I=convertRowsRange(I)

				if (getColumnsNumber() != v.getColumnsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+getColumnsNumber()+" columns, instead of "+v.getColumnsNumber())
				if (I.size() != v.getRowsNumber()) 
					throw new IllegalArgumentException("Input Matrix must have "+I.size+" rows, instead of "+v.getRowsNumber())
	
				def i=1
				for (itI in I.iterator()) {
					setRowRef(itI,v.getRowRef(i))
					i++
				}	
			}	
		}			
	}
	
	def void putAt(IntRange I,double[] v) {
		putAt(I,new Matrix(v))
	}
	
	def void putAt(IntRange I,double[][] v) {
		putAt(I,new Matrix(v))
	}
	
	def void putAt(IntRange I,ArrayList v) {
		putAt(I,new Matrix(v))
	}
	
	def void putAt(IntRange I,Number v) {
		if (subMatrixTag){
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
							
				for (itI in I.iterator()) {
					set(1,itI,v.doubleValue())
				}
			} else {
				I=convertColumnsRange(I)
										
				for (i in 1..getRowsNumber()) {
					for (itI in I.iterator()) {
						set(i,itI,v.doubleValue())
					}	
				}
			}
		} else {
			if (getRowsNumber()==1) {
				I=convertColumnsRange(I)
								
				for (itI in I.iterator()) {
					set(1,itI,v.doubleValue())
				}
			} else {
				I=convertRowsRange(I)

				for (j in 1..getColumnsNumber()) {
					for (itI in I.iterator()) {
						set(itI,i,v.doubleValue())
					}
				}
			}	
		}			
	}

	def void rightShift(Matrix M) {		
		if (getColumnsNumber() != M.getColumnsNumber())
			throw new IllegalArgumentException("Rows append needs same number of columns. Upper (=right) Matrix has "+M.getColumnsNumber()+" columns, while left Matrix has "+getColumnsNumber()+" columns")
				
		M.setRef(DoubleArray.insertRows(M.getRef(),M.getRowsNumber(),getRef()))
	}
	
	def void leftShift(Matrix M) {
		if (getColumnsNumber() != M.getColumnsNumber())
			throw new IllegalArgumentException("Rows append needs same number of columns. Upper (=right) Matrix has "+M.getColumnsNumber()+" columns, while left Matrix has "+getColumnsNumber()+" columns")
			
		setRef(DoubleArray.insertRows(M.getRef(),M.getRowsNumber(),getRef()))
		
		
		setRef(DoubleArray.insertColumns(getRef(),getColumnsNumber(),M.getRef()))
	}
	
	def void rightShiftUnsigned(Matrix M) {
		if (getRowsNumber() != M.getRowsNumber())
			throw new IllegalArgumentException("Columns append needs same number of rows. Right Matrix has "+M.getRowsNumber()+" rows, while left Matrix has "+getRowsNumber()+" rows")

		M.setRef(DoubleArray.insertColumns(M.getRef(),M.getColumnsNumber(),t(this).getRef()))
	}
	
	/*def void leftShiftUnsigned(Matrix rowMatrix) {
	}*/
	
	def static void eachValue(Matrix M, Closure c) {
		M.eachValue(c)
	}
	
	def void eachValue(Closure c) {
		for (i in 1..getRowsNumber())
			for (j in 1..getRowsNumber())
				set(i,j,c.call(get(i,j)))
	}
	
	/////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////

	def boolean equals(Object M) {
		if (!(M instanceof Matrix)) 
			return false
		else {
			if (M.getRowsNumber() != getRowsNumber()) return false
			if (M.getColumnsNumber() != getColumnsNumber()) return false
			for (i in 1..getRowsNumber())
				for (j in 1..getColumnsNumber())
					if (M.get(i,j) != get(i,j)) return false
			return true
		}
	}
	
	def size() {
		if (getRowsNumber()==1)
			return getColumnsNumber()
		else
			return [getRowsNumber(), getColumnsNumber()]
	}
	
	def static size(Matrix M) {
		return M.size()
	}
	
	def static void resize(Matrix M, int m, int n) {
		M = M.resize(m,n)
	}
	
	///////////////////////////
	// Common matrix methods // 
	///////////////////////////
	
	def static Matrix sum(Matrix M)  {
		if (M.getRowsNumber()==1)
			return new Matrix(DoubleArray.sum(M.getRowRef(1)))
		else
			return new Matrix(DoubleArray.sum( M.getRef()) )
	}
	
	def static Matrix prod(Matrix M)  {
		if (M.getRowsNumber()==1)
			return new Matrix(DoubleArray.product( M.getRowRef(1)) )
		else
			return new Matrix(DoubleArray.product( M.getRef()) )
	}
	
	def static Matrix cumsum(Matrix M)  {
		if (M.getRowsNumber()==1)
			return new Matrix(DoubleArray.cumSum( M.getRowRef(1)) )
		else
			return new Matrix(DoubleArray.cumSum( M.getRef()) )
	}
	
	def static Matrix cumprod(Matrix M)  {
		if (M.getRowsNumber()==1)
			return new Matrix(DoubleArray.cumProduct( M.getRowRef(1)) )
		else
			return new Matrix(DoubleArray.cumProduct( M.getRef()) )
	}
	
	
	def static Matrix sort(Matrix M)  {
		if (M.getRowsNumber()==1)
			return new Matrix(DoubleArray.sort( M.getRowRef(1)) )
		else
			return new Matrix(DoubleArray.sort( M.getRef(), 0) )
	}
	
	def static Matrix sort(Matrix M,int column)  {
		return new Matrix(DoubleArray.sort( M.getRef(), column) )
	}
	
	
	def static Matrix transpose(Matrix M)  {
		return new Matrix(DoubleArray.transpose( M.getRef()) )
	}
	
	def static Matrix t(Matrix M)  {
		return transpose(M)
	}
	
	def static min(Matrix M)  {
		if (M.getRowsNumber()==1)
			return DoubleArray.min( M.getRowRef(1))
		else if (M.getColumnsNumber()==1)
			return DoubleArray.min( M.getColumnCopy(1))
		else
			return new Matrix(DoubleArray.min( M.getRef()) )
	}
	
	def static max(Matrix M)  {
		if (M.getRowsNumber()==1)
			return DoubleArray.max( M.getRowRef(1))
		else if (M.getColumnsNumber()==1)
			return DoubleArray.max( M.getColumnCopy(1))
		else
			return new Matrix(DoubleArray.max( M.getRef()) )
	}
	
	def static Matrix identity(int m)  {
		return new Matrix(DoubleArray.identity(m) )
	}
	
	def static Matrix id(int m)  {
		return identity(m)
	}
	
	def static Matrix diagonal(int m, double c)  {
		return new Matrix(DoubleArray.diagonal(m, c) )
	}
	
	def static Matrix diag(int m, double c)  {
		return diagonal( m,  c)
	}
	
	def static Matrix diagonal(double[] c)  {
		return new Matrix(DoubleArray.diagonal( c) )
	}
	
	def static Matrix diag(double[] c)  {
		return diagonal( c)
	}
	
	def static Matrix diagonal(Matrix M,int m)  {
		return new Matrix(DoubleArray.getDiagonal( M,m) )
	}
	
	def static Matrix diag(Matrix M,int m)  {
		return diagonal( M, m)
	}
	
	def static Matrix one(int m, int n)  {
		return new Matrix(DoubleArray.one(m, n) )
	}
	
	def static Matrix one(int m, int n, double c)  {
		return new Matrix(DoubleArray.one(m, n, c) )
	}
	
	def static Matrix fill(int m, int n, double c)  {
		return new Matrix(DoubleArray.fill(m, n, c) )
	}
	
	def static Matrix random(int m)  {
		return random(1,m)
	}
	
	def static Matrix rand(int m)  {
		return  random(m)
	}
	
	def static Matrix random(int m,int  n)  {
		return new Matrix(DoubleArray.random(m, n) )
	}
	
	def static Matrix rand(int m,int  n)  {
		return random( m,  n)
	}
	
	def static Matrix random(int m, int n, double min, double max)  {
		return new Matrix(DoubleArray.random(m, n, min, max) )
	}
	
	def static Matrix rand(int m, int n, double min, double max)  {
		return random( m,  n,  min,  max)
	}
	
	def static Matrix random(int m, int n, double[] min, double[] max)  {
		return new Matrix(DoubleArray.random(m, n, min, max) )
	}
	
	def static Matrix rand(int m, int n, double[] min, double[] max)  {
		return random( m,  n,  min,  max)
	}
	
	def static Matrix increment(int m, int n, double begin, double pitch)  {
		return new Matrix(DoubleArray.increment(m, n, begin, pitch) )
	}
	
	def static Matrix inc(int m, int n, double begin, double pitch)  {
		return increment( m,  n,  begin,  pitch)
	}
	
	def static Matrix increment(int m, int n, double[] begin, double[] pitch)  {
		return new Matrix(DoubleArray.increment(m, n, begin, pitch) )
	}
	
	def static Matrix inc(int m, int n, double[] begin, double[] pitch)  {
		return increment( m,  n,  begin,  pitch) 
	}
	
	def static Matrix copy(Matrix M)  {
		return new Matrix(DoubleArray.copy( M.getRef()) )
	}
	
	///////////////////////////////////////////////////////////////////
	// Linear algebra methods, coming from JMathArray, based on JAMA // 
	///////////////////////////////////////////////////////////////////
	
	def Matrix minus( Matrix v2)  {
		return new Matrix(LinearAlgebra.minus( getRef(),  v2.getRef()) )
	}
	
	def Matrix minus(Number v2)  {
		return new Matrix(LinearAlgebra.minus( getRef(), v2.doubleValue()) )
	}
	
	def Matrix plus( Matrix v2)  {
		return new Matrix(LinearAlgebra.plus( getRef(),  v2.getRef()) )
	}
	
	def Matrix plus( Number v2)  {
		return new Matrix(LinearAlgebra.plus( getRef(), v2) )
	}
	
	def Matrix multiply( Number v)  {
		return new Matrix(LinearAlgebra.times( getRef(), v.doubleValue()) )
	}
	
	def Matrix div( Number v)  {
		return new Matrix(LinearAlgebra.divide( getRef(), v.doubleValue()) )
	}
	
	def private Matrix power(int n, Matrix m)  {
		if (n < 0)
			return power(-n,inv(m))
		else if (n == 1) 
			return copy(m) 
		else
			return m * power(n-1,m)
	}
	
	def Matrix power( Number n)  {
		if (n.intValue()==n)
			return power(n.intValue(),this)
		else 
			throw new IllegalArgumentException("Value "+n+" no yet supported for power method");
	}
	
	def Matrix raise( Number n)  {
		return new Matrix(LinearAlgebra.raise( getRef(), n.doubleValue()) )
	}
	
	def Matrix multiply( Matrix v2)  {
		return new Matrix(LinearAlgebra.times( getRef(),  v2.getRef()) )
	}
	
	def Matrix mult( Matrix v2)  {
		return multiply(v2)
	}
	
	def Matrix divide_LU( Matrix v2)  {
		return new Matrix(LinearAlgebra.divideLU( getRef(), v2.getRef()) )
	}
	
	def Matrix divide_QR( Matrix v2)  {
		return new Matrix(LinearAlgebra.divideQR( getRef(), v2.getRef()) )
	}
	
	def Matrix divide( Matrix v2)  {
		return new Matrix(LinearAlgebra.divide(getRef(),  v2.getRef()) )
	}
	
	def Matrix div( Matrix v2)  {
		return divide(v2)
	}
	
	def static Matrix inverse_LU(Matrix M)  {
		return new Matrix(LinearAlgebra.inverseLU( M.getRef()) )
	}
	
	def static Matrix inverse_QR(Matrix M)  {
		return new Matrix(LinearAlgebra.inverseQR( M.getRef()) )
	}
	
	def static Matrix inverse(Matrix M)  {
		return new Matrix(LinearAlgebra.inverse( M.getRef()) )
	}
	
	def static Matrix inv(Matrix M)  {
		return inverse(M)
	}
	
	def static Matrix Cholesky_L(Matrix M) {
		return new Matrix(LinearAlgebra.cholesky( M.getRef()).getL().getArray() )
	}
	
	def static Matrix Cholesky_SPD(Matrix M) {
		return new Matrix(LinearAlgebra.cholesky( M.getRef()).isSPD() )
	}
	
	def static Matrix Cholesky_solve(Matrix A,Matrix b) {
		return new Matrix(LinearAlgebra.cholesky( A.getRef()).solve(b.getRef()))
	}
	
	def static Matrix LU_L(Matrix M) {
		return new Matrix(LinearAlgebra.LU( M.getRef()).getL().getArray() )
	}
	
	def static Matrix L(Matrix M) {
		return LU_L( M)
	}
	
	def static Matrix LU_P(Matrix M) {
		return new Matrix(LinearAlgebra.LU( M.getRef()).getP().getArray() )
	}
	
	def static Matrix LU_U(Matrix M) {
		return new Matrix(LinearAlgebra.LU( M.getRef()).getU().getArray() )
	}
	
	def static Matrix U(Matrix M) {
		return LU_U( M)
	}
	
	def static double LU_det(Matrix M) {
		return LinearAlgebra.LU( M.getRef()).det()
	}
	
	def static double det(Matrix M) {
		return LinearAlgebra.det(M.getRef())
	}
	
	def static Matrix LU_solve(Matrix A,Matrix b) {
		return new Matrix(LinearAlgebra.LU( A.getRef()).solve(b.getRef()))
	}
	
	def static Matrix QR_H(Matrix M) {
		return new Matrix(LinearAlgebra.QR( M.getRef()).getH().getArray() )
	}
	
	def static Matrix QR_Q(Matrix M) {
		return new Matrix(LinearAlgebra.QR( M.getRef()).getQ().getArray() )
	}
	
	def static Matrix Q(Matrix M) {
		return QR_Q( M) 
	}
	
	def static Matrix QR_R(Matrix M) {
		return new Matrix(LinearAlgebra.QR( M.getRef()).getR().getArray())
	}
	
	def static Matrix R(Matrix M) {
		return QR_R( M) 
	}
	
	def static Matrix QR_solve(Matrix A,Matrix b) {
		return new Matrix(LinearAlgebra.QR( A.getRef()).solve(b.getRef()))
	}
	
	def static Matrix solve(Matrix A,Matrix b) {
		return new Matrix(LinearAlgebra.solve( A.getRef(),b.getRef()))
	}
	
	def static double Singular_cond(Matrix M) {
		return LinearAlgebra.singular( M.getRef()).cond()
	}
	
	def static double cond(Matrix M) {
		return LinearAlgebra.cond( M.getRef())
	}
	
	def static Matrix Singular_S(Matrix M) {
		return new Matrix(LinearAlgebra.singular(M.getRef()).getS().getArray())
	}
	
	def static Matrix S(Matrix M) {
		return  Singular_S( M) 
	}
	
	def static Matrix Singular_values(Matrix M) {
		return new Matrix(LinearAlgebra.singular(M.getRef()).getSingularValues())
	}
	
	def static Matrix Singular_U(Matrix M) {
		return new Matrix(LinearAlgebra.singular(M.getRef()).getU().getArray())
	}
	
	def static Matrix Singular_V(Matrix M) {
		return new Matrix(LinearAlgebra.singular(M.getRef()).getV().getArray())
	}
	
	def static double Singular_norm2(Matrix M) {
		return LinearAlgebra.singular( M.getRef()).norm2()
	}
	
	def static double norm2(Matrix M) {
		return LinearAlgebra.norm2( M.getRef())
	}
	
	def static double norm1(Matrix M) {
		return LinearAlgebra.norm1( M.getRef())
	}
	
	def static double normF(Matrix M) {
		return LinearAlgebra.normF( M.getRef())
	}
	
	def static double normInf(Matrix M) {
		return LinearAlgebra.normInf( M.getRef())
	}
	
	def static int Singular_rank(Matrix M) {
		return LinearAlgebra.singular( M.getRef()).rank()
	}
	
	def static int rank(Matrix M) {
		return LinearAlgebra.rank( M.getRef())
	}
	
	def static int trace(Matrix M) {
		return LinearAlgebra.trace( M.getRef())
	}
	
	def static Matrix Eigen_V(Matrix M) {
		return new Matrix(LinearAlgebra.eigen(M.getRef()).getV().getArray())
	}
	
	def static Matrix V(Matrix M) {
		return Eigen_V( M) 
	}
	
	def static Matrix Eigen_D(Matrix M) {
		return new Matrix(LinearAlgebra.eigen(M.getRef()).getD().getArray())
	}
	
	def static Matrix D(Matrix M) {
		return Eigen_D( M) 
	}
	
	//////////////////////////////////////////////////////
	// Statistic sample methods, coming from JMathArray // 
	//////////////////////////////////////////////////////
	
	def static mean(Matrix v)  {
		if (v.getRowsNumber()==1)
			return StatisticSample.mean(v.getRowRef(1))[0]
		else if (v.getColumnsNumber()==1)
			return StatisticSample.mean(v.getColumnCopy(1))[0]
		else
			return new Matrix(StatisticSample.mean( v.getRef()) )
	}
	
	def static variance(Matrix v)  {
		if (v.getRowsNumber()==1)
			return StatisticSample.variance(v.getRowRef(1))[0]
		else if (v.getColumnsNumber()==1)
			return StatisticSample.variance(v.getColumnCopy(1))[0]
		else
			return new Matrix(StatisticSample.variance( v.getRef()) )
	}
	
	def static var(Matrix v)  {
		return variance( v) 
	}
	
	def static covariance(Matrix v1, Matrix v2)  {
		if (v1.getRowsNumber()==1 && v2.getRowsNumber()==1)
			return StatisticSample.covariance(v1.getRowRef(1),v2.getRowRef(1))[0]
		else if (v1.getColumnsNumber()==1 && v2.getColumnsNumber()==1)
			return StatisticSample.covariance(v1.getColumnCopy(1),v2.getColumnCopy(1))[0]
		else
			return new Matrix(StatisticSample.covariance( v1.getRef(),  v2.getRef()) )
	}
	
	def static cov(Matrix v1, Matrix v2)  {
		return covariance( v1,  v2)
	}
	
	def static covariance(Matrix v)  {
		return covariance( v,  v)
	}
	
	def static cov(Matrix v)  {
		return covariance( v) 
	}
	
	def static correlation(Matrix v1, Matrix v2)  {
		if (v1.getRowsNumber()==1 && v2.getRowsNumber()==1)
			return StatisticSample.correlation(v1.getRowRef(1),v2.getRowRef(1))[0]
		else if (v1.getColumnsNumber()==1 && v2.getColumnsNumber()==1)
			return StatisticSample.correlation(v1.getColumnCopy(1),v2.getColumnCopy(1))[0]
		else
			return new Matrix(StatisticSample.correlation( v1.getRef(),  v2.getRef()) )
	}
	
	def static corr(Matrix v1, Matrix v2)  {
		return correlation( v1,  v2)
	}
	
	def static correlation(Matrix v)  {
		return correlation( v,v)
	}
	
	def static corr(Matrix v)  {
		return correlation( v) 
	}
	
	def static Matrix randomUniform(int m, int n, double min, double max)  {
		return new Matrix(StatisticSample.randomUniform(m, n, min, max) )
	}
	
	def static Matrix randomDirac(int m, int n, double[] values, double[] prob)  {
		return new Matrix(StatisticSample.randomDirac(m, n, values, prob) )
	}
	
	def static Matrix randomNormal(int m, int n, double mu, double sigma)  {
		return new Matrix(StatisticSample.randomNormal(m, n, mu, sigma) )
	}
	
	def static Matrix randomChi2(int m, int n, int d)  {
		return new Matrix(StatisticSample.randomChi2(m, n, d) )
	}
	
	def static Matrix randomLogNormal(int m, int n, double mu, double sigma)  {
		return new Matrix(StatisticSample.randomLogNormal(m, n, mu, sigma) )
	}
	
	def static Matrix randomExponential(int m, int n, double lambda)  {
		return new Matrix(StatisticSample.randomExponential(m, n, lambda) )
	}
	
	def static Matrix randomTriangular(int m, int n, double min, double max)  {
		return new Matrix(StatisticSample.randomTriangular(m, n, min, max) )
	}
	
	def static Matrix randomTriangular(int m, int n, double min, double med, double max)  {
		return new Matrix(StatisticSample.randomTriangular(m, n, min, med, max) )
	}
	
	def static Matrix randomBeta(int m, int n, double a, double b)  {
		return new Matrix(StatisticSample.randomBeta(m, n, a, b) )
	}
	
	def static Matrix randomCauchy(int m, int n, double mu, double sigma)  {
		return new Matrix(StatisticSample.randomCauchy(m, n, mu, sigma) )
	}
	
	def static Matrix randomWeibull(int m, int n, double lambda, double c)  {
		return new Matrix(StatisticSample.randomWeibull(m, n, lambda, c) )
	}

}
