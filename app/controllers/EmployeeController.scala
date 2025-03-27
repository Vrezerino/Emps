package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import dao.EmployeeDAO
import models.EmployeeModel
import config.DatabaseConnection

@Singleton
class EmployeeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

def listEmployees(): Action[AnyContent] = Action {
    DatabaseConnection.connection match {
      case scala.util.Success(conn) =>
        val employeeDAO = new EmployeeDAO(conn)
        val employees = employeeDAO.getAllEmployees
        Ok(views.html.employees(employees))
      case scala.util.Failure(exception) =>
        BadRequest(s"Error: ${exception.getMessage}")
    }
  }

  def getEmployee(id: Int): Unit = {
    DatabaseConnection.connection match {
      case scala.util.Success(conn) =>
        val employeeDAO = new EmployeeDAO(conn)
        val employeeOpt = employeeDAO.findEmployeeById(id)
        employeeOpt match {
          case Some(emp) => println(s"Employee found: ID=${emp.id}, Name=${emp.name}, Position=${emp.position}")
          case None      => println(s"No employee found with ID $id")
        }
      case scala.util.Failure(exception) =>
        println(s"Error: ${exception.getMessage}")
    }
  }

  def insertEmployee() = Action { implicit request: Request[AnyContent] =>
    Created("9")
  }
}
