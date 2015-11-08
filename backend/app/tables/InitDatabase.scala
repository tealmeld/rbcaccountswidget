package tables

/**
 *
 * Created by cravefm on 10/2/15.
 */

import javax.sql.DataSource

import com.typesafe.config.ConfigFactory
import org.flywaydb.core.Flyway
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io

object InitDatabase extends App {

  val doSeed = args.contains("--seed")
  val doInit = args.contains("--init")
  val rdbms = args.filter(!_.startsWith("-"))

  if (doInit) rdbms.foreach{ x => setupBaseLine(setupDatabase(x)) }
  if (doSeed) rdbms.foreach(seedTestData)

  def seedTestData(realm: String): Unit = {
      seedTestData()
  }

  def seedTestData() = {
    val actions = for {
      _ <- Slick.db.run {DBIO.seq(
          Tables.Balances.schema.create
        , Tables.Balances ++= tables.seed.InitBalances.list
      )
      }
    } yield ()
    Await.ready(actions, Duration.Inf)
  }

  def setupDatabase(instance: String): DataSource = {

    lazy val config = {
      val config = ConfigFactory.load().getConfig("rdmbs").getConfig(instance)
      val pwdOrFile = config.getString("properties.password")
      val pwd = if (!pwdOrFile.startsWith("file://")) pwdOrFile
      else {
        io.Source.fromFile(pwdOrFile.drop("file://".length)).getLines().take(1).mkString
      }
      config
        .withValue("properties.password", com.typesafe.config.ConfigValueFactory.fromAnyRef(pwd))
        .withValue("dataSourceClass", com.typesafe.config.ConfigValueFactory.fromAnyRef("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"))
    }

    val db = slick.driver.MySQLDriver.api.Database.forConfig("", config)

    val ds: javax.sql.DataSource = db.source.asInstanceOf[slick.jdbc.hikaricp.HikariCPJdbcDataSource].ds
    ds

  }

  def setupBaseLine(ds: DataSource): Unit = {

    val flyway = new Flyway()

      flyway.setDataSource(ds)

      val info = flyway.info
      val latestVersion = if (info.all.nonEmpty)
        info.all.last.getVersion.getVersion
      else
        flyway.getBaselineVersion.getVersion

      flyway.setBaselineOnMigrate(true)
      flyway.setBaselineVersionAsString(latestVersion)
      flyway.baseline()

    }

}

