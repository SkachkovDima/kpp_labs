package sjumper

import scala.collection.mutable

class Statistics {
  val tempList: mutable.MutableList[String] = mutable.MutableList()
  val pCodeList: List[String] = List()

  def get(xs: mutable.MutableList[String], value: String): IndexedSeq[String] = {
    for (i <- 0 until xs.length if xs(i) == value) yield xs(i)
  }

  def add(element: String) {
    tempList.+=(element)
  }

  def getMoveUpCount(): Int = {
    get(tempList, "Up").size
  }

  def getMoveRightCount(): Int = {
    get(tempList, "Right").size
  }

  def getMoveLeftCount(): Int = {
    get(tempList, "Left").size
  }

  def getEasyCount(): Int = {
    get(tempList, "0").size
  }

  def getMediumCount(): Int = {
    get(tempList, "1").size
  }

  def getHardCount(): Int = {
    get(tempList, "2").size
  }

  def makeNotationX(action: Double): String = {
    action match {
      case 0 => "None"
      case x: Double if x > 0 => "Right"
      case x: Double if x < 0 => "Left"
    }
  }

  def makeNotationY(action: Double): String = {
    action match {
      case 0 => "None"
      case y: Double if y < 0 => "Jump"
      case y: Double if y > 0 => "Down"
    }
  }
//
//  def getPCode(n: Int): String = {
//    pCodeList(n)
//  }
}
