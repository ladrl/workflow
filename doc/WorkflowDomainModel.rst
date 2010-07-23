======================
 Workflow domain model
======================

Outline the domain of workflow as it is to be used in this project.

Definitons
----------

 Workflow   : A tree of actions connected by conditions
 Action     : A single chunk of work. This could be anything expressible
 Path       : Conceptional link from one action to the next with the associated condition.
 Condition  : An expression which evaluates to a boolean value and governs the progression from action to action
 Message    : A chunk of information processed by the workflow. Workflows are triggered by messages, thus entirely event driven.

 Dispatcher : A function which triggers workflows as a reaction to incoming messages.
 Todo       : A container for ongoing workflows. This is the only part of the workflow domain which has mutable state.

 WorkflowEntry : Structural element which make up a Workflow, containing the Action and the Paths from it.
 



Relations
---------

 - A Workflow is made up from WorkflowEntries
 - A WorkflowEntry connects an action with a list of paths
 - A Path is a condition and a reference to a WorkflowEntry
 - A Condition is a closure taking a message and evaluating to boolean
 
 - A Message carries data in arbitrary form