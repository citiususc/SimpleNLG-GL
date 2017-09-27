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
package simplenlg.syntax.spanish;

import org.junit.Ignore;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.AdjPhraseSpec;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.spanish.Realiser;

/**
 * @author Dave Westwater, Data2Text Ltd
 */
@Ignore
public class StandAloneExample {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // below is a simple complete example of using simplenlg V4
        // afterwards is an example of using simplenlg just for morphology

        // set up
        Lexicon lexicon = new simplenlg.lexicon.spanish.XMLLexicon();                          // default simplenlg lexicon
        NLGFactory nlgFactory = new NLGFactory(lexicon);             // factory based on lexicon

        // create sentences
        // 	"John did not go to the bigger park. He played football there."
        NPPhraseSpec thePark = nlgFactory.createNounPhrase("el", "parque");   // create an NP
        AdjPhraseSpec bigp = nlgFactory.createAdjectivePhrase("gran");        // create AdjP
        bigp.setFeature(Feature.IS_COMPARATIVE, true);                       // use comparative form ("bigger")
        thePark.addModifier(bigp);                                        // add adj as modifier in NP
        // above relies on default placement rules.  You can force placement as a premodifier
        // (before head) by using addPreModifier
        PPPhraseSpec toThePark = nlgFactory.createPrepositionPhrase("a");    // create a PP
        toThePark.setObject(thePark);                                     // set PP object
        // could also just say nlgFactory.createPrepositionPhrase("to", the Park);

        SPhraseSpec johnGoToThePark = nlgFactory.createClause("John",      // create sentence
                "ir", toThePark);

        johnGoToThePark.setFeature(Feature.TENSE, Tense.PAST);              // set tense
        johnGoToThePark.setFeature(Feature.NEGATED, true);                 // set negated

        // note that constituents (such as subject and object) are set with setXXX methods
        // while features are set with setFeature

        DocumentElement sentence = nlgFactory                            // create a sentence DocumentElement from SPhraseSpec
                .createSentence(johnGoToThePark);

        // below creates a sentence DocumentElement by concatenating strings
        StringElement hePlayed = new StringElement("él jugó");
        StringElement there = new StringElement("allí");
        WordElement football = new WordElement("fútbol");

        DocumentElement sentence2 = nlgFactory.createSentence();
        sentence2.addComponent(hePlayed);
        sentence2.addComponent(football);
        sentence2.addComponent(there);

        // now create a paragraph which contains these sentences
        DocumentElement paragraph = nlgFactory.createParagraph();
        paragraph.addComponent(sentence);
        paragraph.addComponent(sentence2);

        // create a realiser.  Note that a lexicon is specified, this should be
        // the same one used by the NLGFactory
        Realiser realiser = new Realiser(lexicon);
        //realiser.setDebugMode(true);     // uncomment this to print out debug info during realisation
        NLGElement realised = realiser.realise(paragraph);

        System.out.println(realised.getRealisation());

        // end of main example

        // second example - using simplenlg just for morphology
        // below is clumsy as direct access to morphology isn't properly supported in V4.2
        // hopefully will be better supported in later versions

        // get word element for "child"
        WordElement word = (WordElement) nlgFactory.createWord("niño", LexicalCategory.NOUN);
        // create InflectedWordElement from word element
        InflectedWordElement inflectedWord = new InflectedWordElement(word);
        // set the inflected word to plural
        inflectedWord.setPlural(true);
        // realise the inflected word
        String result = realiser.realise(inflectedWord).getRealisation();

        System.out.println(result);
    }
}
