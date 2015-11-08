package tables

import org.scalatest.FunSpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import dao.{BalanceDAO}

class BalancesUnitTest extends FunSpec with MySQLSpec with ScalaFutures {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(5, Seconds))

  val balanceDao = new BalanceDAO

  describe("Balance testing") {

    it ("should find a Balance") {
//      val all = balanceDao.all().futureValue
//      assert(all.size>1)
    }
  }

}