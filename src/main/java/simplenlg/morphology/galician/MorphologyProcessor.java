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
 * Contributor(s): Ehud Reiter, Albert Gatt, Dave Wewstwater, Roman Kutlak, Margaret Mitchell.
 */
package simplenlg.morphology.galician;

import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * This is the processor for handling morphology within the SimpleNLG. The
 * processor inflects words form the base form depending on the features applied
 * to the word. For example, <em>kiss</em> is inflected to <em>kissed</em> for
 * past tense, <em>dog</em> is inflected to <em>dogs</em> for pluralisation.
 * </p>
 * <p>
 * <p>
 * As a matter of course, the processor will first use any user-defined
 * inflection for the world. If no inflection is provided then the lexicon, if
 * it exists, will be examined for the correct inflection. Failing this a set of
 * very basic rules will be examined to inflect the word.
 * </p>
 * <p>
 * <p>
 * All processing modules perform realisation on a tree of
 * <code>NLGElement</code>s. The modules can alter the tree in whichever way
 * they wish. For example, the syntax processor replaces phrase elements with
 * list elements consisting of inflected words while the morphology processor
 * replaces inflected words with string elements.
 * </p>
 * <p>
 * <p>
 * <b>N.B.</b> the use of <em>module</em>, <em>processing module</em> and
 * <em>processor</em> is interchangeable. They all mean an instance of this
 * class.
 * </p>
 *
 * @author D. Westwater, University of Aberdeen.
 * @version 4.0
 */
public class MorphologyProcessor extends simplenlg.morphology.MorphologyProcessor {

    private static final String[] PREPOSITIONS = new String[]{"a", "con", "de", "en", "por", "tras"};
    private static final String[] PREP_INDET = new String[]{"con", "de", "en"};
    private static final String[] CI_PRONOUNS = new String[]{"me", "che", "lle", "nos", "vos", "lles"};
    private static final String[] DEFINITES = new String[]{"o", "a", "os", "as"};
    private static final String[] INDEFINITES = new String[]{"un", "unha", "uns", "unhas"};
    private static final String[] VOWELS = new String[]{"a", "e", "i", "o", "u", "á", "é", "í", "ó", "ú"};
    private static final String[] ACCENTUATED_VOWELS = new String[]{"á", "é", "í", "ó", "ú"};
    private static final String[] NOTACCENTUATED_VOWELS = new String[]{"a", "e", "i", "o", "u"};
    private static final String[] STRONG_VOWELS = new String[]{"a", "e", "o"};
    private static final String[] SOFT_VOWELS = new String[] {"i", "u"};

    public MorphologyProcessor() {
        super(new MorphologyRules());
    }

    @Override
    public void initialise() {
        // Do nothing
    }

    @Override
    public List<NLGElement> realise(List<NLGElement> elements) {
        List<NLGElement> realisedElements = new ArrayList<NLGElement>();
        NLGElement currentElement = null;
        NLGElement determiner = null;
        NLGElement prevElement = null;

        if (elements != null) {
            for (NLGElement eachElement : elements) {
//                if (DiscourseFunction.SPECIFIER.equals(eachElement.getFeature(InternalFeature.DISCOURSE_FUNCTION))){
//                    int index = elements.indexOf(eachElement);
//                    NLGElement nextElement;
//                    if (elements.size()>index+1){
//                        nextElement=elements.get(index+1);
//                        eachElement.setFeature(LexicalFeature.GENDER, nextElement.getFeature(LexicalFeature.GENDER));
//                        eachElement.setFeature(Feature.NUMBER, nextElement.getFeature(Feature.NUMBER));
//                    }
//                }
                InflectedWordElement prep = null;
                InflectedWordElement art = null;
                if (eachElement instanceof ListElement) {
                    try {
                        if (((ListElement) eachElement).getFirst() instanceof InflectedWordElement) {
                            prep = (InflectedWordElement) ((ListElement) eachElement).getFirst();
                            if (((ListElement) eachElement).getSecond() instanceof ListElement && Arrays.asList(PREPOSITIONS).contains(prep.getBaseForm())) {
                                ListElement aux = ((ListElement) ((ListElement) eachElement).getSecond());
                                art = (InflectedWordElement) aux.getFirst();
                                if ((Arrays.asList(DEFINITES).contains(art.getBaseForm().substring(0, art.getBaseForm().indexOf(" ")))) || (Arrays.asList(INDEFINITES).contains(art.getBaseForm().substring(0, art.getBaseForm().indexOf(" "))) && Arrays.asList(PREP_INDET).contains(prep.getBaseForm()))) {
                                    String letter = morphologyRules.buildPrepositionArticleConjunction(prep.getBaseForm());
                                    /*if (letter.equals("p")) {
                                        letter = "pol";
                                    }*/
                                    if (letter.equals("a") && art.getBaseForm().startsWith("a")) {
                                        letter = "á";
                                        art.setFeature(LexicalFeature.BASE_FORM, art.getBaseForm().substring(1, art.getBaseForm().length()));
                                    }
                                    art.setFeature(LexicalFeature.BASE_FORM, letter + art.getBaseForm());
                                    ((ListElement) eachElement).getFirst().clearAllFeatures();
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                }

                currentElement = realise(eachElement);

                //when possesive + noun add determiner -> determiner + possesive + noun
                if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("meu") || currentElement.getRealisation().equals("teu") || currentElement.getRealisation().equals("seu") || currentElement.getRealisation().equals("noso") || currentElement.getRealisation().equals("voso"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("o " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("miña") || currentElement.getRealisation().equals("túa") || currentElement.getRealisation().equals("súa") || currentElement.getRealisation().equals("nosa") || currentElement.getRealisation().equals("vosa"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("a " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("meus") || currentElement.getRealisation().equals("teus") || currentElement.getRealisation().equals("seus") || currentElement.getRealisation().equals("nosos") || currentElement.getRealisation().equals("vosos"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("os " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("miñas") || currentElement.getRealisation().equals("túas") || currentElement.getRealisation().equals("súas") || currentElement.getRealisation().equals("nosas") || currentElement.getRealisation().equals("vosas"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("as " + currentElement.getRealisation());
                    }
                }

                if (currentElement != null) {
                    //pass the discourse function and appositive features -- important for orth processor
                    currentElement.setFeature(Feature.APPOSITIVE, eachElement.getFeature(Feature.APPOSITIVE));
                    Object function = eachElement.getFeature(InternalFeature.DISCOURSE_FUNCTION);

                    if (function != null) {
                        currentElement.setFeature(InternalFeature.DISCOURSE_FUNCTION, function);
                    }

                    StringElement prevString = null;
                    try {
                        prevString = (StringElement) realisedElements.get(realisedElements.size() - 1);
                    } catch (Exception e) {

                    }

                    //verb+pronoun
                    if (eachElement.getFeatureAsBoolean(Feature.PRONOUN_AFTER) && prevElement != null && LexicalCategory.VERB.equals(prevElement.getCategory()) && LexicalCategory.PRONOUN.equals(eachElement.getCategory())) {
                        String result = doAccentuation(prevElement.getRealisation(), prevString.toString(), currentElement.getRealisation());
                        prevString.setRealisation(result);
                        currentElement.setRealisation("");
                    }

                    //preposition+article
                    if (prevElement != null && LexicalCategory.PREPOSITION.equals(prevElement.getCategory())) {
                        NLGElement root = elements.get(0);
                        while (root.getParent() != null) {
                            root = root.getParent();
                        }
                        if (root.getFeatureAsBoolean(Feature.PASSIVE) && root.hasFeature(Feature.INTERROGATIVE_TYPE)) {
                            realisedElements.remove(realisedElements.size() - 1);
                        } else {
                            //StringElement prevString = (StringElement) realisedElements.get(realisedElements.size() - 1);
                            String secondPart = "";
                            if (currentElement.getRealisation().contains(" ")) {
                                secondPart = currentElement.getRealisation().substring(currentElement.getRealisation().indexOf(" "));
                            }
                            if (Arrays.asList(PREPOSITIONS).contains(prevString.toString())) {
                                Boolean startsWithO = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("o")) || (currentElement.getRealisation().length() >= 2 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 2).equals("o "));
                                Boolean startsWithA = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("a")) || (currentElement.getRealisation().length() >= 2 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 2).equals("a "));
                                Boolean startsWithOs = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("os")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("os "));
                                Boolean startsWithAs = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("as")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("as "));
                                Boolean startsWithUn = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("un")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("un "));
                                Boolean startsWithUnha = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("unha")) || (currentElement.getRealisation().length() >= 5 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 5).equals("unha "));
                                Boolean startsWithUns = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("uns")) || (currentElement.getRealisation().length() >= 4 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 4).equals("uns "));
                                Boolean startsWithUnhas = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("unhas")) || (currentElement.getRealisation().length() >= 6 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 6).equals("unhas "));

                                String letter = morphologyRules.buildPrepositionArticleConjunction(prevString.toString());
                            /*if(letter.equals("p")) {
                                letter = "pol";
                            }*/
                                if (startsWithO) {
                                    prevString.setRealisation(letter + "o");
                                } else if (startsWithA) {
                                    if (letter.equals("a")) {
                                        prevString.setRealisation("á");
                                    } else {
                                        prevString.setRealisation(letter + "a");
                                    }
                                } else if (startsWithOs) {
                                    prevString.setRealisation(letter + "os");
                                } else if (startsWithAs) {
                                    if (letter.equals("a")) {
                                        prevString.setRealisation("ás");
                                    } else {
                                        prevString.setRealisation(letter + "as");
                                    }
                                }
                                if (letter.equals("c") || letter.equals("d") || letter.equals("n")) {
                                    if (startsWithUn) {
                                        prevString.setRealisation(letter + "un");
                                    } else if (startsWithUnha) {
                                        prevString.setRealisation(letter + "unha");
                                    } else if (startsWithUns) {
                                        prevString.setRealisation(letter + "uns");
                                    } else if (startsWithUnhas) {
                                        prevString.setRealisation(letter + "unhas");
                                    }
                                }

                                if (startsWithO || startsWithA || startsWithOs || startsWithAs || startsWithUn || startsWithUnha || startsWithUns || startsWithUnhas) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if (currentElement instanceof ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            }
                        }
                    }

                    //pronoun+article
                   /* StringElement prevString = null;
                    if (prevElement != null && LexicalCategory.PRONOUN.equals(prevElement.getCategory())) {
                        for (Object s : realisedElements) {
                            if (s instanceof StringElement) {
                                prevString = (StringElement) s;
                                if (Arrays.asList(CI_PRONOUNS).contains(prevString.getRealisation())) {
                                    String conj = morphologyRules.buildPronounsConjunction(prevString.getRealisation(), currentElement.getRealisation());
                                    prevString.setRealisation(conj);
                                    realisedElements.set(realisedElements.indexOf(s), prevString);
                                    currentElement.setRealisation("");
                                }
                            }
                        }
                    }*/

                    if (prevElement != null && (LexicalCategory.VERB.equals(prevElement.getCategory()) || LexicalCategory.MODAL.equals(prevElement.getCategory())) && LexicalCategory.PRONOUN.equals(eachElement.getCategory())) {
                        int i;
                        for (i = 0; i < elements.size(); i++) {
                            if (LexicalCategory.VERB.equals(elements.get(i).getCategory())) {
                                break;
                            }
                        }
                        realisedElements.add(i, currentElement);
                    } else {
                        realisedElements.add(currentElement);
                    }

//                    if (determiner == null && DiscourseFunction.SPECIFIER.equals(currentElement.getFeature(
//                            InternalFeature.DISCOURSE_FUNCTION))) {
//                        determiner = currentElement;
//                        determiner.setFeature(Feature.NUMBER, eachElement.getFeature(Feature.NUMBER));
//                        // MorphologyRules.doDeterminerMorphology(determiner,
//                        // currentElement.getRealisation());
//
//                    } else if (determiner != null) {
//
//                        if (currentElement instanceof ListElement) {
//                            // list elements: ensure det matches first element
//                            NLGElement firstChild = ((ListElement) currentElement).getChildren().get(0);
//
//                            if (firstChild != null) {
//                                //AG: need to check if child is a coordinate
//                                if (firstChild instanceof CoordinatedPhraseElement) {
//                                    morphologyRules.doDeterminerMorphology(determiner,
//                                            firstChild.getChildren().get(0).getRealisation());
//                                } else {
//                                    morphologyRules.doDeterminerMorphology(determiner, firstChild.getRealisation());
//                                }
//                            }
//
//                        } else {
//                            // everything else: ensure det matches realisation
//                            morphologyRules.doDeterminerMorphology(determiner, currentElement.getRealisation());
//                        }
//
//                        determiner = null;
//                    }
                }
                prevElement = eachElement;
            }
        }

        return realisedElements;
    }

    /**
     * This is the main method for performing the morphology. It effectively
     * examines the lexical category of the element and calls the relevant set
     * of rules from <code>MorphologyRules</em>.
     *
     * @param element the <code>InflectedWordElement</code>
     * @return an <code>NLGElement</code> reflecting the correct inflection for
     * the word.
     */
    @Override
    protected NLGElement doMorphology(InflectedWordElement element) {
        NLGElement realisedElement = null;
        if (element.getFeatureAsBoolean(InternalFeature.NON_MORPH).booleanValue()) {
            realisedElement = new StringElement(element.getBaseForm());
            realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                    element.getFeature(InternalFeature.DISCOURSE_FUNCTION));

        } else {
            NLGElement baseWord = element.getFeatureAsElement(InternalFeature.BASE_WORD);

            ElementCategory category = element.getCategory();

            if ((baseWord == null && this.lexicon != null) || LexicalCategory.MODAL.equals(category)) {
                if (LexicalCategory.MODAL.equals(category)) {
                    baseWord = this.lexicon.lookupWord(element.getBaseForm(), LexicalCategory.VERB);
                } else {
                    baseWord = this.lexicon.lookupWord(element.getBaseForm());
                }
            }

            if (category instanceof LexicalCategory) {
                switch ((LexicalCategory) category) {
                    case PRONOUN:
                        realisedElement = morphologyRules.doPronounMorphology(element);
                        break;

                    case NOUN:
                        realisedElement = morphologyRules.doNounMorphology(element, (WordElement) baseWord);
                        break;

                    case VERB:
                    case MODAL:
                        realisedElement = morphologyRules.doVerbMorphology(element, (WordElement) baseWord);
                        break;

                    case ADJECTIVE:
                        realisedElement = morphologyRules.doAdjectiveMorphology(element, (WordElement) baseWord);
                        break;

                    case ADVERB:
                        realisedElement = morphologyRules.doAdverbMorphology(element, (WordElement) baseWord);
                        break;

                    case DETERMINER:
                        realisedElement = morphologyRules.doDeterminerMorphology(element);
                        break;

                    default:
                        realisedElement = new StringElement(element.getBaseForm());
                        realisedElement.setFeature(InternalFeature.DISCOURSE_FUNCTION,
                                element.getFeature(InternalFeature.DISCOURSE_FUNCTION));
                }
            }
        }
        return realisedElement;
    }

    public String doAccentuation(String infinitive, String conjugated, String pronoun) {
        List<String> syllables = splitSyllables(conjugated);
        int accentIndex = -1, index, j, position;
        char letter;
        String result = "";
        for (int i = 0; i < ACCENTUATED_VOWELS.length; i++) {
            if (conjugated.contains(ACCENTUATED_VOWELS[i])) {
                accentIndex = conjugated.indexOf(ACCENTUATED_VOWELS[i]);
            }
        }
        //monosílabas: si lleva tilde se mantiene
        if (syllables.size() == 1) {
            System.out.println("AGUDA MONOSÍLABA: " + conjugated + pronoun);
        }
        //graves acentuadas
        else if ((conjugated.substring(conjugated.length() - 1).equals("n") || conjugated.substring(conjugated.length() - 1).equals("s") || conjugated.substring(conjugated.length() - 2).equals("ns")) && accentIndex > -1) {
            System.out.println("GRAVE CON ACENTO: " + conjugated + pronoun);
        } //graves no acentuadas
        else if ((!conjugated.substring(conjugated.length() - 1).equals("n") || !conjugated.substring(conjugated.length() - 1).equals("s") || !conjugated.substring(conjugated.length() - 2).equals("ns")) && accentIndex == -1) {
            index = syllables.get(syllables.size() - 2).length() - 1;
            j = 0;
            do {
                position = index - j;
                letter = syllables.get(syllables.size() - 2).charAt(position);
                j++;
            } while (!Arrays.asList(NOTACCENTUATED_VOWELS).contains(String.valueOf(letter)));
            for (int k = 0; k < syllables.size(); k++) {
                if (k == syllables.size() - 2) {
                    if (letter == 'a') {
                        result += syllables.get(k).replaceFirst(String.valueOf(syllables.get(k).charAt(position)), "á");
                    } else if (letter == 'e') {
                        result += syllables.get(k).replaceFirst(String.valueOf(syllables.get(k).charAt(position)), "é");
                    } else if (letter == 'i') {
                        result += syllables.get(k).replaceFirst(String.valueOf(syllables.get(k).charAt(position)), "í");
                    } else if (letter == 'o') {
                        result += syllables.get(k).replaceFirst(String.valueOf(syllables.get(k).charAt(position)), "ó");
                    } else if (letter == 'u') {
                        result += syllables.get(k).replaceFirst(String.valueOf(syllables.get(k).charAt(position)), "ú");
                    }
                } else {
                    result += syllables.get(k);
                }
            }
            conjugated = result;
            System.out.println("GRAVE SIN ACENTO: " + conjugated
                    + pronoun);
        }
        //aguda acentuada
        else if ((Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 1)) || (Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 2)) && (conjugated.substring(conjugated.length() - 1).equals("n")) || conjugated.substring(conjugated.length() - 1).equals("s")) || ((Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 3)) && conjugated.substring(conjugated.length() - 2).equals("n")) && conjugated.substring(conjugated.length() - 1).equals("s"))) && accentIndex > -1) {
            letter = conjugated.charAt(accentIndex);
            if (letter == 'á') {
                conjugated = conjugated.substring(0, accentIndex) + "a" + conjugated.substring(accentIndex + 1, conjugated.length());
            } else if (letter == 'é') {
                conjugated = conjugated.substring(0, accentIndex) + "e" + conjugated.substring(accentIndex + 1, conjugated.length());
            } else if (letter == 'í') {
                conjugated = conjugated.substring(0, accentIndex) + "i" + conjugated.substring(accentIndex + 1, conjugated.length());
            } else if (letter == 'ó') {
                conjugated = conjugated.substring(0, accentIndex) + "o" + conjugated.substring(accentIndex + 1, conjugated.length());
            } else if (letter == 'ú') {
                conjugated = conjugated.substring(0, accentIndex) + "u" + conjugated.substring(accentIndex + 1, conjugated.length());
            }
            System.out.println("AGUDA CON ACENTO: " + conjugated + pronoun);
        }
        //aguda no acentuada
        else if ((!Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 1)) || !(Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 2)) && (conjugated.substring(conjugated.length() - 1).equals("n")) || conjugated.substring(conjugated.length() - 1).equals("s")) || !((Arrays.asList(VOWELS).contains(conjugated.substring(conjugated.length() - 3)) && conjugated.substring(conjugated.length() - 2).equals("n")) && conjugated.substring(conjugated.length() - 1).equals("s"))) && accentIndex == -1) {
            System.out.println("AGUDA SIN ACENTO: " + conjugated + pronoun);
        }
        return conjugated + pronoun;
    }

    public List<String> splitSyllables(String word) {
        List<String> syllables = new ArrayList<String>();
        boolean completed = false;
        do {
            if (word.length() == 0) {
                completed = true;
            } else {
                for (int i = 1; i < word.length(); i++) {
                    System.out.println(word.charAt(i - 1) + " " + word.charAt(i));
                    //consonant+vowel
                    if (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i - 1))) && Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i)))) {
                        if ((word.charAt(i - 1) != 'q' || word.charAt(i - 1) != 'g') && word.charAt(i) != 'u') {
                            try {
                                //+consonant+consonant-> syllable: consonant+vowel+consonant
                                if (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i + 1))) && !Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i + 2)))) {
                                    syllables.add(word.substring(0, i + 2));
                                    word = word.substring(i + 2, word.length());
                                    //+consonant+vowel -> syllable: consonant+vowel
                                } else if (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i + 1))) && Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i + 2)))) {
                                    syllables.add(word.substring(0, i + 1));
                                    word = word.substring(i + 1, word.length());
                                }
                            } catch (Exception e) {
                                syllables.add(word.substring(0, i + 1));
                                word = word.substring(i + 1, word.length());
                            }
                        }
                        //q or g + u
                        else {
                            try {
                                if (word.charAt(i + 1) == 'e' || word.charAt(i + 1) == 'i') {
                                    syllables.add(word.substring(0, i + 2));
                                    word = word.substring(i + 2, word.length());
                                } else {
                                    syllables.add(word.substring(0, i));
                                    word = word.substring(i, word.length());
                                }
                            } catch (Exception e) {
                                syllables.add(word.substring(0, i));
                                word = word.substring(i, word.length());
                            }
                        }
                    }
                    //consonant + consonant (not r or l)
                    else if (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i - 1))) && (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i))) && (word.charAt(i) != 'r' || word.charAt(i) != 'l'))) {
                        syllables.add(word.substring(0, i));
                        word = word.substring(i, word.length());
                    }
                    //consonant + consonant r or l
                    else if (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i - 1))) && (!Arrays.asList(VOWELS).contains(String.valueOf(word.charAt(i))) && (word.charAt(i) == 'r' || word.charAt(i) == 'l'))) {
                        syllables.add(word.substring(0, i + 1));
                        word = word.substring(i + 1, word.length());
                    }
                    //strong vowel+ soft vowel or consonant
                    else if (Arrays.asList(STRONG_VOWELS).contains(String.valueOf(word.charAt(i - 1))) && !Arrays.asList(STRONG_VOWELS).contains(String.valueOf(word.charAt(i)))) {
                        syllables.add(word.substring(0, i));
                        word = word.substring(i, word.length());
                    }
                }
            }
        } while (!completed);
        return syllables;
    }

}
