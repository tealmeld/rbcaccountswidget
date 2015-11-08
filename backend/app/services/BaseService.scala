package services

import org.slf4j.LoggerFactory

trait BaseService {
  lazy val logger = LoggerFactory.getLogger(getClass.getName)

}

