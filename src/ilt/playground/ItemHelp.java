package ilt.playground;

public class ItemHelp {

	public String itemName;
	public String helpText;
	public String defenceHelp;
	public boolean shown = false;
	public boolean isSection = false;
	public boolean defenceShown = false;
	
	public ItemHelp(String itemName, String helpText, String defenceHelp) {
		this.itemName = itemName;
		this.helpText = helpText;
		this.defenceHelp = defenceHelp;
	}
}
