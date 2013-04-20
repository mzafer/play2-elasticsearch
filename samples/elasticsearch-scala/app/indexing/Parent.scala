package indexing

import com.github.cleverage.elasticsearch.IndexService
import com.github.cleverage.elasticsearch.ScalaHelpers._
import play.api.libs.json._

case class Parent(id:String, content:String) extends Indexable

case class Child(id:String, parentId:String, content:String) extends Indexable

object ParentManager extends IndexableManager[Parent]{
	val indexType = "parent"
	val reads:Reads[Parent] = Json.reads[Parent]
	val writes:Writes[Parent] = Json.writes[Parent]
}

object ChildManager extends IndexableManager[Child]{
	val indexType = "child"
	val reads:Reads[Child] = Json.reads[Child]
	val writes: Writes[Child] = Json.writes[Child]

	def indexWithParent(c:Child,parentId:String) = {
		val irb = IndexService.getIndexRequestBuilder(indexPath, c.id, Json.toJson(c)(writes).toString())
		irb.setParent(parentId)
		IndexService.index(irb)
	} 
}