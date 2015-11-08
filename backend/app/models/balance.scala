package models

case class Balance(
    account: String
  , balance: Long
  , balanceDiff: Long
  , time: String) extends TBalance

case class EntityBalance(
  id: Long
  , account: String
  , balance: Long
  , balanceDiff: Long
  , time: String) extends TBalance with TId
