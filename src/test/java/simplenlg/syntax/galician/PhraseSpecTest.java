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
import simplenlg.features.Form;
import simplenlg.features.InternalFeature;
import simplenlg.features.Tense;
import simplenlg.framework.*;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.phrasespec.VPPhraseSpec;

/**
 * test suite for simple XXXPhraseSpec classes
 *
 * @author ereiter
 */

public class PhraseSpecTest extends SimpleNLG4Test {

    public PhraseSpecTest(String name) {
        super(name);
    }


    @Override
    @After
    public void tearDown() {
        super.tearDown();
    }

    /**
     * Check that empty phrases are not realised as "null"
     */
    @Test
    public void emptyPhraseRealisationTest() {
        SPhraseSpec emptyClause = this.phraseFactory.createClause();
        Assert.assertEquals("", this.realiser.realise(emptyClause)
                .getRealisation());
    }


    /**
     * Test SPhraseSpec
     */
    @Test
    public void testSPhraseSpec() {

        // simple test of methods
        SPhraseSpec c1 = (SPhraseSpec) phraseFactory.createClause();
        c1.setVerb("dar");
        c1.setSubject("John");
        c1.setObject("unha mazá");
        c1.setIndirectObject("Mary");
        c1.setFeature(Feature.TENSE, Tense.PAST);
        c1.setFeature(Feature.NEGATED, true);

        // check getXXX methods
        Assert.assertEquals("dar", getBaseForm(c1.getVerb()));
        Assert.assertEquals("John", getBaseForm(c1.getSubject()));
        Assert.assertEquals("unha mazá", getBaseForm(c1.getObject()));
        Assert.assertEquals("Mary", getBaseForm(c1.getIndirectObject()));

        Assert.assertEquals("John non deu a Mary unha mazá", this.realiser //$NON-NLS-1$
                .realise(c1).getRealisation());


        // test modifier placement
        SPhraseSpec c2 = (SPhraseSpec) phraseFactory.createClause();
        c2.setVerb("ver");
        //c2.setFeature(Feature.);
        c2.setSubject("o home");
        c2.setObject("eu");
        c2.addFrontModifier("afortunadamente");
        c2.addModifier("rápidamente");
        c2.addModifier("no parque");
        //c2.setFeature(Feature.NEGATED, true);
        // try setting tense directly as a feature
        c2.setFeature(Feature.TENSE, Tense.PAST);
        Assert.assertEquals("afortunadamente o home viume rápidamente no parque", this.realiser //$NON-NLS-1$
                .realise(c2).getRealisation());
    }

    /**
     * Test Subordinate
     */
    @Test
    public void testSubordinate() {
        SPhraseSpec p = phraseFactory.createClause("eu", "estar", "feliz");
        SPhraseSpec q = phraseFactory.createClause(null, "dicir");
        q.setIndirectObject("lle");

        q.setFeature(Feature.COMPLEMENTISER, "porque");
        q.setFeature(Feature.TENSE, Tense.PAST);
        p.addComplement(q);

        Assert.assertEquals("eu estou feliz porque lle dixen", this.realiser //$NON-NLS-1$
                .realise(p).getRealisation());

    }

    // get string for head of constituent
    private String getBaseForm(NLGElement constituent) {
        if (constituent == null)
            return null;
        else if (constituent instanceof StringElement)
            return constituent.getRealisation();
        else if (constituent instanceof WordElement)
            return ((WordElement) constituent).getBaseForm();
        else if (constituent instanceof InflectedWordElement)
            return getBaseForm(((InflectedWordElement) constituent).getBaseWord());
        else if (constituent instanceof PhraseElement)
            return getBaseForm(((PhraseElement) constituent).getHead());
        else
            return null;
    }
}
