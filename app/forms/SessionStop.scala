package forms

import play.api.data.Form
import play.api.data.Forms._

case class SessionStop(user_id: Int)

object SessionStop {

  val stopForm: Form[SessionStop] = Form(
    mapping(
      "user_id" -> number
    )(SessionStop.apply)(SessionStop.unapply)
  )
}