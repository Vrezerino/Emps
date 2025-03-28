package dao

import java.sql.Connection
import scala.concurrent.{Future, ExecutionContext}
import models.EmployeeModel

class EmployeeDAO(connection: Connection)(implicit ec: ExecutionContext) {

  def getAllEmployees: Future[Seq[EmployeeModel]] = Future {
    val query = "SELECT * FROM employees"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)
    
    Iterator
      .continually((resultSet, resultSet.next()))
      .takeWhile(_._2)
      .map { case (rs, _) =>
        EmployeeModel(
          rs.getInt("id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getString("position"),
          rs.getDate("hire_date").toLocalDate,
          Option(rs.getDate("end_date")).map(_.toLocalDate)
        )
      }
      .toList
  }

  def findEmployeeById(id: Int): Future[Option[EmployeeModel]] = Future {
    val query = "SELECT * FROM employees WHERE id = ?"
    val statement = connection.prepareStatement(query)
    statement.setInt(1, id)
    val rs = statement.executeQuery()
    
    if (rs.next()) {
      Some(EmployeeModel(rs.getInt("id"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getString("position"),
          rs.getDate("hire_date").toLocalDate,
          Option(rs.getDate("end_date")).map(_.toLocalDate)
        )
      )
    } else {
      None
    }
  }

  def insertEmployee(employee: EmployeeModel): Future[Int] = Future {
    val query = "INSERT INTO employees (first_name, last_name, position, hire_date, end_date) VALUES (?, ?, ?, ?, ?) RETURNING id"
    val stmt = connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)
    stmt.setString(1, employee.first_name)
    stmt.setString(2, employee.last_name)
    stmt.setString(3, employee.position)
    stmt.setDate(4, java.sql.Date.valueOf(employee.hire_date))
    employee.end_date match {
      case Some(date) => stmt.setDate(5, java.sql.Date.valueOf(date))
      case None       => stmt.setNull(5, java.sql.Types.DATE)
    }
    stmt.executeUpdate()

    // Retrieve auto-generated id from db
    val resultSet = stmt.getGeneratedKeys
    if (resultSet.next()) resultSet.getInt(1) else 0
  }
}