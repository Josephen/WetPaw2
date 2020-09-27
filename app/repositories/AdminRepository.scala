package repositories

import java.time.LocalDate
import java.util.UUID

import javax.inject.Inject
import models.WPSchema
import models.user.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import exceptions.UserException._
import models.article.Article

import scala.concurrent.ExecutionContext

class AdminRepository@Inject() (
                                 override val dbConfigProvider: DatabaseConfigProvider
                               )(
                                 implicit ec: ExecutionContext
                               ) extends HasDatabaseConfigProvider[JdbcProfile]
  with WPSchema {

  import dbConfig.profile.api._

  private val defaultUser = TableQuery[UserTable]
  private val defaultArticle = TableQuery[ArticleTable]

  def list(role: Option[Long]): DBIO[Seq[User]] = role match{
    case Some(value) => defaultUser.to[Seq].filter(_.roleId === value).result
    case _ => defaultUser.to[Seq].result
  }

  def changeArticle(id: UUID, isApprove: Option[Boolean] = Some(true)) =
    defaultArticle.filter(_.id === id)
      .map(p => p.isApproved)
      .update(isApprove.get)

  def changeUser(id: UUID, isActive: Option[Boolean] = Some(true)) =
    defaultUser.filter(_.id === id)
      .map(p => p.isActive)
      .update(isActive.get)
}
