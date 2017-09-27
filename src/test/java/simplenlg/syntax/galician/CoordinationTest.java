/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is "Simplenlg".
 *
 * The Initial Developer of the Original Code is Ehud Reiter, Albert Gatt and Dave Westwater.
 * Portions created by Ehud Reiter, Albert Gatt and Dave Westwater are Copyright (C) 2010-11 The University of Aberdeen. All Rights Reserved.
 *
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */

package simplenlg.syntax.galician;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import simplenlg.features.Feature;
import simplenlg.features.LexicalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * Some tests for coordination, especially of coordinated VPs with modifiers.
 *
 * @author Albert Gatt
 */
public class CoordinationTest extends SimpleNLG4Test {

    public CoordinationTest(String name) {
        super(name);
    }

    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }


    /**
     * Check that empty coordinate phrases are not realised as "null"
     */
    @Test
    public void testEmptyCoordination() {
        // first a simple phrase with no coordinates
        CoordinatedPhraseElement coord = this.phraseFactory
                .createCoordinatedPhrase();
        Assert.assertEquals("", this.realiser.realise(coord).getRealisation());

        // now one with a premodifier and nothing else
        coord.addPreModifier(this.phraseFactory.createAdjectivePhrase("bonito"));
        Assert.assertEquals("bonito", this.realiser.realise(coord)
                .getRealisation());
    }

    /**
     * Test pre and post-modification of coordinate VPs inside a sentence.
     */
    @Test
    public void testModifiedCoordVP() {
        this.getUp.setObject("el");
        this.getUp.getObject().setFeature(LexicalFeature.REFLEXIVE, true);
        this.fallDown.setObject("el");
        this.fallDown.getObject().setFeature(LexicalFeature.REFLEXIVE, true);
        CoordinatedPhraseElement coord = this.phraseFactory
                .createCoordinatedPhrase(this.getUp, this.fallDown);
        coord.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("se levantou e se caeu", this.realiser
                .realise(coord).getRealisation());

        // add a premodifier
        coord.addPreModifier("despacio");
        Assert.assertEquals("despacio se levantou e se caeu", this.realiser
                .realise(coord).getRealisation());

        // adda postmodifier
        coord.addPostModifier(this.behindTheCurtain);
        Assert.assertEquals("despacio se levantou e se caeu tras a cortina",
                this.realiser.realise(coord).getRealisation());

        // put within the context of a sentence
        SPhraseSpec s = this.phraseFactory.createClause("Jake", coord);
        s.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals(
                "Jake despacio se levantou e se caeu tras a cortina",
                this.realiser.realise(s).getRealisation());

        // add premod to the sentence
        s.addPreModifier(this.lexicon.getWord("sen embargo", LexicalCategory.ADVERB));
        Assert.assertEquals(
                "Jake sen embargo despacio se levantou e se caeu tras a cortina",
                this.realiser.realise(s).getRealisation());

        // add postmod to the sentence
        s.addPostModifier(this.inTheRoom);
        Assert.assertEquals(
                "Jake sen embargo despacio se levantou e se caeu tras a cortina na habitación",
                this.realiser.realise(s).getRealisation());
    }

    /**
     * Test due to Chris Howell -- create a complex sentence with front modifier
     * and coordinateVP. This is a version in which we create the coordinate
     * phrase directly.
     */
    @Test
    public void testCoordinateVPComplexSubject() {
        // "As a result of the procedure the patient had an adverse contrast media reaction and went into cardiogenic shock."
        SPhraseSpec s = this.phraseFactory.createClause();

        s.setSubject(this.phraseFactory.createNounPhrase("o", "pacente"));

        // first VP
        VPPhraseSpec vp1 = this.phraseFactory.createVerbPhrase(this.lexicon
                .getWord("ter", LexicalCategory.VERB));
        NPPhraseSpec np1 = this.phraseFactory.createNounPhrase("un",
                this.lexicon.getWord("reacción de medios de contraste",
                        LexicalCategory.NOUN));
        np1.addPostModifier(this.lexicon.getWord("adversa",
                LexicalCategory.ADJECTIVE));
        vp1.addComplement(np1);

        // second VP
        VPPhraseSpec vp2 = this.phraseFactory.createVerbPhrase(this.lexicon
                .getWord("entrar", LexicalCategory.VERB));
        PPPhraseSpec pp = this.phraseFactory
                .createPrepositionPhrase("en", this.lexicon.getWord(
                        "shock cardioxénico", LexicalCategory.NOUN));
        vp2.addComplement(pp);

        // coordinate
        CoordinatedPhraseElement coord = this.phraseFactory
                .createCoordinatedPhrase(vp1, vp2);
        coord.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals(
                "tivo un reacción de medios de contraste adversa e entrou en shock cardioxénico",
                this.realiser.realise(coord).getRealisation());

        // now put this in the sentence
        s.setVerbPhrase(coord);
        s.addFrontModifier("Como resultado do procedemento");
        Assert.assertEquals(
                "Como resultado do procedemento o pacente tivo un reacción de medios de contraste adversa e entrou en shock cardioxénico",
                this.realiser.realise(s).getRealisation());

    }

    /**
     * Test setting a conjunction to null
     */
    public void testNullConjunction() {
        SPhraseSpec p = this.phraseFactory.createClause("eu", "ser", "feliz");
        SPhraseSpec q = this.phraseFactory.createClause("eu", "comer", "peixe");
        CoordinatedPhraseElement pq = this.phraseFactory
                .createCoordinatedPhrase();
        pq.addCoordinate(p);
        pq.addCoordinate(q);
        pq.setFeature(Feature.CONJUNCTION, "");

        // should come out without conjunction
        Assert.assertEquals("eu son feliz eu como peixe", this.realiser.realise(pq)
                .getRealisation());

        // should come out without conjunction
        pq.setFeature(Feature.CONJUNCTION, null);
        Assert.assertEquals("eu son feliz eu como peixe", this.realiser.realise(pq)
                .getRealisation());

    }

    /**
     * Check that the negation feature on a child of a coordinate phrase remains
     * as set, unless explicitly set otherwise at the parent level.
     */
    @Test
    public void testNegationFeature() {
        SPhraseSpec s1 = this.phraseFactory
                .createClause("el", "ter", "asma");
        SPhraseSpec s2 = this.phraseFactory.createClause("el", "ter",
                "diabetes");
        s1.setFeature(Feature.NEGATED, true);
        CoordinatedPhraseElement coord = this.phraseFactory
                .createCoordinatedPhrase(s1, s2);
        String realisation = this.realiser.realise(coord).getRealisation();
        Assert.assertEquals("el non ten asma e el ten diabetes",
                realisation);
    }
}
