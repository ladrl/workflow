package clamp.gate

/**
 * Create a Monad using a type constructor M taking a covariant type as parameter. Usually, a monad has a unit method returning a 
 * new instance, but this is currently done using case class.
 * flatMap allows to treat the Monad as some type S while not having direct access to its instance.
**/
trait Monad[M[+_], +A] {
	def flatMap[B](f: A => M[B]) : M[B]
}

/**
 * A monad encapsulating some sort of state, of type S.
 * Using def but val to declare state allows implementing classes to _not_ have a state, like End does.
**/
trait Stateful[+S] extends Monad[Stateful, S] {
	def state: S
	override def flatMap[B](f: S => Stateful[B]): Stateful[B] = f(state)
}

/**
 * An actual State monad of type S
**/
case class State[+S](val state: S) extends Stateful[S]

/**
 * Transform is the host for the curring function which takes the actual transformation function, the state monad 
 * containing the state to transform and then some sort of message as a parameter to the transformation. This 
 * allow to build constructs like a statemachine or a workflow, using m and state to decide which state comes next.
**/
object Transform
{
	def apply[S, M](f: (S, M) => Stateful[S])(s: Stateful[S])(m: M) = s flatMap { s => f(s, m) }
}

/**
 * A generally usable termination state, similar to None or Nil in the sense that its compatible with any 
 * type parameters a State may carry.
**/
case object End extends Stateful[Nothing] {
	def state: Nothing = throw new Exception("Trying to flatMap on End")
}


/*

======================
 Workflow domain model
======================

Outline the domain of workflow as it is to be used in this project.

Definitons
----------

 Workflow   : A tree of actions connected by Paths. Does not exist as concrete implementation.
 Action     : A single chunk of work. This could be anything expressible
 Path       : Conceptional link from one action to the next with the associated condition. Made up from a Condition and a reference to a WorkflowEntry
 Condition  : An expression which evaluates to a boolean value and governs the progression from action to action
 Message    : A chunk of information processed by the workflow. Workflows are triggered by messages, thus entirely event driven.

 Dispatcher : A function which triggers workflows as a reaction to incoming messages.
 Todo       : A container for ongoing workflows.

 WorkflowEntry : 


Relations
---------

 - A Workflow is made up from WorkflowEntries
 - A WorkflowEntry connects an action with a list of paths
 - A Path is a condition and a reference to a WorkflowEntry
 - A Condition is a closure taking a message and evaluating to boolean
 
 - A Message carries data in arbitrary form

*/