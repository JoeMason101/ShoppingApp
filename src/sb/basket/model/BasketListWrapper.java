package sb.basket.model;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



	@XmlRootElement(name = "items") 
	public class BasketListWrapper {
		private List<Items> items;
		
		@XmlElement(name = "Items")
		public List<Items> getItems() {
			return items;
		}
		
		public void setItems(List<Items> items) {
			this.items = items;
		}
	
}
