/*
package Lectures.Section_2

import scala.annotation.tailrec
import scala.runtime.Nothing$

object RecapTheScalaBasics extends App
{
    val aCondition: Boolean = false
    val aConditionedVal = if(aCondition) 42 else 65

    val aCodeBlock =
    {
        if(aCondition) 42
        56
    }

    val theUnit = println("Hello, Scala")

    def aFunction(x: Int) =
    {
        x + 1
    }

    @tailrec
    def factorial(n: Int, acumulator: Int): Int =
    {
        if(n <= 0) acumulator
        else factorial(n -1, n * acumulator)
    }

    class Animal
    class Dog extends Animal
    val aDog: Animal = new Dog

    trait Carnivore
    {
        def eat(a: Animal): Unit
    }

    class Crocodile extends Animal with Carnivore
    {
        override def eat(a: Animal): Unit = println("food!")
    }

    val aCroc = new Crocodile
    aCroc.eat(aDog)
    aCroc eat aDog

    1 + 2
    1.+(2)

    val aCarnivore = new Carnivore
    {
        override def eat(a: Animal): Unit = println("roooar!")
    }

    abstract class MyList[+A]

    object MyList

    case class Person(name: String, age: Int)

    val throwsException = throw new RuntimeException
    val aPotentialFailure = try
    {
        throw new RuntimeException
    }
    catch
    {
        case e: Exeption => "I cought an exeption!"
    }
    finally
    {
        println("Some log´s")
    }

    val incrementer = new Function1[Int, Int]
    {
        override def apply(v1: Int): Int = v1 +1
    }

    val anonymusIncrementer = (x: Int) => x +1
    List(1, 2, 3).map(anonymusIncrementer)

    val pairs = for
    {
        num     <-  List(1, 2, 3)
        char    <-  List('1', '2', '3')
    } yield( num + '-' + char)

    val aMap = Map(

        "Daniel"    ->  789,
        "Jess"      ->  555
    )

    val anOption = Some(2)

    val x = 2
    val order = x.match
    {
        case 1 => "first"
        case 2 => "second"
        case 3 => "third"
        case _ => x + "th"
    }

    val bob = Person("Bob", 22)
    val greeting = bob.match
    {
        case Person(n, _) => s"Hi, my name is $n"
    }
}
*/
