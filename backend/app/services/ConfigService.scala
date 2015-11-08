package services

import com.typesafe.config.ConfigFactory

/**
 *
 */
object ConfigService extends BaseService {

  private lazy val rootConfig = ConfigFactory.load()

  val currentInstanceName = "rbcwidget"

  def getConfigNames(name: String) : List[String] = {
    import collection.JavaConversions._
    rootConfig.getConfig(name).root().keySet.toList
  }

}
