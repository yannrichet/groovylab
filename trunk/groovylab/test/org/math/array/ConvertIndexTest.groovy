package org.math.array;

import org.math.array.Matrix as Matrix

class ConvertIndexTest extends GroovyTestCase {

	void testNoMinus() {
		assert 1..5 == Matrix.convert(1..5,10)
		assert 5..1 == Matrix.convert(5..1,10)
	}
		
	void testOneMinus() {
		assert 1..9 == Matrix.convert(1..-2,10)
		assert 5..3 == Matrix.convert(5..-8,10)
		assert 3..5 == Matrix.convert(-8..5,10)
	}
		
	void testTwoMinus() {
		assert 3..9 == Matrix.convert(-8..-2,10)
		assert 9..3 == Matrix.convert(-2..-8,10)
	}

}
