package controllers

import java.util.UUID

import controllers.actions.{SecuredController, SecuredControllerComponents}
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc._
import controllers.UserController._
import services.{AdminService, UserService}

import scala.concurrent.ExecutionContext

class AdminController @Inject() (
  scc: SecuredControllerComponents,
  adminService: AdminService
)(
  implicit executionContext: ExecutionContext
) extends SecuredController(scc) {

  def list(role: Option[Long]) = AdminAction.async {
    implicit request =>
      adminService.list(role).map { users =>
        Ok(Json.toJson(users))
      }
  }

  def changeArticle(id: UUID, isApprove: Option[Boolean]) = AdminAction.async {
    implicit request =>
      adminService.changeArticle(id, isApprove).map { _ =>
        Ok(Json.toJson(s"Approve: $isApprove"))
      }
  }

  def changeUser(id: UUID, isActive: Option[Boolean]) = AdminAction.async {
    implicit request =>
      adminService.changeUser(id: UUID, isActive).map { _ =>
        Ok(Json.toJson(s"User active: $isActive"))
      }
  }
}
