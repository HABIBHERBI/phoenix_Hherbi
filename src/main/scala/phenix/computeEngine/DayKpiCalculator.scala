package phenix.computeEngine

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import phenix.containers._


/**
  * Does the calculations to get indicators (sales, turnover) according to cases for one day.
  */


trait Calculator {
  /**
    *
    * @param numberToRound the number you wish to round
    * @return
    */
  def roundValue(numberToRound: Double): Double = Math.round(numberToRound * 100.0) / 100.0
}





object DayKpiCalculator extends LazyLogging with Calculator {

  /**
    * Calls all the calculations to get results for one day.
    *
    * WARNING: this does not handle sorting. The values here are streams and not ordered, you'll have to do it before you output them to files.
    *
    * @param dayTransactions the transactions for the day
    * @param dayProductsList the associated products (associating should be done prior to this method)
    * @return
    */



  def compute100TopDay(dayTransactions: Transactions, dayProductsList: Stream[Products]): CompleteDayKpi1 = {
    logger.info(s"day calculations. Date: ${dayTransactions.metaData.date}")

    val productKpiMapByShop = computeProductDayKpiByShop(dayTransactions, dayProductsList)

    val dayShopSales = productKpiMapByShop.keys.map(shopUuid => {
      val productSaleExtract = productKpiMapByShop(shopUuid).map(productDataTuple => productDataTuple._1)
      ShopSale(shopUuid, productSaleExtract)
    }).toStream

    val dayGlobalSale = computeGlobalDaySales(dayTransactions.metaData.date, dayShopSales)

    CompleteDayKpi1(dayTransactions.metaData.date, dayShopSales, dayGlobalSale)
  }






  def computeDayKpi(dayTransactions: Transactions, dayProductsList: Stream[Products]): CompleteDayKpi = {
    logger.info(s"day calculations. Date: ${dayTransactions.metaData.date}")

    val productKpiMapByShop = computeProductDayKpiByShop(dayTransactions, dayProductsList)

    val dayShopSales = productKpiMapByShop.keys.map(shopUuid => {
      val productSaleExtract = productKpiMapByShop(shopUuid).map(productDataTuple => productDataTuple._1)
      ShopSale(shopUuid, productSaleExtract)
    }).toStream

    val dayShopTurnovers = productKpiMapByShop.keys.map(shopUuid => {
      val productSaleExtract = productKpiMapByShop(shopUuid).map(productDataTuple => productDataTuple._2)
      ShopTurnover(shopUuid, productSaleExtract)
    }).toStream

    val dayGlobalSale = computeGlobalDaySales(dayTransactions.metaData.date, dayShopSales)
    val dayGlobalTurnover = computeGlobalDayTurnover(dayTransactions.metaData.date, dayShopTurnovers)

    CompleteDayKpi(dayTransactions.metaData.date, dayShopSales, dayGlobalSale, dayShopTurnovers, dayGlobalTurnover)
  }

  def computeGlobalDaySales(date: LocalDate, dayShopSales: Stream[ShopSale]): GlobalSale = {
    logger.info(s"Calculating global sales number for $date")

    val aggregatedProductSales = dayShopSales
      .flatMap(dayShopSale => dayShopSale.productSales)
      .groupBy(productSale => productSale.productId)
      .mapValues(productSale => {
        productSale.foldLeft(0)((acc, productSale2) => acc + productSale2.quantity)
      }).map(globalResultMap => {
      ProductSale(globalResultMap._1, globalResultMap._2)
    })

    GlobalSale(aggregatedProductSales.toStream)
  }

  def computeGlobalDayTurnover(date: LocalDate, dayShopTurnovers: Stream[ShopTurnover]): GlobalTurnover = {
    logger.info(s"Calculating global turnover for $date")

    val aggregateProductTurnovers = dayShopTurnovers
      .flatMap(dayShopTurnover => dayShopTurnover.productTurnovers)
      .groupBy(productTurnover => productTurnover.productId)
      .mapValues(productTurnover => {
        productTurnover.foldLeft(0.0)((acc, productTurnover2) => roundValue(acc + productTurnover2.turnover))
      }).map(globalResultMap => {
      ProductTurnover(globalResultMap._1, globalResultMap._2)
    })

    GlobalTurnover(aggregateProductTurnovers.toStream)
  }

  def computeProductDayKpiByShop(dayTransactions: Transactions, dayProductsList: Stream[Products]): Map[String, Stream[(ProductSale, ProductTurnover)]] = {
    logger.info(s"Calculating global KPI (Sales, Turnover) for date ${dayTransactions.metaData.date}")

    dayTransactions.transactions
      .groupBy(transaction => (transaction.shopUuid, transaction.productId))
      .mapValues(transactions => {
        transactions.foldLeft(0)((acc, transaction2) => acc + transaction2.quantity)
      })
      .map(transactionMap => {
        val productId = transactionMap._1._2
        val shopUuid = transactionMap._1._1
        val productQuantity = transactionMap._2
        val productTotalPrice = productQuantity * getProductPriceFromProducts(productId, shopUuid, dayProductsList)
        (shopUuid, productId, productQuantity, productTotalPrice)
      })
      .groupBy(calculationResult => calculationResult._1) // group by shop uuid
      .map(shopProductCalc => { // remove uuid field from all parts of the list and turn them into case classes
      val shopProductsCalcValues = shopProductCalc._2.toStream
      shopProductCalc._1 -> shopProductsCalcValues.map(productAggregateCalc => (ProductSale(productAggregateCalc._2, productAggregateCalc._3), ProductTurnover(productAggregateCalc._2, roundValue(productAggregateCalc._4)))) // Rounder to 2 decimals
    })
  }

  /**
    * Given a shop uuid and a products list, this function returns the correct product price.
    * If the product price is not found for this shop, returns 0.
    *
    * @param productId the product which price you want to get
    * @param shopUuid the shop you want to find it in
    * @param dayProductsList the products to browse for you product price
    * @return
    */
  def getProductPriceFromProducts(productId: Int, shopUuid: String, dayProductsList: Stream[Products]): Double = {
    dayProductsList
      .find(dayProducts => dayProducts.metaData.shopUuid == shopUuid)
      .get.products.find(product => product.productId == productId)
    match {
      case Some(prod) => prod.price
      case None => 0.0
    }
  }
}
