package services

import java.util.UUID

import javax.inject.Inject
import models.user.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.UserRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject() (
  protected val dbConfigProvider: DatabaseConfigProvider
)(
  userRep: UserRepository
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(user: User): Future[User] =
    db.run {
      userRep.create(user)
    }

  def login(email: String, password: String): Future[User] =
    db.run {
      userRep.login(email, password)
    }

  def get(id: UUID): Future[User] =
    db.run {
      userRep.get(id)
    }

  def getUserByEmail(email: String): Future[Option[User]] =
    db.run {
      userRep.getUserByEmail(email)
    }

}
