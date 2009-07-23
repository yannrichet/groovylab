package org.math.array;

import org.math.array.Matrix as Matrix
import org.math.array.DoubleArray as DoubleArray

class MatrixGetTest extends GroovyTestCase {
	
	def vectorlist =   [1.0 ,2.0,3.0,4.0]
	def arraylist =  [[1.1,1.2,1.3,1.4,1.5],[2.1,2.2,2.3,2.4,2.5],[3.1,3.2,3.3,3.4,3.5],[4.1,4.2,4.3,4.4,4.5]]
	
	def vectordouble=DoubleArray.increment(4,1.0,1.0)
	double[] beg = [1.1,1.2,1.3,1.4,1.5]
	double[] pitch = [1.0,1.0,1.0,1.0,1.0]
	def arraydouble=DoubleArray.increment(4,5,beg,pitch)
	
	void testNoRangeGet() {
		def Avd=new Matrix(vectordouble)
		assert Avd[2] ==2.0
		
		def Aad=new Matrix(arraydouble)
		assert Aad[2][3] ==2.3
		//println "Aad[2]="+(Aad[2]+"\n")
		//println "new Matrix(arraydouble[1])="+(new Matrix(arraydouble[1])+"\n")
		assert Aad[2] ==new Matrix(arraydouble[1])		
	}
	
	
	void testRangeGet() {
		def Avd=new Matrix(vectordouble)
		//println "Avd="+(Avd+"\n")
		
		assert Avd[1..2] ==new Matrix([1.0,2.0])
		assert Avd[2..-1] ==new Matrix([2.0,3.0,4.0])
		assert Avd[-2..-1] ==new Matrix([3.0,4.0])
		assert Avd[-2..-3] ==new Matrix([3.0,2.0])
		
		
		def Aad=new Matrix(arraydouble)
		//println "Aad="+(Aad+"\n")
		//println "Aad[1..2]="+(Aad[1..2]+"\n")
		//println "Aad[1..2][3]="+(Aad[1..2][3]+"\n")
		assert Aad[1..2][3] ==new Matrix([[1.3],[2.3]])
		//println "Aad[1..2][2..3]="+(Aad[1..2][2..3]+"\n")
		assert Aad[1..2][2..3] ==new Matrix([[1.2,1.3],[2.2,2.3]])
		
		assert Aad[2..-1][3] ==new Matrix([[2.3],[3.3],[4.3]])
		assert Aad[2..-1][2..3]  ==new Matrix([[2.2,2.3],[3.2,3.3],[4.2,4.3]])
	}
}
