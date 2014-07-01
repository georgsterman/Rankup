package com.fvza.rankup;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

import com.fvza.rankup.util.Config;
import com.fvza.rankup.util.Language;

public class Ranking {
	
	public static boolean pay(Player player, Double amount){
		
		String newRank = Config.getRankToGroup( player );
		
		if ( Rankup.econ == null ){
			
			Language.send( player, "&4&lECONOMY PLUGIN MISSING. CONTACT THE OWNER");
			return false; 
			
		}
		
		EconomyResponse r = Rankup.econ.withdrawPlayer(player.getName(), amount );
		
		if(r.transactionSuccess()){
			return true; 
		} else {
			Language.noMoney( player, amount, newRank );
			return false; 
		}
	}
	
	public static boolean rankup(Player player){
		
		if( Rankup.perms.getGroups().length == 0 || !Rankup.perms.hasSuperPermsCompat() ){
			
			Language.send( player, "&8[&6&oALT&bRankup&8] &cYou do not have permission to use this command.");
			return false; 
			
		}
		
		if( !player.hasPermission("rankup.rankup")){
			Language.send( player, "&8[&6&oALT&bRankup&8] &4Please tell an owner that you are missing rankup.rankup");
		}
		
		if( Config.getRankToGroup( player ) != null ){
			
			String newRank = Config.getRankToGroup( player );
			Double rankPrice = Config.getGroupPrice( newRank );
			
			if( rankPrice < 0 ){
				Language.send( player, "notRankable");
				return false; 
			}
			
			
			boolean paid = pay( player, rankPrice );  
			
			if( paid ){
				
				if( Config.getOverride() ){

					for(String b : Rankup.perms.getPlayerGroups( player )){
						
						if(b != newRank){
							
							Rankup.perms.playerRemoveGroup(player, b);
							
						}
					}
						
				} else {
					Rankup.perms.playerRemoveGroup(player, Config.getCurrentRankableGroup( player ));
				}
			
				Rankup.perms.playerAddGroup(player, newRank);
				Language.broadcast( "&b" + player.getDisplayName() + "&8&l[&6&l&oALT&b&lRankup&8&l] &a&l" + newRank + "." );
				
				return true;
				
			}
		}
		
		return false; 
	}
}
