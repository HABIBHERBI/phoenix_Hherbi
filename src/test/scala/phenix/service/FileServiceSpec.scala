package phenix.service

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

class FileServiceSpec extends FlatSpec with Matchers {
  val dayDate: LocalDate = LocalDate.parse("20170514", TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)

  "The ProductSale Filename service" should "return correct day shop file name" in {
    // EXECUTE
    val fileName = ProductSaleFileNameService.generateDayShopFileName(dayDate, "shopuuid")

    // ASSERT
    fileName shouldBe "top_100_ventes_shopuuid_20170514.data"
  }

  "The ProductSale Filename service" should "return correct day global file name" in {
    // EXECUTE
    val fileName = ProductSaleFileNameService.generateDayGlobalFileName(dayDate)

    // ASSERT
    fileName shouldBe "top_100_ventes_GLOBAL_20170514.data"
  }

  "The ProductSale Filename service" should "return correct week shop file name" in {
    // EXECUTE
    val fileName = ProductSaleFileNameService.generateWeekShopFileName(dayDate, "shopuuid")

    // ASSERT
    fileName shouldBe "top_100_ventes_shopuuid_20170514-J7.data"
  }

  "The ProductSale Filename service" should "return correct week global file name" in {
    // EXECUTE
    val fileName = ProductSaleFileNameService.generateWeekGlobalFileName(dayDate)

    // ASSERT
    fileName shouldBe "top_100_ventes_GLOBAL_20170514-J7.data"
  }

  "The ProductTurnover Filename service" should "return correct day shop file name" in {
    // EXECUTE
    val fileName = ProductTurnoverFileNameService.generateDayShopFileName(dayDate, "shopuuid")

    // ASSERT
    fileName shouldBe "top_100_ca_shopuuid_20170514.data"
  }

  "The ProductTurnover Filename service" should "return correct day global file name" in {
    // EXECUTE
    val fileName = ProductTurnoverFileNameService.generateDayGlobalFileName(dayDate)

    // ASSERT
    fileName shouldBe "top_100_ca_GLOBAL_20170514.data"
  }

  "The ProductTurnover Filename service" should "return correct week shop file name" in {
    // EXECUTE
    val fileName = ProductTurnoverFileNameService.generateWeekShopFileName(dayDate, "shopuuid")

    // ASSERT
    fileName shouldBe "top_100_ca_shopuuid_20170514-J7.data"
  }

  "The ProductTurnover Filename service" should "return correct week global file name" in {
    // EXECUTE
    val fileName = ProductTurnoverFileNameService.generateWeekGlobalFileName(dayDate)

    // ASSERT
    fileName shouldBe "top_100_ca_GLOBAL_20170514-J7.data"
  }
}
