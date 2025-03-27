error id: 
file:///C:/ohjelmointi/Scala/scalaapi/app/controllers/EmployeeController.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 46
uri: file:///C:/ohjelmointi/Scala/scalaapi/app/controllers/EmployeeController.scala
text:
```scala
package controllers

import javax.inject._
@@import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class EmployeeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  def getEmployees() = Action { implicit request: Request[AnyContent] =>
    Ok("ee")
  }

  def insertEmployee() = Action { implicit request: Request[AnyContent] =>
    Created("9")
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 