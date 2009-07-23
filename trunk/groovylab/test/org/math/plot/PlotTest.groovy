package org.math.array;

import org.math.array.Matrix as Matrix
import org.math.plot.Plot as Plot


class PlotTest extends GroovyTestCase {
	
	void testFigure() {
		Plot.plot("1",Matrix.rand(10,3))	
		
		Plot.figure()
		Plot.plot("2",Matrix.rand(10,3),"LINE")	
		
		Plot.figure(1)
		Plot.plot("3",Matrix.rand(10,1),Matrix.rand(10,1),Matrix.rand(1,10),"BAR")	
		
		Plot.close(2)
	}

}
