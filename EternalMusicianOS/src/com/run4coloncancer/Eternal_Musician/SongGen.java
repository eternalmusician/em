package com.run4coloncancer.Eternal_Musician;

import java.io.File;
import java.util.ArrayList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.MidiEvent;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

class SongGen {
	
	private MidiTrack track;
	private MidiTrack infoTrack;
	private ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
	private  MidiFile song;
	private float beatsPerMin;
	private int[][] chords;
	private int index = 0;
	static final int[][] MEASUREPATTERNS = { {96, 48, 48, 96, 48, 48}, {192, 96, 96}, {192, 96, 48, 48}, {192, 48, 48, 96}, {192, 48, 48, 48, 48}, {96, 96, 48, 48, 96}, {48, 48, 96, 48, 48, 96}, {48, 48, 144, 144}, {24, 24, 24, 24, 96, 192}, {192, 192}, {144, 48, 192}};
	private KeyGen noteGetter;
	
	public void go (File parent) {
		makeTrack();
		setInstruments();
		noteGetter = new KeyGen ();
		chords = noteGetter.getChords();
		makeMelody(noteGetter.getNotes());
		makeVerseChords();
		makeVerseDrums();
		makeBridgeChords();
		chords = (int[][]) ArrayHandle.reverse(chords);
		makeChorusChords();
		makeChorusMelody(noteGetter.getNotes());
		makeChorusDrums();
		makeMidiFile(parent);
	}
	
	public void go (File parent, int x) {
		makeTrackBPM(x);
		setInstruments();
		noteGetter = new KeyGen ();
		chords = noteGetter.getChords();
		makeMelody(noteGetter.getNotes());
		makeVerseChords();
		makeBridgeChords();
		chords = (int[][]) ArrayHandle.reverse(chords);
		makeChorusChords();
		makeChorusMelody(noteGetter.getNotes());
		makeThrobDrums();
		makeMidiFile(parent);
	}
	
	public void makeTrack () {
		track = new MidiTrack();
		infoTrack = new MidiTrack();
		beatsPerMin = 60 + ((int) (Math.random() * 100));
		Tempo tempo = new Tempo();
		tempo.setBpm(beatsPerMin);
		infoTrack.insertEvent(tempo);
		TimeSignature timeSig = new TimeSignature ();
		timeSig.setTimeSignature(4, 4, 96, 24);
		infoTrack.insertEvent(timeSig);
	}
	
	public void makeTrackBPM (int x) {
		track = new MidiTrack();
		infoTrack = new MidiTrack();
		beatsPerMin = x;
		Tempo tempo = new Tempo();
		tempo.setBpm(beatsPerMin);
		infoTrack.insertEvent(tempo);
		TimeSignature timeSig = new TimeSignature ();
		timeSig.setTimeSignature(4, 4, 96, 24);
		infoTrack.insertEvent(timeSig);
	}
	
	public void setInstruments () {
		int[] chan1Types = {0, 4, 8, 12, 14, 40, 42, 46,  73, 75, 80, 81, 85, 88, 89, 90, 98, 114};
		int[] chan2Types = {0, 48, 51, 52, 53, 89, 92, 95};
		int chan1Type = chan1Types[(int) (Math.random() * chan1Types.length)];
		int chan2Type = chan2Types[(int) (Math.random() * chan2Types.length)];
		track.insertEvent(makeEvent(192, 1, chan1Type, 0, 0));
		track.insertEvent(makeEvent(192, 2, chan2Type, 0, 0));
	}
	
	public void makeMidiFile (File parent) {
		tracks.clear();
		tracks.add(infoTrack);
		tracks.add(track);
		File midiFile = new File(parent, "Song"+index+".mid");
		song = new MidiFile(96, tracks);
		try {
			song.writeToFile(midiFile);
		} catch (Exception ex) {ex.printStackTrace();}
	}
	
	public void makeMelody(int[] notes) {
		int[] noteLengths = MEASUREPATTERNS[(int) (Math.random() * MEASUREPATTERNS.length)];
		for (int i = 0; i<chords.length; i++) {
			int beat = 96 + (i*384);
			int firstNote = 8+chords[i][(int) (Math.random() * chords[i].length)];
			if ((Math.random()<0.5 && firstNote<29) || firstNote<9) {
				for (int p=0; p<noteLengths.length; p++) {
					int step;
					if (Math.random() < 0.8) {
						step=p;
					} else {
						step=p+1;
					}
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*2)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*3)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*4)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*7)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*8)));
					beat=beat+noteLengths[p];
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*2)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*3)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*4)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*7)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*8)));
				}
			} else {
				for (int p=0; p<noteLengths.length; p++) {
					int step;
					if (Math.random() < 0.8) {
						step=p;
					} else {
						step=p+1;
					}
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*2)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*3)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*4)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*7)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*8)));
					beat=beat+noteLengths[p];
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*2)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*3)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*4)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*7)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*8)));
				}
			}
		}
	}
	
	public void makeVerseChords () {
		for (int i = 0; i<chords.length; i++) {
			int beat = 96 + (i*384);
			for (int p = 0; p<chords[i].length; p++) {
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*2)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*3)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*4)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*7)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*8)));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*2)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*3)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*4)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*7)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*8)+384));
			}
		}
	}
	
	public void makeVerseDrums () {
		if (Math.random()<0.5) {
			for (int i = 0; i<chords.length; i++) {
				int beat = 96 + (i*384);
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+96));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)+96));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+192));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+192));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+216));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+216));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+240));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+240));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+264));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+264));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+312));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+312));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+336));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*3)+336));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*3)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+96));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)+96));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+192));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+192));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+216));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+216));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+240));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+240));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+264));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+264));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+312));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+312));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+336));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*4)+336));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*4)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+96));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*7)+96));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+192));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+192));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+216));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+216));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+240));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+240));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+264));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+264));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+312));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+312));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+336));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*7)+336));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*7)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+96));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)+96));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+192));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+192));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+216));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+216));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+240));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+240));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+264));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+264));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+312));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+312));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+336));
				track.insertEvent(makeEvent(144, 9, 42, 80, beat+(chords.length*384*8)+336));
				track.insertEvent(makeEvent(128, 9, 42, 80, beat+(chords.length*384*8)+384));
			}
		} else {
			for (int i = 0; i<chords.length; i++) {
				int beat = 96 + (i*384);
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+192));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)+192));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+288));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)+288));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+336));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*3)+336));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*3)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+192));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)+192));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+288));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)+288));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+336));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*4)+336));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*4)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*7)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+192));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*7)+192));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+288));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*7)+288));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+336));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*7)+336));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*7)+384));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+192));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)+192));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+288));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)+288));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+336));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*8)+336));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*8)+384));
			}
		}
	}
	
	public void makeChorusChords () {
		for (int i = 0; i<chords.length; i++) {
			int beat = 96 + (i*384);
			for (int p = 0; p<chords[i].length; p++) {
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*5)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*6)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*9)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*10)));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*13)-768));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*14)-768));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*15)-768));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*16)-768));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*5)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*6)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*9)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*10)+384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*13)-384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*14)-384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*15)-384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 70, beat+(chords.length*384*16)-384));
			}
		}
	}
	
	public void makeChorusMelody (int[] notes) {
		int[] noteLengths = MEASUREPATTERNS[(int) (Math.random() * MEASUREPATTERNS.length)];
		for (int i = 0; i<chords.length; i++) {
			int beat = 96 + (i*384);
			int firstNote = 8+chords[i][(int) (Math.random() * chords[i].length)];
			if ((Math.random()<0.5 && firstNote<29) || firstNote<9) {
				for (int p=0; p<noteLengths.length; p++) {
					int step;
					if (Math.random() < 0.8) {
						step=p;
					} else {
						step=p+1;
					}
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*5)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*6)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*9)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*10)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*13)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*14)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*15)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote+step], 100, beat+(chords.length*384*16)-768));
					beat=beat+noteLengths[p];
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*5)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*6)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*9)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*10)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*13)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*14)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*15)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote+step], 100, beat+(chords.length*384*16)-768));
				}
			} else {
				for (int p=0; p<noteLengths.length; p++) {
					int step;
					if (Math.random() < 0.8) {
						step=p;
					} else {
						step=p+1;
					}
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*5)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*6)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*9)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*10)));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*13)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*14)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*15)-768));
					track.insertEvent(makeEvent(144, 1, notes[firstNote-step], 100, beat+(chords.length*384*16)-768));
					beat=beat+noteLengths[p];
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*5)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*6)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*9)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*10)));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*13)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*14)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*15)-768));
					track.insertEvent(makeEvent(128, 1, notes[firstNote-step], 100, beat+(chords.length*384*16)-768));
				}
			}
		}
	}
	
	public void makeChorusDrums() {
	}
	
	public void makeBridgeChords() {
		for (int i = 0; i<(chords.length-1); i++) {
			int beat = 96 + (i*384);
			for (int p = 0; p<chords[i].length; p++) {
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+96));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+96));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+192));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+192));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+288));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+288));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*11)+384));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-384));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-288));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-288));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-192));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-192));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-96));
				track.insertEvent(makeEvent(144, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)-96));
				track.insertEvent(makeEvent(128, 2, noteGetter.getNotes()[chords[i][p]], 90, beat+(chords.length*384*12)));
			}
		}
	}
	
	public void makeThrobDrums() {
		for (int i=0; i<chords.length; i++) {
			int beat = 96 + (i*384);
			for (int p=1; p<16; p++) {
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*p)));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*p)+96));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*p)+96));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*p)+192));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*p)+192));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*p)+288));
				track.insertEvent(makeEvent(144, 9, 35, 100, beat+(chords.length*384*p)+288));
				track.insertEvent(makeEvent(128, 9, 35, 100, beat+(chords.length*384*p)+384));
			}
		}
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, long tick) {
		MidiEvent event = null;
		if (comd == 144) {
			event = new NoteOn (tick, chan, one, two);
		} else if (comd == 128) {
			event = new NoteOff(tick, chan, one, two);
		} else {
			event = new ProgramChange(tick, chan, one);
		}
		return event;
	}
}