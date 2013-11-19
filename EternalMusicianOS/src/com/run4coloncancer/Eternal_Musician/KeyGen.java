package com.run4coloncancer.Eternal_Musician;

class KeyGen {
	
	private int[] notes;
	
	public KeyGen () {
		notes = KeyNotesStore.KEYNOTES[(int) (Math.random() * KeyNotesStore.KEYNOTES.length)];
	}
	
	public int [] getNotes () {
		return notes;
	}
	
	public int [][] getChords () {
		return ChordProgs.getChords(notes);
	}
	
}