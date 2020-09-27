package services

import java.util.UUID

import javax.inject.Inject
import models.user.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.AdminRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class AdminService @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider
)(
  adminRep: AdminRepository
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  def list(role: Option[Long]): Future[Seq[User]] =
    db.run {
      adminRep.list(role)
    }

  def changeArticle(id: UUID, isApprove: Option[Boolean]): Future[Int] =
    db.run {
      adminRep.changeArticle(id, isApprove)
    }

  def changeUser(id: UUID, isActive: Option[Boolean]): Future[Int] =
    db.run {
      adminRep.changeUser(id, isActive)
    }

}
