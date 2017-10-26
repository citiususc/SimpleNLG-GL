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
import org.junit.Before;
import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.galician.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.galician.Realiser;

/**
 * JUnit test case for interrogatives.
 *
 * @author agatt
 */
public class InterrogativeTest2 extends SimpleNLG4Test {

    // set up a few more fixtures
    /**
     * The s5.
     */
    SPhraseSpec s1, s2, s3, s4, s5;

    /**
     * Instantiates a new interrogative test.
     *
     * @param name the name
     */
    public InterrogativeTest2(String name) {
        super(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see simplenlg.test.SimplenlgTest#setUp()
     */
    @Override
    @Before
    protected void setUp() {
        super.setUp();

        // // the man gives the woman John's flower
        NPPhraseSpec flor = this.phraseFactory.createNounPhrase("a", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flor.setPostModifier(deJohn);
        PhraseElement _woman = this.phraseFactory.createNounPhrase(
                "a", "muller"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause(this.man, this.give, flor);
        this.s3.setIndirectObject(_woman);

        CoordinatedPhraseElement subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "coller", //$NON-NLS-1$
                "as pelotas"); //$NON-NLS-1$
        this.s4.getObject().setPlural(true);
        this.s4.getObject().setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        this.s4.addPostModifier("na tenda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sen embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañán"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        // this.s5 = new SPhraseSpec();
        // this.s5.setSubject(new NPPhraseSpec("the", "dog"));
        // this.s5.setHead("be");
        // this.s5.setComplement(new NPPhraseSpec("the", "rock"),
        // DiscourseFunction.OBJECT);

    }

    /**
     * Test for wh questions.
     */
    @Test
    public void testWHQuestions() {

        // subject interrogative
        setUp();
        this.realiser.setLexicon(this.lexicon);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals(
                "sen embargo quen collerán as pelotas na tenda mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // subject interrogative in passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_SUBJECT);

        Assert.assertEquals(
                "sen embargo por quen serán as pelotas collidas na tenda mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // object interrogative
        setUp();
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals(
                "sen embargo que collerán Jane e Andrew na tenda mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // object interrogative with passive
        setUp();
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        this.s4.setFeature(Feature.PASSIVE, true);

        Assert.assertEquals(
                "sen embargo que serán collidas na tenda por Jane e Andrew mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // how-question + passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        Assert.assertEquals(
                "sen embargo como serán collidas as pelotas na tenda por Jane e Andrew mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // // why-question + passive
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals(
                "sen embargo por que serán collidas as pelotas na tenda por Jane e Andrew mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // how question with modal
        setUp();
        this.s4.setFeature(Feature.PASSIVE, true);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        this.s4.setFeature(Feature.MODAL, "deber"); //$NON-NLS-1$
        Assert.assertEquals(
                "sen embargo como deberán ser collidas as pelotas na tenda por Jane e Andrew mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // indirect object
        setUp();
        this.realiser.setLexicon(this.lexicon);
        this.s3.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHO_INDIRECT_OBJECT);
        Assert.assertEquals("a quen dá o home a flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());
    }

    /**
     * Test for questions with "be"
     */
    @Test
    public void testBeQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("unha", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "xoguete"));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("que é unha pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("é unha pelota un xoguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("que é un xoguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por que é Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("onde é Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen é bonita", this.realiser.realise(p2)
                .getRealisation());
    }

}
