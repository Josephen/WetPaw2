package models

import java.time.LocalDateTime

case class UserRole(
  id: Long,
  role: String,
  creation_ts: LocalDateTime = LocalDateTime.now()
)

object UserRole {
  lazy val DEFAULT_ID = 1
  lazy val DEFAULT_ROLE = UserRoleEnum.USER
}