error id: local1
file:///C:/ohjelmointi/Scala/scalaapi/app/controllers/EmployeeController.scala
empty definition using pc, found symbol in pc: 
found definition using semanticdb; symbol local1
empty definition using fallback
non-local guesses:

offset: 396
uri: file:///C:/ohjelmointi/Scala/scalaapi/app/controllers/EmployeeController.scala
text:
```scala
package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class EmployeeController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  def getEmployees() = Action { implicit request: Request[AnyContent] =>
    Ok("ee")
  }

  def insertEmployee() = Action { implicit request@@ =>
    Created("9")
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 