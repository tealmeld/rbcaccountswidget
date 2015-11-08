package tables

import org.flywaydb.core.Flyway
import com.typesafe.config.ConfigFactory
import services.{ConfigService, AuthService, BaseService}
import scala.io

object Slick extends BaseService {

  private class DataSource(rdbms: String) {
    lazy val ds: javax.sql.DataSource = db.source.asInstanceOf[slick.jdbc.hikaricp.HikariCPJdbcDataSource].ds
    val rdbmsConfig = "rdbms"

    lazy val db = {
      logger.info(s"Setting up DB for rdbms $rdbms")
      val db = slick.driver.MySQLDriver.api.Database.forConfig("", config)
//      val flyway = new Flyway()
//      flyway.setDataSource(ds)
//      AuthService.getAppRealmVersion(rdbms, flyway) match {
//        case AuthService.Version(_, Some(dbVersion), Some(appDBVersion)) if appDBVersion != dbVersion =>
//          throw new Exception(
//            s"App DBVersion:%s and DBVersion: %s are not in sync.".format(dbVersion, appDBVersion)
//          )
//        case _=> // Either there are no version info yet, or the db and appdbVersion are the same
//      }
      db
    }

    lazy val config = {
      val config = ConfigFactory.load().getConfig(rdbmsConfig).getConfig(rdbms)
      val pwdOrFile = config.getString("properties.password")
      val pwd = if (!pwdOrFile.startsWith("file://")) pwdOrFile else {
        logger.info(s"Reading DB password from $pwdOrFile")
        io.Source.fromFile(pwdOrFile.drop("file://".length)).getLines.take(1).mkString
      }
      config
        .withValue("properties.password", com.typesafe.config.ConfigValueFactory.fromAnyRef(pwd))
        .withValue("dataSourceClass", com.typesafe.config.ConfigValueFactory.fromAnyRef("com.mysql.jdbc.jdbc2.optional.MysqlDataSource"))
    }

  }

  def shutdown() = {
    logger.info(s"Shutting down DB connections...")
    import concurrent._
    import duration._
    import ExecutionContext.Implicits.global
    Await.ready(Future.sequence(dbMap.values.map(_.db.shutdown)), Duration.Inf)
  }

  def db = rds.db
  def ds = rds.ds
  def config = rds.config

  private val dbMap = collection.mutable.Map.empty[String, DataSource]

  private def rds = {
    getDataSource(ConfigService.currentInstanceName)
  }

  private def getDataSource(instance: String) = {
    dbMap.get(instance) match {
      case Some(realmDS) => realmDS
      case None =>
        // only synchronize on a miss
        dbMap.synchronized {
          dbMap.get(instance) match {
            case Some(r) => r
            case None =>
              val realmDS = new DataSource(instance)
              dbMap += (instance -> realmDS)
              realmDS
          }
        }
    }
  }

}

