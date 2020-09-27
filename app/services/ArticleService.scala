package services

import java.util.UUID

import javax.inject.Inject
import models.article.Article
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.ArticleRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class ArticleService @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider,
  articleRep: ArticleRepository
)(
  implicit ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(article: Article): Future[Article] =
    db.run {
      articleRep.create(article)
    }

  def get(id: UUID): Future[Article] =
    db.run {
      articleRep.get(id)
    }

  def update(id: UUID, article: Article) =
    db.run {
      for {
        r <- articleRep.get(id)
        _ <- articleRep.update(r.id, article)
      } yield r
    }

  def list(
    author: Option[String],
    category: Option[String],
    isApproved: Option[Boolean],
    sex: Option[Boolean]
  ): Future[Seq[Article]] =
    db.run {
      articleRep.list(author, category, isApproved, sex)
    }
}
