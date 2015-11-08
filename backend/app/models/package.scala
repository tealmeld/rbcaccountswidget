/**
 * Created by cravefm on 9/14/15.
 */
package object models {

  object JsonFormats {
    import play.api.libs.json.Json

    // Generates Writes and Reads for Feed and User thanks to Json Macros
    implicit val balanceFormat = Json.format[Balance]

  }

}
