package clamp.gate

import org.scalatest._
import org.scalatest.matchers._

class GateWorkflowTest extends FlatSpec with MustMatchers {
	{
		val item = WorkflowItem(None, Nil, None)
		"A empty workflow item" must "process a message and return the EndWorkflow state" in {
			item process (new WorkflowMessage {}, WorkflowInstance(item)) must be (WorkflowEnd)
		}
		
		it must "allow to add a piece of work" in {
			var workDone = false
			val itemWithWork = item (_ => {
				workDone = true
			})
			itemWithWork process (new WorkflowMessage{}, WorkflowInstance(itemWithWork)) must be (WorkflowEnd)
			workDone must be (true)
		}
	}
}