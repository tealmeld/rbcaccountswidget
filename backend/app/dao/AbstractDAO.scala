package dao

import org.slf4j.LoggerFactory
import tables.Slick

abstract class AbstractDAO {

  lazy val db = Slick.db

  lazy val logger = LoggerFactory.getLogger(getClass.getName)


}