package models.dbentity

import java.time.LocalDateTime
import java.util.UUID

import models.article.Article
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait ArticleSchema {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import dbConfig.profile.api._

  class ArticleTable(tag: Tag) extends Table[Article](tag, "articles") {
    def id = column[UUID]("id", O.PrimaryKey)

    def location = column[String]("location")

    def author = column[String]("author")

    def sex = column[Boolean]("sex")

    def description = column[String]("description")

    def contact = column[String]("contact")

    def image = column[String]("image")

    def category = column[String]("category")

    def isApproved = column [Boolean]("is_approved")

    def createdAt = column[LocalDateTime]("created_at")

    def updatedAt = column[LocalDateTime]("updated_at")

    def * = (id, location, author, sex, description, contact, image,category, isApproved, createdAt, updatedAt) <> ((Article.apply _).tupled, Article.unapply)
  }
}
