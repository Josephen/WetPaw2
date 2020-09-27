package models.dbentity

import java.time.LocalDateTime

import models.UserRole
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait UserRoleSchema {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import dbConfig.profile.api._

  class UserRoleTable(tag: Tag) extends Table[UserRole](tag, "user_role") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def role = column[String]("role")

    def creation_ts = column[LocalDateTime]("creation_ts", O.Default(LocalDateTime.now()))

    def * = (id, role, creation_ts) <> ((UserRole.apply _).tupled, UserRole.unapply)
  }
}
