package adapter;

public interface ChooseAuto {
	public boolean setOptionChoice(String automobileKey, String optionSetName, String optionName);

	public String getOptionChoice(String automobileKey, String optionSetName);

	public Double getOptionChoicePrice(String automobileKey, String optionSetName);
}
