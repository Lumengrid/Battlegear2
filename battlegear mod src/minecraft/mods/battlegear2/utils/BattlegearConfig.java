package mods.battlegear2.utils;

import mods.battlegear2.inventory.CreativeTabMB_B_2;
import mods.battlegear2.items.*;
import mods.battlegear2.recipies.DyeRecipie;
import mods.battlegear2.recipies.QuiverRecipie2;
import mods.battlegear2.recipies.ShieldRemoveArrowRecipie;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;

public class BattlegearConfig {
	public static final CreativeTabs customTab=new CreativeTabMB_B_2("Battlegear2");
	public static boolean forceBackSheath = false, arrowForceRendered = true;
	public static boolean enableGUIKeys = false, enableGuiButtons = true;
	public static final String[] itemNames = new String[] {"heraldric","chain","quiver", "dagger","waraxe","mace","spear","shield","knight.armour", "mb.arrow"};
	public static final String[] toolTypes = new String[] {"wood", "stone", "iron", "diamond", "gold"};
    public static final String[] shieldTypes = new String[] {"wood", "hide", "iron", "diamond", "gold"};
	public static final String[] armourTypes = new String[] {"helmet", "plate", "legs", "boots"};
	public static final String[] enchantsName = {"BashWeight", "BashPower", "BashDamage", "ShieldUsage", "ShieldRecovery", "BowLoot", "BowCharge"};
	public static final int firstDefaultItemIndex = 26201;
	public static int[] itemOffests = new int[]{0, 1, 2, 5, 10, 15, 20, 25, 30, 35};
	public static int[] enchantsId = {125, 126, 127, 128, 129, 130, 131};
	public static ItemWeapon[] dagger=new ItemWeapon[toolTypes.length],warAxe=new ItemWeapon[toolTypes.length],mace=new ItemWeapon[toolTypes.length],spear=new ItemWeapon[toolTypes.length];
    public static ItemShield[] shield=new ItemShield[shieldTypes.length];
	public static Item chain,quiver,heradricItem, MbArrows;
	public static Block banner;
	public static ItemBlock bannerItem;
	public static ItemArmor[] knightArmor=new ItemArmor[armourTypes.length];

	public static String[] disabledItems = new String[0];
    public static String[] disabledRecipies = new String[0];
    public static String[] disabledRenderers = new String[0];

    public static double[] skeletonArrowSpawnRate = new double[ItemMBArrow.names.length];
	public static int quiverBarOffset = 0, shieldBarOffset = 0;
	
	public static void getConfig(Configuration config) {
		config.load();
		
		StringBuffer sb = new StringBuffer();
        sb.append("This will disable completely the provided item, along with their renderers and recipes including them.\n");
        sb.append("These should all be placed on separate lines between the provided \'<\' and \'>\'.  \n");
        sb.append("The valid values are: \n");
        int count = 0;
        for(int i = 1; i < itemNames.length; i++){
            sb.append(itemNames[i]);
            sb.append(", ");
            count++;
            if(count % 5 == 0){
                sb.append("\n");
            }
        }
        disabledItems = config.get(config.CATEGORY_GENERAL, "Disabled Items", new String[0], sb.toString()).getStringList(); 
        Arrays.sort(disabledItems);

        heradricItem = new HeraldryCrest(config.getItem(itemNames[0], firstDefaultItemIndex).getInt());

        if(Arrays.binarySearch(disabledItems, itemNames[1]) < 0){
        	chain = new Item(config.getItem(itemNames[1], firstDefaultItemIndex+itemOffests[1]).getInt());
        	chain.setUnlocalizedName("battlegear2:"+itemNames[1]).setTextureName("battlegear2:"+itemNames[1]).setCreativeTab(customTab);
        }
        enableGUIKeys=config.get(config.CATEGORY_GENERAL, "Enable GUI Keys", false).getBoolean(false);
        enableGuiButtons=config.get(config.CATEGORY_GENERAL, "Enable GUI Buttons", true).getBoolean(true);
        
        for(int i=0; i<enchantsName.length; i++){
        	enchantsId[i] = config.get("EnchantmentsID", enchantsName[i], enchantsId[i]).getInt();
        }
        config.get("Coremod", "ASM debug Mode", false, "Only use for advanced bug reporting when asked by a dev.");
        
        if(Arrays.binarySearch(disabledItems, itemNames[2]) < 0){
        	quiver = new ItemQuiver(config.getItem(itemNames[2], firstDefaultItemIndex+2).getInt());
        	quiver.setUnlocalizedName("battlegear2:"+itemNames[2]).setTextureName("battlegear2:quiver/"+itemNames[2]).setCreativeTab(customTab);
        }
        if(Arrays.binarySearch(disabledItems, itemNames[9]) < 0){
        	MbArrows = new ItemMBArrow(config.getItem(itemNames[9], firstDefaultItemIndex+itemOffests[9]).getInt());
        	MbArrows.setUnlocalizedName("battlegear2:" + itemNames[9]).setTextureName("battlegear2:" + itemNames[9]).setCreativeTab(customTab).setContainerItem(Item.arrow);
        }
        String category = "Skeleton CustomArrow Spawn Rate";
        config.addCustomCategoryComment(category, "The spawn rate (between 0 & 1) that Skeletons will spawn with Arrows provided from this mod");

        //default 10% for everything but ender (which is 0%)
        for(int i = 0; i < ItemMBArrow.names.length; i++){
            skeletonArrowSpawnRate[i] = config.get(category, ItemMBArrow.names[i], i!=1 && i!=5?0.1F:0).getDouble(i!=1?0.1F:0);
        }
        
        sb = new StringBuffer();
        sb.append("This will disable the crafting recipie for the provided item/blocks.\n");
        sb.append("It should be noted that this WILL NOT remove the item from the game, it will only disable the recipe.\n");
        sb.append("In this way the items may still be obtained through creative mode and cheats, but playes will be unable to craft them.\n");
        sb.append("These should all be placed on separate lines between the provided \'<\' and \'>\'. The valid values are: \n");

        count = 0;
        for(int i = 1; i < itemNames.length; i++){

            if(i != 9){
                sb.append(itemNames[i]);
                sb.append(", ");
                count++;
                if(count % 5 == 0){
                    sb.append("\n");
                }
            }
        }

        for(int i = 0; i < ItemMBArrow.names.length; i++){
            sb.append(itemNames[9]);
            sb.append('.');
            sb.append(ItemMBArrow.names[i]);
            sb.append(", ");
            count++;
            if(count % 5 == 0){
                sb.append("\n");
            }
        }

        int last_comma = sb.lastIndexOf(",");
        if(last_comma > 0){
            sb.deleteCharAt(last_comma);
        }
        disabledRecipies = config.get(config.CATEGORY_GENERAL, "Disabled Recipies", new String[0], sb.toString()).getStringList();
        Arrays.sort(disabledRecipies);

        category = "Rendering";
        config.addCustomCategoryComment(category, "This category is client side, you don't have to sync its values with server in multiplayer.");
        sb = new StringBuffer();
        sb.append("This will disable the special rendering for the provided item.\n");
        sb.append("These should all be placed on separate lines between the provided \'<\' and \'>\'.  \n");
        sb.append("The valid values are: spear, shield, bow, quiver");
        disabledRenderers = config.get(category, "Disabled Renderers", new String[0], sb.toString()).getStringList(); 
        Arrays.sort(disabledRenderers);
        quiverBarOffset = config.get(category, "Quiver hotbar relative horizontal position", 0, "Change to move this bar in your gui").getInt();
        shieldBarOffset = config.get(category, "Shield bar relative vertical position", 0, "Change to move this bar in your gui").getInt();
        arrowForceRendered = config.get(category, "Render arrow with bow uncharged", true).getBoolean(true);
        forceBackSheath=config.get(category, "Force Back Sheath", false).getBoolean(false);
        
        for(int i = 0; i < 5; i++){
        	EnumToolMaterial material = EnumToolMaterial.values()[i];
        	if(Arrays.binarySearch(disabledItems, itemNames[4]) < 0){
	            warAxe[i]=new ItemWaraxe(
	                    config.getItem(itemNames[4]+toolTypes[i], firstDefaultItemIndex+itemOffests[4]+i).getInt(),
	                    material, itemNames[4], i==4?2:1);
        	}
        	if(Arrays.binarySearch(disabledItems, itemNames[3]) < 0){
	        	dagger[i]=new ItemDagger(
	        			config.getItem(itemNames[3]+"_"+toolTypes[i], firstDefaultItemIndex+itemOffests[3]+i).getInt(),
	        			material, itemNames[3]);
        	}
        	if(Arrays.binarySearch(disabledItems, itemNames[5]) < 0){
	    		mace[i]=new ItemMace(
	    				config.getItem(itemNames[5]+toolTypes[i], firstDefaultItemIndex+itemOffests[5]+i).getInt(),
	    				material, itemNames[5], 0.05F + 0.05F*i);
        	}
        	if(Arrays.binarySearch(disabledItems, itemNames[6]) < 0){
	    		spear[i]=new ItemSpear(
	    				config.getItem(itemNames[6]+toolTypes[i], firstDefaultItemIndex+itemOffests[6]+i).getInt(),
	    				material, itemNames[6]);
        	}
        	if(Arrays.binarySearch(disabledItems, itemNames[7]) < 0){
	            shield[i] = new ItemShield(
	                    config.getItem(itemNames[7]+shieldTypes[i], firstDefaultItemIndex+itemOffests[7]+i).getInt(),
	                    EnumShield.values()[i]);
        	}

        }
        if (config.hasChanged()){        
        	config.save();     	
        }
	}

	public static void registerRecipes() {
		
		//2 Iron ingots = 3 chain. This is because the chain armour has increased in damage resistance

		if(chain!=null){
	        if(Arrays.binarySearch(disabledRecipies, itemNames[1]) < 0)
	            GameRegistry.addShapedRecipe(new ItemStack(chain, 3),
	                "I", "I", Character.valueOf('I'), Item.ingotIron
	            );
	        if(Arrays.binarySearch(disabledRecipies, "chain.armour") < 0){
	            //Chain armor recipes
	            GameRegistry.addRecipe(new ItemStack(Item.helmetChain), 
	                    "LLL","L L",Character.valueOf('L'),chain);
	            GameRegistry.addRecipe(new ItemStack(Item.plateChain), 
	                    "L L","LLL","LLL",Character.valueOf('L'),chain);
	            GameRegistry.addRecipe(new ItemStack(Item.legsChain), 
	                    "LLL","L L","L L",Character.valueOf('L'),chain);
	            GameRegistry.addRecipe(new ItemStack(Item.bootsChain), 
	                    "L L","L L",Character.valueOf('L'),chain);
	        }
		}

		if(quiver!=null){
	        //Quiver recipes :
	        if(Arrays.binarySearch(disabledRecipies, itemNames[2])  < 0)
	            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(quiver),
	                "X X", "X X","XXX",Character.valueOf('X'), Item.leather));
	
	        GameRegistry.addRecipe(new QuiverRecipie2());
		}

        GameRegistry.addRecipe(new DyeRecipie());

        
		//Weapon recipes
		String woodStack = "plankWood";
		for(int i = 0; i < 5; i++){
			Item craftingMaterial = Item.itemsList[EnumToolMaterial.values()[i].getToolCraftingMaterial()];
            if(warAxe[i]!=null && Arrays.binarySearch(disabledRecipies, itemNames[4])  < 0){
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(warAxe[i]), "L L","LSL"," S ",
                            Character.valueOf('S'), "stickWood",
                            Character.valueOf('L'),
                            i!=0?craftingMaterial:woodStack));
            }
            if(mace[i]!=null && Arrays.binarySearch(disabledRecipies, itemNames[5])  < 0) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(mace[i]), " LL"," LL","S  ",
                                Character.valueOf('S'), "stickWood",
                                Character.valueOf('L'),
                                i!=0?craftingMaterial:woodStack));
            }
            if(dagger[i]!=null && Arrays.binarySearch(disabledRecipies, itemNames[3])  < 0){
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(dagger[i]), "L","S",
                                Character.valueOf('S'), "stickWood",
                                Character.valueOf('L'),
                                i!=0?craftingMaterial:woodStack));
            }

            if(spear[i]!=null && Arrays.binarySearch(disabledRecipies, itemNames[6])  < 0){
                if(i == 0){
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spear[i]), "  S"," S ","S  ",
                                    Character.valueOf('S'), "stickWood"));
                }else{
                    GameRegistry.addRecipe(new ItemStack(spear[i]), " L","S ",
                                    Character.valueOf('S'), spear[0],
                                    Character.valueOf('L'), craftingMaterial);
                }
            }
		}

        if(Arrays.binarySearch(disabledItems, itemNames[7]) < 0 && Arrays.binarySearch(disabledRecipies, itemNames[7]) < 0){
            //Wood Shield
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield[0]), " W ","WWW", " W ",
                            Character.valueOf('W'), woodStack));
            //Hide Shield
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield[1]), " H ","HWH", " H ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('H'), Item.leather));
            //Iron Shield
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield[2]), "I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.ingotIron));
            //Diamond Shield
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield[3]), "I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.diamond));
            //Gold Shield
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(shield[4]), "I I","IWI", " I ",
                            Character.valueOf('W'), woodStack,
                            Character.valueOf('I'), Item.ingotGold));
            GameRegistry.addRecipe(new ShieldRemoveArrowRecipie());
        }

        if(MbArrows!=null){
	        for(int i=0;i<ItemMBArrow.component.length;i++){
		        if(Arrays.binarySearch(disabledRecipies, itemNames[9]+"."+ItemMBArrow.names[i]) < 0){
		            GameRegistry.addRecipe(new ItemStack(MbArrows, 1, i), "G","A",
		                            Character.valueOf('G'), ItemMBArrow.component[i],
		                            Character.valueOf('A'), Item.arrow
		                    );
		            if(i!=2 && i!=3){//We can't have those components being duplicated by an "Infinity" bow
		            	GameRegistry.addShapelessRecipe(new ItemStack(ItemMBArrow.component[i]), new ItemStack(MbArrows, 1, i));
		            }
		        }
	        }
        }

		
		for(int i = 0; i < 4; i++){
			//GameRegistry.addRecipe(new KnightArmourRecipie(i));
		}

        /*
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				ItemStack bannerStack = new ItemStack(bannerItem);
				((IHeraldyItem)bannerStack.getItem()).setHeraldryCode(bannerStack,
						SigilHelper.packSigil(HeraldryPattern.HORIZONTAL_BLOCK, (byte) 0, (byte) 0,
                                new Color(ItemDye.dyeColors[15 - x]), new Color(ItemDye.dyeColors[15 - y]),
                                HeraldryIcon.Blank, HeraldryPositions.SINGLE, Color.WHITE, Color.WHITE)
						);
				GameRegistry.addRecipe(bannerStack,
						" a "," b ", " S ",
						Character.valueOf('a'), new ItemStack(Block.cloth,0, x),
						Character.valueOf('b'), new ItemStack(Block.cloth,0, y),
						Character.valueOf('S'), Item.stick
						
				);
			}
		}
		
		GameRegistry.addRecipe(new ItemStack(bannerItem), 
				" W "," W ", " S ",Character.valueOf('W'),Block.cloth, Character.valueOf('S'), Item.stick);
		*/
        try{
            for(Field f: BattlegearConfig.class.getFields()){
                if(Item.class.isAssignableFrom(f.getType())){
                    Item it = (Item)f.get(null);
                    if(it!=null){
                        GameRegistry.registerItem(it, it.getUnlocalizedName());
                    }
                }
            }
        }catch(Exception e){

        }

	}

}
