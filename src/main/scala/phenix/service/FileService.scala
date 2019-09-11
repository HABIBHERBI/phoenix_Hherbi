package phenix.service

import java.nio.file.Path
import java.time.LocalDate

import better.files._
import com.typesafe.scalalogging.LazyLogging
import phenix.containers.{ProductSale, ProductTurnover}

import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

trait FileService {
  val CARREFOUR_DATA_FILE_EXTENSION = """.data"""
}

trait FileNameChecker {
  val TRANSACTION_FILENAME_PATTERN: Regex = """^transactions\_[0-9]{8}\.data$""".r
  val PRODUCT_FILENAME_PATTERN: Regex = """^reference\_prod\-(.*)\_[0-9]{8}\.data$""".r

  def fileIsTransactionRecord(fileName: String): Boolean = {
    TRANSACTION_FILENAME_PATTERN.findFirstIn(fileName).isDefined
  }

  def fileIsProductRecord(fileName: String): Boolean = {
    PRODUCT_FILENAME_PATTERN.findFirstIn(fileName).isDefined
  }
}

/**
  * Default methods to write content into files
  */
trait FileProducer extends FileService with LazyLogging {
  /**
    * Outputs data to a file line by line, create the file and its parent path if it does not exist
    * WARN: overwrites file content.
    *
    * @param outputFilePath the file path to write
    * @param content the content to write as a string stream
    */
  def writeRecordFile(outputFilePath: Path, content: Stream[String]): Unit = {
    val fileToOutput = file"${outputFilePath.toAbsolutePath.toString}"
    logger.info(s"Writing to file ${outputFilePath.toAbsolutePath.toString}")

    fileToOutput
      .createIfNotExists(asDirectory = false, createParents = true)
      .overwrite("")

    content.foreach(line => {
      fileToOutput.appendLine(line)
    })
  }
}

/**
  * Default methods to consume content of files
  */
trait FileIngester extends FileService with LazyLogging {
  /**
    * Ingests file content line by line, except if the file does not exist.
    *
    * @param filePath the file you want to ingest, HAS to exist !
    * @return a string stream containing each line if the file is found, or an error
    */
  def ingestRecordFile(filePath: Path): Stream[String] = {
    logger.info(s"Ingesting record file ${filePath.toAbsolutePath}")
    val fileToIngest = File(filePath.toString)

    fileToIngest.lineIterator.toStream
  }
}

trait FileNameService[T] {
  val TOP100_PREFIX = "top_100"
  val TOP100_GLOBAL = "GLOBAL"
  val TOP100_PREFIX_CATEGORY = s"${TOP100_PREFIX}_default"
  val EXTENSION = ".data"
  val J7_SUFFIX = "-J7"

  def generateDayShopFileName(date: LocalDate, shopUuid: String) : String = {
    s"${TOP100_PREFIX_CATEGORY}_${shopUuid}_${date.format(TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)}$EXTENSION"
  }

  def generateWeekShopFileName(date: LocalDate, shopUuid: String) : String = {
    s"${TOP100_PREFIX_CATEGORY}_${shopUuid}_${date.format(TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)}$J7_SUFFIX$EXTENSION"
  }

  def generateDayGlobalFileName(date: LocalDate) : String = {
    s"${TOP100_PREFIX_CATEGORY}_${TOP100_GLOBAL}_${date.format(TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)}$EXTENSION"
  }

  def generateWeekGlobalFileName(date: LocalDate) : String = {
    s"${TOP100_PREFIX_CATEGORY}_${TOP100_GLOBAL}_${date.format(TransactionMarshaller.CARREFOUR_FILENAME_DATE_FORMAT)}$J7_SUFFIX$EXTENSION"
  }
}

object ProductSaleFileNameService extends FileNameService[ProductSale] {
  override val TOP100_PREFIX_CATEGORY = s"${TOP100_PREFIX}_ventes"
}

object ProductTurnoverFileNameService extends FileNameService[ProductTurnover] {
  override val TOP100_PREFIX_CATEGORY = s"${TOP100_PREFIX}_ca"

}


