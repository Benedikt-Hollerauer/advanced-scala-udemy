package Section_2

object AdvancesPatternMatching extends App
{
    val numbers = List(1)

    val description = numbers.match
    {
        case head :: Nil    => println(s"the only element is $head.")
        case _              =>
    }

    class Person(val name: String, val age: Int)

    object Person
    {
        def unapply(person: Person): Option[(String, Int)] =
        {
            if(person.age < 18) None
            else Some(person.name, person.age)
        }

        def unapply(age: Int): Option[String] = {
            Some(
                if(age < 18) "minor"
                else "major"
            )
        }
    }
    
    val bob = new Person(name = "Bob", age = 18)
    val greeting = bob.match
    {
        case Person(n, a) => s"Hi, my name is $n, and Iam $a years old"
    }

    println(greeting)

    val legalStatus = bob.age.match
    {
        case Person(status) => s"my legal status is $status"
    }

    println(legalStatus)

    // Exercise

    // my take:

    //class Matching(number: Int)
    //
    //object Matching
    //{
    //    def unapply(matching: Matching): Option[Int] =
    //    {
    //        if(matching.number == )
    //    }
    //}

    // solution

    object even // this matching objects should start with a small letter
    {
        def unapply(number: Int): Boolean =
        {
            number % 2 == 0
        }
    }

    object singleDigit
    {
        def unapply(number: Int): Boolean =
        {
            number < 10
        }
    }

    val number = 2
    val matchedNumber = number.match
    {
        case even()        => "the number is even"
        case singleDigit() => "the number has only one digit"
        case _              => "nothing is true on this number"
    }

    println(matchedNumber)

    case class Or[A, B](a: A, b: B)

    val either = Or[Int, String](a = 2, b = "two")

    val matching = either.match
    {
        // case Or(number, string) => s"$number is written the same as $string" // is the same as:
        case number Or string   => s"$number is written the same as $string"
    }

    println(matching)
}
