package com.examples.gg.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Match implements Parcelable{
	
	public final static int LIVE = 1;
	public final static int NOTSTARTED = 2;
	public final static int ENDED = 3;
	
	private String teamName1;
	private String teamName2;
	
	private String teamIcon1;
	private String teamIcon2;
	
	private String score;
	
	private String gosuLink;
	
	private String time;
	
	public int getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(int matchStatus) {
		this.matchStatus = matchStatus;
	}

	private int matchStatus;
		
	public Match() {
		// TODO Auto-generated constructor stub
	}

	public Match(Parcel in) {
		// TODO Auto-generated constructor stub
		teamName1 = in.readString();
		teamName2 = in.readString();
		teamIcon1 = in.readString();
		teamIcon2 = in.readString();
		
		score = in.readString();
		
		gosuLink = in.readString();
		
		time = in.readString();
		matchStatus = in.readInt();

	}

	public String getTeamName1() {
		return teamName1;
	}

	public void setTeamName1(String teamName1) {
		this.teamName1 = teamName1;
	}

	public String getTeamName2() {
		return teamName2;
	}

	public void setTeamName2(String teamName2) {
		this.teamName2 = teamName2;
	}

	public String getTeamIcon1() {
		return teamIcon1;
	}

	public void setTeamIcon1(String teamIcon1) {
		this.teamIcon1 = teamIcon1;
	}

	public String getTeamIcon2() {
		return teamIcon2;
	}

	public void setTeamIcon2(String teamIcon2) {
		this.teamIcon2 = teamIcon2;
	}

	public String getGosuLink() {
		return gosuLink;
	}

	public void setGosuLink(String gosuLink) {
		this.gosuLink = gosuLink;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
		public Match createFromParcel(Parcel in) {
			return new Match(in);
		}

		public Match[] newArray(int size) {
			return new Match[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		
		dest.writeString(teamName1);
		dest.writeString(teamName2);
		dest.writeString(teamIcon1);
		dest.writeString(teamIcon2);
		
		dest.writeString(score);
		
		dest.writeString(gosuLink);
		
		dest.writeString(time);
		dest.writeInt(matchStatus);

		
	}	
	
}
