package Section_6

object RockingInheritance extends App
{
    // convenience
    trait Writer[T]
    {
        def write(value: T): Unit
    }

    trait Closeable
    {
        def close(status: Int): Unit
    }

    trait GenericStream[T]
    {
        // some methods here
        def foreach(f: T => Unit): Unit
    }

    def processStream[T](stream: GenericStream[T] with Writer[T] with Closeable): Unit =
    {
        stream.foreach(println)
        stream.close(0)
    }
}