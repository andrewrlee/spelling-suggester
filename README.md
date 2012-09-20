spelling-suggester
==================

This is a java implementation of Peter Norvig's simple [algorithm](http://norvig.com/spell-correct.html) for a Spelling corrector using google's [guava library](http://code.google.com/p/guava-libraries/).   	`

It takes a dictionary file in it's constructor and can then return a likely alternative for a misspelled word. 

If the provided word is present in the dictionary then it will just return that word. Given the default dictionary (Of around 1.1 million words) it generally has an accuracy of around 67- 74%. This is within .5% of the original algorith implemented in python. 

Due to java being a compiled language, it is considerably faster than the original, interpreted, python version. 
