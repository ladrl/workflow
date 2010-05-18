package cmdLine

import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec

class ArgumentFormatTest extends FlatSpec with MustMatchers {
	val af = new ArgumentFormat("-+", "=")
	"""ArgumentFormat("-+", "=")""" should """extract "a" and "b" from "-a=b"""" in {
		af.extract("-a=b") must be (Some("a", "b"))
	}
}