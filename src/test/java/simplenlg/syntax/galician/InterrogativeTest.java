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
import org.junit.Ignore;
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
public class InterrogativeTest extends SimpleNLG4Test {

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
    public InterrogativeTest(String name) {
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
     * Tests a couple of fairly simple questions.
     */
    @Test
    public void testSimpleQuestions() {
        setUp();
        this.phraseFactory.setLexicon(this.lexicon);
        this.realiser.setLexicon(this.lexicon);

        // simple present
        this.s1 = this.phraseFactory.createClause(this.woman, this.kiss,
                this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PRESENT);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

        NLGFactory docFactory = new NLGFactory(this.lexicon);
        DocumentElement sent = docFactory.createSentence(this.s1);
        Assert.assertEquals("Bica a muller o home?", this.realiser //$NON-NLS-1$
                .realise(sent).getRealisation());

        // simple past
        // sentence: "the woman kissed the man"
        this.s1 = this.phraseFactory.createClause(this.woman, this.kiss,
                this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PAST);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("bicou a muller o home", this.realiser //$NON-NLS-1$
                .realise(this.s1).getRealisation());

        // copular/existential: be-fronting
        // sentence = "there is the dog on the rock"
        this.s2 = this.phraseFactory.createClause(null, "haber", this.dog); //$NON-NLS-1$ //$NON-NLS-2$
        this.s2.addPostModifier(this.onTheRock);
        this.s2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("hai o can na roca", this.realiser //$NON-NLS-1$
                .realise(this.s2).getRealisation());

        // progressive
        // sentence: "the man was giving the woman John's flower"
        NPPhraseSpec flower = this.phraseFactory.createNounPhrase("a", "flor"); //$NON-NLS-1$
        NPPhraseSpec john = this.phraseFactory.createNounPhrase("John"); //$NON-NLS-1$
        PPPhraseSpec deJohn = this.phraseFactory.createPrepositionPhrase("de", john);
        flower.setPostModifier(deJohn);
        PhraseElement _woman = this.phraseFactory.createNounPhrase(
                "a", "muller"); //$NON-NLS-1$ //$NON-NLS-2$
        this.s3 = this.phraseFactory.createClause(this.man, this.give, flower);
        this.s3.setIndirectObject(_woman);
        this.s3.setFeature(Feature.TENSE, Tense.PAST);
        this.s3.setFeature(Feature.PROGRESSIVE, true);
        this.s3.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        NLGElement realised = this.realiser.realise(this.s3);
        Assert.assertEquals("estivo o home dando á muller a flor de John", //$NON-NLS-1$
                realised.getRealisation());


        // complex case with cue phrases
        // sentence: "however, tomorrow, Jane and Andrew will pick up the balls
        // in the shop"
        // this gets the front modifier "tomorrow" shifted to the end
        setUp();
        CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "recoller", //$NON-NLS-1$
                "as pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("na tenda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sen embargo,"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañán"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals(
                "sen embargo, recollerán Jane e Andrew as pelotas na tenda mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Test for sentences with negation.
     */
    @Test
    public void testNegatedQuestions() {
        setUp();
        this.phraseFactory.setLexicon(this.lexicon);
        this.realiser.setLexicon(this.lexicon);

        // sentence: "the woman did not kiss the man"
        this.s1 = this.phraseFactory.createClause(this.woman, "bicar", this.man);
        this.s1.setFeature(Feature.TENSE, Tense.PAST);
        this.s1.setFeature(Feature.NEGATED, true);
        this.s1.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("non bicou a muller o home", this.realiser //$NON-NLS-1$
                .realise(this.s1).getRealisation());

        // sentence: however, tomorrow, Jane and Andrew will not pick up the
        // balls in the shop
        CoordinatedPhraseElement subjects = new CoordinatedPhraseElement(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, "coller", //$NON-NLS-1$
                "as pelotas"); //$NON-NLS-1$
        this.s4.addPostModifier("na tenda"); //$NON-NLS-1$
        this.s4.setFeature(Feature.CUE_PHRASE, "sen embargo,"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañán"); //$NON-NLS-1$
        this.s4.setFeature(Feature.NEGATED, true);
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals(
                "sen embargo, non collerán Jane e Andrew as pelotas na tenda mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Tests for coordinate VPs in question form.
     */
    @Test
    public void testCoordinateVPQuestions() {

        // create a complex vp: "kiss the dog and walk in the room"
        setUp();
        CoordinatedPhraseElement complex = this.phraseFactory.createCoordinatedPhrase(
                this.kiss, this.walk);
        this.kiss.addComplement(this.dog);
        this.walk.addComplement(this.inTheRoom);

        // sentence: "However, tomorrow, Jane and Andrew will kiss the dog and
        // will walk in the room"
        CoordinatedPhraseElement subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.s4 = this.phraseFactory.createClause(subjects, complex);
        this.s4.setFeature(Feature.CUE_PHRASE, "sen embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañán"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);

        Assert.assertEquals(
                "sen embargo mañán Jane e Andrew bicarán o can e camiñarán na habitación", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());

        // setting to interrogative should automatically give us a single,
        // wide-scope aux
        setUp();
        subjects = this.phraseFactory.createCoordinatedPhrase(
                this.phraseFactory.createNounPhrase("Jane"), //$NON-NLS-1$
                this.phraseFactory.createNounPhrase("Andrew")); //$NON-NLS-1$
        this.kiss.addComplement(this.dog);
        this.walk.addComplement(this.inTheRoom);
        complex = this.phraseFactory.createCoordinatedPhrase(this.kiss, this.walk);
        this.s4 = this.phraseFactory.createClause(subjects, complex);
        this.s4.setFeature(Feature.CUE_PHRASE, "sen embargo"); //$NON-NLS-1$
        this.s4.addFrontModifier("mañán"); //$NON-NLS-1$
        this.s4.setFeature(Feature.TENSE, Tense.FUTURE);
        this.s4.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);

        Assert.assertEquals(
                "sen embargo Jane e Andrew bicarán o can e camiñarán na habitación mañán", //$NON-NLS-1$
                this.realiser.realise(this.s4).getRealisation());
    }

    /**
     * Test for simple WH questions in present tense.
     */
    @Test
    public void testSimpleQuestions2() {
        setUp();
        this.realiser.setLexicon(this.lexicon);
        PhraseElement s = this.phraseFactory.createClause("a muller", "bicar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("o", "home"))); //$NON-NLS-1$

        // try with the simple yes/no type first
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("bica a muller ao home", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // now in the passive
        s = this.phraseFactory.createClause("a muller", "bicar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("o", "home"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        s.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("é o home bicado pola muller", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // // subject interrogative with simple present
        // // sentence: "the woman kisses the man"
        s = this.phraseFactory.createClause("a muller", "bicar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("o", "home"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);

        Assert.assertEquals("quen bica ao home", this.realiser.realise(s) //$NON-NLS-1$
                .getRealisation());

        // object interrogative with simple present
        s = this.phraseFactory.createClause("a muller", "bicar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("o", "home"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quen bica a muller", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());

        // subject interrogative with passive
        s = this.phraseFactory.createClause("a muller", "bicar", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", this.phraseFactory.createNounPhrase("o", "home"))); //$NON-NLS-1$
        s.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        s.setFeature(Feature.PASSIVE, true);
        Assert.assertEquals("por quen é o home bicado", this.realiser //$NON-NLS-1$
                .realise(s).getRealisation());
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
                "sen embargo por quen será as pelotas collidas na tenda mañán", //$NON-NLS-1$
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
                "sen embargo que será collido na tenda por Jane e Andrew mañán", //$NON-NLS-1$
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
        Assert.assertEquals("a quen da o home a flor de John", //$NON-NLS-1$
                this.realiser.realise(this.s3).getRealisation());
    }

    /**
     * WH movement in the progressive
     */
    @Test
    public void testProgrssiveWHSubjectQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("a", "tarta"));
        p.setFeature(Feature.PROGRESSIVE, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen está comendo a tarta", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());
    }

    /**
     * WH movement in the progressive
     */
    @Test
    public void testProgrssiveWHObjectQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("a", "tarta"));
        p.setFeature(Feature.PROGRESSIVE, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("que está comendo Mary", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());

        // AG -- need to check this; it doesn't work
        // p.setFeature(Feature.NEGATED, true);
        //		Assert.assertEquals("what is Mary not eating", //$NON-NLS-1$
        // this.realiser.realise(p).getRealisation());

    }

    /**
     * Negation with WH movement for subject
     */
    @Test
    public void testNegatedWHSubjQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("a", "tarta"));
        p.setFeature(Feature.NEGATED, true);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen non come a tarta", //$NON-NLS-1$
                this.realiser.realise(p).getRealisation());
    }

    /**
     * Negation with WH movement for object
     */
    @Test
    public void testNegatedWHObjQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("comer");
        p.setObject(this.phraseFactory.createNounPhrase("a", "tarta"));
        p.setFeature(Feature.NEGATED, true);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        NLGElement realisation = this.realiser.realise(p);
        Assert.assertEquals("que non come Mary", //$NON-NLS-1$
                realisation.getRealisation());
    }

    /**
     * Test questyions in the tutorial.
     */
    @Test
    public void testTutorialQuestions() {
        setUp();
        this.realiser.setLexicon(this.lexicon);

        PhraseElement p = this.phraseFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", "George")); //$NON-NLS-1$
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("persegue Mary a George", this.realiser.realise(p) //$NON-NLS-1$
                .getRealisation());

        p = this.phraseFactory.createClause("Mary", "perseguir", //$NON-NLS-1$ //$NON-NLS-2$
                this.phraseFactory.createPrepositionPhrase("a", "George")); //$NON-NLS-1$
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quen persegue Mary", this.realiser.realise(p) //$NON-NLS-1$
                .getRealisation());

    }

    /**
     * Subject WH Questions with modals
     */
    @Test
    public void testModalWHSubjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("o can enfadou ao home", this.realiser.realise(p)
                .getRealisation());

        // first without modal
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen enfadou ao home", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("que enfadou ao home", this.realiser.realise(p)
                .getRealisation());


    }

    /**
     * Subject WH Questions with modals
     */
    @Test
    public void testModalWHObjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);

        Assert.assertEquals("a quen enfadou o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a que enfadou o can", this.realiser
                .realise(p).getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);

        p.setFeature(Feature.MODAL, "poder");
        Assert.assertEquals("a quen poderá enfadar o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a que poderá enfadar o can", this.realiser.realise(p)
                .getRealisation());
    }

    /**
     * Questions with tenses requiring auxiliaries + subject WH
     */
    @Test
    public void testAuxWHSubjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));
        p.setFeature(Feature.TENSE, Tense.PRESENT);
        Assert.assertEquals("o can enfada ao home",
                this.realiser.realise(p).getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen enfada ao home", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("que enfada ao home", this.realiser.realise(p)
                .getRealisation());
    }

    /**
     * Questions with tenses requiring auxiliaries + subject WH
     */
    @Test
    public void testAuxWHObjectQuestion() {
        SPhraseSpec p = this.phraseFactory.createClause(this.dog, "enfadar",
                this.phraseFactory.createPrepositionPhrase("a", this.man));

        // first without any aux
        p.setFeature(Feature.TENSE, Tense.PAST);
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a que enfadou o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quen enfadou o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.TENSE, Tense.PRESENT);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        Assert.assertEquals("a quen enfada o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a que enfada o can", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.TENSE, Tense.FUTURE);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("a que enfadará o can", this.realiser
                .realise(p).getRealisation());

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

    /**
     * Test for questions with "be" in future tense
     */
    @Test
    public void testBeQuestionsFuture() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("unha", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "xoguete"));
        p.setFeature(Feature.TENSE, Tense.FUTURE);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("que será unha pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("será unha pelota un xoguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("que será un xoguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.TENSE, Tense.FUTURE);
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por que será Mary bonita", this.realiser
                .realise(p2).getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("onde será Mary bonita", this.realiser
                .realise(p2).getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen será bonita", this.realiser.realise(p2)
                .getRealisation());
    }

    /**
     * Tests for WH questions with be in past tense
     */
    @Test
    public void testBeQuestionsPast() {
        SPhraseSpec p = this.phraseFactory.createClause(
                this.phraseFactory.createNounPhrase("unha", "pelota"),
                this.phraseFactory.createWord("ser", LexicalCategory.VERB),
                this.phraseFactory.createNounPhrase("un", "xoguete"));
        p.setFeature(Feature.TENSE, Tense.PAST);

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_OBJECT);
        Assert.assertEquals("que foi unha pelota", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        Assert.assertEquals("foi unha pelota un xoguete", this.realiser.realise(p)
                .getRealisation());

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHAT_SUBJECT);
        Assert.assertEquals("que foi un xoguete", this.realiser.realise(p)
                .getRealisation());

        SPhraseSpec p2 = this.phraseFactory.createClause("Mary", "ser",
                "bonita");
        p2.setFeature(Feature.TENSE, Tense.PAST);
        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("por que foi Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("onde foi Mary bonita", this.realiser.realise(p2)
                .getRealisation());

        p2.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_SUBJECT);
        Assert.assertEquals("quen foi bonita", this.realiser.realise(p2)
                .getRealisation());
    }


    /**
     * Test WHERE, HOW and WHY questions, with copular predicate "be"
     */
    public void testSimpleBeWHQuestions() {
        SPhraseSpec p = this.phraseFactory.createClause("eu", "estar");

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHERE);
        Assert.assertEquals("Onde estou eu?", realiser.realiseSentence(p));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHY);
        Assert.assertEquals("Por que estou eu?", realiser.realiseSentence(p));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.HOW);
        Assert.assertEquals("Como estou eu?", realiser.realiseSentence(p));

    }

    /**
     * Test a simple "how" question, based on query from Albi Oxa
     */
    @Test
    public void testHowPredicateQuestion() {
        SPhraseSpec test = this.phraseFactory.createClause();
        NPPhraseSpec subject = this.phraseFactory.createNounPhrase("ti");

        subject.setFeature(Feature.PRONOMINAL, true);
        subject.setFeature(Feature.PERSON, Person.SECOND);
        test.setSubject(subject);
        test.setVerb("estar");

        test.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.HOW_PREDICATE);
        test.setFeature(Feature.TENSE, Tense.PRESENT);

        String result = realiser.realiseSentence(test);
        Assert.assertEquals("Como estás ti?", result);

    }

    /**
     * Case 1 checks that "What do you think about John?" can be generated.
     * <p>
     * Case 2 checks that the same clause is generated, even when an object is
     * declared.
     */
    @Test
    public void testWhatObjectInterrogative() {
        Lexicon lexicon = new XMLLexicon();
        NLGFactory nlg = new NLGFactory(lexicon);
        Realiser realiser = new Realiser(lexicon);

        // Case 1, no object is explicitly given:
        SPhraseSpec clause = nlg.createClause("ti", "pensar");
        PPPhraseSpec aboutJohn = nlg.createPrepositionPhrase("acerca de", "John");
        clause.addPostModifier(aboutJohn);
        clause.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.WHAT_OBJECT);
        String realisation = realiser.realiseSentence(clause);
        System.out.println(realisation);
        Assert.assertEquals("Que pensas ti acerca de John?", realisation);

        // Case 2:
        // Add "bad things" as the object so the object doesn't remain null:
        clause.setObject("cousas malas");
        realisation = realiser.realiseSentence(clause);
        System.out.println(realiser.realiseSentence(clause));
        Assert.assertEquals("Que pensas ti acerca de John?", realisation);
    }
}
