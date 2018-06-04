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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.LexicalCategory;
import simplenlg.framework.NLGElement;
import simplenlg.framework.PhraseElement;
import simplenlg.phrasespec.*;

// TODO: Auto-generated Javadoc

/**
 * The Class STest.
 */
public class ContractionsTest extends SimpleNLG4Test {
    /**
     * Instantiates a new s test.
     *
     * @param name the name
     */
    public ContractionsTest(String name) {
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
    }

    @After
    public void tearDown() {
        super.tearDown();
    }


    /**
     * Preposition + article tests
     */
    @Test
    public void testPrepositions() {
        SPhraseSpec p = phraseFactory.createClause();
        PhraseElement phrase;
        NPPhraseSpec nounPhrase = phraseFactory.createNounPhrase("un", "amigo");
        p.setSubject("María");
        p.setVerb("pasear");

        phrase = this.phraseFactory.createPrepositionPhrase("a", "o can");
        p.setObject(phrase);
        Assert.assertEquals("María pasea ao can.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("a", "a cadela");
        p.setObject(phrase);
        Assert.assertEquals("María pasea á cadela.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("con", "o can");
        p.setObject(phrase);
        Assert.assertEquals("María pasea co can.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("con", "a cadela");
        p.setObject(phrase);
        Assert.assertEquals("María pasea ca cadela.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("con", nounPhrase);
        p.setObject(phrase);
        Assert.assertEquals("María pasea cun amigo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("de", "a man");
        p.setObject(phrase);
        Assert.assertEquals("María pasea da man.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("de", "unha man");
        p.setObject(phrase);
        Assert.assertEquals("María pasea dunha man.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));


        phrase = this.phraseFactory.createPrepositionPhrase("en", "o parque");
        p.setObject(phrase);
        Assert.assertEquals("María pasea no parque.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("en", "un parque");
        p.setObject(phrase);
        Assert.assertEquals("María pasea nun parque.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("por", "o parque");
        p.setObject(phrase);
        Assert.assertEquals("María pasea polo parque.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        phrase = this.phraseFactory.createPrepositionPhrase("tras", "o can");
        p.setObject(phrase);
        Assert.assertEquals("María pasea tralo can.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));
    }

    /**
     * Contractions between atonic pronouns
     */
    @Test
    public void testPronounsContractions() {
        SPhraseSpec p = phraseFactory.createClause();
        p.setSubject("María");
        p.setIndirectObject("me");
        p.setObject("o");
        p.setVerb("dicir");
        Assert.assertEquals("María dimo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        p.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("María díxomo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        Assert.assertEquals("María diramo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        p.setIndirectObject("lles");
        p.setObject("o");
        p.setFeature(Feature.TENSE, Tense.FUTURE);
        Assert.assertEquals("María dirállelo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        p.setIndirectObject("che");
        p.setObject("o");
        p.setFeature(Feature.TENSE, Tense.PRESENT);
        Assert.assertEquals("María dicho.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        p.setIndirectObject("nos");
        p.setObject("o");
        Assert.assertEquals("María dínolo.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        SPhraseSpec p1 = phraseFactory.createClause();
        p1.setSubject("María");
        VPPhraseSpec vPhrase = phraseFactory.createVerbPhrase("dicir");
        vPhrase.setIndirectObject("vos");
        NLGElement object = phraseFactory.createNLGElement("as", LexicalCategory.PRONOUN);
        object.setFeature(LexicalFeature.GENDER, Gender.FEMININE);
        object.setPlural(true);
        vPhrase.setObject(object);
        p1.setVerb(vPhrase);
        Assert.assertEquals("María dívolas.", this.realiser //$NON-NLS-1$
                .realiseSentence(p1));
    }

    /**
     * Atonic pronoun collocation with the verb
     */
    @Test
    public void testPronounsCollocation() {
        SPhraseSpec p = phraseFactory.createClause();
        p.setSubject("María");

        VPPhraseSpec vPhrase = phraseFactory.createVerbPhrase("dicir");
        vPhrase.setFeature(Feature.NEGATED, true);
        vPhrase.setObject("me");

        p.setVerb(vPhrase);
        Assert.assertEquals("María non me di.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        vPhrase.setFeature(Feature.NEGATED, false);
        p.setVerb(vPhrase);
        Assert.assertEquals("María dime.", this.realiser //$NON-NLS-1$
                .realiseSentence(p));

        SPhraseSpec s = this.phraseFactory.createClause("María", "dicir");
        s.setFeature(Feature.TENSE, Tense.PAST);
        s.setIndirectObject("che");
        s.setObject("o");
        s.addPreModifier(this.lexicon.getWord("quizais", LexicalCategory.ADVERB));

        Assert.assertEquals("María quizais cho dixo.", this.realiser.realiseSentence(s));
    }
}
