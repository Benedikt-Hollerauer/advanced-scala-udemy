package Section_5

object OrganizingImplicits extends App
{
    /*implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)*/
    /*implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)*/
    /*println(List(1,3,56,8,3).sorted)*/

    /*Implicits: (used as implicit parameters):
        - val/var
        - object
        - accessor methods = def´s with no parentheses
    */

    // Exercise
    case class Person(name: String, age: Int)

    val persons = List(
        Person("Steve", 30),
        Person("John", 34),
        Person("Amy", 19),
    )

    object AlphabeticNameOrdering
    {
        implicit val orderByName: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
    }

    object AgeOrdering
    {
        implicit val orderByAge: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age.compareTo(b.age) < 0)
    }

    import AlphabeticNameOrdering.*             // so kann man librarys erstellen ( sehr gut für generics )

    println(persons.sorted)

    /**
     * Implicit scope
     *  - normal scope = LOCAL SCOPE
     *  - imported scope
     *  - companions of all types involved
     *    - List
     *    - Ordering
     *    - all the types involved = A or any supertype
     *
     *    def sorted[B >: A](implicit ord: Ordering[B]): List [B]
     */

    // Exercise

    /**
     *  - totalPrice = most used ( 50% )
     *  - by unit count = 25%
     *  - by unit price = 25%
     */

    case class Purchase(nUnits: Int, unitPrice: Double)

    object Purchase
    {
        implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
    }

    object UnitCountOrdering
    {
        implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
    }

    object UnitPriceOrdering
    {
        implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
    }
}
