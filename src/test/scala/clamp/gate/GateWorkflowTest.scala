package clamp.gate

import org.scalatest._
import org.scalatest.matchers._


class StateTest extends FlatSpec with MustMatchers {
	"A state" must "return a state when mapped" in {
		val s = new State(0)
		(s flatMap {x:Int => State(x + 1)}) must be (new State(1))
	}
	
	it must "accept suptypes as returns" in {
		val s = State(0.)
		(s flatMap {x: Double => State((x + 1).asInstanceOf[Int])}) must be (State(1.))
	}
	
	"The end state" must "be a normal state allowing mapping, but raise an exception if someone tries" in {
		evaluating {
			End flatMap {x: Int => State(x + 100) }
		} must produce [Exception]
	}
	
	
	"A transformation" must "return a new stateful when applied with a message" in {
		val s = State("")
		val t = Transform({ (s:String, m:Int) => if( m > 0) State(s + ", %d" format m) else End }) _
		
		val s2 = t (s) (1)
		t (s2) (2) must be (State(", 1, 2"))
		t (s2) (0) must be (End)
		
		evaluating {
			t (End) (0)
		} must produce [Exception]
	}
	
	
}

class TransformableTest extends FlatSpec with MustMatchers {
	"A transformable" must "be able to return itself" in {
		val infinite = new Transformable[Int] {
			def chooseNextState = (i: Int) => this
		}
		(infinite chooseNextState(0)) must be (infinite)
	}
	
	it must "be able to return some other transformable" in {
		val second = new Transformable[Int] {
			def chooseNextState = (i: Int) => this
		}
		
		val first = new Transformable[Int] {
			def chooseNextState = (i: Int) => second
		}
		
		(first chooseNextState(0)) must be (second)
	}
	
	it must "be able to return alternatively one or another transformable" in {
		val left = new Transformable[Int] {
			def chooseNextState = (i: Int) => this
		}
		val right = new Transformable[Int] {
			def chooseNextState = (i: Int) => this
		}
		val choose = new Transformable[Int] {
			def chooseNextState = (i: Int) => i match {
				case 0 => left
				case _ => right
			}
		}
		
		(choose chooseNextState(0)) must be=== (left)
		(choose chooseNextState(1)) must be=== (right)
	}


}


class GateWorkflowTest extends FlatSpec with MustMatchers {
	
	/*
	"A workflow accepting ints" must "accept a chain of apply calls in ints" in {
		val wf:Workflow = new Workflow(0)
		wf(10)(9)(8)(7)(6)(5)(4)(3)(2)(1)(0)
	}
	
	it must "return End when given 0" in {
		val wf:Workflow[Int] = new Workflow({ x => if(x == 0) End else wf })
		wf(1) must be (wf)
		wf(0) must be (End)
	}
	*/
}