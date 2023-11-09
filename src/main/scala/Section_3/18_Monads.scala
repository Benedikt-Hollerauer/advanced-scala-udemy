package Section_3

object Monads extends App
{
    trait Attempt[+A]
    {
        def flatMap[B](f: A => Attempt[B]): Attempt[B]
    }

    object Attempt
    {
        def apply[A](a: => A): Attempt[A] =
        {
            try
            {
                Success(a)
            } catch
            {
                case e: Throwable => Fail(e)
            }
        }
    }

    case class Success[+A](value: A) extends Attempt[A]
    {
        def flatMap[B](f: A => Attempt[B]): Attempt[B] =
        {
            try
            {
                f(value)
            } catch
            {
                case e: Throwable => Fail(e)
            }
        }
    }

    case class Fail(e: Throwable) extends Attempt[Nothing]
    {
        def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
    }

    // val attempt = Attempt
    // {
    //     throw new RuntimeException("My own monad, yes!")
    // }

    // println(attempt)

    /** Exercise:
     *
     *  1) implement a Lazy[T] monad = computation which will only be execute when it´s needed.
     *   unit/apply
     *   flatMap
     *
     *  2) Monads = unit + flatMap
     *    Monads = unit + map + flatten
     *
     *    Monad[T]
     *    {
     *      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
     *
     *      def map[B](f: T => B): Monad[B] = ???
     *      def flatten(m: Monad[Monad[T]]): Monad[T] = ???
 *        }
     *
     *    ( have List in mind )
     *
     */

    // 1)

    // My take:

    trait Lazy[+A]
    {
        def flatMap[B](f: A => Lazy[B]): Lazy[B]
    }

    object Lazy
    {
        def apply[A](a: => A): Lazy[A] =
        {
            ??? //LazilyEvaluated(a).flatMap(_)
        }
    }

    case class LazilyEvaluated[A](value: A) extends Lazy[A]
    {
        def flatMap[B](f: A => Lazy[B]): Lazy[B] = f(value)
    }

    // Solution

    class LazyS[+A](value: => A)
    {
        private val internalValue = value
        def use: A = internalValue
        def flatMap[B](f: A => LazyS[B]) = f(internalValue)
    }

    object LazyS
    {
        def apply[A](value: => A): LazyS[A] = new LazyS(value)
    }

    val lazyInstance = LazyS {
        println("test")
        42
    }

    val flatMappedInstance = lazyInstance.flatMap(x => LazyS{
        x * 10
    })

    val flatMappedInstance2 = lazyInstance.flatMap(x => LazyS{
        x * 10
    })

    flatMappedInstance.use
    flatMappedInstance2.use
}

