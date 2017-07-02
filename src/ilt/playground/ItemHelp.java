package ilt.playground;

public class ItemHelp {

	public String itemName;
	public String helpText;
	public boolean shown = false;
	public boolean isSection = false;
	
	public ItemHelp(String itemName, String helpText) {
		this.itemName = itemName;
		this.helpText = helpText;
	}
}
