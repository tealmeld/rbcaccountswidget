package tables.rows

import models.EntityBalance
import slick.driver.MySQLDriver.api._
import slick.lifted.Tag

/**
 *
 * Created by cravefm on 10/17/15.
 */

class BalanceRow(tag: Tag) extends Table[EntityBalance](tag, "Balances") {

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def account = column[String]("Account")
  def balance = column[Long]("Balance")
  def balanceDiff = column[Long]("BalanceDiff")
  def time = column[String]("Time")

//  def idx = index("idx_n123", number, unique = true)
//  Error: BLOB/TEXT column 'BandName' used in key specification without a key length

  def * = (id, account, balance, balanceDiff, time) <> (EntityBalance.tupled, EntityBalance.unapply)

}


