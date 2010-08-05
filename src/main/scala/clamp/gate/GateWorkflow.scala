package clamp.gate

trait Monad[M[+_], +A] {
	def flatMap[B](f: A => M[B]) : M[B]
}

trait Stateful[+S] extends Monad[Stateful, S] {
	def s: S
	override def flatMap[B](f: S => Stateful[B]): Stateful[B] = f(s)
}

case class State[S](val s: S) extends Stateful[S] 


object Transform
{
	def apply[S, M](f: (S, M) => Stateful[S])(s: Stateful[S])(m: M) = s flatMap { s => f(s, m) }
}

case object End extends Stateful[Nothing] {
	def s: Nothing = throw new Exception("Trying to flatMap on End")
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