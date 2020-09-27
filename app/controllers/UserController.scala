package controllers

import java.time.{Clock, LocalDate}
import java.util.UUID

import com.google.inject.Inject
import controllers.actions.{SecuredController, SecuredControllerComponents}
import implicits.ControllerImplicits
import models.user.User
import models.{JwtInfo, UserRoleEnum}
import pdi.jwt.JwtSession._
import play.api.Configuration
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._
import repositories.UserRoleRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}
import UserController._
import exceptions.UserException._

class UserController @Inject() (
  userService: UserService,
  roleRep: UserRoleRepository,
  scc: SecuredControllerComponents
)(implicit ec: ExecutionContext, configuration: Configuration)
    extends SecuredController(scc)
    with ControllerImplicits {
  implicit val clock: Clock = Clock.systemUTC

  private def errorHandler: PartialFunction[Throwable, Result] = {
    case UserEmailError(msg) =>
      BadRequest(Json.obj("status" -> 400, "msg" -> msg))
    case UserNotFoundError(msg) =>
      NotFound(Json.obj("status" -> 404, "msg" -> msg))
    case UserExistError(msg) =>
      NotAcceptable(Json.obj("status" -> 406, "msg" -> msg))
    case err: Error => BadRequest(Json.obj("status" -> 400, "msg" -> s"$err"))
  }

  def register: Action[UserCreateRequest] =
    Action.async(parse.json[UserCreateRequest]) { implicit request =>
      userService
        .create(userCreate(request.body))
        .map { u =>
          Ok(Json.toJson(u))
        }
        .recover(errorHandler)
    }

  def login: Action[UserLogin] =
    Action.async(parse.json[UserLogin]) { implicit request =>
      userService
        .login(request.body.email, request.body.password)
        .flatMap { u =>
          roleRep.get(u.roleId).map { roleRow =>
            Ok(Json.obj("username" -> u.username))
              .addingToJwtSession(
                JwtInfo.IDENTIFIER,
                JwtInfo(u.id, u.email, UserRoleEnum.withName(roleRow.role))
              )
          }
        }
        .recover(errorHandler)
    }

  def get(id: UUID) = AuthenticatedAction.async { implicit request =>
    userService
      .get(id)
      .flatMap(userToResponse)
      .map { u =>
        Ok(Json.toJson(u))
      }
      .recover(errorHandler)
  }

}

object UserController {

  case class UserCreateRequest(
    email: String,
    username: String,
    password: String
  )

  object UserCreateRequest {
    implicit val format: OFormat[UserCreateRequest] = Json.format
  }

  def userCreate: UserCreateRequest => User = { request =>
    User(
      id = UUID.randomUUID(),
      email = request.email,
      username = request.username,
      password = request.password,
      createdAt = LocalDate.now()
    )
  }

  case class UserLogin(email: String, password: String)

  object UserLogin {
    implicit val format: OFormat[UserLogin] = Json.format
  }

  case class UserResponse(id: UUID, email: String, username: String)

  object UserResponse {
    implicit val format: OFormat[UserResponse] = Json.format
  }

  def userToResponse(
    implicit ec: ExecutionContext
  ): User => Future[UserResponse] = {
    case User(id, email, username, password, roleId, isActive, createdAt) => {
      val res = UserResponse(id, email, username)
      Future(res)
    }
  }
}
