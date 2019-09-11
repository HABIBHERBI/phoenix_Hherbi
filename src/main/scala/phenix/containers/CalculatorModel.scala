package phenix.containers

import java.time.LocalDate

case class ProductSale(productId: Int, quantity: Int) extends Ordered[ProductSale] {
  override def compare(ps2: ProductSale): Int = this.quantity compare ps2.quantity
}



case class ProductTurnover(productId: Int, turnover: Double) extends Ordered[ProductTurnover] {
  override def compare(pt2: ProductTurnover): Int = this.turnover compare pt2.turnover
}



abstract class Shop {
  val shopUuid: String
}


case class ShopSale(shopUuid: String, productSales: Stream[ProductSale]) extends Shop {

  def sort(): ShopSale = {
    this.copy(productSales = this.productSales.sorted.reverse)
  }

  def truncateTop100(): ShopSale = {
    this.copy(productSales = productSales.take(100))
  }
}


case class GlobalSale(productSales: Stream[ProductSale]) {
  def sort(): GlobalSale = {
    this.copy(productSales = this.productSales.sorted.reverse)
  }

  def truncateTop100(): GlobalSale = {
    this.copy(productSales = productSales.take(100))
  }
}


case class ShopTurnover(shopUuid: String, productTurnovers: Stream[ProductTurnover]) extends Shop {
  def sort(): ShopTurnover = {
    this.copy(productTurnovers = this.productTurnovers.sorted.reverse)
  }

  def truncateTop100(): ShopTurnover = {
    this.copy(productTurnovers = productTurnovers.take(100))
  }
}


case class GlobalTurnover(productTurnovers: Stream[ProductTurnover]) {
  def sort(): GlobalTurnover = {
    this.copy(productTurnovers = this.productTurnovers.sorted.reverse)
  }

  def truncateTop100(): GlobalTurnover = {
    this.copy(productTurnovers = productTurnovers.take(100))
  }
}




case class CompleteDayKpi(date: LocalDate, dayShopSales: Stream[ShopSale], dayGlobalSales: GlobalSale,
                          dayShopTurnovers: Stream[ShopTurnover], dayGlobalTurnover: GlobalTurnover) {
  def sortResults(): CompleteDayKpi = {
    this.copy(
      dayShopSales = this.dayShopSales.map(_.sort()),
      dayGlobalSales = this.dayGlobalSales.sort(),
      dayShopTurnovers = this.dayShopTurnovers.map(_.sort()),
      dayGlobalTurnover = this.dayGlobalTurnover.sort()
    )
  }

  def truncateTop100(): CompleteDayKpi = {
    this.copy(
      dayShopSales = this.dayShopSales.map(_.truncateTop100()),
      dayGlobalSales = this.dayGlobalSales.truncateTop100(),
      dayShopTurnovers = this.dayShopTurnovers.map(_.truncateTop100()),
      dayGlobalTurnover = this.dayGlobalTurnover.truncateTop100()
    )
  }
}










case class CompleteDayKpi1 (date: LocalDate, dayShopSales: Stream[ShopSale], dayGlobalSales: GlobalSale) {
  def sortResults(): CompleteDayKpi1 = {
    this.copy(
      dayShopSales = this.dayShopSales.map(_.sort()),
      dayGlobalSales = this.dayGlobalSales.sort(),

    )
  }

  def truncateTop100(): CompleteDayKpi1 = {
    this.copy(
      dayShopSales = this.dayShopSales.map(_.truncateTop100()),
      dayGlobalSales = this.dayGlobalSales.truncateTop100(),
    )
  }
}












case class CompleteWeekKpi(lastDayDate: LocalDate, weekShopSales: Stream[ShopSale], weekGlobalSales: GlobalSale,
                           weekShopTurnover: Stream[ShopTurnover], weekGlobalTurnover: GlobalTurnover) {
  def sortResults(): CompleteWeekKpi = {
    this.copy(
      weekShopSales = this.weekShopSales.map(_.sort()),
      weekGlobalSales = this.weekGlobalSales.sort(),
      weekShopTurnover = this.weekShopTurnover.map(_.sort()),
      weekGlobalTurnover = this.weekGlobalTurnover.sort()
    )
  }

  def truncateTop100(): CompleteWeekKpi = {
    this.copy(
      weekShopSales = this.weekShopSales.map(_.truncateTop100()),
      weekGlobalSales = this.weekGlobalSales.truncateTop100(),
      weekShopTurnover = this.weekShopTurnover.map(_.truncateTop100()),
      weekGlobalTurnover = this.weekGlobalTurnover.truncateTop100()
    )
  }
}