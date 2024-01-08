package typingTutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordMover extends Thread {
	private FallingWord myWord;

	private FallingWord hungry;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch startLatch; //so all can start at once
	
	WordMover(FallingWord word, FallingWord hungry) {
		myWord = word;
		this.hungry = hungry;
	}
	
	WordMover(FallingWord word, FallingWord hungry, WordDictionary dict, Score score,
			  CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
		this(word, hungry);
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}

	public void run() {

		//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
		try {
//			System.out.println(TypingTutorApp.wordsH[0].getWord() + " waiting to start and position x=" + TypingTutorApp.wordsH[0].getX() + " y=" + TypingTutorApp.wordsH[0].getY());
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
//		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
			while (!myWord.dropped() && !done.get()) {
					myWord.drop(10);
					try {
						sleep(myWord.getSpeed());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					};		
					while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}
