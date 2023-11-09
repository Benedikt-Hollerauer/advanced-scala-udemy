package Section_5

object Implicits extends App
{
    val pair: (String, String) = "Daniel" -> "555"
    val intPair: (Int, Int) = 1 -> 2

    case class Person(name: String)
    {
        def greet = s"Hi, my name is $name"
    }

    implicit def fromStringToPerson(string: String): Person = Person(string)

    println("Peter".greet)  // println(fromStringToPerson("Peter").greet)

    /*class A
    {
        def greet: Int = 2
    }

    implicit def fromStringToA(string: String): A = new A*/

    // implicit parameters
    def increment(x: Int)(implicit amount: Int): Int = x + amount
    implicit val defaultAmount: Int = 10

    increment(2)


}
