package Section_5

object PimpMyLibrary extends App
{
    // 2.isPrime would be cool syntax

    implicit class RichInt(val value: Int) extends AnyVal
    {
        def isEven: Boolean = value % 2 == 0
        def sqrt: Double = Math.sqrt(value)

        def times(function: () => Unit): Unit =
        {
            def timesAux(n: Int): Unit =
            {
                if(n <= 0) ()
                else{
                    function()
                    timesAux(n - 1)
                }
            }

            timesAux(value)
        }
    }


    /*implicit class RicherInt(richInt: RichInt)
    {
        def isOdd: Boolean = richInt.value % 2 == 0
    }*/

    // compiler doesn´t do multiple implicit searches.
    // 42.isOdd <- this is not possible

    // you could do like this:
    new RichInt(42).isEven

    // or like this
    42.isEven

    1 to 10 // this is this implicit conversion

    import scala.concurrent.duration._
    3.seconds

    /**
     *  Exercise:
     *
     *  Enrich the String class
     *  - asInt
     *  - encrypt
     *      "John" -> Lnjp
     *  Keep enriching the Int class
     *  - times(function)
     *      2.times(() => ...)
     *  - *
     *      3 * List(1, 2) => List(1, 2, 1, 2, 1, 2)
     */

    implicit class RichString(string: String) extends AnyVal
    {
        def asInt: Int = Integer.valueOf(string)
        def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
    }

    println("3".asInt + 5)
    println("moin".encrypt(5))
    3.times(() => println("Scala Rocks!"))

    // "3" / 4
    implicit def stringToInt(string: String): Int = Integer.valueOf(string)

    println("6" / 3)
}