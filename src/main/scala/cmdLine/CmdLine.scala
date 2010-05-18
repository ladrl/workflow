package cmdLine


// Trait which allows for initialization of fields via arguments
/* Example:
 * 
 * class A(val args: List[String]) extends CmdLine {
 * 	val namedValue = StringValue("Named") get
 *  val optionalValue = IntValue("Optional") getOrElse(-1)	
 *  val posValue = PositionValue(0) 
 * } 
 */
trait CmdLine {
	class CmdLineException(msg: String) extends Exception(msg) {
		def this(argName: String, format: ArgumentFormat, args: List[String]) = this("Argument '" + argName + "' with format '" + format + "' not found in " + args)
	}
	
	val argsString : Option[String]
	
	lazy val args : List[String] = argsString match { case Some(string) => string.split(" +").toList; case None => null }

	lazy val defaultFormat = new ArgumentFormat("-", "=")
	
	lazy val defaultExtractedArgs = { println(args); args.map(defaultFormat.extract(_)) }
	
	def Value(name : String, format: ArgumentFormat = defaultFormat): CmdLineArgument = {
		val extractedArgs = if(format == defaultFormat) 
								defaultExtractedArgs
							else 
								args.map(format.extract(_))
		val argsMap = extractedArgs.filter(_ != None).map(_.get).foldRight(Map[String, String]())( (a, b) => { b(a._1) = a._2 } )
		if(argsMap.contains(name)) new CmdLineArgument(argsMap(name)) else throw new CmdLineException(name, format, args)
	} 
}


class CmdLineArgument(val arg: String) {
	def asInt = java.lang.Integer.parseInt(arg)
	def asDouble = java.lang.Double.parseDouble(arg)
	def asFloat = java.lang.Float.parseFloat(arg)
	def asURL = { import java.net.URL; new URL(arg) }
	def asString = { val Quoted = """\"*([^"]*)\"*""".r; val Quoted(string) = arg; string }	
}