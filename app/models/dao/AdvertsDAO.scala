package models.dao

import models.Advert
import org.joda.time.DateTime
import scala.collection.mutable
import scala.collection.JavaConversions._
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2._
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import play.api.libs.json._


object AdvertsDAO {
  val client = AmazonDynamoDBClientBuilder.standard()
    .withRegion(Regions.EU_CENTRAL_1)
    .build();
  val dynamodb = new DynamoDB(client)
  val tableName = "adverts"
  val table = dynamodb.getTable(tableName)

  // Temporary in-memory repository
  var adverts = mutable.Buffer[Advert] (
    new Advert(1, "Audi A4", "gasoline", 10000, false, Option(100000), Option(DateTime.parse("2010"))),
    new Advert(2, "BMW 5", "gasoline", 15000, false, Option(120000), Option(DateTime.parse("2009"))),
    new Advert(3, "Volkswagen Touran", "diesel", 4500, false, Option(140000), Option(DateTime.parse("2004"))),
    new Advert(4, "Opel Astra", "gasoline", 20000, true)
  )

  def add(advert: Advert) : Option[String] = {
    var advertJson = Json.toJson(advert).toString()
    val item = new Item()
      .withPrimaryKey("id", advert.id)
      .withJSON("document", advertJson)
    val putItemSpec = new PutItemSpec().withItem(item).withConditionExpression("attribute_not_exists(id)")
    try {
      table.putItem(putItemSpec)
      None
    }
    catch {
      case e =>
        Some(e.getMessage())
    }
  }

  def edit(id: Int, advert: Advert): Option[Advert] = {
    get(id) match {
      case None =>
        None
      case Some(_) =>
        var advertJson = Json.toJson(advert).toString()
        var item = new Item()
          .withPrimaryKey("id", advert.id)
          .withJSON("document", advertJson)
        // Using putItem instead of updateItem intentionally, because the goal is to replace the item
        table.putItem(item)
        Some(advert)
    }
  }

  def delete(id: Int) = {
    table.deleteItem("id", id)
  }

  def get(): List[Advert] = {
    val scanRequest = new ScanRequest().withTableName(tableName)
    val scanResult = client.scan(scanRequest)
    // Had to get every item separately, because it's not trivial at all to get a json value of an attribute when scanning
    val adverts = scanResult.getItems().map(item => item.get("id").getN().toInt).map(id => get(id)).flatten
    adverts.toList
  }

  def get(id: Int): Option[Advert] = {
    table.getItem("id", id) match {
      case null =>
        None
      case item =>
        val advertJson = item.getJSON("document")
        var advert = Json.parse(advertJson).as[Advert]
        Some(advert)
    }
  }
}
