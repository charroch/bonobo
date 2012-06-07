import sbt._
import sbt.Keys._
import akka.sbt.AkkaKernelPlugin
import akka.sbt.AkkaKernelPlugin.{Dist, outputDirectory, distJvmOptions}

object ZoidbergBuild extends Build {
  lazy val androidHome = SettingKey[File]("android-home", "root dir of android")
  lazy val Zoidberg = Project(
    id = "Zoidberg",
    base = file("."),
    settings = Project.defaultSettings ++ AkkaKernelPlugin.distSettings ++ Seq(
      name := "Zoidberg",
      organization := "com.novoda",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2",
      androidHome := file(System.getenv("ANDROID_HOME")),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" % "akka-actor" % "2.1-SNAPSHOT",
        "com.typesafe.akka" % "akka-kernel" % "2.1-SNAPSHOT",
        "org.scalatest" %% "scalatest" % "1.8" % "test"
      ),
      unmanagedJars in Compile <<= androidHome map {
        androidHome: File => (androidHome / "tools/lib/" ** (
          "monkeyrunner.jar" || "chimpchat.jar" || "hierarchyviewer*" || "guava*" || "ddm*" || "swt*")).classpath
      },
      initialCommands := """
        import novoda.zoidberg.device._
        import RichDevice._
        import com.android.hierarchyviewerlib.device.DeviceBridge
        import collection.JavaConversions._

        def loadAdb = {
          DeviceBridge.initDebugBridge("/opt/android-sdk-linux/platform-tools/adb")
        }

        lazy val device = DeviceBridge.getDevices.head
                         """
    )
  )
}
