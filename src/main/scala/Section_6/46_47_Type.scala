package Section_6

object TypeMembers extends App
{
    class Animal
    class Dog extends Animal
    class Cat extends Animal

    class AnimalCollection {
        object Test
        type AnimalType // abstract type member
        type BoundedAnimal <: Animal
    }

    val test = (new AnimalCollection).Test
}