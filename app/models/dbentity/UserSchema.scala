package models.dbentity

import java.time.LocalDate
import java.util.UUID

import models.UserRole
import models.user.User
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait UserSchema {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import dbConfig.profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[UUID]("id", O.PrimaryKey)

    def email = column[String]("email")

    def username = column[String]("username")

    def password = column[String]("password")

    def roleId = column[Long]("role_id", O.Default(UserRole.DEFAULT_ID))

    def isActive = column[Boolean]("is_active", O.Default(true))

    def createdAt = column[LocalDate]("createdat")

    def * = (id, email, username, password, roleId, isActive, createdAt) <> ((User.apply _).tupled, User.unapply)
  }
}
