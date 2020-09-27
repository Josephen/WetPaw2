package models.user

import java.time.LocalDate
import java.util.UUID

import models.UserRole
import play.api.libs.json.Json

case class User(
  id: UUID,
  email: String,
  username: String,
  password: String,
  roleId: Long = UserRole.DEFAULT_ID,
  isActive: Boolean = true,
  createdAt: LocalDate
)

object User {
  implicit val userFormat = Json.format[User]
}
