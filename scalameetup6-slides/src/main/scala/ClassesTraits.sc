import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

abstract class IntQueue {
  def get(): Int

  def put(x: Int): Unit
}

class BasicIntQueue extends IntQueue {
  private val list = ArrayBuffer[Int]()

  def get(): Int = list.remove(0)

  def put(x: Int) = {
    list += x
  }
}

trait Incrementing extends IntQueue {
  abstract override def put(x: Int): Unit = super.put(x + 1)
}

trait Filter extends IntQueue {
  abstract override def put(x: Int): Unit = if (x >= 0) super.put(x)
}

trait Doubling extends IntQueue {
  abstract override def put(x: Int): Unit = super.put(2 * x)
}

val queue = new BasicIntQueue with Doubling with Incrementing

queue.put(1)

queue.put(0)

queue.put(-1)

queue.get()

queue.get()

queue.get()
