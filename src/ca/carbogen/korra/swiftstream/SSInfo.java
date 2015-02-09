package ca.carbogen.korra.swiftstream;

import com.projectkorra.ProjectKorra.Ability.Combo.ComboAbilityModule;
import com.projectkorra.ProjectKorra.ComboManager;
import com.projectkorra.ProjectKorra.Element;
import com.projectkorra.ProjectKorra.ProjectKorra;
import com.projectkorra.ProjectKorra.SubElement;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by carbogen on 09/02/15.
 */
public class SSInfo
		extends ComboAbilityModule
{
	public SSInfo()
	{
		super("SwiftStream");
	}

	@Override
	public void onThisLoad()
	{
		ProjectKorra.plugin.getConfig().addDefault("ExtraAbilities.Carbogen.SwiftStream.dragFactor", 1.5);
		ProjectKorra.plugin.getConfig().addDefault("ExtraAbilities.Carbogen.SwiftStream.duration", 2000);


		ProjectKorra.plugin.saveConfig();

		SwiftStream.handle();
	}

	@Override
	public String getVersion()
	{
		return "v1.0.0";
	}

	@Override
	public String getElement()
	{
		return Element.Air.toString();
	}

	public SubElement getSubElement()
	{
		return SubElement.Flight;
	}

	@Override
	public String getAuthor()
	{
		return "Carbogen";
	}

	@Override
	public String getDescription()
	{
		return "Create a stream of air as you fly which causes nearby entities to " +
				"be thrown in your direction.";
	}

	@Override
	public String getInstructions()
	{
		return "Flight (Start Flying) " +
				"> Flight (Release Shift) " +
				"> Flight (Left Click) " +
				"> Flight (Left Click)";
	}

	@Override
	public Object createNewComboInstance(Player player)
	{
		return new SwiftStream(player);
	}

	@Override
	public ArrayList<ComboManager.AbilityInformation> getCombination()
	{
		ArrayList<ComboManager.AbilityInformation> combination = new ArrayList<ComboManager.AbilityInformation>();

		combination.add(new ComboManager.AbilityInformation("Flight", ComboManager.ClickType.SHIFTUP));
		combination.add(new ComboManager.AbilityInformation("Flight", ComboManager.ClickType.LEFTCLICK));
		combination.add(new ComboManager.AbilityInformation("Flight", ComboManager.ClickType.LEFTCLICK));

		return combination;
	}
}
