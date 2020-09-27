package services

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
}
