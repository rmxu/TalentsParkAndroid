package de.fhkl.mHelloWorld.implementation;

import android.location.Location;

/**
 * Small class to combine 
 * a Name and a Location.
 * Hit me, I didn't use Getters & Setters. ;) 
 */
public class Friend{
	public Location itsLocation = null;
	public String itsName = null;
	public long itsId=0;
	public Friend(Location aLocation, String aName, long aId){
		this.itsLocation = aLocation;
		this.itsName = aName;
		this.itsId = aId;
		
	}
}
