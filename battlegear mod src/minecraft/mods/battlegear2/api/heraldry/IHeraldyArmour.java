package mods.battlegear2.api.heraldry;

public interface IHeraldyArmour extends IHeraldryItem{
	
	public String getBaseArmourPath(int armourSlot);

    public String getPatternArmourPath(HeraldryPattern pattern, int armourSlot);
}
