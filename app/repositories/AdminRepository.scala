package repositories

import java.time.LocalDate
import java.util.UUID

import javax.inject.Inject
import models.WPSchema
import models.user.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import exceptions.UserException._

import scala.concurrent.ExecutionContext

class AdminRepository@Inject() (
                                 override val dbConfigProvider: DatabaseConfigProvider
                               )(
                                 implicit ec: ExecutionContext
                               ) extends HasDatabaseConfigProvider[JdbcProfile]
  with WPSchema {

  import dbConfig.profile.api._

  private val default = TableQuery[UserTable]

  def list(role: Option[Long]): DBIO[Seq[User]] = role match{
    case Some(value) => default.to[Seq].filter(_.roleId === value).result
    case _ => default.to[Seq].result
  }

}
