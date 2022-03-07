/**
 * external API to give to our clients like Toyota, Ford, and Honda.
 */
package adapter;

public interface UpdateAuto {
	public boolean updateOptionSetName(String automobileKey, String optionSetName, String nameNew);

	public boolean updateOptionPrice(String automobileKey, String optionSetName, String optionName, double priceNew);

	public boolean updateOptionName(String automobileKey, String optionSetName, String optionName,
		String optionNameNew);
}
