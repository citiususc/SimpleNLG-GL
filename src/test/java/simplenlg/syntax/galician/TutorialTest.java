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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Westwater, Roman Kutlak, Margaret Mitchell, Saad Mahamood.
 */

package simplenlg.syntax.galician;

import org.junit.Test;
import simplenlg.features.*;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.galician.XMLLexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;
import simplenlg.realiser.galician.Realiser;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Tests from SimpleNLG tutorial
 * <hr>
 * <p>
 * <p>
 * Copyright (C) 2011, University of Aberdeen
 * </p>
 *
 * @author Ehud Reiter
 */
public class TutorialTest {


    // no code in sections 1 and 2

    /**
     * test section 3 code
     */
    @Test
    public void section3_Test() {
        Lexicon lexicon = new XMLLexicon();                         // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon

        NLGElement s1 = nlgFactory.createSentence("o meu can é feliz");

        Realiser r = new Realiser(lexicon);

        String output = r.realiseSentence(s1);

        assertEquals("O meu can é feliz.", output);
    }

    /**
     * test section 5 code
     */
    @Test
    public void section5_Test() {
        Lexicon lexicon = new XMLLexicon();                         // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("o meu can");
        p.setVerb("perseguir");
        p.setObject("George");

        String output = realiser.realiseSentence(p);
        assertEquals("O meu can persegue George.", output);
    }

    /**
     * test section 6 code
     */
    @Test
    public void section6_Test() {
        Lexicon lexicon = new XMLLexicon();                         // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject(nlgFactory.createPrepositionPhrase("a", "George"));

        p.setFeature(Feature.TENSE, Tense.PAST);
        String output = realiser.realiseSentence(p);
        assertEquals("Mary perseguiu a George.", output);

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        output = realiser.realiseSentence(p);
        assertEquals("Mary perseguirá a George.", output);

        p.setFeature(Feature.NEGATED, true);
        output = realiser.realiseSentence(p);
        assertEquals("Mary non perseguirá a George.", output);

        p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject(nlgFactory.createPrepositionPhrase("a", "George"));

        p.setFeature(Feature.INTERROGATIVE_TYPE,
                InterrogativeType.YES_NO);
        output = realiser.realiseSentence(p);
        assertEquals("Persegue Mary a George?", output);

        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        output = realiser.realiseSentence(p);
        assertEquals("A quen persegue Mary?", output);

        p = nlgFactory.createClause();
        p.setSubject("o can");
        p.setVerb("esperta");
        output = realiser.realiseSentence(p);
        assertEquals("O can esperta.", output);

    }

    /**
     * test ability to use variant words
     */
    @Test
    public void variantsTest() {
        Lexicon lexicon = new XMLLexicon();                         // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("o meu can");
        p.setVerb("ser");  // variant of be
        p.setObject("George");

        String output = realiser.realiseSentence(p);
        assertEquals("O meu can é George.", output);

        p = nlgFactory.createClause();
        p.setSubject("o meu can");
        p.setVerb("perseguir");  // variant of chase
        p.setObject(nlgFactory.createPrepositionPhrase("a", "George"));

        output = realiser.realiseSentence(p);
        assertEquals("O meu can persegue a George.", output);


        p = nlgFactory.createClause();
        p.setSubject(nlgFactory.createNounPhrase("os", "cans"));   // variant of "dog"
        p.setVerb("é");  // variant of be
        p.setObject("felices");  // variant of happy
        output = realiser.realiseSentence(p);
        assertEquals("O can é feliz.", output);

        p = nlgFactory.createClause();
        p.setSubject(nlgFactory.createNounPhrase("os", "nenos"));   // variant of "child"
        p.setVerb("son");  // variant of be
        p.setObject("felices");  // variant of happy
        output = realiser.realiseSentence(p);
        assertEquals("O neno é feliz.", output);

        // following functionality is enabled
        p = nlgFactory.createClause();
        p.setSubject(nlgFactory.createNounPhrase("os", "cans"));   // variant of "dog"
        p.setVerb("son");  // variant of be
        p.setObject("felices");  // variant of happy
        output = realiser.realiseSentence(p);
        assertEquals("O can é feliz.", output); //corrected automatically
    }

	/* Following code tests the section 5 to 15
     * sections 5 & 6 are repeated here in order to match the simplenlg tutorial version 4
	 * James Christie
	 * June 2011
	 */

    /**
     * test section 5 to match simplenlg tutorial version 4's code
     */
    @Test
    public void section5A_Test() {
        Lexicon lexicon = new XMLLexicon();      // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject("o mono");

        String output = realiser.realiseSentence(p);
        assertEquals("Mary persegue o mono.", output);
    } // testSection5A

    /**
     * test section 6 to match simplenlg tutorial version 4' code
     */
    @Test
    public void section6A_Test() {
        Lexicon lexicon = new XMLLexicon();    // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject(nlgFactory.createPrepositionPhrase("a", nlgFactory.createNounPhrase("o", "mono")));

        p.setFeature(Feature.TENSE, Tense.PAST);
        String output = realiser.realiseSentence(p);
        assertEquals("Mary perseguiu ao mono.", output);

        p.setFeature(Feature.TENSE, Tense.FUTURE);
        output = realiser.realiseSentence(p);
        assertEquals("Mary perseguirá ao mono.", output);

        p.setFeature(Feature.NEGATED, true);
        output = realiser.realiseSentence(p);
        assertEquals("Mary non perseguirá ao mono.", output);

        p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject(nlgFactory.createPrepositionPhrase("a", nlgFactory.createNounPhrase("o", "mono")));

        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.YES_NO);
        output = realiser.realiseSentence(p);
        assertEquals("Persegue Mary ao mono?", output);

        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setFeature(Feature.INTERROGATIVE_TYPE, InterrogativeType.WHO_OBJECT);
        output = realiser.realiseSentence(p);
        assertEquals("A quen persegue Mary?", output);
    }

    /**
     * test section 7 code
     */
    @Test
    public void section7_Test() {
        Lexicon lexicon = new XMLLexicon();      // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject("Mary");
        p.setVerb("perseguir");
        p.setObject("o mono");
        p.addComplement("moi rápido");
        p.addComplement("a pesar do seu esgotamento");

        String output = realiser.realiseSentence(p);
        assertEquals("Mary persegue o mono moi rápido a pesar do seu esgotamento.", output);
    }

    /**
     * test section 8 code
     */
    @Test
    public void section8_Test() {
        Lexicon lexicon = new XMLLexicon();      // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        NPPhraseSpec subject = nlgFactory.createNounPhrase("Mary");
        NPPhraseSpec object = nlgFactory.createNounPhrase("o mono");
        VPPhraseSpec verb = nlgFactory.createVerbPhrase("perseguir");
        subject.addModifier("rápido");
        subject.setFeature(LexicalFeature.GENDER, Gender.FEMININE);

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject(subject);
        p.setVerb(verb);
        p.setObject(object);

        String outputA = realiser.realiseSentence(p);
        assertEquals("Rápida Mary persegue o mono.", outputA);

        verb.addModifier("rápidamente");

        String outputB = realiser.realiseSentence(p);
        assertEquals("Rápida Mary persegue rápidamente o mono.", outputB);
    }

    // there is no code specified in section 9

    /**
     * test section 10 code
     */
    @Test
    public void section10_Test() {
        Lexicon lexicon = new XMLLexicon();      // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon
        Realiser realiser = new Realiser(lexicon);

        NPPhraseSpec subject1 = nlgFactory.createNounPhrase("Mary");
        NPPhraseSpec subject2 = nlgFactory.createNounPhrase("túa", "xirafa");

        // next line is not correct ~ should be nlgFactory.createCoordinatedPhrase ~ may be corrected in the API
        CoordinatedPhraseElement subj = nlgFactory.createCoordinatedPhrase(subject1, subject2);

        VPPhraseSpec verb = nlgFactory.createVerbPhrase("perseguir");

        SPhraseSpec p = nlgFactory.createClause();
        p.setSubject(subj);
        p.setVerb(verb);
        p.setObject("o mono");

        String outputA = realiser.realiseSentence(p);
        assertEquals("Mary e a túa xirafa perseguen o mono.", outputA);

        NPPhraseSpec object1 = nlgFactory.createNounPhrase("o mono");
        NPPhraseSpec object2 = nlgFactory.createNounPhrase("George");

        // next line is not correct ~ should be nlgFactory.createCoordinatedPhrase ~ may be corrected in the API
        CoordinatedPhraseElement obj = nlgFactory.createCoordinatedPhrase(object1, object2);
        obj.addCoordinate("Martha");
        p.setObject(obj);

        String outputB = realiser.realiseSentence(p);
        assertEquals("Mary e a túa xirafa perseguen o mono, George e Martha.", outputB);

        obj.setFeature(Feature.CONJUNCTION, "ou");

        String outputC = realiser.realiseSentence(p);
        assertEquals("Mary e a túa xirafa perseguen o mono, George ou Martha.", outputC);
    }

    /**
     * test section 11 code
     */
    @Test
    public void section11_Test() {
        Lexicon lexicon = new XMLLexicon();     // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon

        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec pA = nlgFactory.createClause("Mary", "perseguir", "o mono");
        pA.addComplement("no parque");

        String outputA = realiser.realiseSentence(pA);
        assertEquals("Mary persegue o mono no parque.", outputA);

        // alternative build paradigm
        NPPhraseSpec place = nlgFactory.createNounPhrase("parque");
        SPhraseSpec pB = nlgFactory.createClause("Mary", "perseguir", "o mono");

        // next line is depreciated ~ may be corrected in the API
        place.setDeterminer("o");
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
        pp.addComplement(place);
        pp.setPreposition("en");

        pB.addComplement(pp);

        String outputB = realiser.realiseSentence(pB);
        assertEquals("Mary persegue o mono no parque.", outputB);

        place.addPreModifier("frondoso");

        String outputC = realiser.realiseSentence(pB);
        assertEquals("Mary persegue o mono no frondoso parque.", outputC);
    } // testSection11

    // section12 only has a code table as illustration

    /**
     * test section 13 code
     */
    @Test
    public void section13_Test() {
        Lexicon lexicon = new XMLLexicon();     // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon

        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec s1 = nlgFactory.createClause("o meu gato", "querer", "pescado");
        SPhraseSpec s2 = nlgFactory.createClause("o meu can", "querer", "ósos grandes");
        SPhraseSpec s3 = nlgFactory.createClause("o meu cabalo", "querer", "herba");

        CoordinatedPhraseElement c = nlgFactory.createCoordinatedPhrase();
        c.addCoordinate(s1);
        c.addCoordinate(s2);
        c.addCoordinate(s3);

        String outputA = realiser.realiseSentence(c);
        assertEquals("O meu gato quere pescado, o meu can quere ósos grandes e o meu cabalo quere herba.", outputA);

        SPhraseSpec p = nlgFactory.createClause("eu", "ser", "feliz");
        SPhraseSpec q = nlgFactory.createClause("eu", "comer", "peixe");
        q.setFeature(Feature.COMPLEMENTISER, "porque");
        q.setFeature(Feature.TENSE, Tense.PAST);
        q.getSubject().setFeature(Feature.ELIDED, true);
        p.addComplement(q);

        String outputB = realiser.realiseSentence(p);
        assertEquals("Eu son feliz porque comín peixe.", outputB);
    }

    /**
     * test section 14 code
     */
    @Test
    public void section14_Test() {
        Lexicon lexicon = new XMLLexicon();     // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);  // factory based on lexicon

        Realiser realiser = new Realiser(lexicon);

        SPhraseSpec p1 = nlgFactory.createClause("Mary", "perseguir", "o mono");
        SPhraseSpec p2 = nlgFactory.createClause("O mono", "pelexar");
        SPhraseSpec p3 = nlgFactory.createClause("Mary", "estar", "nerviosa");

        DocumentElement s1 = nlgFactory.createSentence(p1);
        DocumentElement s2 = nlgFactory.createSentence(p2);
        DocumentElement s3 = nlgFactory.createSentence(p3);

        DocumentElement par1 = nlgFactory.createParagraph(Arrays.asList(s1, s2, s3));

        String output14a = realiser.realise(par1).getRealisation();
        assertEquals("Mary persegue o mono. O mono pelexa. Mary está nerviosa.\n\n", output14a);

        DocumentElement section = nlgFactory.createSection("As probas e tribulacións de María e o mono");
        section.addComponent(par1);
        String output14b = realiser.realise(section).getRealisation();
        assertEquals("As probas e tribulacións de María e o mono\nMary persegue o mono. O mono pelexa. Mary está nerviosa.\n\n", output14b);
    }

} 
