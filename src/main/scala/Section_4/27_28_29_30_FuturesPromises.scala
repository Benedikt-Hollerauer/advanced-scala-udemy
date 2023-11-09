package Section_4

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._

object FuturesPromises extends App
{
    def calculateMeaningOfLife: Int =
    {
        Thread.sleep(2000)
        42
    }

    val aFuture = Future {
        calculateMeaningOfLife
    }

    println("Waiting on the future")

    aFuture.onComplete {
        case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
        case Failure(exception)     => println(s"I have failed with $exception")
    }

    Thread.sleep(3000)

    case class Profile(id: String, name: String)
    {
        def poke(anotherProfile: Profile): Unit =
        {
            println(
                s"${this.name} poking ${anotherProfile.name}"
            )
        }
    }

    object SocialNetwork
    {
        // database
        val names = Map(
            "fb.id.1-zuck"  -> "Mark",
            "fb.id.2-bill"  -> "Bill",
            "fb.id.0-dummy" -> "Dummy"
        )

        val friends = Map(
            "fb.id.1-zuck"  -> "fb.id.2-bill"
        )

        val random = new Random()

        // Api

        def fetchProfile(id: String): Future[Profile] =
        {
            Future{
                Thread.sleep(random.nextInt(300))
                Profile(id, names(id))
            }
        }

        def fetchBestFriend(profile: Profile): Future[Profile] =
        {
            Future{
                Thread.sleep(random.nextInt(400))
                val bestFriendId = friends(profile.id)
                Profile(bestFriendId, names(bestFriendId))
            }
        }

        // client: mark to poke bill

        val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
        /*mark.onComplete {
            case Failure(failure)       =>  failure.printStackTrace()
            case Success(markProfile)   =>  val bill = SocialNetwork.fetchBestFriend(markProfile)
                                            bill.onComplete {
                                                case Success(billProfile)   => markProfile.poke(billProfile)
                                                case Failure(failure)       => failure.printStackTrace()
                                            }
        }*/



        val nameOnTheWall = mark.map(profile => profile.name)
        val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
        val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

        for {
            mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
            bill <- SocialNetwork.fetchBestFriend(mark)
        } mark.poke(bill)

        Thread.sleep(1000)

        // fallbacks
        val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover{
            case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
        }

        val aFetchedProfileNotMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith{
            case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
        }

        val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

        case class User(name: String)
        case class Transaction(sender: String, receiver: String, amount: Double, status: String)

        object BankingApp {
            val name = "Rock the JVM banking"

            def fetchUser(name: String): Future[User] = Future {
                // simulate fetching from the DB
                Thread.sleep(500)
                User(name)
            }

            def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
                // simulate some processes
                Thread.sleep(1000)
                Transaction(user.name, merchantName, amount, "SUCCESS")
            }

            def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
                // fetch the user from the DB
                // create a transaction
                // WAIT for the transaction to finish
                val transactionStatusFuture = for {
                    user <- fetchUser(username)
                    transaction <- createTransaction(user, merchantName, cost)
                } yield transaction.status

                Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
            }
        }

        println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm store", 3000))

        // promises

        val promise = Promise[Int]() // "controller" over a future
        val future = promise.future

        // thread 1 - "consumer"
        future.onComplete {
            case Success(r) => println("[consumer] I've received " + r)
        }

        // thread 2 - "producer"
        val producer = new Thread(() => {
            println("[producer] crunching numbers...")
            Thread.sleep(500)
            // "fulfilling" the promise
            promise.success(42)
            println("[producer] done")
        })

        producer.start()
        Thread.sleep(1000)
    }

    val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")

    // functional composition of futures
    // map, flatMap, filter
    val nameOnTheWall = mark.map(profile => profile.name)
    val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
    val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))

    // for-comprehensions
    for {
        mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
        bill <- SocialNetwork.fetchBestFriend(mark)
    } mark.poke(bill)

    Thread.sleep(1000)

    // fallbacks
    val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
        case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
    }

    val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
        case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

    val fallbackResult =  SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

    // online banking app
    case class User(name: String)
    case class Transaction(sender: String, receiver: String, amount: Double, status: String)

    object BankingApp {
        val name = "Rock the JVM banking"

        def fetchUser(name: String): Future[User] = Future {
            // simulate fetching from the DB
            Thread.sleep(500)
            User(name)
        }

        def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
            // simulate some processes
            Thread.sleep(1000)
            Transaction(user.name, merchantName, amount, "SUCCESS")
        }

        def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
            // fetch the user from the DB
            // create a transaction
            // WAIT for the transaction to finish
            val transactionStatusFuture = for {
                user <- fetchUser(username)
                transaction <- createTransaction(user, merchantName, cost)
            } yield transaction.status

            Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
        }
    }

    println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm store", 3000))

    // promises

    val promise = Promise[Int]() // "controller" over a future
    val future = promise.future

    // thread 1 - "consumer"
    future.onComplete {
        case Success(r) => println("[consumer] I've received " + r)
    }

    // thread 2 - "producer"
    val producer = new Thread(() => {
        println("[producer] crunching numbers...")
        Thread.sleep(500)
        // "fulfilling" the promise
        promise.success(42)
        println("[producer] done")
    })

    producer.start()
    Thread.sleep(1000)

    /*
        1) fulfill a future IMMEDIATELY with a value
        2) inSequence(fa, fb)
        3) first(fa, fb) => new future with the first value of the two futures
        4) last(fa, fb) => new future with the last value
        5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
    */

    // 1)

    def fulfillImmediately(value: Int): Future[Int] = Future(value)

    // 2)

    def inSequence[A, B](fa: Future[A], fb: Future[B]): Future[B] =
    {
        fa.flatMap(_ => fb)
    }

    def fb: Future[Int] = Future{

        println("[fb] running now...")
        42
    }

    def fa: Future[Int] = Future{

        println("[fa] finished...")
        42
    }

    inSequence(fa, fb)

    // 3

    def first[A](fa: Future[A], fb: Future[A]): Future[A] =
    {
        val promise: Promise[A] = Promise[A]

        fa.onComplete(promise.tryComplete)
        fb.onComplete(promise.tryComplete)

        promise.future
    }

    // 4

    def last[A](fa: Future[A], fb: Future[A]): Future[A] =
    {
        val bothPromise = Promise[A]
        val lastPromise = Promise[A]
        val checkAndComplete = (result: Try[A]) => if(!bothPromise.tryComplete(result)) lastPromise.complete(result)

        fa.onComplete(checkAndComplete)
        fb.onComplete(checkAndComplete)

        lastPromise.future
    }

    val fast = Future{
        Thread.sleep(100)
        42
    }

    val slow = Future{
        Thread.sleep(200)
        45
    }

    first(fast, slow).foreach(println)
    last(fast, slow).foreach(println)

    Thread.sleep(1000)

    // 5

    def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] =
    {
        action()
            .filter(condition)
            .recoverWith{
                case _ => retryUntil(action, condition)
            }
    }

    val random = new Random()
    val action = () => Future{
        Thread.sleep(100)
        val nextValue = random.nextInt(100)
        println("generated " + nextValue)
        nextValue
    }

    retryUntil(action, (x: Int) => x < 50).foreach(result => println("settled at " + result))

    Thread.sleep(10000)
}