val str = "hello world"
val search = "dlrow"

def index(str : String, search : String, i : Int): Int ={
    if(i == str.length - search.length) -1
    if (str.substring(i, i + search.length).equals(search)) i else index(str, search, i + 1)
}

index(str, search.reverse, 0)

val start = 2000
val end = 2016

def countYears(start : Int, end : Int, count : Int): Int = {
  val a = Range(start, end + 1).count(x => x % 100 == 0 && x % 400 == 0)
  Range(start, end + 1).count(x => x % 4 == 0 && x % 100 != 0) + a
}

countYears(start, end, 0)

def swapElements(list : List[Int], i : Int, solution : List[Int]): List[Int] = {
  if(i <= 0) solution else swapElements(list, i - 2, list(i)::list(i-1)::solution)
}

val list = 1::2::4::3::7::2::Nil
swapElements(list, list.length - 1, Nil)