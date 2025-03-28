package models

import play.api.libs.json.{Json, OFormat}

case class EmployeeModel(id: Int, first_name: String, last_name: String, position: String)

object EmployeeModel {
  implicit val employeeFormat: OFormat[EmployeeModel] = Json.format[EmployeeModel]
}