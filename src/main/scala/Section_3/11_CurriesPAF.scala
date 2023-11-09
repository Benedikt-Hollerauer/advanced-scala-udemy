package Section_3

object CurriesPAF extends App
{
	val superAdder: Int => Int => Int = x => y => x + y
	val add3 = superAdder(3) // Int => Int = 3 + y

	println(add3(5))
	println(superAdder(3)(5))

	// METHOD!
	def curriedAdder(x: Int)(y: Int): Int = x + y

	// lifting = ETA-Expansion
	val add4: Int => Int = curriedAdder(4)

	def inc(x: Int): Int = x + 1
	List(1, 2, 3).map(inc) // === List(1,2,3).map(x => inc(x)) <- Automatic ETA-Expansion

	val add5 = curriedAdder(5) _

	// Exercise
	val simpleAddFunction = (x: Int, y: Int) => x + y
	def simpleAddMethod(x: Int, y: Int): Int = x + y
	def curriedAddMethod(x: Int)(y: Int): Int = x + y

	// add7: Int => Int = y => 7 + y
	val add7 = (x: Int) => simpleAddMethod(7, x)
	val add7_1 = simpleAddMethod.curried(7)
	val add7_2 = curriedAddMethod(7)
	val add7_3 = curriedAddMethod(7)(_)
	val add7_4 = simpleAddMethod(7, _: Int)

	println(add7(5))
	println(add7_1(5))
	println(add7_2(5))
	println(add7_3(5))
	println(add7_4(5))

	def concatenator(a: String, b: String, c: String): String = a + b + c
	val insertName = concatenator("Hello, I´m ", _: String, ", how are you?")
	println(insertName("Bene"))

	val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)
	println(fillInTheBlanks("Bene", " Scala is awesome"))

	// Exercises
	/**
	 * 1. Process a list of numbers and return their string representations with different formats
	 * Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
	 * 	- Formatting: println("%8.6.format(Math.pi))
	 *
	 * 2. difference between:
	 * 		- functions vs methods
	 * 		- parameters: by-name vs 0-lambda
	 */

	def byName(n: => Int): Int = n + 1
	def byFunction(f: () => Int): Int = f() + 1

	def method: Int = 42
	def parenMethod(): Int = 42

	/**
	 * calling byName and byFunction
	 * 	- int
	 * 	- method
	 * 	- parenMethod
	 * 	- lambda
	 * 	- PAF
	 */

	/**
	 * 1
	 */

	def format(format: String)(number: Any): String  = format.format(number)

	val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

	val format1 = format("%4.2f") _
	val format2 = format("%8.6f") _
	val format3 = format("%14.12f") _

	println(numbers.map(format3))

	/**
	 * 2
	 */

	byName(23)
	byName(method)
	byName(parenMethod())
	// byName(() => 42)			<= not ok -> error
	byName((() => 42)())
	// byName(parenMethod _)	<= not ok -> error

	// byFunction(45)			<= not ok -> error
	// byFunction(method)		<= not ok -> error
	byFunction(parenMethod)
	byFunction(() => 46)
	byFunction(parenMethod _)
}