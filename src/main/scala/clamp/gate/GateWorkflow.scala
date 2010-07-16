package clamp.gate


// Workflow prcessing
// Triggering is done by passing the incoming messages through a list of workflows
/**
Usage:
	val msg = new ClampRequest
	val workflows = W1 :: W2 :: W3 :: Nil
	
	workflows process msg

 */
trait WorkflowMessage

// Trait for a single chunk of work. Maintains a sequence of following items, each associated with a conditon function
// 
trait WorkItem{
	val followingItems: WorkItem.ItemsList
	val messagePreProcessor: Option[WorkItem.MessageProcessor]
	val action: (WorkflowMessage) => Unit
	def process(msg: WorkflowMessage, state: WorkflowState): WorkflowState = {
		state match {
			case WorkflowInstance(item) if(item == this) => {
				val preProcessedMsg:WorkflowMessage = messagePreProcessor.getOrElse((x:WorkflowMessage) => x)(msg)
				
				action(preProcessedMsg) // Execute assigned action with preprocessed message and no checks
				
				followingItems.dropWhile(_ match {
					case (Some(check), _) => check(state, preProcessedMsg)
					case (None, _) => true
				}) match {
					case (_, nextItem) :: _ => new WorkflowInstance(nextItem)
					case Nil => WorkflowEnd
				}
			}
			case _ => state
		}
	}
}
// Companion class holding the associated types
object WorkItem {
	type Condition = (WorkflowState, WorkflowMessage) => Boolean
	type ItemsList = List[(Option[Condition], WorkItem)]
	type MessageProcessor = WorkflowMessage => WorkflowMessage
}

// State of workflow: Running (has as particular item receiving the next message) and Ended
abstract class WorkflowState(val item: Option[WorkItem])
final case class WorkflowInstance(currentItem: WorkItem) extends WorkflowState(Some(currentItem))
final case object WorkflowEnd extends WorkflowState(None)

// Construction interface for workflows
/*
Workflow()
	_ match {
		case Request(who, what) => checkIf who isAllowedTo what {
			case Allowed => Approve
			case Disallowed => Declined("Needs fancy condition")
		}
	}
	then when 
*/
case class WorkflowItem(
	val work: Option[(WorkflowMessage) => Unit], 
	val followingItems: WorkItem.ItemsList, 
	val messagePreProcessor: Option[WorkItem.MessageProcessor]) extends WorkItem {
		
	def then(item: WorkItem) = {
		val newFollowingItems = followingItems ++ List(None -> item)
		copy(followingItems = newFollowingItems)
	}
	
	def when(check: WorkItem.Condition) = {
		val newFollowingItems = followingItems ++ List(check -> followingItems.last)
		copy(followingItems = newFollowingItems.asInstanceOf[WorkItem.ItemsList])
	}
	
	def preprocess(preproc: WorkItem.MessageProcessor) = copy(messagePreProcessor = Some(preproc))
	
	def nullWork(msg: WorkflowMessage) = ()
	val action = work getOrElse nullWork _
	
	def apply(work: (WorkflowMessage) => Unit) = copy(Some(work))
}

// Generate a workflow by specifing a trigger, then a sequence of actions and condition-item pairs
object Workflow {
	
}

/*
Must do:
- Trigger workflows as response to processing requests
- Hold the states of workflows in execution
  > Workflow context maintains a map of trigger request to workflow
- Dispatche the requests to the running workflows
  > Implies that the requests need some identificition or grouping mechanism

Could do:
 - Record history -> Logging of workflow processing
*/
class WorkflowContext






