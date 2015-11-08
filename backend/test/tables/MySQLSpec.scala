package tables

import org.flywaydb.core.Flyway
import org.scalatest.{BeforeAndAfterAll, Suite}

trait MySQLSpec extends Suite with BeforeAndAfterAll{

  RecreateSchema.init
  override def beforeAll() {
  }

}

object RecreateSchema {

  lazy val init = {

    val flyway = new Flyway()
    flyway.setDataSource(Slick.ds)
    flyway.clean()

    InitDatabase.seedTestData()

  }

}

