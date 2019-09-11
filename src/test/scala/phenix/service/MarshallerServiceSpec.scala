package phenix.service

import org.scalatest.{FlatSpec, Matchers}
import phenix.containers.{Transaction, _}


class MarshallerServiceSpec extends FlatSpec with Matchers {

  "The Transaction File Marshaller" should "return a stream with the right transactions" in {
    // PREPARE/EXECUTE
    val transactions: Transactions = TransactionMarshaller.marshallLines(Stream(
      "1|20170514T223544+0100|2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71|531|5",
      "3818|20170514T223544+0100|72a2876c-bc8b-4f35-8882-8d661fac2606|989|4",
      "9999|20170514T223544+0100|10f2f3e6-f728-41f3-b079-43b0aa758292|703|1"
    ), "transactions_20170514.data")

    // ASSERT
    transactions.transactions should have size 3
    transactions.transactions should contain allOf(
      Transaction(1, "2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71", 531, 5),
      Transaction(3818, "72a2876c-bc8b-4f35-8882-8d661fac2606", 989, 4),
      Transaction(9999, "10f2f3e6-f728-41f3-b079-43b0aa758292", 703, 1)
    )
  }

  "The Transaction FileName Marshaller" should "return a marshalled value when filename is correct" in {
    // PREPARE/EXECUTE
    val txMetadata: TransactionFileMetaData = TransactionMarshaller.marshallFileName("transactions_20170514.data")

    // ASSERT
    txMetadata.date.toString shouldBe "2017-05-14"
  }

  "The Product File Marshaller" should "return a stream with the right products" in {
    // PREPARE/EXECUTE
    val products: Products = ProductMarshaller.marshallLines(Stream(
      "1|4.7",
      "500|34.86",
      "999|0.68"
    ), "reference_prod-2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71_20170514.data")

    // ASSERT
    products.products should have size 3
    products.products should contain allOf(
      Product(1, 4.7),
      Product(500, 34.86),
      Product(999, 0.68)
    )
  }

  "The Product FileName Marshaller" should "return a marshalled value when filename is correct" in {
    // PREPARE/EXECUTE
    val pxMetadata: ProductFileMetaData = ProductMarshaller.marshallFileName("reference_prod-shopuuid2_20170514.data")

    // ASSERT
    pxMetadata.date.toString shouldBe "2017-05-14"
    pxMetadata.shopUuid shouldBe "shopuuid2"
  }
}