package phenix.computeEngine

import java.time.LocalDate

import phenix.containers._
import phenix.service.TransactionMarshaller

trait CalculatorSpec {
  val dayDate: LocalDate = LocalDate.parse("20170514", TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)

  val (shopUuidOne, shopUuidTwo) = ("shopUuid1", "shopUuid2")
  val (productIdOne, productIdTwo, productIdThree) = (1, 2, 3)

  val transactionFileMetadata = TransactionFileMetaData(dayDate)
  val productFileMetaDataShopOne = ProductFileMetaData(shopUuidOne, dayDate)
  val productFileMetaDataShopTwo = ProductFileMetaData(shopUuidTwo, dayDate)

  val transactionStream: Stream[Transaction] = List(
    // Shop 1
    Transaction(1, shopUuidOne, productIdOne, 2),
    Transaction(5, shopUuidOne, productIdOne, 10),
    Transaction(10, shopUuidOne, productIdTwo, 15),
    Transaction(3, shopUuidOne, productIdThree, 1),
    Transaction(9, shopUuidOne, productIdThree, 2),

    // Shop 2
    Transaction(2, shopUuidTwo, productIdTwo, 5),
    Transaction(6, shopUuidTwo, productIdTwo, 8),
    Transaction(7, shopUuidTwo, productIdTwo, 3),
    Transaction(4, shopUuidTwo, productIdThree, 3),
    Transaction(8, shopUuidTwo, productIdThree, 3)
  ).toStream
  val transactions = Transactions(transactionStream, transactionFileMetadata)

  val productStreamShopOne: Stream[Product] = List(
    Product(productIdOne, 12.2),
    Product(productIdTwo, 2.6),
    Product(productIdThree, 40.0)
  ).toStream

  val productStreamShopTwo: Stream[Product] = List(
    Product(productIdOne, 1.3),
    Product(productIdTwo, 12.4),
    Product(productIdThree, 4.1)
  ).toStream

  val productsOne = Products(productStreamShopOne, productFileMetaDataShopOne)
  val productsTwo = Products(productStreamShopTwo, productFileMetaDataShopTwo)

}
