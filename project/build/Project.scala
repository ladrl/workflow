package build

import sbt.{DefaultProject, ProjectInfo, Path}
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info) with Eclipsify {
	
	val maven = "Maven Central" at "http://localhost:8081/nexus/content/repositories/central"
	val maven2 = "Maven Central 2" at "http://localhost:8081/nexus/content/repositories/central2/"
	val scalatest = "org.scalatest" % "scalatest" % "1.2-for-scala-2.8.0.final-SNAPSHOT" % "test"
	//"org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.RC1-SNAPSHOT" % "test"
	val scalaTools = "Scala tools" at "http://localhost:8081/nexus/content/repositories/scala-tools/"
	val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
	 
	val guice = "com.google.inject" % "guice" % "2.0"
	
	val akkaRepo = "Local akka" at "http://localhost:8081/nexus/content/repositories/akka/"
	val multiRepo = "Multiverse Maven 2" at "http://localhost:8081/nexus/content/repositories/multiverse/"
	val guicy = "Guicey Fruit" at "http://localhost:8081/nexus/content/repositories/GuiceyFruit/"
	val jboss = "jboss" at "http://localhost:8081/nexus/content/repositories/jboss/"

	//val akka = "se.scalablesolutions.akka" % "akka-core_2.8.0.RC3" % "0.9"
}