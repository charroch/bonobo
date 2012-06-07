package com.novoda.bonobo.android

import com.android.chimpchat.hierarchyviewer.HierarchyViewer
import com.android.ddmlib.log.LogReceiver
import com.android.ddmlib.log.LogReceiver.{LogEntry, ILogListener}
import com.android.hierarchyviewerlib.device.ViewNode
import com.android.hierarchyviewerlib.device.ViewNode.Property

import collection.JavaConversions._
import com.android.ddmlib.{RawImage, IShellOutputReceiver, IDevice}
import java.io.File
import javax.imageio.ImageIO
import com.android.chimpchat.adb.image.ImageUtils

class RichDevice(val device: IDevice) extends Shellable with Viewable with Loggable with Screenshotable

object RichDevice {
  implicit def toRichDevice(device: IDevice) = new RichDevice(device)

  implicit def toLowerDevice(device: RichDevice) = device.device
}

trait Shellable {
  this: RichDevice =>

  type ShellOutput[T] = IShellOutputReceiver with T

  def shell[T](command: String)(receiver: ShellOutput[T]): T = {
    device.executeShellCommand(command, receiver)
    receiver.asInstanceOf[T]
  }
}

trait Viewable {
  this: RichDevice =>

  lazy val hierarchyViewer = new HierarchyViewer(this)

  def views(f: ViewNode): Stream[ViewNode] =
    if (f.children.isEmpty) Stream(f) else f.children.toStream.map(views).flatten

  def find(f: ViewNode) = views(f).filter(_.properties.exists(isTextView))

  def isTextView = (prop: Property) => prop.name == "text:mText"

  class RichViewProperty(prop: Property) {
    def isTextView = prop.name == "text:mText"
  }

}

trait Loggable {
  this: RichDevice =>
  def log(to: LogReceiver) {
    this.device.runEventLogService(to)
  }

  class ConsoleLogger extends ILogListener {
    def newEntry(p1: LogEntry) {
      println(p1.data)
    }

    def newData(p1: Array[Byte], p2: Int, p3: Int) {}
  }

}

trait Screenshotable {
  this: RichDevice =>
  def screenshot = device.getScreenshot
}

class RichRawImage(image: RawImage) {
  def save(to: Option[File] = None) = {
    ImageIO.write(ImageUtils.convertImage(image), "png", to.getOrElse(new File("/tmp/test.png")));
  }
}

object RichRawImage {
  def apply(image: RawImage) = new RichRawImage(image)
}

trait Root {
  this: RichDevice =>

  def isRoot = device.e
}