package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import java.time.LocalDate
import play.api.data.format.Formats._
import play.api.data.Forms._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}

import dao.EmployeeDAO
import models.EmployeeModel
import config.DatabaseConnection

@Singleton
class EmployeeController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext)
    extends BaseController
    with I18nSupport {

  val employeeForm: Form[EmployeeModel] = Form(
    mapping(
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
      "position" -> nonEmptyText,
      "hire_date" -> localDate("yyyy-MM-dd"),
      "end_date" -> optional(localDate("yyyy-MM-dd"))
    ) { case (first_name, last_name, position, hire_date, end_date) =>
      EmployeeModel(0, first_name, last_name, position, hire_date, end_date)
    } { employee =>
      Some(
        (
          employee.first_name,
          employee.last_name,
          employee.position,
          employee.hire_date,
          employee.end_date
        )
      )
    }
  )

  def listEmployees(): Action[AnyContent] = Action.async { implicit request =>
    DatabaseConnection.connection match {
      case Success(conn) =>
        val employeeDAO = new EmployeeDAO(conn)
        val startTime = System.nanoTime()

        employeeDAO.getAllEmployees
          .map { employees =>
            val endTime = System.nanoTime()
            val elapsedSeconds = (endTime - startTime) / 1e9
            Ok(views.html.employees(employees, employeeForm, elapsedSeconds))
          }
          .recover { case ex: Exception =>
            BadRequest(s"Error: ${ex.getMessage}")
          }

      case Failure(exception) =>
        Future.successful(BadRequest(s"Error: ${exception.getMessage}"))
    }
  }

  def getEmployee(id: Int): Action[AnyContent] = Action.async {
    DatabaseConnection.connection match {
      case Success(conn) =>
        val employeeDAO = new EmployeeDAO(conn)
        employeeDAO
          .findEmployeeById(id)
          .map {
            case Some(emp) =>
              Ok(
                s"Employee found: ID=${emp.id}, First name=${emp.first_name}, Lastname=${emp.last_name}, Position=${emp.position}"
              )
            case None => NotFound(s"No employee found with ID $id")
          }
          .recover { case ex: Exception =>
            BadRequest(s"Error: ${ex.getMessage}")
          }

      case Failure(exception) =>
        Future.successful(BadRequest(s"Error: ${exception.getMessage}"))
    }
  }

  def submitInsertForm(): Action[AnyContent] = Action.async {
    implicit request =>
      employeeForm.bindFromRequest.fold(
        formWithErrors => {
          DatabaseConnection.connection match {
            case Success(conn) =>
              val employeeDAO = new EmployeeDAO(conn)
              val startTime = System.nanoTime()

              employeeDAO.getAllEmployees
                .map { employees =>
                  val endTime = System.nanoTime()
                  val elapsedSeconds = (endTime - startTime) / 1e9
                  BadRequest(views.html.employees(employees, formWithErrors, elapsedSeconds))
                }
                .recover { case ex: Exception =>
                  InternalServerError(s"Database error: ${ex.getMessage}")
                }

            case Failure(exception) =>
              Future.successful(
                BadRequest(
                  s"Database connection error: ${exception.getMessage}"
                )
              )
          }
        },
        employeeData => {
          DatabaseConnection.connection match {
            case Success(conn) =>
              val employeeDAO = new EmployeeDAO(conn)
              employeeDAO
                .insertEmployee(employeeData)
                .map { id =>
                  Redirect(routes.EmployeeController.listEmployees())
                }
                .recover { case ex: Exception =>
                  InternalServerError(s"Database error: ${ex.getMessage}")
                }

            case Failure(exception) =>
              Future.successful(
                BadRequest(
                  s"Database connection error: ${exception.getMessage}"
                )
              )
          }
        }
      )
  }
}
