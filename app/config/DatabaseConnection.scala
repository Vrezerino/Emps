package config

import java.sql.{Connection, DriverManager}
import scala.util.{Try, Failure, Success}

object DatabaseConnection {
  private val url = sys.env.getOrElse("DB_URL", "jdbc:postgresql://localhost:5433/scalaempl")
  private val username = sys.env.getOrElse("DB_USERNAME", "postgres")
  private val password = sys.env.getOrElse("DB_PASSWORD", "Doh33veig") // for demo purposes

  val connection: Try[Connection] = Try {
    DriverManager.getConnection(url, username, password)
  }

  connection match {
    case Success(conn) =>
      println("Database connection established successfully.")
      // conn object can now be passed to DAO class
    case Failure(exception) =>
      println(s"Failed to connect to the database: ${exception.getMessage}")
  }
}