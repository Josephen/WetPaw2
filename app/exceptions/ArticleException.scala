package exceptions

object ArticleException {
  case class ArticleNotFoundError(msg: String) extends Throwable

}
