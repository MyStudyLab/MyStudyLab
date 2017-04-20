package controllers

// Standard Library
import javax.inject.Inject

import scala.concurrent.Future

// Project
import constructs.ResultInfo
import forms._

// Play Framework
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

// Reactive Mongo
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}


/**
  * Controller to manage the study sessions API.
  *
  * @param reactiveMongoApi Holds a reference to the database.
  */
class Sessions @Inject()(val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  // Reference to the sessions model
  protected val sessions = new models.Sessions(reactiveMongoApi)


  /**
    * Invoke the model layer to start a new study session.
    *
    * @return
    */
  protected def start = Action.async { implicit request =>

    SessionStartForm.startForm.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.startSession(goodForm.username, goodForm.subject).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Start a study session via the query string
    *
    * @param username
    * @param subject
    * @return
    */
  def startSessionFromParams(username: String, subject: String) = Action.async { implicit request =>

    sessions.startSession(username, subject).map(result => Ok(Json.toJson(result)))
  }


  /**
    * Invoke the model layer to stop the current study session
    *
    * @return
    */
  protected def stop = Action.async { implicit request =>

    SessionStopForm.stopForm.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.stopSession(goodForm.username, goodForm.message).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Invoke the model layer to abort the current study session.
    *
    * @return
    */
  protected def cancel = Action.async { implicit request =>

    SessionCancelForm.form.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.abortSession(goodForm.username).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Invoke the model layer to add a subject.
    *
    * @return
    */
  protected def add = Action.async { implicit request =>

    AddSubjectForm.form.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.addSubject(goodForm.username, goodForm.subject, goodForm.description).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }

  /**
    * Add a subject via the query string
    *
    * @param username
    * @param subject
    * @param description
    * @return
    */
  def addSubjectFromParams(username: String, subject: String, description: String) = Action.async { implicit request =>

    sessions.addSubject(username, subject, description).map(resInfo => Ok(Json.toJson(resInfo)))
  }


  /**
    * Invoke the model layer to remove a subject.
    *
    * @return
    */
  protected def remove = Action.async { implicit request =>

    RemoveSubjectForm.form.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.removeSubject(goodForm.username, goodForm.subject).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Invoke the model layer to rename a subject.
    *
    * @return
    */
  protected def rename = Action.async { implicit request =>

    RenameSubjectForm.form.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.renameSubject(goodForm.username, goodForm.oldName, goodForm.newName).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Invoke the model layer to merge subjects.
    *
    * @return
    */
  protected def merge = Action.async { implicit request =>

    MergeSubjectsForm.form.bindFromRequest()(request).fold(
      _ => invalidFormResponse,
      goodForm => sessions.mergeSubjects(goodForm.username, goodForm.absorbed, goodForm.absorbing).map(resultInfo => Ok(Json.toJson(resultInfo)))
    )
  }


  /**
    * Retrieve the session and status data for the given username
    *
    * @param username The username for which to retrieve session data
    * @return
    */
  def sessionsForUsername(username: String) = Action.async { implicit request =>

    sessions.getUserSessionData(username).map(optData => optData.fold(Ok(Json.toJson(ResultInfo.failWithMessage("failed to retrieve sessions"))))(data => Ok(Json.toJson(data))))
  }

  /**
    *
    * @param username
    * @return
    */
  def userStatus(username: String) = Action.async { implicit request =>

    sessions.getUserStatus(username).map(optData => optData.fold(Ok(Json.toJson(ResultInfo.failWithMessage("failed to retrieve status"))))(data => Ok(Json.toJson(data))))
  }


  /**
    * Start a study session
    *
    * @return
    */
  def startSession() = checkSession(start)


  /**
    * Stop a study session
    *
    * @return
    */
  def stopSession() = checkSession(stop)

  /**
    * Abort the current study session
    *
    * @return
    */
  def cancelSession() = checkSession(cancel)

  /**
    * Add a subject to a user's subject list
    *
    * @return
    */
  def addSubject() = checkSession(add)

  /**
    * Remove a subject from a user's subject list
    *
    * @return
    */
  def removeSubject() = checked(remove)(reactiveMongoApi)

  /**
    * Rename one of the subjects in a user's subject list
    *
    * @return
    */
  def renameSubject() = checked(rename)(reactiveMongoApi)

  /**
    * Merge two subjects from a user's subject list
    *
    * @return
    */
  def mergeSubjects() = checked(merge)(reactiveMongoApi)

}
