package services

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[AdvertsDataService])
trait DataService[T] {
  def add(item: T) : Option[String]
  def edit(id: Int, item: T): Option[T]
  def delete(id: Int)
  def getAllSortInt(f: T => Int): List[T]
  def getAllSortString(f: T => String): List[T]
  def get(id: Int): Option[T]
}
