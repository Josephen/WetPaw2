package repositories

import javax.inject.Inject
import models.{UserRole, WPSchema}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UserRoleRepository @Inject() (
  override val dbConfigProvider: DatabaseConfigProvider
)(
  implicit ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with WPSchema {

  import dbConfig.profile.api._

  private val default = TableQuery[UserRoleTable]

  def getOpt(id: Long): Future[Option[UserRole]] = db.run {
    default.filter(_.id === id).result.headOption
  }

  def get(id: Long): Future[UserRole] = db.run{
    default.filter(_.id === id).result.head
  }
}
