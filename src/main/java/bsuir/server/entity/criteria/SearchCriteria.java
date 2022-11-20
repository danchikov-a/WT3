package bsuir.server.entity.criteria;

public final class SearchCriteria {

	public enum Client {
		name("name"),
		password("password");
		private final String enumName;

		Client(String enumName) {
			this.enumName = enumName;
		}

		public String getEnumName(){
			return enumName;
		}

		public static String getCriteriaName(){
			return "Client";
		}

	}

	public enum Student{
		name("name"),
		averageScore("averageScore");
		private final String enumName;

		Student(String enumName) {
			this.enumName = enumName;
		}

		public String getEnumName(){
			return enumName;
		}

		public static String getCriteriaName(){
			return "Client";
		}

	}

	private SearchCriteria() {}
}

