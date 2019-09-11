package phenix.pilot

import java.nio.file.Paths

import org.scalatest.{FlatSpec, Matchers}
import phenix.containers.FolderArguments

class FileOrchestratorSpec extends FlatSpec with Matchers {
  "The File Orchestrator" should "determine InputFiles correctly in non-empty folder" in {
    // PREPARE
    val folderArguments = FolderArguments(Paths.get("data/input/simple-example"), Paths.get("./phenix-output"), simpleCalc = false)

    // EXECUTE
    val inputFiles = FileOrchestrator.determineInputFiles(folderArguments)

    // ASSERT
    inputFiles.inputProductFiles should have size 2
    inputFiles.inputTransactionsFiles should have size 1
  }

  "The File Orchestrator" should "convert input files to marhsalled valued correctly for non-empty folder" in {
    // PREPARE
    val folderArguments = FolderArguments(Paths.get("data/input/simple-example"), Paths.get("./phenix-output"), simpleCalc = false)

    // EXECUTE
    val inputFiles = FileOrchestrator.determineInputFiles(folderArguments)
    val marshalledValues = FileOrchestrator.convertInputFilesToMarshalledValues(inputFiles)

    // ASSERT
    val transactions = marshalledValues._1
    val products = marshalledValues._2

    transactions should have size 1
    products should have size 2

    val firstTransaction = transactions.toList.head

    firstTransaction.metaData.date.toString shouldBe "2017-05-14"
    firstTransaction.transactions should have size 10

    products.map(product => product.metaData.date.toString) should contain ("2017-05-14")
    products.map(product => product.metaData.shopUuid) should contain allOf("shopuuid1", "shopuuid2")
  }
}
