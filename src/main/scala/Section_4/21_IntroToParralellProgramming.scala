package Section_4

import java.util.concurrent.Executors

object IntroToParralellProgramming extends App
{
    val runnable = new Runnable
    {
        override def run(): Unit = println("on another thread.")
    }

    val aThread = new Thread(runnable)

    aThread.start()             // gives the signal to the JVM to start a JVM thread
    aThread.run()               // does´t do anything in parallel!
    aThread.join()              // blockt bis ein aThread fertig ist
    aThread.wait()              // lässt einen Thread warten,
    aThread.notify()            // bis man Ihn mit notify/notifyAll weiter laufen lässt
    aThread.notifyAll()

    val resource = new Object

    new Thread(() =>
    {
        resource.synchronized{
            println("this is synchronized")
        }
    }).start()

    new Thread(() =>
    {
        resource.synchronized{
            println("this is synchronized")
        }
    }).start()



    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
    val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
    // different runs produce different results!

    threadHello.start()
    threadGoodbye.start()

    // executors
    val pool = Executors.newFixedThreadPool(10)

    pool.execute(() => println("something in the thread pool"))

    pool.execute(() =>
    {
        Thread.sleep(1000)
        println("done after one second")
    })

    pool.execute(() =>
    {
        Thread.sleep(1000)
        println("almost done")
        Thread.sleep(2000)
        println("done after two second")
    })

    // pool.shutdown()

    // pool.execute(() => println("should not appear"))

    pool.shutdownNow()
}
