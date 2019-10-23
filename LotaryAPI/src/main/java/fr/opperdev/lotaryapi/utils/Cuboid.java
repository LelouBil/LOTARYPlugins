package fr.opperdev.lotaryapi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

public class Cuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {
	
	World w;
	int xmax;
	int xmin;
	int ymax;
	int ymin;
	int zmax;
	int zmin;
	
	/**
	 * 
	 * @param c
	 */
	public Cuboid(Cuboid c){
		w = c.getWorld();
		xmax = c.getXmax();
		xmin = c.getXmin();
		ymax = c.getYmax();
		ymin = c.getYmin();
		zmax = c.getZmax();
		zmin = c.getZmin();
	}
	
	/**
	 * 
	 * @param l1
	 * @param l2
	 */
	public Cuboid(Location l1, Location l2){
		if(l1.getWorld().getName().equals(l2.getWorld().getName())){
			w = l1.getWorld();
		    xmax = Math.max(l1.getBlockX(), l2.getBlockX());
		    xmin = Math.min(l1.getBlockX(), l2.getBlockX());
		    ymax = Math.max(l1.getBlockY(), l2.getBlockY());
		    ymin = Math.min(l1.getBlockY(), l2.getBlockY());
		    zmax = Math.max(l1.getBlockZ(), l2.getBlockZ());
		    zmin = Math.min(l1.getBlockZ(), l2.getBlockZ());
		}
	}
	
	/**
	 * 
	 * @param xmax
	 * @param xmin
	 * @param ymax
	 * @param ymin
	 * @param zmax
	 * @param zmin
	 * @param world
	 */
	public Cuboid(int xmax, int xmin, int ymax, int ymin, int zmax, int zmin, World world){
		w = world;
	    this.xmax = xmax;
	    this.xmin = xmin;
	    this.ymax = ymax;
	    this.ymin = ymin;
	    this.zmax = zmax;
	    this.zmin = zmin;
	}
	
	/**
	 * 
	 * @param map
	 */
	public Cuboid(Map<String, Object> map){
	    this.xmax = ((Integer)map.get("xmax")).intValue();
	    this.xmin = ((Integer)map.get("xmin")).intValue();
	    this.ymax = ((Integer)map.get("ymax")).intValue();
	    this.ymin = ((Integer)map.get("ymin")).intValue();
	    this.zmax = ((Integer)map.get("zmax")).intValue();
	    this.zmin = ((Integer)map.get("zmin")).intValue();
	    this.w = Bukkit.getServer().getWorld(map.get("world").toString());
	}
	
	/**
	 * @return Iterator<Block>
	 */
	public Iterator<Block> iterator(){
		return new CuboidIterator(new Cuboid(this.xmax, this.xmin, this.ymax, this.ymin, this.zmax, this.zmin, this.w));
	}
	
	/**
	 * 
	 * @param player
	 * @return boolean
	 */
	public boolean isInside(Player player){
		return isInside(player.getLocation());
	}
	
	/**
	 * 
	 * @param block
	 * @return boolean
	 */
	public boolean isInside(Block block){
		return isInside(block.getLocation());
	}
	
	/**
	 * @param loc
	 * @return boolean
	 */
	public boolean isInside(Location loc) {
		if ((this.xmin <= loc.getX()) && (this.xmax >= loc.getX()) && (this.ymin <= loc.getY()) && (this.ymax >= loc.getY()) && (this.zmin <= loc.getZ()) && (this.zmax >= loc.getX()) && (this.w.getName().equals(loc.getWorld().getName())))return true;
		return false;
	}
	
	/**
	 * @return Map<String, Object>
	 */
	public Map<String, Object> serialize(){
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("world", getWorld().toString());
	    map.put("xmax", Integer.valueOf(getXmax()));
	    map.put("xmin", Integer.valueOf(getXmin()));
	    map.put("ymax", Integer.valueOf(getYmax()));
	    map.put("ymin", Integer.valueOf(getYmin()));
	    map.put("zmax", Integer.valueOf(getZmax()));
	    map.put("zmin", Integer.valueOf(getZmin()));
	    return map;
	}
	
	/**
	 * 
	 * @return int
	 */
	  public int getXmax()
	  {
	    return this.xmax;
	  }
	  
	  /**
	   * 
	   * @return int
	   */
	  public int getXmin()
	  {
	    return this.xmin;
	  }
	  
	  /**
	   * 
	   * @return int
	   */
	  public int getYmax()
	  {
	    return this.ymax;
	  }
	  
	  /**
	   * 
	   * @return int
	   */
	  public int getYmin()
	  {
	    return this.ymin;
	  }
	  
	  /**
	   * 
	   * @return int
	   */
	  public int getZmax()
	  {
	    return this.zmax;
	  }
	  
	  /**
	   * 
	   * @return int
	   */
	  public int getZmin()
	  {
	    return this.zmin;
	  }
	  
	  /**
	   * 
	   * @return World
	   */
	  public World getWorld()
	  {
	    return this.w;
	  }
	  
	  /**
	   * 
	   * @param xmax
	   */
	  public void setXmax(int xmax)
	  {
	    this.xmax = xmax;
	  }
	  
	  /**
	   * 
	   * @param xmin
	   */
	  public void setXmin(int xmin)
	  {
	    this.xmin = xmin;
	  }
	  
	  /**
	   * 
	   * @param ymax
	   */
	  public void setYmax(int ymax)
	  {
	    this.ymax = ymax;
	  }
	  
	  /**
	   * 
	   * @param ymin
	   */
	  public void setYmin(int ymin)
	  {
	    this.ymin = ymin;
	  }
	  
	  /**
	   * 
	   * @param zmax
	   */
	  public void setZmax(int zmax)
	  {
	    this.zmax = zmax;
	  }
	  
	  /**
	   * 
	   * @param zmin
	   */
	  public void setZmin(int zmin)
	  {
	    this.zmin = zmin;
	  }
	  
	  /**
	   * 
	   * @param world
	   */
	  public void setWorld(World world)
	  {
	    this.w = world;
	  }
	  
	  /**
	   * 
	   * @author user
	   *
	   */
	  public class CuboidIterator implements Iterator<Block> {
		    Cuboid cci;
		    World wci;
		    int baseX;
		    int baseY;
		    int baseZ;
		    int sizeX;
		    int sizeY;
		    int sizeZ;
		    private int x;
		    private int y;
		    private int z;
		    ArrayList<Block> blocks;
		    Map<Location, Material> blocks2;
		    ArrayList<Location> blocks3;
		    
		    /**
		     * 
		     * @param c
		     */
		    public CuboidIterator(Cuboid c){
		        this.cci = c;
		        this.wci = c.getWorld();
		        this.baseX = Cuboid.this.getXmin();
		        this.baseY = Cuboid.this.getYmin();
		        this.baseZ = Cuboid.this.getZmin();
		        this.sizeX = (Math.abs(Cuboid.this.getXmax() - Cuboid.this.getXmin()) + 1);
		        this.sizeY = (Math.abs(Cuboid.this.getYmax() - Cuboid.this.getYmin()) + 1);
		        this.sizeZ = (Math.abs(Cuboid.this.getZmax() - Cuboid.this.getZmin()) + 1);
		        this.x = (this.y = this.z = 0);
		    }
		    
		    /**
		     * 
		     * @return Cuboid
		     */
		    public Cuboid getCuboid(){
		    	return cci;
		    }
		    
		    /**
		     * @return boolean
		     */
		    public boolean hasNext(){
		    	return (this.x < this.sizeX) && (this.y < this.sizeY) && (this.z < this.sizeZ);
		    }
		    
		    public Block next(){
		        Block b = Cuboid.this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
		        if (++this.x >= this.sizeX)
		        {
		          this.x = 0;
		          if (++this.y >= this.sizeY)
		          {
		            this.y = 0;
		            this.z += 1;
		          }
		        }
		        return b;
		    }
		    
		    public void remove() {}
		    
		    public Map<Location, Material> getBlockAtLocations(){
		        this.blocks2 = new HashMap<Location, Material>();
		        for (int x = this.cci.getXmin(); x <= this.cci.getXmax(); x++) {
		          for (int y = this.cci.getYmin(); y <= this.cci.getYmax(); y++) {
		            for (int z = this.cci.getZmin(); z <= this.cci.getZmax(); z++) {
		              this.blocks2.put(new Location(this.cci.getWorld(), x, y, z), Cuboid.this.getWorld().getBlockAt(x, y, z).getType());
		            }
		          }
		        }
		        return this.blocks2;
		    }
		    
		    public Collection<Location> getLocations(){
		        this.blocks3 = new ArrayList<Location>();
		        for (int x = this.cci.getXmin(); x <= this.cci.getXmax(); x++) {
		          for (int y = this.cci.getYmin(); y <= this.cci.getYmax(); y++) {
		            for (int z = this.cci.getZmin(); z <= this.cci.getZmax(); z++) {
		              this.blocks3.add(new Location(this.wci, x, y, z));
		            }
		          }
		        }
		        return this.blocks3;
		    }
		    
		    public Collection<Block> iterateBlocks(){
		        this.blocks = new ArrayList<Block>();
		        for (int x = this.cci.getXmin(); x <= this.cci.getXmax(); x++) {
		          for (int y = this.cci.getYmin(); y <= this.cci.getYmax(); y++) {
		            for (int z = this.cci.getZmin(); z <= this.cci.getZmax(); z++) {
		              this.blocks.add(this.cci.getWorld().getBlockAt(x, y, z));
		            }
		          }
		        }
		        return this.blocks;
		    }
	  }
	  
	    public Cuboid clone() throws CloneNotSupportedException {
	    	return new Cuboid(this);
	    }
	  
}

