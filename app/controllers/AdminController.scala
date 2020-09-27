package controllers

import controllers.actions.{SecuredController, SecuredControllerComponents}
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import controllers.UserController._
import services.{AdminService, UserService}

import scala.concurrent.ExecutionContext

class AdminController @Inject() (
  scc: SecuredControllerComponents,
  userService: UserService,
  adminService: AdminService
)(
  implicit executionContext: ExecutionContext
) extends SecuredController(scc) {

  def list(role: Option[Long]) = AdminAction.async { implicit request =>
    adminService.list(role).map { users =>
      Ok(Json.toJson(users))
    }
  }
}
