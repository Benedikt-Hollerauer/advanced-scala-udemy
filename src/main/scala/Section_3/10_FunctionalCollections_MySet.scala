package Exercises.Section_3

import scala.annotation.tailrec
import scala.jdk.Accumulator

trait MySet[A] extends (A => Boolean)
{
	def apply(element: A): Boolean =
		contains(element)

	def contains(value: A): Boolean

	def +(value: A): MySet[A]
	def ++(anotherSet: MySet[A]): MySet[A]

	def map[A](f: A => B): MySet[B]
	def flatMap[B](f: A => MySet[B]): MySet[B]
	def filter(predicate: A => Boolean): MySet[A]
	def foreach(f: A => Unit): Unit

	/**
	 * Exercise:
	 * - removing an element
	 * - intersection with another set
	 * - difference with another set
	 */

	def remove(remove: A): MySet[A]
	def intersect(value: MySet[A]): MySet[A]
	def difference(anotherSet: MySet[A]): MySet[A]

	/**
	 * Exercise:
	 * - negation of a set
	 */

	def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A]
{
	override def contains(value: A): Boolean = false

	override def +(value: A): MySet[A] = new NonEmptySet[A](value, this)
	override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

	override def map[A](f: A => B): MySet[B] = new EmptySet[B]
	override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
	override def filter(predicate: A => Boolean): MySet[A] = this
	override def foreach(f: A => Unit): Unit = ()

	override def remove(remove: A): MySet[A] = this
	override def intersect(value: MySet[A]): MySet[A] = this
	override def difference(anotherSet: MySet[A]): MySet[A] = this

	override def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)

    /**
	 * Exercise:
	 * - removing an element
	 * - intersection with another set
	 * - difference with another set
	 */

    // override def remove(remove: A): MySet[A] = ???
    // override def intersect(value: MySet[A]): MySet[A] = ???
    // override def difference(anotherSet: MySet[A]): MySet[A] = ???

    /**
	 * Exercise:
	 * - negation of a set
	 */

    // override def unary_! : MySet[A] = ???
}

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A]
{
	override def contains(value: A): Boolean = property(value)

	// { x in A | property(x) } + element = { x in A | property(x) || x == element }
	override def +(value: A): MySet[A] = new PropertyBasedSet[A](x => property(x) || x == value)

	//  { x in A | property(x) } ++ set => { x in A | property(x) || set contains x }
	override def ++(anotherSet: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || anotherSet(x))

	// all integers => (x % 3) => [0, 1, 2]
	override def map[A](f: A => Any): MySet[A] = politelyFail
	override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
	override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))
	override def foreach(f: A => Unit): Unit = politelyFail

	def remove(remove: A): MySet[A] = filter(x => x != remove)
	def intersect(value: MySet[A]): MySet[A] = filter(value)
	def difference(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

	def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

	def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole here")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A]
{
	override def contains(value: A): Boolean =
	{
		value == head || tail.contains(value)
	}

	override def +(value: A): MySet[A] =
	{
		if(this.contains(value)) this
		else new NonEmptySet[A](value, this)
	}

	override def ++(value: MySet[A]): MySet[A] =
	{
		tail ++ value + head
	}

	override def map[A](f: A => B): MySet[B] =
	{
		tail.map(f) + f(head)
	}

	override def flatMap[B](f: A => MySet[B]): MySet[B] =
	{
		tail.flatMap(f) ++ f(head)
	}

	override def filter(predicate: A => Boolean): MySet[A] =
	{
		val filteredTail = tail.filter(predicate)
		if(predicate(head)) filteredTail + head
		else filteredTail
	}

	override def foreach(f: A => Unit): Unit =
	{
		f(head)
		tail.foreach(f)
	}

	override def remove(remove: A): MySet[A] =
	{
		if(head == remove) tail
		else tail - remove + head
	}

	override def intersect(value: MySet[A]): MySet[A] = filter(value)
	override def difference(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))

	override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySet
{
	def apply[A](values: A*): MySet[A] =
	{
		@tailrec
		def buildSet(valSeq: Seq[A], accumulator: MySet[A]): MySet[A] =
		{
			if(valSeq.isEmpty) accumulator
			else buildSet(valSeq.tail, accumulator + valSeq.head)
		}
		buildSet(values.toSeq, new EmptySet[A])
	}
}

