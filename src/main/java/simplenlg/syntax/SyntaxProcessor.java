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
package simplenlg.syntax;

import simplenlg.features.DiscourseFunction;
import simplenlg.features.Feature;
import simplenlg.features.InternalFeature;
import simplenlg.features.LexicalFeature;
import simplenlg.framework.*;
import simplenlg.phrasespec.VPPhraseSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is the processor for handling syntax within the SimpleNLG. The processor
 * translates phrases into lists of words.
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
public abstract class SyntaxProcessor extends NLGModule {

    protected ClauseHelper clauseHelper;
    protected CoordinatedPhraseHelper coordinatedPhraseHelper;
    protected NounPhraseHelper nounPhraseHelper;
    protected PhraseHelper phraseHelper;
    protected VerbPhraseHelper verbPhraseHelper;

    protected NLGFactory nlgFactory;

    @Override
    public NLGElement realise(NLGElement element) {
        NLGElement realisedElement = null;
        List<NLGElement> subjects;

        if (element != null
                && !element.getFeatureAsBoolean(Feature.ELIDED).booleanValue()) {

            if (element instanceof DocumentElement) {
                List<NLGElement> children = element.getChildren();
                ((DocumentElement) element).setComponents(realise(children));
                realisedElement = element;

            } else if (element instanceof PhraseElement) {
                realisedElement = realisePhraseElement((PhraseElement) element);


            } else if (element instanceof ListElement) {
                realisedElement = new ListElement();
                ((ListElement) realisedElement).addComponents(realise(element
                        .getChildren()));

            } else if (element instanceof InflectedWordElement) {
                String baseForm = ((InflectedWordElement) element)
                        .getBaseForm();
                ElementCategory category = element.getCategory();
                if(category.equalTo(LexicalCategory.VERB) && element.getFeatureAsString(Feature.PERSON) == null) {
                    if(element.getParent() != null) {
                        subjects = element.getParent().getFeatureAsElementList(InternalFeature.SUBJECTS);
                        if(subjects.size() == 0 && element.getParent().getParent() != null) {
                            subjects = element.getParent().getParent().getFeatureAsElementList(InternalFeature.SUBJECTS);
                        }
                        if(subjects.size() == 1) {
                            element.setFeature(Feature.PERSON, subjects.get(0).getFeature(Feature.PERSON));
                        }
                    }

                }

                if (this.lexicon != null && baseForm != null) {
                    WordElement word = ((InflectedWordElement) element)
                            .getBaseWord();

                    if (word == null) {
                        if (category instanceof LexicalCategory) {
                            word = this.lexicon.lookupWord(baseForm,
                                    (LexicalCategory) category);
                        } else {
                            word = this.lexicon.lookupWord(baseForm);
                        }
                    }

                    if (word != null) {
                        ((InflectedWordElement) element).setBaseWord(word);
                    }
                }

                realisedElement = element;

             } else if (element instanceof WordElement) {
                // AG: need to check if it's a word element, in which case it
                // needs to be marked for inflection
                InflectedWordElement infl = new InflectedWordElement(
                        (WordElement) element);

                // // the inflected word inherits all features from the base
                // word
                for (String feature : element.getAllFeatureNames()) {
                    infl.setFeature(feature, element.getFeature(feature));
                }

                realisedElement = realise(infl);

            } else if (element instanceof CoordinatedPhraseElement) {
                realisedElement = coordinatedPhraseHelper.realise(this,
                        (CoordinatedPhraseElement) element);

            } else {
                realisedElement = element;
            }
        }


        // Remove the spurious ListElements that have only one element.
        if (realisedElement instanceof ListElement) {
            if (((ListElement) realisedElement).size() == 1) {
                realisedElement = ((ListElement) realisedElement).getFirst();
            }
        }

        if(element != null && LexicalCategory.VERB.equals(element.getCategory()) && element.getParent() != null) {
            //if the phrase is impersonal, the verb has to be
            if(element.getParent().getFeatureAsBoolean(Feature.IS_IMPERSONAL)) {
                realisedElement.setFeature(Feature.IS_IMPERSONAL, true);
            }
            //if the phrase is reflexive, the verb has to be (except if the phrase has a modal verb)
            else if(element.getParent().getFeatureAsBoolean(LexicalFeature.REFLEXIVE) && element.getParent().getFeature(Feature.MODAL) == null) {
                realisedElement.setFeature(LexicalFeature.REFLEXIVE, true);
            }
        }
        return realisedElement;
    }

    @Override
    public List<NLGElement> realise(List<NLGElement> elements) {
        List<NLGElement> realisedList = new ArrayList<NLGElement>();
        NLGElement childRealisation = null;

        if (elements != null) {
            for (NLGElement eachElement : elements) {
                if (eachElement != null) {
                    childRealisation = realise(eachElement);
                    if (childRealisation != null) {
                        if (childRealisation instanceof ListElement) {
                            realisedList
                                    .addAll(((ListElement) childRealisation)
                                            .getChildren());
                        } else {
                            realisedList.add(childRealisation);
                        }
                    }
                }
            }
        }
        return realisedList;
    }

    /**
     * Realises a phrase element.
     *
     * @param phrase the element to be realised
     * @return the realised element.
     */
    private NLGElement realisePhraseElement(PhraseElement phrase) {
        NLGElement realisedElement = null;
        if (phrase != null) {
            ElementCategory category = phrase.getCategory();

            if (category instanceof PhraseCategory) {
                switch ((PhraseCategory) category) {

                    case CLAUSE:
                        realisedElement = clauseHelper.realise(this, phrase);
                        break;

                    case NOUN_PHRASE:
                        realisedElement = nounPhraseHelper.realise(this, phrase);
                        break;

                    case VERB_PHRASE:
                        realisedElement = verbPhraseHelper.realise(this, phrase);
                        break;

                    case PREPOSITIONAL_PHRASE:
                    case ADJECTIVE_PHRASE:
                    case ADVERB_PHRASE:
                        realisedElement = phraseHelper.realise(this, phrase);
                        break;
                    default:
                        realisedElement = phrase;
                        break;
                }
            }
        }

        return realisedElement;
    }
}
