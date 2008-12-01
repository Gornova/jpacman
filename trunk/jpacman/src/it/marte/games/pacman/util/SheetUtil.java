package it.marte.games.pacman.util;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SpriteSheet;

public class SheetUtil {

    /**
     * Ritorna animazione
     * 
     * @param sheet -
     *                sheet da cui prendere l'animazione
     * @param rigaSheet -
     *                riga da considerare per lo sheet
     * @param startNumFramAnimazione -
     *                colonna da cui si parte per leggere l'animazione (da 0)
     * @param numFramAnimazione -
     *                numero di colonna da cui leggere l'animazione
     * @return l'animazione corrispondente
     */
    public static Animation getAnimationFromSheet(SpriteSheet sheet,
	    int rigaSheet, int startNumFramAnimazione, int numFramAnimazione) {
	Animation animation = new Animation();
	for (int frame = startNumFramAnimazione; frame < numFramAnimazione; frame++) {
	    animation.addFrame(sheet.getSprite(frame, rigaSheet), 150);
	}
	animation.setAutoUpdate(false);
	return animation;
    }

}
