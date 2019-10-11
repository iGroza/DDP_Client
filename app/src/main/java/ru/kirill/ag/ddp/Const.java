package ru.kirill.ag.ddp;

public class Const
{
	//Класс для хранения констант
	
	//подписки
	    public static enum subs{
		PRODUCTS("products"), CATEGORIES("categories"),USERS("users");

		public String fieldId;

		public subs(String fieldId){
			this.fieldId = fieldId;
		}
		@Override
		public String toString() {
			return this.fieldId;
		}
	}
	
	//тип сообщений
	public enum DdpMessage {
		MSG("msg"), ID("id"), METHOD("method"), PARAMS("params"), NAME("name"), SERVER_ID("server_id"),
		SESSION("session"), VERSION("version"), SUPPORT("support");

		private String fieldId;

		private DdpMessage(String fieldId){
			this.fieldId = fieldId;
		}
		@Override
		public String toString() {
			return this.fieldId;
		}
	}
	
	//тип сообщений для Observer
	public enum OBSMessage {
		ADDED("added"), CHANGED("changed"), REMOVED("removed"),CLEARED("cleared");

		private String fieldId;

		private OBSMessage(String fieldId){
			this.fieldId = fieldId;
		}
		@Override
		public String toString() {
			return this.fieldId;
		}
	}
	
	//Поля параметра field
	public enum DdpField {
		CATEGORY_ID("CategoryId"), ID("id"), COLLECTION("collection"), FIELDS("fields"), NAME("Name"), PARRENT_ID("ParentId"),
		POSITION("Position"), PRICE("Price"), TAX_ID("TaxId"),SKU("Sku"),CREATED_ON_UTC("CreatedOnUTC"),UPDATED_ON_UTC("UpdatedOnUTC");

		public String fieldId;

		public DdpField(String fieldId){
			this.fieldId = fieldId;
		}

		@Override
		public String toString() {
			return this.fieldId;
		}
	}
}
