package clamp.gate

trait State[T]{
	protected val state: T
	def unit(state: T): State[T]
	def flatMap[B](f: T => State[B]) : State[B] = {
		f(state)
	}
	def map(f: (T) => T): State[T] = {
		this.flatMap { x:T =>
			val content: T = f(x)
			unit(content)
		}
	}
}

trait WorkflowEntry[M] {
	val action: (M) => Unit
	val nextEntry: (M) => WorkflowEntry[M]
	val workPending: Boolean
}

case class Work[M](val action: (M) => Unit, val nextEntry: (M) => WorkflowEntry[M]) extends WorkflowEntry[M] {
	val workPending = true
}

case class End[M]() extends WorkflowEntry[M] {
	override val action: (M) => Unit = _ => ()
	override val nextEntry: (M) => WorkflowEntry[M] = (_) => this
	val workPending = false
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