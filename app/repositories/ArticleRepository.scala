package repositories

import java.time.{Clock, LocalDateTime}
import java.util.UUID

import exceptions.ArticleException._
import javax.inject.Inject
import models.WPSchema
import models.article.Article
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

class ArticleRepository @Inject() (
  override val dbConfigProvider: DatabaseConfigProvider
)(
  implicit ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with WPSchema {

  import dbConfig.profile.api._

  val default = TableQuery[ArticleTable]
  implicit val clock: Clock = Clock.systemUTC

  def create(article: Article): DBIO[Article] =
    (default += article).map(_ => article)

  def get(id: UUID): DBIO[Article] =
    default.filter(_.id === id).result.headOption.flatMap {
      case Some(value) => DBIO.successful(value)
      case _ =>
        DBIO.failed(ArticleNotFoundError(s"Article with id $id doesn't exist"))
    }

  def update(id: UUID, article: Article): DBIO[Article] =
    get(id).flatMap { result =>
      default.filter(_.id === id).update(article).map(_ => article)
    }

  def list(
    author: Option[String] = None,
    category: Option[String] = None,
    isApproved: Option[Boolean] = None,
    sex: Option[Boolean] = None
  ): DBIO[Seq[Article]] = author match {
    case Some(_) => default.to[Seq].filter(_.author === author).result
    case _ =>
      category match {
        case Some(value) =>
          default
            .to[Seq]
            .filter(_.category.toLowerCase === value.toLowerCase)
            .result
        case _ =>
          isApproved match {
            case Some(_) =>
              default.to[Seq].filter(_.isApproved === isApproved).result
            case _ =>
              sex match {
                case Some(_) => default.to[Seq].filter(_.sex === sex).result
                case _       => default.to[Seq].result
              }
          }
      }
  }

}
