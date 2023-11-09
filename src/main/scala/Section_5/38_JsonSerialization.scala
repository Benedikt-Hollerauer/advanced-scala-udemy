package Section_5

import java.util.Date

object JsonSerialization extends App
{
    case class User(name: String, age: Int, email: String)
    case class Post(content: String, createdAt: Date)
    case class Feed(user: User, post: List[Post])

    /*
        1 - intermediate data types: Int, String, List, Date
        2 - type classes for conversion to intermediate  data types
        3 - serialize to JSON
    */

    sealed trait JsonValue  // intermediate data type
    {
        def stringify: String
    }

    final case class JsonString(value: String) extends JsonValue
    {
        def stringify: String =
            "\"" + value + "\""
    }

    final case class JsonNumber(value: Int) extends JsonValue
    {
        def stringify: String = value.toString
    }

    final case class JsonArray(values: List[JsonValue]) extends JsonValue
    {
        def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
    }

    final case class JsonObject(values: Map[String, JsonValue]) extends JsonValue
    {
        /*
          {
            name: "John"
            age: 22
            friends: [ ... ]
            latestPost: {
              content: "Scala Rocks"
              date: ...
            }
          }
         */
        def stringify: String = values.map{
            case (key, value) => "\"" + key + "\":" + value.stringify
        }.mkString("{", ",", "}")

    }

    val data = JsonObject(Map(
        "user" -> JsonString("Daniel"),
        "posts" -> JsonArray(List(
            JsonString("Scala Rocks!"),
            JsonNumber(453)
        ))
    ))

    println(data.stringify)

    // type class
    /*
        1 - type class
        2 - type class instances (implicit)
        3 - pimp library to use type class instances
     */

    // 2.1
    trait JsonConverter[T]{
        def convert(value: T): JsonValue
    }

    // 2.2
    implicit object StringConverter extends JsonConverter[String]
    {
        override def convert(value: String): JsonValue = JsonString(value)
    }

    implicit object NumberConverter extends JsonConverter[Int]
    {
        override def convert(value: Int): JsonValue = JsonNumber(value)
    }

    // custom data types
    implicit object UserConverter extends JsonConverter[User]
    {
        def convert(user: User): JsonValue = JsonObject(Map(
            "name" -> JsonString(user.name),
            "age" -> JsonNumber(user.age),
            "email" -> JsonString(user.email)
        ))
    }

    implicit object PostConverter extends JsonConverter[Post]
    {
        def convert(post: Post): JsonValue = JsonObject(Map(
            "content" -> JsonString(post.content),
            "created:" -> JsonString(post.createdAt.toString)
        ))
    }

    implicit object FeedConverter extends JsonConverter[Feed]
    {
        def convert(feed: Feed): JsonValue = JsonObject(
            Map(
                "user" -> feed.user.toJson,
                "posts" -> JsonArray(feed.post.map(_.toJson))
            )
        )
    }

    // 2.3
    implicit class JsonOps[T](value: T)
    {
        def toJson(implicit converter: JsonConverter[T]): JsonValue = converter.convert(value)
    }

    // call stringify on result
    val now = new Date(System.currentTimeMillis())
    val john = User("John", 34, "john@rockthejvm.com")
    val feed = Feed(john, List(
        Post("hello", now),
        Post("look at this cute puppy", now)
    ))

    println(feed.toJson.stringify)
}