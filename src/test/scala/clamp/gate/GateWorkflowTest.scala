package clamp.gate

import org.scalatest._
import org.scalatest.matchers._

class GateWorkflowTest extends FlatSpec with MustMatchers {
	
	case class IntState(val state: Int) extends State[Int] {
		def unit(s: Int) = IntState(s)
	}
	
	"A state monad" must "be instancible" in {
		val intState = IntState(0)
	}
	
	it must "create a new one when mapped" in {
		val s = IntState(0)
		s.map(_ + 1) must be (IntState(1))
		s.map(_ + 10) must be (IntState(10))
	}
	
	it must "support for-comprehensions" in {
		val s = IntState(0)
		val newS = for(i <- s) yield i + 1
		newS must be (IntState(1))
	}
	
	"End" must "be a normal worflow entry" in {
		val e:WorkflowEntry[Int] = End()
		e.nextEntry(0) must be (End())
	}
	
	"A workflow entry" must "deliver the next entry" in {
		val we = new Work[Int](_ => (), _ => End())
		we.nextEntry(0) must be (End())
	}
	
	it must "allow to get alternative next entries" in {
		val we2 = new Work[Int](_ => (), _ => End())
		val we = new Work[Int](_ => (), _ match { case 1 => we2; case _ => End()})
		we.nextEntry(0) must be (End())
		we.nextEntry(1) must be (we2)
	}
	
	case class WEState[T](val state: WorkflowEntry[T]) extends State[WorkflowEntry[T]] {
		def unit(state: WorkflowEntry[T]) = new WEState(state)
	}	
	
	"A workflow entry" must "be wrapped in a state" in {
		val we = new Work[Int](_ => (), _ => End())
		val s = new WEState(we)
		s.state must be (we)
	}
	
	it must "be executable with map" in {
		val s = WEState(new Work[Int](_ => (), _ => End()))
		(s map {_.nextEntry(0)}) must be (WEState(End()))
	}
	
	it must "allow to get alternative results" in {
		val we2 = new Work[Int](_ => (), _ => End())
		val s = WEState(new Work[Int](_ => (), _ match {
			case 1 => we2
			case _ => End()
		}))
		(s map {_.nextEntry(1)}) must be (WEState(we2))
		(s map {_.nextEntry(0)}) must be (WEState(End()))
	}
	
	it must "inform if there is more work pending" in {
		pending
	}

	"The workflow end" must "return itself when mapped" in {
		pending
	}
	
	/*{
		"A workflow" must "be created by extending WorkflowTemplate" in {
			class MyMessage
			object TestWorkflow extends WorkflowTemplate {
				type Message = MyMessage
			}
		}
	}
	
	{
		class MyMessage(val value: String)
		object TestWorkflow extends WorkflowTemplate {
				type Message = MyMessage
		}
		import TestWorkflow._
		"A workflow entry" must "be created by specifing an action and a empty list" in {
			val action = (m: MyMessage) => { () }
			val entry = WorkflowEntry(action)
			entry.action must be (action)
			entry.paths must be (List())
		}
		
		it must "process a message from a current state" in {
			val action = (m: MyMessage) => { () }
			val entry = WorkflowEntry(action)
			val entry2 = WorkflowEntry(action)
			val state = new WorkflowState[MyMessage] {
				val currentEntry = entry
			}
			val state2 = new WorkflowState[MyMessage] {
				val currentEntry = entry2
			}
			
			val newState = entry.process(state, new MyMessage("test"))
			newState must be (entry2)
		}
		
		"A path" must "be created by specifing a condition and a workflow entry" in {
			val condition = (m: MyMessage) => { true }
			
			val action = (m: MyMessage) => { () }
			val paths: List[Path[MyMessage]] = List()
			val entry = WorkflowEntry[MyMessage](action)
			
			val path = Path(condition, entry)
			path.condition must be (condition)
			path.next must be (entry)
		}
		
	}*/
}