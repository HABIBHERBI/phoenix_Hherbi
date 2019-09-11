package phenix.service

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import phenix.containers._

trait Marshaller[T, U] extends FileService {
  val CARREFOUR_HORIZONTAL_SEPARATOR: String = """\|"""

  /**
    * Transforms the line by line strings of a file to the desired input containers
    * @param fileContent the line by line file content
    * @param deserializeFunction the function used to transform each String to a containers
    * @return a stream of correctly marshalled case classes
    */
  def marshallFileContent(fileContent: Stream[String], deserializeFunction: String => U): Stream[U] = {
    fileContent.map(line => deserializeFunction(line))
  }

  def marshallLines(fileContent: Stream[String], fileName: String): T

  // NOTE: For now this method should not consider any possible errors in the source data
  def marshallLineString(line: String): U
}

trait FileNameMarshaller[T] {
  val CARREFOUR_FILENAME_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
  val CARREFOUR_FILE_METADATA_SEPARATOR: String = "_"

  /**
    * Extracts data from a data filename. Does not handle possible errors in filename.
    *
    * @param filename a VALID transaction or product filename
    *
    * @return meta data coming from the filename
    */
  def marshallFileName(fileName: String): T
}

object TransactionMarshaller extends Marshaller[Transactions, Transaction] with FileNameMarshaller[TransactionFileMetaData] {

  override def marshallLines(fileContent: Stream[String], fileName: String): Transactions = {
    Transactions(marshallFileContent(fileContent, marshallLineString), marshallFileName(fileName))
  }

  override def marshallLineString(line: String): Transaction = {
    line.split(CARREFOUR_HORIZONTAL_SEPARATOR) match {
      case Array(transactionId, _, shopUuid, productId, quantity) =>
        Transaction(transactionId.toInt, shopUuid, productId.toInt, quantity.toInt)
    }
  }

  override def marshallFileName(fileName: String): TransactionFileMetaData = {
    fileName.replaceFirst(CARREFOUR_DATA_FILE_EXTENSION, "")
      .split(CARREFOUR_FILE_METADATA_SEPARATOR) match {
      case Array(_, dateStr) => TransactionFileMetaData(LocalDate.parse(dateStr, CARREFOUR_FILENAME_DATE_FORMAT))
    }
  }
}

object ProductMarshaller extends Marshaller[Products, Product] with FileNameMarshaller[ProductFileMetaData] {
  val CARREFOUR_PRODUCT_FILE_PREFIX = "reference_prod-"

  override def marshallLines(fileContent: Stream[String], fileName: String): Products = {
    Products(marshallFileContent(fileContent, marshallLineString), marshallFileName(fileName))
  }

  override def marshallLineString(line: String): Product = {
    line.split(CARREFOUR_HORIZONTAL_SEPARATOR) match {
      case Array(productId, price) => Product(productId.toInt, price.toDouble)
    }
  }

  override def marshallFileName(fileName: String): ProductFileMetaData = {
    fileName
      .replaceFirst(CARREFOUR_PRODUCT_FILE_PREFIX, "")
      .replaceFirst(CARREFOUR_DATA_FILE_EXTENSION, "")
      .split(CARREFOUR_FILE_METADATA_SEPARATOR) match {
      case Array(shopUuid, dateStr) => ProductFileMetaData(shopUuid, LocalDate.parse(dateStr, CARREFOUR_FILENAME_DATE_FORMAT))
    }
  }
}
