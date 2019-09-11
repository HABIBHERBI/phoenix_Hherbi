package phenix.computeEngine

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import phenix.containers._

/**
  * Does the calculations to get indicators (sales, turnover) according to cases for one day.
  * Depends on per day calculations done by DayKpiCalculator, wich you can get sorted and truncated through Orchestrator.
  */



object WeekKpiCalculator extends LazyLogging with Calculator {
  /**
    * Does the calculations for the last 7 days given.
    * Per day calculations need to be done prior to this
    *
    * @param completeDayKpis a Stream of 7 or less CompleteDayKpis, should be filtered before to avoid having too many or irrelevant dates.
    */
  def computeWeekKpi(lastDayDate: LocalDate, completeDayKpis: Stream[CompleteDayKpi]): CompleteWeekKpi = {
    val allShopSales = completeDayKpis.flatMap(completeDayKpi =>  completeDayKpi.dayShopSales)
    val weekShopSales = computeWeekShopSales(allShopSales)

    val allGlobalSales = completeDayKpis.map(completeDayKpi => completeDayKpi.dayGlobalSales)
    val weekGlobalSales = computeWeekGlobalSales(allGlobalSales)

    val allShopTurnovers = completeDayKpis.flatMap(completeDayKpi =>  completeDayKpi.dayShopTurnovers)
    val weekShopTurnovers = computeWeekShopTurnovers(allShopTurnovers)

    val allGlobalTurnovers = completeDayKpis.map(completeDayKpi => completeDayKpi.dayGlobalTurnover)
    val weekGlobalTurnovers = computeWeekGlobalTurnovers(allGlobalTurnovers)

    CompleteWeekKpi(lastDayDate, weekShopSales, weekGlobalSales, weekShopTurnovers, weekGlobalTurnovers)
  }

  def computeWeekShopSales(allShopSales: Stream[ShopSale]): Stream[ShopSale] = {
    allShopSales
      .groupBy(shopSale => shopSale.shopUuid)
      .map(shopSaleByUuidMap => (shopSaleByUuidMap._1,
        shopSaleByUuidMap._2
          .flatMap(shopSale => shopSale.productSales)
          .groupBy(productSale => productSale.productId)
          .mapValues(groupedByIdProductSale => {
            groupedByIdProductSale.foldLeft(0)((acc, productSale2) => acc + productSale2.quantity)
          })
          .map(weekProductSaleTuple => {
            ProductSale(weekProductSaleTuple._1, weekProductSaleTuple._2)
          }).toStream
      ))
      .map(weekResultMap => {
        ShopSale(weekResultMap._1, weekResultMap._2)
      }).toStream
  }

  def computeWeekGlobalSales(allGlobalSales: Stream[GlobalSale]): GlobalSale = {
    GlobalSale(allGlobalSales
      .flatMap(globalSale => globalSale.productSales)
      .groupBy(productSale => productSale.productId)
      .mapValues(groupedByIdProductSale => {
        groupedByIdProductSale.foldLeft(0)((acc, productSale2) => acc + productSale2.quantity)
      })
      .map(weekProductSaleTuple => {
        ProductSale(weekProductSaleTuple._1, weekProductSaleTuple._2)
      }).toStream)
  }

  def computeWeekShopTurnovers(allShopTurnovers: Stream[ShopTurnover]): Stream[ShopTurnover] = {
    allShopTurnovers
      .groupBy(shopSale => shopSale.shopUuid)
      .map(shopTurnoverByUuidMap => (shopTurnoverByUuidMap._1,
        shopTurnoverByUuidMap._2
          .flatMap(shopTurnover => shopTurnover.productTurnovers)
          .groupBy(productTurnover => productTurnover.productId)
          .mapValues(groupedByIdProductTurnover => {
            groupedByIdProductTurnover.foldLeft(0.0)((acc, productTurnover2) => roundValue(acc + productTurnover2.turnover))
          })
          .map(weekProductTurnoverTuple => {
            ProductTurnover(weekProductTurnoverTuple._1, weekProductTurnoverTuple._2)
          }).toStream
      ))
      .map(weekResultMap => {
        ShopTurnover(weekResultMap._1, weekResultMap._2)
      }).toStream
  }

  def computeWeekGlobalTurnovers(allGlobalTurnovers: Stream[GlobalTurnover]): GlobalTurnover = {
    GlobalTurnover(allGlobalTurnovers
      .flatMap(globalTurnover => globalTurnover.productTurnovers)
      .groupBy(productTurnover => productTurnover.productId)
      .mapValues(groupedByIdProductTurnover => {
        groupedByIdProductTurnover.foldLeft(0.0)((acc, productTurnover2) => roundValue(acc + productTurnover2.turnover))
      })
      .map(weekProductSaleTuple => {
        ProductTurnover(weekProductSaleTuple._1, weekProductSaleTuple._2)
      }).toStream)
  }


}
