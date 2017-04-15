package controllers

// Standard Library
import javax.inject.Inject

// Play Framework
import play.api.mvc._
import play.api.i18n.{I18nSupport, MessagesApi}


/**
  *
  * @param messagesApi
  */
class Application @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def home() = Action {
    Ok(views.html.home())
  }

  /**
    * The Home Page
    *
    * @return
    */
  def dashboard(username: String) = Action {
    Ok(views.html.dashboard(username))
  }


  /**
    * My resume as a PDF
    *
    * @return
    */
  def resume: Action[AnyContent] = controllers.Assets.at(path = "/public", file = "pdfs/resume.pdf")

  /**
    * The About Page
    *
    * @return
    */
  def about = Action {
    Ok(views.html.about())
  }

  /**
    * The Quotes Page
    *
    * @return
    */
  def quotes = Action {
    Ok(views.html.quotes())
  }

  /**
    * The Profiles Page
    *
    * @return
    */
  def profiles = Action {
    Ok(views.html.profiles())
  }

}
