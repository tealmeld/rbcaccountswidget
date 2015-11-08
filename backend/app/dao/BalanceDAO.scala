package dao

import models.{EntityBalance, Balance}
import tables.Tables.{Balances}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

class BalanceDAO extends AbstractDAO {

  def all(): Future[List[Balance]] = {
    for (balances <- db.run(Balances.result).map(_.toList)) yield
      for (balance <- balances) yield {
        Balance(
          account = balance.account
          , balance = balance.balance
          , balanceDiff = balance.balanceDiff
          , time = balance.time
        )
      }
  }

    def insert(balance: Balance): Future[Unit] = {
      db.run(Balances += EntityBalance(
        id = 1
        , account = balance.account
        , balance = balance.balance
        , balanceDiff = balance.balanceDiff
        , time = balance.time)).map(_ => ())
    }

    def find(account: String): Future[Option[Balance]] = {
      for {
        a <- db.run(Balances.filter(_.account === account).result.headOption)
      } yield
        a match {
          case Some(b) =>
            Some(Balance(
              account = b.account
              , balance = b.balance
              , balanceDiff = b.balanceDiff
              , time = b.time
            ))
          case None => None
        }
    }

    def update(balance: Balance): Future[Int] = db.run {
      Balances.filter(_.account === balance.account).map(b => (b.account, b.balance, b.balanceDiff, b.time)).update((balance.account, balance.balance, balance.balanceDiff, balance.time))
    }

    def delete(balance: Balance): Future[Int] = db.run {
      Balances.filter(_.account === balance.account).delete
    }

}