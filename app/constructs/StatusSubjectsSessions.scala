package constructs

import play.api.libs.json.Json
import reactivemongo.bson.BSONDocument

/**
  * Used when a user's status, subject vector, and session vector are needed.
  *
  * @param username The username.
  * @param status   The study status of the user.
  * @param subjects The subject vector for the user.
  * @param sessions The session list for the user.
  */
case class StatusSubjectsSessions(username: String, status: Status, subjects: Vector[Subject], sessions: Vector[Session])


object StatusSubjectsSessions {

  import reactivemongo.bson.Macros

  // Implicitly convert to/from BSON
  implicit val StatusSubjectsSessionsHandler = Macros.handler[StatusSubjectsSessions]

  // Implicitly convert to JSON
  implicit val StatusSubjectsSessionsWrites = Json.writes[StatusSubjectsSessions]

  // TODO: Use reflection to generate this directly from the case class?
  // A MongoDB projector for get only the fields for this class
  val projector = BSONDocument("username" -> 1, "status" -> 1, "subjects" -> 1, "sessions" -> 1, "_id" -> 0)
}
