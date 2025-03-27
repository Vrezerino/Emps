package dao

import java.sql.Connection
import scala.util.{Try, Success, Failure}
import models.EmployeeModel

class EmployeeDAO(connection: Connection) {
  def getAllEmployees: Seq[EmployeeModel] = {
    val query = "SELECT id, name, position FROM employees"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(query)

    // Build a list of Employee objects
    Iterator
      .continually((resultSet, resultSet.next))
      .takeWhile(_._2)
      .map { case (rs, _) =>
        EmployeeModel(rs.getInt("id"), rs.getString("name"), rs.getString("position"))
      }
      .toList
  }

  def findEmployeeById(id: Int): Option[EmployeeModel] = {
    val query = s"SELECT id, name, position FROM employees WHERE id = ?"

    val statement = connection.prepareStatement(query)
    statement.setInt(1, id)

    val resultSet = statement.executeQuery()

    if (resultSet.next()) {
      Some(EmployeeModel(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("position")))
    } else {
      None
    }
  }
}