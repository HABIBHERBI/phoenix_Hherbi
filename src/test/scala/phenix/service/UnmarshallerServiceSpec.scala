package phenix.service

import org.scalatest.{FlatSpec, Matchers}
import phenix.containers.{ProductSale, ProductTurnover}

class UnmarshallerServiceSpec extends FlatSpec with Matchers {
  "The ProductSale Unmarshaller" should "return the right stream of string for 3 product sales" in { // TODO
    // PREPARE
    val productSales: Stream[ProductSale] = Stream(
      ProductSale(1, 2),
      ProductSale(34, 18),
      ProductSale(99, 5)
    )

    // EXECUTE
    val unmarhsallResult: Stream[String] = ProductSaleUnmarshaller.unmarshallRecords(productSales)


    // ASSERT
    unmarhsallResult should have size 3
    unmarhsallResult.toList shouldBe List("1|2", "34|18", "99|5")
  }

  "The ProductTurnover Unmarshaller" should "return the right stream of string for 3 product turnovers" in {
    // PREPARE
    val productTurnovers: Stream[ProductTurnover] = Stream(
      ProductTurnover(1, 2.0),
      ProductTurnover(34, 18.46),
      ProductTurnover(99, 5.12)
    )

    // EXECUTE
    val unmarhsallResult: Stream[String] = ProductTurnoverUnmarshaller.unmarshallRecords(productTurnovers)


    // ASSERT
    unmarhsallResult should have size 3
    unmarhsallResult.toList shouldBe List("1|2.0", "34|18.46", "99|5.12")
  }
}
