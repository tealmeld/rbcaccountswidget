package tables.seed

import java.text.SimpleDateFormat

import models.EntityBalance

/**
 *
 * Created by cravefm on 10/2/15.
 */
object InitBalances {

  val dateFormat = new SimpleDateFormat("MM/dd/yyyy")

  val list = List(
    EntityBalance(
        id = 1
      , account = "CHQ"
      , balance = 500000
      , balanceDiff = -10000
      , time = "5d9h"
    )
  ,
    EntityBalance(
      id = 1
      , account = "SAV"
      , balance = 132551045
      , balanceDiff = 1150000
      , time = "1d0h"
    )
  , EntityBalance(
    id = 1
    , account = "VISA"
    , balance = 499999
    , balanceDiff = 29
    , time = "15d0h"
    )
    , EntityBalance(
      id = 1
      , account = "MC"
      , balance = 1500
      , balanceDiff = -250000
      , time = "45d0h"
    )
  )

}
