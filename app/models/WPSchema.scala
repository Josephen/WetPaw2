package models

import models.dbentity._
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait WPSchema extends UserSchema
  with UserRoleSchema
  with ArticleSchema {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

}
