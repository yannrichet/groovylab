package org.math.array;

import org.math.array.Matrix as Matrix
import org.math.array.DoubleArray as DoubleArray

class MatrixTransformTest extends GroovyTestCase {
	
	def vectorlist =   [1.0 ,2.0,3.0,4.0,5.0]
	def collist =   [[0.1],[0.2],[0.3],[0.4]]
	def arraylist =  [[1.1,1.2,1.3,1.4,1.5],[2.1,2.2,2.3,2.4,2.5],[3.1,3.2,3.3,3.4,3.5],[4.1,4.2,4.3,4.4,4.5]]
	
	
	void testResize() {
		def Avd=new Matrix(vectorlist)
		def Aad=new Matrix(arraylist)
		
		Matrix.resize(Avd,7,2)
		//println Avd
		assert Avd[7][2] == 0.0
		
		Aad=new Matrix(arraylist)
		Avd=new Matrix(vectorlist)
		Avd >> Aad 
		//println Aad
		assert Aad[5][1] == 1.0
		
		Aad=new Matrix(arraylist)
		Avd=new Matrix(vectorlist)
		Aad << Avd
		//println Aad
		assert Aad[1][1] == 1.0
		
		Aad=new Matrix(arraylist)
		Avd=new Matrix(collist)
		Avd >>> Aad 
		println Aad
		assert Aad[1][6] == 0.1
		
	}
	
}


