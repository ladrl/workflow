package build

import sbt.{DefaultProject, ProjectInfo, Path}
import de.element34.sbteclipsify._

class Project(info: ProjectInfo) extends DefaultProject(info) with Eclipsify {
	val mavenRepo = "DefaultMavenRepository" at	"http://localhost:8081/nexus/content/repositories/central/"
	val scalaRepo = "ScalaToolsReleases" at "http://localhost:8081/nexus/content/repositories/scala-tools/"
	val scalaToolRepo = "ScalaToolsSnapshots" at "http://localhost:8081/nexus/content/repositories/scala-devel/"
	
	val scalaTest = "org.scalatest" % "scalatest" % "1.2-for-scala-2.8.0.RC6-SNAPSHOT" % "test"
	
}