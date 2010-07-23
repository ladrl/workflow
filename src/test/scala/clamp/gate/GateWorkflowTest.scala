package clamp.gate

import org.scalatest._
import org.scalatest.matchers._

class GateWorkflowTest extends FlatSpec with MustMatchers {
	{
		"A workflow" must "be created by extending WorkflowTemplate" in {
			class MyMessage
			object TestWorkflow extends WorkflowTemplate {
				type Message = MyMessage
			}
		}
	}
	
	{
		class MyMessage
		object TestWorkflow extends WorkflowTemplate {
				type Message = MyMessage
		}
		import TestWorkflow._
		"A workflow entry" must "be created by specifing an action and a empty list" in {
			val action = (m: MyMessage) => { () }
			val paths = List()
			val entry = WorkflowEntry(action, paths)
			entry.action must be (action)
			entry.paths must be (paths)
		}
		
		"A path" must "be created by specifing a condition and a workflow entry" in {
			val condition = (m: MyMessage) => { true }
			
			val action = (m: MyMessage) => { () }
			val paths = List()
			val entry = WorkflowEntry(action, paths)
			
			val path = Path(condition, entry)
			path.condition must be (condition)
			path.next must be (entry)
		}
	}
}