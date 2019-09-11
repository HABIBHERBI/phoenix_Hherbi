package phenix.service

import phenix.containers.{ProductSale, ProductTurnover}


trait Unmarshaller[T] extends FileProducer {
  val OUTPUT_HORIZONTAL_SEPARATOR: String = "|"

  def unmarshallRecords(records: Stream[T]): Stream[String] = {
    records.map(record => unmarshallRecord(record))
  }

  def unmarshallRecord(record: T): String
}

object ProductSaleUnmarshaller extends Unmarshaller[ProductSale] {
  override def unmarshallRecord(productSale: ProductSale): String =
    s"${productSale.productId}$OUTPUT_HORIZONTAL_SEPARATOR${productSale.quantity}"
}

object ProductTurnoverUnmarshaller extends Unmarshaller[ProductTurnover] {
  override def unmarshallRecord(productTurnover: ProductTurnover): String =
    s"${productTurnover.productId}$OUTPUT_HORIZONTAL_SEPARATOR${productTurnover.turnover}"
}