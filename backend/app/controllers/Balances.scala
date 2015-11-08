package controllers

import javax.inject.{Inject, Singleton}

import dao.BalanceDAO
import models.Balance
import org.slf4j.{Logger, LoggerFactory}
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * The Artists controllers encapsulates the Rest endpoints and the interaction with the Slick, via ReactiveSlick
 * play plugin.
 */
@Singleton
class Balances @Inject()(val balancesDAO: BalanceDAO)
  extends Controller {

  private final val logger: Logger = LoggerFactory.getLogger(classOf[Balances])

  // ------------------------------------------ //
  // Using case classes + Json Writes and Reads //
  // ------------------------------------------ //

  import models.JsonFormats._

  def createBalance:Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[Balance].map {
        balance =>
          balancesDAO.insert(balance = Balance(
              account = balance.account
            , balance = balance.balance
            , balanceDiff = balance.balanceDiff
            , time = balance.time)).map {
            lastError =>
              logger.debug(s"Successfully inserted with LastError: $lastError")
              Created(s"Artist Created")
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))

  }

  def updateBalance(account: String): Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[Balance].map {
        balance =>
            balancesDAO.find(balance.account).flatMap {
              case Some(x) =>
                val bal = Balance(
                  account = balance.account
                  , balance = balance.balance
                  , balanceDiff = balance.balanceDiff
                  , time = balance.time)
              balancesDAO.update(balance = bal).map {
                updateCount =>
                  if (updateCount == 1) {
                    logger.debug(s"Successfully updated with updateCount: $updateCount")
                    Created(s"Balance Updated")
                  } else {
                    logger.debug(s"Successfully updated with LastError: $updateCount")
                    BadRequest(s"Balance $account not Updated")
                  }
              }
              case None => Future.successful(BadRequest("Balance not found"))
            }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteBalance(account: String): Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[Balance].map {
        balance =>
          balancesDAO.find(balance.account).flatMap {
            case Some(x) =>
              val bal = Balance(
                account = balance.account
                , balance = balance.balance
                , balanceDiff = balance.balanceDiff
                , time = balance.time)
              balancesDAO.delete(balance = bal).map {
                updateCount =>
                  if (updateCount == 1) {
                    logger.debug(s"Successfully deleted with updateCount: $updateCount")
                    Created(s"Balance Deleted")
                  } else {
                    logger.debug(s"Successfully deleted with LastError: $updateCount")
                    BadRequest(s"Balance $account not Deleted")
                  }
              }
            case None => Future.successful(BadRequest("Balance not found"))
          }
      }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def findBalances:Action[AnyContent] = Action.async {
    // gather all the JsObjects in a list
    for {
      records <-
        for (balances <- balancesDAO.all()) yield
        for (balance <- balances) yield
          Balance(
            account = balance.account
            , balance = balance.balance
            , balanceDiff = balance.balanceDiff
            , time = balance.time)
    } yield Ok(toJson(records))
  }

}
