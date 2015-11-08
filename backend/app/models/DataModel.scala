package models

/**
 *
 * Created by setrar on 10/17/15.
 */

trait TId {
  def id: Long
}

trait TBalance{
  def account: String
  def balance: Long
  def balanceDiff: Long
  def time: String
}