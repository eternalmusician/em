package com.run4coloncancer.Eternal_Musician;

class ChordProgs {
	
	static final int[][] CommonProgs = {{1, 4, 5, 1}, {1, 5, 4}, {1, 6, 4, 5}, {1, 5, 6, 4}, {1, 4, 1, 5}, {1, 3, 4, 1}, {1, 2, 3, 4, 5}, {1, 4, 5, 5}, {1, 1, 4, 5}, {1, 4, 1, 5}, {1, 4, 5, 4}};
	
	static int[] getChord (int Octave, int chordNum) {
		chordNum = chordNum - 1;
		int [] Chord = {Octave+chordNum, Octave+2+chordNum, Octave+4+chordNum};
		return Chord;
	}
	
	static int[][] getChords (int[] KeyNotes) {
		int firstNote = 8 * ( (int) (Math.random() * ((KeyNotes.length-8) / 8) ) );
		int a = (int) (Math.random()*CommonProgs.length);
		int [] ChordNums = CommonProgs[a];
		int[][] Chords = new int[ChordNums.length][3];
		for (int i = 0; i < ChordNums.length; i++) {
			Chords[i] = getChord(firstNote, ChordNums[i]);
		}
		return Chords;
	}
	
}