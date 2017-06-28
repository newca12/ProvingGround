import akka.http.scaladsl.marshalling.{Marshaller, _}
import akka.http.scaladsl.model.MediaType
import akka.http.scaladsl.model.MediaTypes._
import play.twirl.api.{Html, Txt, Xml}

package object provingground {

  /** Twirl marshallers for Xml, Html and Txt mediatypes */
  implicit val twirlHtmlMarshaller: akka.http.scaladsl.marshalling.ToEntityMarshaller[play.twirl.api.Html] = twirlMarshaller[Html](`text/html`)
  implicit val twirlTxtMarshaller: akka.http.scaladsl.marshalling.ToEntityMarshaller[play.twirl.api.Txt]  = twirlMarshaller[Txt](`text/plain`)
  implicit val twirlXmlMarshaller: akka.http.scaladsl.marshalling.ToEntityMarshaller[play.twirl.api.Xml]  = twirlMarshaller[Xml](`text/xml`)

  def twirlMarshaller[A](contentType: MediaType): ToEntityMarshaller[A] = {
    Marshaller.StringMarshaller.wrap(contentType)(_.toString)
  }
}
