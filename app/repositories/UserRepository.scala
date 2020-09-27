package repositories

import java.time.LocalDate
import java.util.UUID

import javax.inject.Inject
import models.WPSchema
import models.user.User
import org.mindrot.jbcrypt.BCrypt
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import exceptions.UserException._

import scala.concurrent.{ExecutionContext, Future}

class UserRepository @Inject() (
  override val dbConfigProvider: DatabaseConfigProvider
)(
  implicit ec: ExecutionContext
) extends HasDatabaseConfigProvider[JdbcProfile]
    with WPSchema {

  import dbConfig.profile.api._

  private val default = TableQuery[UserTable]

  def create(user: User): DBIO[User] = validateEmail(user.email) match {
    case true =>
      default
        .filter(u => u.email === user.email || u.username === user.username)
        .result
        .headOption
        .flatMap {
          case None =>
            (default +=
              User(
                id = user.id,
                email = user.email,
                username = user.username,
                password = getHashedPassword(user.password),
                createdAt = user.createdAt
              ))
              .map(_ => user)
          case Some(value) =>
            DBIO.failed(
              UserExistError(
                s"User with email: ${value.email} or username: ${value.username} exist"
              )
            )
        }
    case false =>
      DBIO.failed(UserEmailError("Illegal email format"))
  }

  def get(id: UUID): DBIO[User] =
    default.filter(_.id === id).result.headOption.flatMap {
      case Some(value) => DBIO.successful(value)
      case None        => DBIO.failed(UserNotFoundError(s"User $id doesn't exist"))
    }

  def getUserByEmail(email: String): DBIO[Option[User]] =
    default.filter(_.email === email).result.headOption.flatMap {
      case Some(value) => DBIO.successful(Option(value))
      case None        => DBIO.failed(UserNotFoundError(s"User $email doesn't exist"))
    }

  def login(email: String, password: String): DBIO[User] =
    default
      .filter(q => q.email === email)
      .result
      .map(_.headOption.filter(u => BCrypt.checkpw(password, u.password)))
      .flatMap {
        case Some(value) => DBIO.successful(value)
        case None        => DBIO.failed(UserLoginError("Invalid email or password"))
      }

  private def getHashedPassword(password: String): String = {
    val salt = BCrypt.gensalt()
    BCrypt.hashpw(password, salt)
  }

  private def validateEmail(mail: String): Boolean = {
    val emailRegex =
      """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$""".r
    emailRegex.pattern.matcher(mail).matches()
  }
}
