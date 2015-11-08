package services

import org.flywaydb.core.Flyway


object AuthService extends BaseService {

  case class Version(realm: String, dbVersion: Option[String], appDBVersion: Option[String])

  def getAppRealmVersion (realm: String, flyway: Flyway) : Version = {
    val info = flyway.info()
    val dbVersion = info.applied.lastOption.map(_.getVersion.getVersion)
    val appDBVersion = info.all.lastOption.map(_.getVersion.getVersion)
    Version(realm, dbVersion, appDBVersion)
  }

}
