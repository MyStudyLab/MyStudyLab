package constructs

// Reactive Mongo
import play.api.libs.json._


/**
  * Represents a journal entry
  *
  * @param username  The username responsible for the journal entry
  * @param text      The content of the journal entry
  * @param timestamp The time at which the journal entry was recorded
  * @param pos       The position where the journal entry was recorded
  */
case class JournalEntry(username: String, text: String, timestamp: Long, pos: Point, sentiment: Double, inferredSubjects: List[String]) {

  def toGeoJson: JsValue = {

    JsObject(Map(
      "type" -> JsString("Feature"),
      "properties" -> JsObject(Map(
        "username" -> JsString(username),
        "timestamp" -> JsNumber(timestamp),
        "text" -> JsString(text),
        "sentiment" -> JsNumber(sentiment),
        "inferredSubjects" -> Json.toJson(inferredSubjects)
      )),
      "geometry" -> Json.toJson(pos)
    ))
  }

}

object JournalEntry {

  // Implicitly convert to JSON
  implicit val journalEntryWrites = Json.writes[JournalEntry]

  import reactivemongo.bson.Macros

  // Implicitly convert to/from BSON
  implicit val BSONHandler = Macros.handler[JournalEntry]

}