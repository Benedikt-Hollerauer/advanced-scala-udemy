package Section_5

import jdk.javadoc.internal.doclets.toolkit.Content

object TypeClasses extends App
{
    trait HTMLWritable
    {
        def toHtml: String
    }

    case class User(name: String, age: Int, email: String) extends HTMLWritable
    {
        override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
    }

    User("John", 32, "john@rockthejvm.com").toHtml
    /*
      1 - for the types WE write
      2 - ONE implementation out of quite a number
     */

    // option 2 - pattern matching
    object HTMLSerializerPM
    {
        def serializeToHtml(value: Any) = value match
        {
            case User(n, a, e) =>
            case _ =>
        }
    }

    /*
      1 - lost type safety
      2 - need to  modify the code every time
      3 - still ONE implementation
     */

    trait HTMLSerializer[T]
    {
        def serialize(value: T): String
    }

    implicit object UserSerializer extends HTMLSerializer[User]
    {
        def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
    }

    val john = User("John", 32, "john@rockthejvm.com")
    println(UserSerializer.serialize(john))

    // 1 - we can define serializers for other  types

    import java.util.Date

    object DateSerializer extends HTMLSerializer[Date]
    {
        override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
    }

    // 2 - we can define MULTIPLE serializers
    object PartialUserSerializer extends HTMLSerializer[User]
    {
        def serialize(user: User): String = s"<div>${user.name} </div>"
    }

    trait Equal[T]
    {
        def apply(a: T, b: T): Boolean
    }

    implicit object NameEquality extends Equal[User]
    {
        override def apply(a: User, b: User): Boolean = a.name == b.name
    }

    // part 2
    object HTMLSerializer
    {
        def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
            serializer.serialize(value)

        def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
    }

    implicit object IntSerializer extends HTMLSerializer[Int]
    {
        override def serialize(value: Int): String = s"<div style: color=blue>$value</div>"
    }

    println(HTMLSerializer.serialize(42))
    println(HTMLSerializer.serialize(john))

    // access to the entire type class  interface
    println(HTMLSerializer[User].serialize(john))

    object Equal
    {
        def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(a, b)
    }

    val anotherJohn = User("John", 45, "anotherJohn@email.com")

    println(Equal(john, anotherJohn))

    // part 3
    implicit class HTMLEnrichment[T](value: T)
    {
        def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
    }

    println(john.toHTML) // println(new HTMLEnrichment[User](john).toHTML(UserSerializer))

    /*
        - extend to new types
        - chose implementation
        - super expressive!!!!!!!!!!!
    */

    println(john.toHTML)
    println(john.toHTML(PartialUserSerializer))

    /*
        - type class itself --- HTMLSerializer[T] { .. }
        - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
        - conversion with implicit classes --- HTMLEnrichment
    */

    /**
     *  Exercise - improve the Equal TC with an implicit conversion class
     *  - ===(anotherValue: T)
     *  - !==(anotherValue: T)
     */

    implicit class TypeSafeEqual[T](value: T)
    {
        def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer(value, anotherValue)
        def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer(value, anotherValue)
    }

    println(john === anotherJohn)
    println(john !== anotherJohn)

    /**
     *  john.===(anotherJohn)
     *  new TypeSafeEqual[User](john).===(anotherJohn)
     *  new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
     */

    // context bounds
    def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    {
        s"<html><body>${content.toHTML(serializer)}</body></html>"
    }

    // hier kann man nicht den namen "serializer" verwenden
    def htmlSugar[T : HTMLSerializer](content: T): String =
    {
        s"<html><body>${content.toHTML}</body></html>"
    }

    // implicitly
    case class Permissions(mask: String)
    implicit val defaultPermissions: Permissions = Permissions("0744")

    // in some other part of the  code
    val standardPerms = implicitly[Permissions]

    // use case: um den namen "serializer" verwenden zu könnten und trozdem eine schlanke signatur zu haben
    def htmlSugar2[T : HTMLSerializer](content: T): String =
    {
        val serializer = implicitly[HTMLSerializer[T]]
        // use serializer
        s"<html><body> ${content.toHTML(serializer)}</body></html>"
    }
}