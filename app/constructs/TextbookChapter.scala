package constructs


case class TextbookChapter(title: String, startPage: Int, endPage: Int, numSections: Int)


object TextbookChapter {

  import reactivemongo.bson.Macros

  // Implicitly convert to/from BSON
  implicit val textbookChapterHandler = Macros.handler[TextbookChapter]

}