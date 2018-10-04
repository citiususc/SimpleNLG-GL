SimpleNLG-gl
=========

SimpleNLG-gl is a simple Java API designed to facilitate the generation of natural language texts in Galician. It is a trilingual English/Spanish/Galician adaptation of the SimpleNLG v4.4.8 library, following the structure used in SimpleNLG-EnFr. The original SimpleNLG library was developed for the English language in the [Department of Computing Science of the University of Aberdeen](https://www.abdn.ac.uk/ncs/departments/computing-science/natural-language-generation-187.php). SimpleNLG-EnFr is its bilingual English-French adaptation, developed at the [Université de Montreal](http://www-etud.iro.umontreal.ca/~vaudrypl/snlgbil/snlgEnFr_francais.xhtml).

SimpleNLG-gl is designed to facilitate the tasks of linguistic realization in Galician in natural language generation systems.

The development of SimpleNLG-gl was made in the framework of the projects TIN2014-56633-C3-1-R and TIN2017-84796-C2-1-R, funded by the Spanish Ministry for Innovation and Economy and the EDRF. It was also supported by the Galician Research Network on "Technologies and Analysis of Linguistic Data (TECANDALI) (Ref. 2016-PG080)", leaded by the Institute for Galician Language (IGL).

Getting started
---------------
For information on how to use SimpleNLG-gl, see the Wiki and the API.

SimpleNLG-gl License
-----------------------------
Being based on SimpleNLG and SimpleNLG-EnFr, SimpleNLG-ES is licensed under the terms and conditions of the [Mozilla Public License (MPL) Version 1.1](https://www.mozilla.org/en-US/MPL/1.1/).

The lexicon used in SimpleNLG-gl is generated from the [FreeLing dictionary](http://nlp.lsi.upc.edu/freeling/) which is licensed under the terms and conditions of the [Lesser General Public License For Linguistic Resources](http://infolingu.univ-mlv.fr/DonneesLinguistiques/Lexiques-Grammaires/lgpllr.html).

Cite
----
The SimpleNLG-GL library was presented at the "[11th International Conference on Natural Language Generation (INLG2018)](https://inlg2018.uvt.nl/)", held in Tilburg (5-8 novembro 2018). If you use SimpleNLG-GL in any project, please quote the work where it is described:

> Andrea Cascallar-Fuentes, Alejandro Ramos-Soto, Alberto Bugarín, "[Adapting SimpleNLG to Galician Language](https://citius.usc.es/investigacion/publicacions/listado/adapting-simplenlg-to-galician-language)". Proceedings of the 11th International Conference on Natural Language Generation (INLG2018).

```
@inproceedings{acascallarfuentes2018adapting,
	title = {Adapting {SimpleNLG} to Galician Language},
	journal = {11th International Conference on Natural Language Generation},
	year = {2018},
	abstract = {In this paper, we describe SimpleNLG-GL, an adaptation of the linguistic realisation SimpleNLG library for the Galician language. This implementation is derived from SimpleNLG-ES, the English-Spanish version of this library. It has been tested using a battery of examples which covers the most common rules for Galician.},
	publisher = {Association for Computational Linguistics},
	author = {A. Cascallar-Fuentes and A. Ramos-Soto and Alberto Bugar\'{i}n}
}
```

Citation
--------
SimpleNLG-GL was presented at the "[11th International Conference on Natural Language Generation (INLG2017)](https://inlg2018.uvt.nl/)", in Tilburg (5-8 november 2018). If you use SimpleNLG-GL in any project, please quote the paper where it is described:

> Andrea Cascallar-Fuentes, Alejandro Ramos-Soto, Alberto Bugarín, "[Adapting SimpleNLG to Galician Language](https://citius.usc.es/investigacion/publicacions/listado/adapting-simplenlg-to-galician-language)". Proceedings of the 11th International Conference on Natural Language Generation (INLG2018).

```
@inproceedings{acascallarfuentes2018adapting,
	title = {Adapting {SimpleNLG} to Galician Language},
	journal = {11th International Conference on Natural Language Generation},
	year = {2018},
	abstract = {In this paper, we describe SimpleNLG-GL, an adaptation of the linguistic realisation SimpleNLG library for the Galician language. This implementation is derived from SimpleNLG-ES, the English-Spanish version of this library. It has been tested using a battery of examples which covers the most common rules for Galician.},
	publisher = {Association for Computational Linguistics},
	author = {A. Cascallar-Fuentes and A. Ramos-Soto and Alberto Bugar\'{i}n}
}
```


SimpleNLG
------------------
The current version of SimpleNLG is V4.4.8 ([API](https://cdn.rawgit.com/simplenlg/simplenlg/master/docs/javadoc/index.html)). For more information, visit his [Github page](https://github.com/simplenlg/simplenlg) or the [SimpleNLG discussion list](https://groups.google.com/forum/#!forum/simplenlg).

If you wish to cite SimpleNLG in an academic publication, please cite the following paper:

* A Gatt and E Reiter (2009). [SimpleNLG: A realisation engine for practical applications](http://aclweb.org/anthology/W/W09/W09-0613.pdf). [Proceedings of the 12th European Workshop on Natural Language Generation (ENLG2009)](http://aclweb.org/anthology/siggen.html#2009_0), 90-93.

If you have other questions about SimpleNLG, please contact Professor Ehud Reiter via email: [ehud.reiter@arria.com](mailto:ehud.reiter@arria.com).

SimpleNLG-EnFr
-----------------------------
The current version of SimpleNLG-EnFr is V1.1. SimpleNLG-EnFr can realize text in both English and French in the same document. The French part covers practically all the grammar in [Le français fondamental (1er degré)](http://fr.wikipedia.org/wiki/Fran%C3%A7ais_fondamental) and has a 3871 entry lexicon covering the [échelle orthographique Dubois Buyse](http://o.bacquet.free.fr/db2.htm).
