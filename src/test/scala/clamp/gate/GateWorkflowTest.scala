package clamp.gate

import org.scalatest._
import org.scalatest.matchers._

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
	
	it must "be able to return End" in {
		val returningEnd = new Transformable[Int] {
			def chooseNextState = (i: Int) => TEnd
		}
		(returningEnd chooseNextState(0)) must be=== (TEnd)
	}
	
	"State" must "be simple to use... :-)" in {
		lazy val s1:State[String] = State { (msg: String) => {
			val Smilies = """(.*):-D(.*)""".r
			msg match {
					case Smilies(a, b) => TEnd
					case _ => s1
				}
			}
		}
		s1("no smiles here") must be=== (s1)
		s1("one here: :-D") must be=== (TEnd)
	}


}