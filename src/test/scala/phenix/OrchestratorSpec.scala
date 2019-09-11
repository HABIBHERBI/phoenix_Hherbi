package phenix

import org.scalatest.{FlatSpec, Matchers}
import phenix.pilot.FileOrchestrator

class OrchestratorSpec extends FlatSpec with Matchers {
  "The File Orchestrator FileName checker" should "return false results with invalid transactions and products file names" in {
    // PREPARE / EXECUTE
    val isFileNameTransactionEmptyName = FileOrchestrator.fileIsTransactionRecord("")
    val isFileNameProductEmptyName = FileOrchestrator.fileIsProductRecord("")

    val isFileNameTransactionIncorrectNameDateTooLong = FileOrchestrator.fileIsTransactionRecord("transactions_2223232232323.data")
    val isFileNameProductIncorrectNameDateToolong = FileOrchestrator.fileIsProductRecord("reference_prod-dd43720c-be43-41b6-bc4a-ac4beabd0d9b_201705142233232.data")

    val isFileNameTransactionIncorrectNameNoPrefix = FileOrchestrator.fileIsTransactionRecord("20170514.data")
    val isFileNameProductIncorrectNameNoPrefix = FileOrchestrator.fileIsProductRecord("20170514.data")

    // ASSERT
    isFileNameTransactionEmptyName shouldBe false
    isFileNameProductEmptyName shouldBe false
    isFileNameTransactionIncorrectNameDateTooLong shouldBe false
    isFileNameProductIncorrectNameDateToolong shouldBe false
    isFileNameTransactionIncorrectNameNoPrefix shouldBe false
    isFileNameProductIncorrectNameNoPrefix shouldBe false
  }

  "The File Orchestrator FileName checker" should "return true results with valid transactions and products file names" in {
    // PREPARE / EXECUTE
    val isFileNameTransactionNormalName = FileOrchestrator.fileIsTransactionRecord("transactions_20170514.data")
    val isFileNameProductNormalName = FileOrchestrator.fileIsProductRecord("reference_prod-dd43720c-be43-41b6-bc4a-ac4beabd0d9b_20170514.data")

    // ASSERT
    isFileNameTransactionNormalName shouldBe true
    isFileNameProductNormalName shouldBe true
  }
}
