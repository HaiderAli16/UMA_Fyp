import numpy as np
import pandas as pd
import sys
import spacy
import regex
from gensim.models import KeyedVectors
from os.path import dirname, join



uruvecFile = os.path.join(os.path.dirname(__file__), "urduvec_140M_100K_300d.bin")

w2v = KeyedVectors.load_word2vec_format(uruvecFile, binary=True)
def pre_process_sentence(sentences):
    sentences = regex.sub(r"\d+", " ", sentences)
    # English punctuations
    sentences = regex.sub(r"""[!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~]+""", " ", sentences)
    # Urdu punctuations
    sentences = regex.sub(r"[:؛؟’‘٭ء،۔]+", " ", sentences)
    # Arabic numbers
    sentences = regex.sub(r"[٠‎١‎٢‎٣‎٤‎٥‎٦‎٧‎٨‎٩]+", " ", sentences)
    sentences = regex.sub(r"[^\w\s]", " ", sentences)
    # Remove English characters and numbers.
    sentences = regex.sub(r"[a-zA-z0-9]+", " ", sentences)
    # remove multiple spaces.
    sentences = regex.sub(r" +", " ", sentences)
    return sentences
def testTxt(txt):
    tokenizer=spacy.blank('ur')
    txt=pre_process_sentence(txt)
    list_of_tokens=tokenizer(txt)
    list_of_token_vec=[]
    for token in list_of_tokens:
        try:
            tokenVec=w2v[""+token.text]

        except:
            tokenVec=np.zeros(300)
        list_of_token_vec.append(tokenVec)
    while len(list_of_token_vec)<20:
        list_of_token_vec.append(np.zeros(300))
    retarr=np.array(list_of_token_vec).reshape(1,20,300)

    return retarr;