package tables

import slick.lifted.TableQuery
import tables.rows.BalanceRow


/**
 *
 * Created by setrar on 9/15/15.
 */
object Tables {

  val Balances = TableQuery[BalanceRow]

}

