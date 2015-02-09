package ca.carbogen.korra.swiftstream;

import com.projectkorra.ProjectKorra.BendingPlayer;
import com.projectkorra.ProjectKorra.Methods;
import com.projectkorra.ProjectKorra.Objects.HorizontalVelocityTracker;
import com.projectkorra.ProjectKorra.ProjectKorra;
import com.projectkorra.ProjectKorra.Utilities.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by carbogen on 09/02/15.
 */
public class SwiftStream
{
	public static ConcurrentHashMap<Player, SwiftStream> instances = new ConcurrentHashMap<Player, SwiftStream>();

	public final static double dragFactor = ProjectKorra.plugin.getConfig().getDouble(
			"ExtraAbilities.Carbogen.SwiftStream.dragFactor");
	public final static long duration = ProjectKorra.plugin.getConfig().getLong(
			"ExtraAbilities.Carbogen.SwiftStream.duration");


	private Player player;
	private List<LivingEntity> affectedEntities = new ArrayList<LivingEntity>();

	private long startTime;

	public SwiftStream(Player player)
	{
		if (!isEligible(player))
			return;

		this.player = player;

		register();
		launch();
	}

	public boolean isEligible(Player player)
	{
		if (!Methods.canBend(player.getName(), "Flight"))
			return false;

		if(Methods.getBoundAbility(player) == null)
			return false;

		if (!Methods.getBoundAbility(player).equalsIgnoreCase("Flight"))
			return false;

		if (!Methods.canAirFlight(player))
			return false;

		return true;
	}

	public void register()
	{
		startTime = System.currentTimeMillis();
		instances.put(player, this);
	}

	public void launch()
	{
		Vector v = player.getEyeLocation().getDirection().normalize();

		v = v.multiply(5);
		v.add(new Vector(0, 0.2, 0));

		Methods.setVelocity(player, v);
	}

	public void affectNearby()
	{
		for (Entity e : Methods.getEntitiesAroundPoint(player.getLocation(), 2.5))
		{
			if (e instanceof LivingEntity && !affectedEntities.contains(e) && e.getEntityId() != player.getEntityId())
			{
				Vector v = player.getVelocity().clone();

				v = v.multiply(dragFactor);

				v = v.setY(player.getVelocity().getY());

				v = v.add(new Vector(0, 0.15, 0));

				Methods.setVelocity(e, v);
				affectedEntities.add((LivingEntity) e);
				new HorizontalVelocityTracker(e, player, 200);
			}
		}
	}

	public void playParticles()
	{
		float x = Methods.rand.nextFloat();
		float y = Methods.rand.nextFloat();
		float z = Methods.rand.nextFloat();

		if(Methods.rand.nextBoolean())
			x = -x;
		if(Methods.rand.nextBoolean())
			y = -y;
		if(Methods.rand.nextBoolean())
			z = -z;

		ParticleEffect.CLOUD.display(player.getLocation(), x, y, z, 0, 2);
	}

	public void progress()
	{
		if(!player.isOnline() || player.isDead())
			instances.remove(player);

		if(!isEligible(player))
			instances.remove(player);

		if(System.currentTimeMillis() > startTime + duration)
			instances.remove(player);

		playParticles();
		affectNearby();
	}

	public static void progressAll()
	{
		for(Player p : instances.keySet())
			instances.get(p).progress();
	}

	public static void handle()
	{
		ProjectKorra.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(ProjectKorra.plugin, new Runnable()
		{
			public void run()
			{
				SwiftStream.progressAll();
			}
		}, 0, 1);
	}
}
