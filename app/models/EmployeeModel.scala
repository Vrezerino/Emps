package models

import play.api.libs.json.{Json, OFormat}

case class EmployeeModel(id: Int, name: String, position: String)

object EmployeeModel {
  implicit val employeeFormat: OFormat[EmployeeModel] = Json.format[EmployeeModel]
}