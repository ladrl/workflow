package cmdLine

import com.google._
import com.google.inject._
import com.google.inject.name._

// Bind the arguments to constants
case class NamedArg(val name: String, val arg: String)
case class PositionArg(val arg: String)


class GuiceCLIInitializer(val args: List[String]) extends AbstractModule {
	override def configure() = {
		val reg = "^(.+)=(.+)$".r
		val typedArgs = args.map(_ match {
			case reg(name, arg) => NamedArg(name, arg)
			case arg => PositionArg(arg)
		})
		
		
		var positionIndex = 0
		typedArgs foreach(_ match {
			case NamedArg(name, arg) => {
				println("Bind %s to %s" format(arg, name))
				bindConstant annotatedWith Names.named(name) to arg
			}
			case PositionArg(arg) => {
				println("Bind %s to #%d" format (arg, positionIndex))
				bindConstant annotatedWith Names.named("#%d" format positionIndex) to(arg)
				positionIndex += 1
			}
			case x => println("Matched %s - no match found" format x)
		})
		
	}	
}