package dao

import java.sql.Connection
import scala.concurrent.{Future, ExecutionContext}
import models.EmployeeModel

class EmployeeDAO(connection: Connection)(implicit ec: ExecutionContext) {

  def getAllEmployees: Future[Seq[EmployeeModel]] = Future {
    val query = "SELECT id, first_name, last_name, position FROM employees"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)
    
    Iterator
      .continually((resultSet, resultSet.next()))
      .takeWhile(_._2)
      .map { case (rs, _) =>
        EmployeeModel(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("position"))
      }
      .toList
  }

  def findEmployeeById(id: Int): Future[Option[EmployeeModel]] = Future {
    val query = "SELECT id, first_name, last_name, position FROM employees WHERE id = ?"
    val statement = connection.prepareStatement(query)
    statement.setInt(1, id)
    val resultSet = statement.executeQuery()
    
    if (resultSet.next()) {
      Some(EmployeeModel(resultSet.getInt("id"), resultSet.getString("first_name"), 
      resultSet.getString("last_name"), resultSet.getString("position")))
    } else {
      None
    }
  }

  def insertEmployee(employee: EmployeeModel): Future[Int] = Future {
    val query = "INSERT INTO employees (first_name, last_name, position) VALUES (?, ?, ?) RETURNING id"
    val statement = connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)
    statement.setString(1, employee.first_name)
    statement.setString(2, employee.last_name)
    statement.setString(3, employee.position)
    statement.executeUpdate()

    // Retrieve auto-generated id from db
    val resultSet = statement.getGeneratedKeys
    if (resultSet.next()) resultSet.getInt(1) else 0
  }
}