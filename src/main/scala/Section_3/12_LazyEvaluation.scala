package Section_3

object LazyEvaluation extends App
{
	// lazy val x: Int = throw new RuntimeException


	lazy val x: Int =
	{
		println("hello")
		42
	}

	println(x)
	println(x)

	// examples
	// side effects
	def sideEffectCondition: Boolean =
	{
		println("Boo")
		true
	}

	def simpleCondition: Boolean = true

	lazy val lazyCondition = sideEffectCondition
	println(if (simpleCondition && lazyCondition) "yes"
	else "No")

	// in conjunction with call by name
	def byNameMethod(n: => Int): Int =
	{
		lazy val t = n
		t + t + t + 1
	}

	def retrieveMagicValue: Int =
	{
		println("waiting...")
		Thread.sleep(1000)
		42
	}

	// use lazy val´s
	println(byNameMethod(retrieveMagicValue))

	// filtering with lazy val´s
	def lessThan30(i: Int): Boolean =
	{
		println(s"$i is less than 30?")
		i < 30
	}

	def greaterThan20(i: Int): Boolean =
	{
		println(s"$i is greater than 20?")
		i > 20
	}

	val numbers: List[Int] = List(1, 25, 40, 5, 23)
	val lt30: List[Int] = numbers.filter(lessThan30)
	val gt20: List[Int] = lt30.filter(greaterThan20)

	println(gt20)

	val lt30Lazy = numbers.withFilter(lessThan30)
	val gt20Lazy = lt30Lazy.withFilter(greaterThan20)

	println
	gt20Lazy.foreach(println)

	// for-comprehensions use withFilter with guards

	for
	{
		a	<-	List(1, 2, 3) if(a % 2 == 0)
	} yield(a + 1)

	List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1)	// List[Int]
}
