package build

import sbt.{DefaultProject, ProjectInfo, Path}
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info) with Eclipsify {
	val launchInterface = "org.scala-tools.sbt" % "launcher-interface" % "0.7.4-SNAPSHOT" % "provided"
	val cmdLineParser ="eed3si9n" % "scopt" % "1.0"
	
	val maven = "Nexus" at "http://localhost:8081/nexus/content/repositories/central"
	val mavenLocal = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
	
	val scalatest = "org.scalatest" % "scalatest" % "1.0.1-for-scala-2.8.0.RC1-SNAPSHOT" % "test"
	val scalaToolsSnapshots = "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
	 
}