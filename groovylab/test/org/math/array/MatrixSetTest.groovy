package org.math.array;

import org.math.array.Matrix as Matrix
import org.math.array.DoubleArray as DoubleArray

class MatrixSetTest extends GroovyTestCase {
	
	def vectorlist =   [1.0 ,2.0,3.0,4.0]
	def arraylist =  [[1.1,1.2,1.3,1.4,1.5],[2.1,2.2,2.3,2.4,2.5],[3.1,3.2,3.3,3.4,3.5],[4.1,4.2,4.3,4.4,4.5]]
	
	def vectordouble=DoubleArray.increment(4,1.0,1.0)
	double[] beg = [1.1,1.2,1.3,1.4,1.5]
	double[] pitch = [1.0,1.0,1.0,1.0,1.0]
	def arraydouble=DoubleArray.increment(4,5,beg,pitch)
	
	void testConstructors() {
		def Avl=new Matrix(vectorlist)
		def Bvl=new Matrix(vectorlist)
		//println "Avl="+(Avl+"\n")
		//println "Bvl="+(Bvl+"\n")
		Avl[2]=0.0
		//println "Avl="+(Avl+"\n")
		//println "Bvl="+(Bvl+"\n")
		assert Avl[2] != Bvl[2]
		
		
		def Aal=new Matrix(arraylist)
		def Bal=new Matrix(arraylist)
		//println "Aal="+(Aal+"\n")
		//println "Bal="+(Bal+"\n")
		Aal[1][2]=0.0
		//println "Aal="+(Aal+"\n")
		//println "Bal="+(Bal+"\n")
		assert Aal[1][2] != Bal[1][2]
		//println "Aal[1]="+(Aal[1]+"\n")
		
		def Avd=new Matrix(vectordouble)
		def Bvd=new Matrix(vectordouble)
		//println "Avd="+(Avd+"\n")
		//println "Bvd="+(Bvd+"\n")
		Avd[2]=0.0
		//println "Avd="+(Avd+"\n")
		//println "Bvd="+(Bvd+"\n")
		assert Avd[2] != Bvd[2]
		
		
		def Aad=new Matrix(arraydouble)
		def Bad=new Matrix(arraydouble)
		//println "Aad="+(Aad+"\n")
		//println "Bad="+(Bad+"\n")
		Aad[1][2]=0.0
		//println "Aad="+(Aad+"\n")
		//println "Bad="+(Bad+"\n")
		assert Aad[1][2] != Bad[1][2]
		//println "Aad[1]="+(Aad[1]+"\n")

	}
	
	void testNoRangeSet() {
		def Avd=new Matrix(vectordouble)
		def Bvd=new Matrix(vectordouble)
		
		Avd[2] =-2.0
		assert (Avd[2] ==-2.0) && (Bvd[2] ==2.0)
		
		def Aad=new Matrix(arraydouble)
		def Bad=new Matrix(arraydouble)
		
		Aad[2][3] =-2.3
		assert (Aad[2][3] ==-2.3) && (Bad[2][3] ==2.3)
		//println "Aad[2]="+(Aad[2]+"\n")
		//println "new Matrix(arraydouble[1])="+(new Matrix(arraydouble[1])+"\n")
		
		//println "Aad[2]="+(Aad[2]+"\n")
		//println "Bad[2]="+(Bad[2]+"\n")
		Aad[2] = Bad[2]-10.0
		//println "Aad[2]="+(Aad[2]+"\n")
		//println "Bad[2]="+(Bad[2]+"\n")
		assert (Aad[2] ==new Matrix([-7.9, -7.8, -7.7, -7.6, -7.5])) && (Bad[2] ==new Matrix([2.1 ,2.2 ,2.3 ,2.4,2.5]))
	
		Aad[2] = -10.0
		//println "Aad[2]="+(Aad[2]+"\n")
		assert (Aad[2] ==new Matrix([-10.0, -10.0, -10.0, -10.0, -10.0]))
		
		Aad[2] = [-11.0,-12.0,-13.0,-14.0,-15.0]
		//println "Aad[2]="+(Aad[2]+"\n")
		assert (Aad[2] ==new Matrix([-11.0,-12.0,-13.0,-14.0,-15.0]))
	}
	
	
	void testRangeSet() {
		def Avd=new Matrix(vectordouble)
		def Bvd=new Matrix(vectordouble)
		
		Avd[1..2] =new Matrix([-1.0,-2.0])
		assert (Avd[1..2] ==new Matrix([-1.0,-2.0])) && (Bvd[1..2] ==new Matrix([1.0,2.0]))
				
		
		def Aad=new Matrix(arraydouble)
		def Bad=new Matrix(arraydouble)
		
		//println "Aad[1..2][3]=\n"+(Aad[1..2][3]+"\n")
		Aad[1..2][3] = new Matrix([[-1.3],[-2.3]])
		//println "Aad[1..2][3]=\n"+(Aad[1..2][3]+"\n")
		//println "Aad[1..2]=\n"+(Aad[1..2]+"\n")
		assert (Aad[1..2][3] ==new Matrix([[-1.3],[-2.3]])) && (Bad[1..2][3] ==new Matrix([[1.3],[2.3]]))
		
		Aad[1..2][2..3] =new Matrix([[-1.2,-1.3],[-2.2,-2.3]])
		assert (Aad[1..2][2..3] ==new Matrix([[-1.2,-1.3],[-2.2,-2.3]])) && (Bad[1..2][2..3] ==new Matrix([[1.2,1.3],[2.2,2.3]]))
	
		Aad[-1..2][-2..3] =new Matrix([[-1.2,-1.3],[-2.2,-2.3],[-2.2,-2.3]])
		assert Aad[-1..2][-2..3] ==new Matrix([[-1.2,-1.3],[-2.2,-2.3],[-2.2,-2.3]])
		
		Aad[-1..2][-2..3] = [[-1.2,-1.3],[-2.2,-2.3],[-2.2,-2.3]]
		assert Aad[-1..2][-2..3] ==new Matrix([[-1.2,-1.3],[-2.2,-2.3],[-2.2,-2.3]])
		
		Aad[-1..2][-2..3] = 0.0
		assert Aad[-1..2][-2..3] == Matrix.fill(3,2,0.0)
			
		Aad.eachValue{it -> it+1.0}
		//println "Aad=\n"+(Aad+"\n")
		assert Aad[3][3] == 1.0

	}
}
