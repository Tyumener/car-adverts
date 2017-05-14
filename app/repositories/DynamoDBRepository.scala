package repositories

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import scala.collection.JavaConversions._
import models.Advert
import play.api.libs.json.Json

class DynamoDBRepository extends Repository[Advert] {
  val client = AmazonDynamoDBClientBuilder.standard()
    .withRegion(Regions.EU_CENTRAL_1)
    .build();
  val dynamodb = new DynamoDB(client)
  val tableName = "adverts"
  val table = dynamodb.getTable(tableName)

  override def add(advert: Advert): Option[String] = {
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

  override def edit(id: Int, advert: Advert): Option[Advert] = {
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

  override def delete(id: Int): Unit = {
    table.deleteItem("id", id)
  }

  override def get(): List[Advert] = {
    val scanRequest = new ScanRequest().withTableName(tableName)
    val scanResult = client.scan(scanRequest)
    // Had to get every item separately, because it's not trivial at all to get a json value of an attribute when scanning
    val adverts = scanResult.getItems().map(item => item.get("id").getN().toInt).map(id => get(id)).flatten
    adverts.toList
  }

  override def get(id: Int): Option[Advert] = {
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
