package com.rs.dota;

import java.util.ArrayList;

import android.content.SharedPreferences;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.examples.gg.data.Tip;
import com.google.gson.Gson;

public class TipsValidator {
	private SherlockFragmentActivity mSfa;

	public TipsValidator(SherlockFragmentActivity sfa) {
		this.mSfa = sfa;

	}

	public void Validation() {

		SharedPreferences tipsPrefs = mSfa.getSharedPreferences("Tips", 0);
		SharedPreferences.Editor tipsEditor = tipsPrefs.edit();

		String json = tipsPrefs.getString("json", "");	
		if (json.equals("")) {
			// No Tips stored, storing the tips
			Gson gson = new Gson();
			ArrayList<Tip> tips = new ArrayList<Tip>();
			
			// Adding tips
			tips.add(new Tip("Your Goal","Your goal should be to hit at least 5 out of 6 minions every wave. If you watch streamers they will literally beat themselves up if they don't hit every single creep. In order to be the best you need to make the least mistakes as possible."));
			tips.add(new Tip("How to improve last hitting","Last hitting is a skill that need s to be practiced. Set your goal to hit every creep and make it happen. You probably do not sit and wonder how professional athletes are so good. It is known that they are perfectionists and practice non stop. Also, when playing ranged carry, set attack damage runes and your last hitting will improve greatly."));
			tips.add(new Tip("Never stop farming","Some players stop farming during the later stages of the game and this really cripples them during team fights. You want to make sure you are always going for last hits and farming the jungle so you can be really strong when it comes time to team fight."));
			tips.add(new Tip("Think like a winner","Winners can always salvage a situation and always believe they can win no matter what the odds. Be a winner and never accept defeat."));
			tips.add(new Tip("Never Rage","Rage really does nothing when you are trying to win games. It really just makes people play worse and get upset. Do not point out mistakes and try to keep everyone positive."));
			tips.add(new Tip("Learn all the roles ","There is always someone at the bottom of the team that says that he does not play support. As annoying as this may be, it is vital to know each role and play them well. If you do not know every role, learn them. You will win a lot more games if you are a versatile player."));
			tips.add(new Tip("Always ward the river","As a support player, it is your job to always have the river warded when your lane is exposed. I say when your lane is exposed because if your lane is being pushed there is no threat of a river gank. This means dragon should be warded buy tri-bush can go without a ward. You will want to conserve your ward so you can use it latter in the game."));
			tips.add(new Tip("Counter dragon wards with pink wards","Pink wards allow vision and the ability to kill enemy wards. This will not only give your team control over dragon, but it will also blind them to ganks by your jungler. If the enemy goes back and buys a pink ward to kill yours, go back. Never lose control of dragon."));
			tips.add(new Tip("Ward the enemy wraith camp at the start","This lets you know where the jungler is so you can either go aggressive or fall back and know when a gank is coming. This helps everyone on your team and can really be helpful in setting up and preventing kills."));
			tips.add(new Tip("Do not take free damage","Keep an eye on your positioning and make sure you are never in a position to take damage. You always want to trade damage and never want to get hit without responding."));
			tips.add(new Tip("Buy an early oracles","An early oracles will give your team a huge advantage. Killing enemy wards basically is like breaking their legs. They will be crippled for the rest of the game."));
			tips.add(new Tip("Stack gold per 5 items","Since you won't be taking the minion kills in lane, you will need a source of income. This is why you need to buy gold per five items. Try and get these items as soon as possible. Also, set up your runes and masteries to get you gold per five as well. This will help you buy more wards an support items to help your team."));
			tips.add(new Tip("They die too much","For the most part, supports tend to be extremely squishy. This makes them targets and easy to die. Be better than noobs. Do not feed and win games. If you have trouble dying a lot as supports, play tank-like supports like Taric, Alistar, and Leona."));
			tips.add(new Tip("Minions hurt","Almost never go for a fight if the enemy got creep advantage or you lose the fight due to creep damage."));
			tips.add(new Tip("Don't be overly aggressive","Don't be overly aggressive if you don't know where the enemy jungler is and you can't handle 1vs2"));
			tips.add(new Tip("Don't force fights","When your team is behind, don't force fights out of despair. Instead, try to stall and wait for them to make mistakes"));
			tips.add(new Tip("Respawn timers","buffs/drake/baron: 5/6/7 minutes"));
			tips.add(new Tip("Don't force ganks.","If there is no lane to gank for you and no opportunity to countergank, just farm up."));
			tips.add(new Tip("Not pushing the lane","A lot of people make the mistake of not pushing the lane top completely to the tower. Use this to your advantage and try to freeze the lane near your tower so can deny him farm and be safe from ganks"));
			tips.add(new Tip("First gank","Most of the time the first gank happens top lane. If you know you can't win 2vs2 with your jungler, don't push early and play it safe"));
			tips.add(new Tip("Freezing a lane","When freezing a lane, don't let it push too hard. Kill just enough creeps so that it barely pushes into your direction or you might die in a dive/gank due to too much creep damage"));
			tips.add(new Tip("Go tanky","When losing a lane, go tanky. If you build damage and the enemy is smart, he will keep killing you because he will have more damage than you. You won't be useful this way later on as opposed to being a tank that can still help your team"));
			tips.add(new Tip("Trade tower for drake","If they are 5man grouped to take dragon and you have no TP and can't join them, push the tower and try to trade tower for drake"));
			tips.add(new Tip("Roam","Only roam if you can guarantee a kill. Don't waste your time going bot if you have no CC and can't make anything happen or you will lose too much farm"));
			tips.add(new Tip("Try to keep up in xp.","Don't leave a lane when there are many creeps and soak up the xp"));

			

			// Commit all tips
			json = gson.toJson(tips);
			tipsEditor.putString("json", json);
			tipsEditor.commit();

		}
	}
}
