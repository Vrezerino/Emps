package models

import play.api.libs.json.{Json, OFormat}
import java.time.LocalDate
import play.api.data.format.Formats._

case class EmployeeModel(
    id: Int,
    first_name: String,
    last_name: String,
    position: String,
    hire_date: LocalDate,
    end_date: Option[LocalDate]
)

object EmployeeModel {
  implicit val employeeFormat: OFormat[EmployeeModel] =
    Json.format[EmployeeModel]
}
