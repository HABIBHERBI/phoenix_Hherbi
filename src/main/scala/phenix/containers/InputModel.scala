package phenix.containers

import java.io.File
import java.time.LocalDate


case class InputFiles(inputTransactionsFiles: Stream[File], inputProductFiles: Stream[File])

case class Transaction(transactionId: Int, shopUuid: String, productId: Int, quantity: Int)
case class Product(productId: Int, price: Double)

case class TransactionFileMetaData(date: LocalDate)
case class ProductFileMetaData(shopUuid: String, date: LocalDate)

case class Transactions(transactions: Stream[Transaction], metaData: TransactionFileMetaData) extends Ordered[Transactions] {
  override def compare(ts2: Transactions): Int = this.metaData.date compareTo ts2.metaData.date
}



case class Products(products: Stream[Product], metaData: ProductFileMetaData)
