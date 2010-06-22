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
class CmdLineException(msg: String) extends Exception(msg) {
	def this(argName: String, format: ArgumentFormat, args: List[String]) = this("Argument '" + argName + "' with format '" + format + "' not found in " + args)
}

class Value(val prefix: Option[String], val keyword: Option[List[String]], val separator: String) {
	def extract(args: List[String]): Option[String] = {
		keyword match {
			case Some(keyword) => {
				val patterns = for(str <- keyword)
					yield ("^" + (prefix getOrElse "") + str + separator + """(.+)$""").r
				val matches = 
					for(Pattern <- patterns; arg <- args)
						yield { 
							arg match { 
								case Pattern(x) => Some(x) 
								case _ => None
							}
						}
				val filteredMatches = matches.filter(_ != None).map(_.get)
				if(filteredMatches.length == 1)
					Some(filteredMatches(0))
				else
					None
			}
			case None => {
				val Pattern = ("^" + (prefix getOrElse "") + "(.+)$").r
				if(args.length > 0) {
					args(0) match {
						case Pattern(x) => Some(x)
						case _ => None
					}
				}
				else 
					None
			}
		}
	}
}

trait CmdLine {
	val argsString : String
	
	

}


class CmdLineArgument(val arg: String) {
	def asInt = java.lang.Integer.parseInt(arg)
	def asDouble = java.lang.Double.parseDouble(arg)
	def asFloat = java.lang.Float.parseFloat(arg)
	def asURL = { import java.net.URL; new URL(arg) }
	def asString = { val Quoted = """\"*([^"]*)\"*""".r; val Quoted(string) = arg; string }	
}