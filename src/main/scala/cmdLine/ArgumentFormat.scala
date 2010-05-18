package cmdLine

/**
 * @author lukasladrach
 * Formatdefiniton for command line arguments.
 * 
 */
class ArgumentFormat(prefix: String, separator: String) {
	val format = prefix + """(\S+)""" + separator + """(\S+)"""
	val AF = ("^" + format + "$").r
	def extract(arg: String): Option[(String, String)] = {
		arg match {
			case AF(name: String, value: String) => Some((name, value))
			case _ => None
		}
	}
	
	override def toString = format 
}
