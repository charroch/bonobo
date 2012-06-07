import sbt._
import sbt.Keys._

object BonoboBuild extends Build {

  lazy val androidHome = SettingKey[File]("android-home", "root dir of android")

  lazy val Bonobo = Project(
    id = "bonobo",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "bonobo",
      organization := "com.novoda",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      androidHome := file(System.getenv("ANDROID_HOME")),
      resolvers ++= Seq(
        "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
        "releases" at "http://oss.sonatype.org/content/repositories/releases"
      ),
      libraryDependencies ++= Seq(
        "org.specs2" %% "specs2" % "1.11"
      ),
      unmanagedJars in Compile <<= androidHome map {
        androidHome: File => (androidHome / "tools/lib/" ** (
          "monkeyrunner.jar" || "chimpchat.jar" || "hierarchyviewer*" || "guava*" || "ddm*" || "swt*")).classpath
      },
      initialCommands :=
        """
         import com.novoda.bonobo.android._
         import RichDevice._
         import RichRawImage._
         import com.android.hierarchyviewerlib.device.DeviceBridge
         import collection.JavaConversions._
         DeviceBridge.initDebugBridge("/opt/android-sdk-linux/platform-tools/adb")
         lazy val device = DeviceBridge.getDevices.head
         def terminate = DeviceBridge.terminate
        """
    )
  )
}
