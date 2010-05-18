package cmdLine

import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers

class CmdLineTest extends FlatSpec with MustMatchers {
	"""CmdLineArgument("123").asInt """ should "be equal to 123" in {
		
		val arg = new CmdLineArgument("123")
		arg.asInt must be (123)
	}
	"""CmdLineArgument("22.02").asDouble """ should "be equal to 22.02" in {
		val arg = new CmdLineArgument("22.02") 
		arg.asDouble must be (22.02)
	}
	
	"""CmdLineArgument(""test"") """ should "be equal to test" in {
		val arg = new CmdLineArgument("\"test\"") 
		arg.asString must be ("test")
	}
	
	"An object extending CmdLine" should "be initialized with the arguments" in {
		val obj = new CmdLine {
			val argsString = Some("-a=123 -b=test")
			
			val a = Value("a").asInt
			val b = Value("b").asString
		}
		
		obj.a must be (123)
		obj.b must be ("test")
	}
	
	"An object with a trait of CmdLine" should "be a normal object" in {
		val str = """-test1=1 -test2=2.3 -test3="booo""""
		
		trait Initalized extends CmdLine {
			val argsString = Some(str)
			val test = Value("test1") asInt
			val test1 = Value("test2") asFloat
			val test2 = Value("test3") asString
		}
		
		val obj = new Initalized {
			override val test = 333
		}
		
		obj.test must be (333)
		obj.test1 must be (2.3f)
		obj.test2 must be ("booo")
	}
}