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

object Transformable {
	type TF[-M] = Function1[M, Transformable[M]]
}


trait Transformable[-M] extends Stateful[Transformable.type#TF[M]] {
	def state = chooseNextState
	def chooseNextState: Transformable.type#TF[M]
}

object State {
	def apply[M](f: Transformable.type#TF[M]) = new State[M](f)
}
class State[-M](val chooseNextState: Transformable.type#TF[M]) extends Transformable[M] {
	def apply(m: M) = chooseNextState(m)
}

case object TEnd extends Transformable[Any] {
	def chooseNextState = (_) => this
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