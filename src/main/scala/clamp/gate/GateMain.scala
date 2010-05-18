package clamp.gate

import xsbti._

class GateMain extends AppMain {
	def run(configuration: xsbti.AppConfiguration) = {
		println("hello world")
		
		new Exit(0)
	}
	
	class Exit(val code : Int) extends xsbti.Exit
}