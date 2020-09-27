package controllers

import java.time.{Clock, LocalDateTime}
import java.util.UUID

import controllers.ArticleController._
import controllers.actions.{SecuredController, SecuredControllerComponents}
import implicits.ControllerImplicits
import javax.inject.Inject
import models.article.Article
import play.api.Configuration
import services.ArticleService
import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import scala.concurrent.ExecutionContext

class ArticleController @Inject() (
  scc: SecuredControllerComponents,
  articleService: ArticleService
)(implicit ec: ExecutionContext, configuration: Configuration)
    extends SecuredController(scc)
    with ControllerImplicits {

  def create: Action[ArticleCreateRequest] =
    AuthenticatedAction.async(parse.json[ArticleCreateRequest]) {
      implicit request =>
        articleService
          .create(articleCreate(request.body))
          .map(a => Ok(Json.toJson(a)))
    }

  def list(
    author: Option[String],
    category: Option[String],
    isApproved: Option[Boolean],
    sex: Option[Boolean]
  ): Action[AnyContent] = Action.async { implicit request =>
    articleService
      .list(author, category, isApproved, sex)
      .map(articles => Ok(Json.toJson(articles)))
  }

  def update(id: UUID): Action[ArticleCreateRequest] =
    AuthenticatedAction.async(parse.json[ArticleCreateRequest]) {
      implicit request =>
        articleService.get(id).flatMap { article =>
          {
            val articleToUpdate = Article(
              id,
              request.body.location,
              article.author,
              request.body.sex,
              request.body.description,
              request.body.contact,
              request.body.image,
              request.body.category,
              isApproved = false,
              article.created_at,
              updated_at = LocalDateTime.now()
            )
            articleService
              .update(id, articleToUpdate)
              .map(value => Ok(Json.toJson(value)))
          }
        }
    }


}

object ArticleController {

  implicit val clock: Clock = Clock.systemUTC

  case class ArticleCreateRequest(
    location: String,
    author: String,
    sex: Boolean,
    description: String,
    contact: String,
    image: String,
    category: String
  )

  object ArticleCreateRequest {
    implicit val format: OFormat[ArticleCreateRequest] = Json.format
  }

  def articleCreate: ArticleCreateRequest => Article = { request =>
    Article(
      id = UUID.randomUUID(),
      location = request.location,
      author = request.author,
      sex = request.sex,
      description = request.description,
      contact = request.contact,
      image = request.image,
      category = request.category,
      isApproved = false,
      created_at = LocalDateTime.now(clock),
      updated_at = LocalDateTime.now(clock)
    )
  }
}
