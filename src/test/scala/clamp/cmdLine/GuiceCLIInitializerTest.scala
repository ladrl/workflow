package cmdLine

import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec

import com.google.inject._
import com.google.inject.name._


class TestNamed @Inject()(@Named("Test") val test: String, @Named("Test2") val test2 : String)
class TestPosition @Inject()(@Named("#1") val position1 : String, @Named("#0") val position0 : String)

class GuiceCLIInitializerTest extends FlatSpec with MustMatchers {
	"CLI initializer" must "provide its named args" in {
		val in = Guice.createInjector(new GuiceCLIInitializer("Test=Result" :: "Test2=Result2" :: Nil))
		
		val res = new TestNamed("Result", "Result2")
		
		val result = in.getInstance(res.getClass).asInstanceOf[TestNamed]
		result.test must be ("Result")
		result.test2 must be ("Result2")
	}
	
	it must "provide its position args" in {
		val in = Guice.createInjector(new GuiceCLIInitializer("Test=Result" :: "Position1" :: "Test2=Result2" :: "Position0" :: Nil))
		val res = new TestPosition("Position1", "Position0")
		val result = in.getInstance(res.getClass).asInstanceOf[TestPosition]
		result.position1 must be ("Position0")
		result.position0 must be ("Position1")
	}
}