import java.text.SimpleDateFormat
import java.util.Date
import org.specs2.matcher.Matcher
import org.specs2.mutable.Specification
import org.specs2.specification.AfterExample

class TestSpec extends context {

  "The co-ordinator" should {

    "invoke the Doomsday Device on the 21st of December 2012".txt

    "given the date is ${21/12/2012}" << {
      (s: String) =>
        clock.after(toDate(s))
    }

    "when the coordinator runs" << {
      //new MasterCoordinator(theDoomsdayDevice, clock).runIt()

    }

    "the Doomsday device should be unleashed" << {
      theDoomsdayDevice should beUnleashed
    }
  }

}

trait context extends ClairvoyantSpec {

  val theDoomsdayDevice = ""
  val clock = new Date

  def toDate(ddMMyyyy: String) = new SimpleDateFormat("dd/MM/yyyy").parse(ddMMyyyy)

  override def capturedInputsAndOutputs = Seq(theDoomsdayDevice)

  def beUnleashed: Matcher[String] = (d: String) => (true, d + " was unleashed", d + " was not unleashed")
}

trait ClairvoyantSpec extends Specification with AfterExample {
  sequential
  noindent
  //args.report(exporter = "org.specs2.clairvoyance.ClairvoyanceHtmlExporting")

  def tearDown {}

  def after {
    tearDown
    // plus all your code here for givens and outputs
  }

  def capturedInputsAndOutputs: Seq[String]

  var interestingGivens: Map[String, Any] = Map[String, Any]()
}