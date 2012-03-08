import java.util.Set;


public class FilterOptions {

	
	private boolean hideAll;
	
	private Set<String> continFilterCustom;
	private ContinFilterType continFilterType=ContinFilterType.all;
	private ContinFilterType previousContinFilterType=ContinFilterType.none;
	
	private Set<String> objectFilterCustom;
	private ObjectFilterType objectFilterType=ObjectFilterType.all;
	private ObjectFilterType previousObjectFilterType=ObjectFilterType.none;
	
	private boolean objectShowNumber=false;
	private boolean objectShowColorCode=true;
	private boolean objectShowContin=true;
	
	private Integer[] continNums;
	
	
	public ObjectFilterType getPreviousObjectFilterType() {
		return previousObjectFilterType;
	}

	public void setPreviousObjectFilterType(ObjectFilterType previousObjectFilterType) {
		this.previousObjectFilterType = previousObjectFilterType;
	}

	

	public ContinFilterType getPreviousContinFilterType() {
		return previousContinFilterType;
	}

	public void setPreviousContinFilterType(ContinFilterType previousContinFilterType) {
		this.previousContinFilterType = previousContinFilterType;
	}

	public Integer[] getContinNums() {
		return continNums;
	}

	public void setContinNums(Integer[] continNums) {
		this.continNums = continNums;
	}

	public static enum ContinFilterType {
		all, none, custom_number,custom_name,custom_color_code
	}

	public static enum ObjectFilterType {
		all, none, custom_number,custom_contin_name,custom_contin_number
	}

	
	public ContinFilterType getContinFilterType() {
		return continFilterType;
	}

	public void setContinFilterType(ContinFilterType continFilterType) {
		this.continFilterType = continFilterType;
	}

	
	public ObjectFilterType getObjectFilterType() {
		return objectFilterType;
	}

	public void setObjectFilterType(ObjectFilterType objectFilterType) {
		this.objectFilterType = objectFilterType;
	}

	public boolean isObjectShowNumber() {
		return objectShowNumber;
	}

	public void setObjectShowNumber(boolean objectShowNumber) {
		this.objectShowNumber = objectShowNumber;
	}

	public boolean isObjectShowColorCode() {
		return objectShowColorCode;
	}

	public void setObjectShowColorCode(boolean objectShowColorCode) {
		this.objectShowColorCode = objectShowColorCode;
	}

	public boolean isObjectShowContin() {
		return objectShowContin;
	}

	public void setObjectShowContin(boolean objectShowContin) {
		this.objectShowContin = objectShowContin;
	}

	public Set<String> getContinFilterCustom() {
		return continFilterCustom;
	}

	public void setContinFilterCustom(Set<String> continFilterCustom) {
		this.continFilterCustom = continFilterCustom;
	}

	public Set<String> getObjectFilterCustom() {
		return objectFilterCustom;
	}

	public void setObjectFilterCustom(Set<String> objectFilterCustom) {
		this.objectFilterCustom = objectFilterCustom;
	}

	public boolean isHideAll() {
		return hideAll;
	}

	public void setHideAll(boolean hideAll) {
		this.hideAll = hideAll;
	}
	
	
}