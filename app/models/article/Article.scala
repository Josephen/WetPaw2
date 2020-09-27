package models.article

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.Json

case class Article(
  id: UUID,
  location: String,
  author: String,
  sex: Boolean,
  description: String,
  contact: String,
  image: String,
  category: String,
  isApproved: Boolean,
  created_at: LocalDateTime,
  updated_at: LocalDateTime
)

object Article {
  implicit val articleFormat = Json.format[Article]
}
