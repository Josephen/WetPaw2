package exceptions

object UserException {
  case class UserNotFoundError(msg: String) extends Throwable

  case class UserExistError(msg: String) extends Throwable

  case class UserEmailError(msg: String) extends Throwable

  case class UserLoginError(msg: String) extends Throwable
}
