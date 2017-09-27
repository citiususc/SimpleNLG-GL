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

import java.util.ArrayList;
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
                currentElement = realise(eachElement);


                //when possesive + noun add determiner -> determiner + possesive + noun
                if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("meu") || currentElement.getRealisation().equals("teu") || currentElement.getRealisation().equals("seu") || currentElement.getRealisation().equals("noso") ||currentElement.getRealisation().equals("voso"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("o " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("miña") || currentElement.getRealisation().equals("túa") || currentElement.getRealisation().equals("súa") || currentElement.getRealisation().equals("nosa") ||currentElement.getRealisation().equals("vosa"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("a " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("meus") || currentElement.getRealisation().equals("teus") || currentElement.getRealisation().equals("seus") || currentElement.getRealisation().equals("nosos") ||currentElement.getRealisation().equals("vosos"))) {
                    if (prevElement == null) {
                        currentElement.setRealisation("os " + currentElement.getRealisation());
                    }
                } else if (currentElement != null && currentElement.getRealisation() != null && (currentElement.getRealisation().equals("miñas") || currentElement.getRealisation().equals("túas") || currentElement.getRealisation().equals("súas") || currentElement.getRealisation().equals("nosas") ||currentElement.getRealisation().equals("vosas"))) {
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

                    if (prevElement != null && LexicalCategory.PREPOSITION.equals(prevElement.getCategory())) {

                        NLGElement root = elements.get(0);
                        while (root.getParent() != null) {
                            root = root.getParent();
                        }
                        if (root.getFeatureAsBoolean(Feature.PASSIVE) && root.hasFeature(Feature.INTERROGATIVE_TYPE)) {
                            realisedElements.remove(realisedElements.size() - 1);
                        } else {
                            StringElement prevString = (StringElement) realisedElements.get(realisedElements.size() - 1);
                            String secondPart = "";
                            if (currentElement.getRealisation().contains(" ")) {
                                secondPart = currentElement.getRealisation().substring(currentElement.getRealisation().indexOf(" "));
                            }
                            Boolean startsWithO = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("o")) || (currentElement.getRealisation().length() >= 2 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 2).equals("o "));
                            Boolean startsWithA = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("a")) || (currentElement.getRealisation().length() >= 2 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 2).equals("a "));
                            Boolean startsWithOs = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("os")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("os "));
                            Boolean startsWithAs = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("as")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("as "));
                            Boolean startsWithUn = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("un")) || (currentElement.getRealisation().length() >= 3 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 3).equals("un "));
                            Boolean startsWithUnha = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("unha")) || (currentElement.getRealisation().length() >= 5 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 5).equals("unha "));
                            Boolean startsWithUns = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("uns")) || (currentElement.getRealisation().length() >=4 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 4).equals("uns "));
                            Boolean startsWithUnhas = (currentElement instanceof ListElement && ((ListElement) currentElement).getFirst().toString().equals("unhas")) || (currentElement.getRealisation().length() >= 6 && currentElement instanceof StringElement && currentElement.getRealisation().substring(0, 6).equals("unhas "));

                            if ("a".equals(prevString.toString())) {
                                if (startsWithO) {
                                    prevString.setRealisation("ao");
                                } else if (startsWithA) {
                                    prevString.setRealisation("á");
                                } else if (startsWithOs) {
                                    prevString.setRealisation("aos");
                                } else if (startsWithAs) {
                                    prevString.setRealisation("ás");
                                }
                                if (startsWithO || startsWithA || startsWithOs || startsWithAs) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if(currentElement instanceof  ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            } else if ("con".equals(prevString.toString())) {
                                if (startsWithO) {
                                    prevString.setRealisation("co");
                                } else if (startsWithA) {
                                    prevString.setRealisation("coa");
                                } else if (startsWithOs) {
                                    prevString.setRealisation("cos");
                                } else if (startsWithAs) {
                                    prevString.setRealisation("coas");
                                }else if (startsWithUn) {
                                    prevString.setRealisation("cun");
                                } else if (startsWithUnha) {
                                    prevString.setRealisation("cunha");
                                } else if (startsWithUns) {
                                    prevString.setRealisation("cuns");
                                } else if (startsWithUnhas) {
                                    prevString.setRealisation("cunhas");
                                }
                                if (startsWithO || startsWithA || startsWithOs || startsWithAs || startsWithUn || startsWithUnha || startsWithUns || startsWithUnhas) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if(currentElement instanceof  ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            } else if ("de".equals(prevString.toString())) {
                                if (startsWithO) {
                                    prevString.setRealisation("do");
                                } else if (startsWithA) {
                                    prevString.setRealisation("da");
                                } else if (startsWithOs) {
                                    prevString.setRealisation("dos");
                                } else if (startsWithAs) {
                                    prevString.setRealisation("das");
                                }else if (startsWithUn) {
                                    prevString.setRealisation("dun");
                                } else if (startsWithUnha) {
                                    prevString.setRealisation("dunha");
                                } else if (startsWithUns) {
                                    prevString.setRealisation("duns");
                                } else if (startsWithUnhas) {
                                    prevString.setRealisation("dunhas");
                                }
                                if (startsWithO || startsWithA || startsWithOs || startsWithAs|| startsWithUn || startsWithUnha || startsWithUns || startsWithUnhas) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if(currentElement instanceof  ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            } else if ("en".equals(prevString.toString())) {
                                if (startsWithO) {
                                    prevString.setRealisation("no");
                                } else if (startsWithA) {
                                    prevString.setRealisation("na");
                                } else if (startsWithOs) {
                                    prevString.setRealisation("nos");
                                } else if (startsWithAs) {
                                    prevString.setRealisation("nas");
                                } else if (startsWithUn) {
                                    prevString.setRealisation("nun");
                                } else if (startsWithUnha) {
                                    prevString.setRealisation("nunha");
                                } else if (startsWithUns) {
                                    prevString.setRealisation("nuns");
                                } else if (startsWithUnhas) {
                                    prevString.setRealisation("nunhas");
                                }
                                if (startsWithO || startsWithA || startsWithOs || startsWithAs || startsWithUn || startsWithUnha || startsWithUns || startsWithUnhas) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if(currentElement instanceof  ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            }else if ("por".equals(prevString.toString())) {
                                if (startsWithO) {
                                    prevString.setRealisation("polo");
                                } else if (startsWithA) {
                                    prevString.setRealisation("pola");
                                } else if (startsWithOs) {
                                    prevString.setRealisation("polos");
                                } else if (startsWithAs) {
                                    prevString.setRealisation("polas");
                                }
                                if (startsWithO || startsWithA || startsWithOs || startsWithAs) {
                                    realisedElements.set(realisedElements.size() - 1, prevString);
                                    if(currentElement instanceof  ListElement) {
                                        ((ListElement) currentElement).getFirst().setRealisation("");
                                    } else {
                                        currentElement.setRealisation(secondPart);
                                    }
                                }
                            }
                        }
                    }
                    StringElement prevString = null;
                    if (prevElement != null && LexicalCategory.PRONOUN.equals(prevElement.getCategory())) {
                        for (Object s : realisedElements) {
                            if (s instanceof StringElement) {
                                prevString = (StringElement) s;
                                if (prevString.getRealisation().equals("me")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("mo");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("ma");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("mos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("mas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                } else if (prevString.getRealisation().equals("che")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("cho");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("cha");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("chos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("chas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                } else if (prevString.getRealisation().equals("lle")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("llo");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("lla");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("llos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("llas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                } else if (prevString.getRealisation().equals("nos")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("nolo");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("nola");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("nolos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("nolas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                } else if (prevString.getRealisation().equals("vos")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("volo");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("vola");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("volos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("volas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                } else if (prevString.getRealisation().equals("lles")) {
                                    if (currentElement.getRealisation().equals("o")) {
                                        prevString.setRealisation("llelo");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("a")) {
                                        prevString.setRealisation("llela");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("os")) {
                                        prevString.setRealisation("llelos");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    } else if (currentElement.getRealisation().equals("as")) {
                                        prevString.setRealisation("llelas");
                                        realisedElements.set(realisedElements.indexOf(s), prevString);
                                        currentElement.setRealisation("");
                                    }
                                    break;
                                }
                            }
                        }
                    }

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

}
