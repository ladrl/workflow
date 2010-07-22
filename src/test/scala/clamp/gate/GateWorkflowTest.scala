package clamp.gate

import org.scalatest._
import org.scalatest.matchers._

class GateWorkflowTest extends FlatSpec with MustMatchers {
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
		
		it must "allow to define a no-condition follow up item" in {
			((item then item) followingItems(0) _2) must be (Some(item))
		}
		
		it must "allow to define a conditional follow up item" in {
			val otherItem = WorkflowItem(None, Nil, None)
			val condItem = item when ((_, _) => {true}) then otherItem
			val altCondItem = WorkflowItem()
			(condItem followingItems) must be (List((Some((_,_) => {true}), Some(otherItem))))
//			condItem process (new WorkflowMessage{}, WorkflowInstance(item)) must be (WorkflowInstance(otherItem))
		}
		
		"A workflow with multiple follow ups" must "correctly select the follow up item" in {
			pending
			case class Message[T](val value: T) extends WorkflowMessage
			val otherItem1 = WorkflowItem()
			println(otherItem1)
			val otherItem2 = WorkflowItem()
			val condItem = item
				.when ((_, x) =>{ x match {case Message(i:Int) => {println("got %d" format i); true}; case _ => false}}) then 
						otherItem1 
				.when ((_, x) => {println("Check 2"); x match {case Message(s:String) => true; case _ => false}}) then 
						otherItem2
							
			println(condItem followingItems)
			condItem process (new Message(0), WorkflowInstance(condItem)) must be (WorkflowInstance(otherItem1))
//			condItem process (new Message(""), WorkflowInstance(condItem)) must be (WorkflowInstance(otherItem2))
		}
}