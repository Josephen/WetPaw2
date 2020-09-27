package models

import UserRoleEnum.UserRole
import play.api.libs.json.Json
import java.util.UUID

case class JwtInfo(userId: UUID, userEmail: String, userRole: UserRole)

object JwtInfo {
  val IDENTIFIER = "jwt-info"

  implicit val format = Json.format[JwtInfo]
}
