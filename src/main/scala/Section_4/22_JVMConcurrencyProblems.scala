package Section_4

import scala.annotation.tailrec

object JVMConcurrencyProblems
{
	def main(args: Array[String]): Unit =
	{
		threadInception(10000)
	}

	def runInParallel(): Unit =
	{
		var x = 0

		val thread1 = new Thread(() => (x = 1))
		val thread2 = new Thread(() => (x = 2))

		thread1.start()
		thread2.start()

		println(x)
	}

	case class BankAccount(var amount: Int)

	def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = bankAccount.amount -= price

	def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit =
	{
		bankAccount.synchronized(
			bankAccount.amount -= price
		)
	}

	def demoBankingProblem(): Unit =
	{
		(1 to 10000).foreach
		{
			val account = BankAccount(50000)
			val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
			val thread2 = new Thread(() => buySafe(account, "iphone", 4000))

			thread1.start()
			thread2.start()
			thread1.join()
			thread2.join()

			if(account.amount != 43000) println(s"AHA! I´ve just broken the bank: ${account.amount}")
		}
	}

	/**	Exercises
	 * 	1 - create "inception threads"
	 * 		thread1
	 * 			-> thread2
	 * 				-> thread3
	 * 					...
	 * 		each thread prints "hello from thread $i"
	 * 		print all messages(threads) IN REVERSE ORDER
	 *
	 * 	2 - what´s the max/min value of x
	 *
	 * 	3 - "sleep fallacy"
	 */

	// 1 ---------------------------------------------------------------------------------------------------------------

	// my Take

	@tailrec
	def threadInception(count: Int): Unit =
	{
		if(count == 0) ()
		else
		{
			val threads = new Thread(() => println(s"Hello, from thread $count" + s""))
			threads.start()
			threads.join()
			threadInception(count - 1)
		}
	}

	// solution

	def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
	{
		new Thread(() =>
		{
			if(i < maxThreads)
			{
				val newThread = inceptionThreads(maxThreads, i + 1)
				newThread.start()
				newThread.join()
			}
			println(s"Hello, from thread $i")
		})
	}

    // 2 ---------------------------------------------------------------------------------------------------------------

	def minMax(): Unit =
	{
		var x = 0
		val threads = (1 to 100).map(_ => new Thread(() => x += 1))

		threads.foreach(_.start())
	}

	// 3 ---------------------------------------------------------------------------------------------------------------

	def demoSleepFallacy(): Unit =
	{
		var message = ""
		val awesomeThread = new Thread(() =>
		{
			Thread.sleep(1000)
			message = "Scala is awesome"
		})

		message = "Scala sucks"
		awesomeThread.start()
		awesomeThread.join()
		Thread.sleep(1001)
		println(message)
	}
}
